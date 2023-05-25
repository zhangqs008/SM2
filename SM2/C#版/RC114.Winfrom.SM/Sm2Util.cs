using System;
using System.Collections.Generic;
using System.Text;
using Org.BouncyCastle.Asn1.GM;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Digests;
using Org.BouncyCastle.Crypto.Engines;
using Org.BouncyCastle.Crypto.Generators;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Math;
using Org.BouncyCastle.Math.EC;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.Utilities.Encoders;

namespace RC114.Winfrom.SM
{
    public class Sm2Util
    {
        /// <summary>
        ///     加密模式
        /// </summary>
        public enum Mode
        {
            C1C2C3,
            C1C3C2
        }

        private readonly Mode _mode;
        private readonly string _privkey;

        private ICipherParameters _privateKeyParameters;
        private string _pubkey;

        private ICipherParameters _publicKeyParameters;

        public Sm2Util(string pubkey, string privkey, Mode mode = Mode.C1C2C3, bool isPkcs8 = false)
        {
            if (pubkey != null)
                _pubkey = pubkey;
            if (privkey != null)
                _privkey = privkey;
            _mode = mode;
        }

        private ICipherParameters PrivateKeyParameters
        {
            get
            {
                try
                {
                    var r = _privateKeyParameters;
                    if (r == null)
                        r = _privateKeyParameters =
                            new ECPrivateKeyParameters(new BigInteger(_privkey, 16),
                                new ECDomainParameters(GMNamedCurves.GetByName("SM2P256V1")));
                    return r;
                }
                catch (Exception ex)
                {
                    return null;
                }
            }
        }

        private ICipherParameters PublicKeyParameters
        {
            get
            {
                try
                {
                    var r = _publicKeyParameters;
                    if (r == null)
                    {
                        //截取64字节有效的SM2公钥（如果公钥首个字节为0x04）
                        if (_pubkey.Length > 128) _pubkey = _pubkey.Substring(_pubkey.Length - 128);
                        //将公钥拆分为x,y分量（各32字节）
                        var stringX = _pubkey.Substring(0, 64);
                        var stringY = _pubkey.Substring(stringX.Length);
                        //将公钥x、y分量转换为BigInteger类型
                        var x = new BigInteger(stringX, 16);
                        var y = new BigInteger(stringY, 16);
                        //通过公钥x、y分量创建椭圆曲线公钥规范
                        var x9Ec = GMNamedCurves.GetByName("SM2P256V1");
                        r = _publicKeyParameters = new ECPublicKeyParameters(x9Ec.Curve.CreatePoint(x, y),
                            new ECDomainParameters(x9Ec));
                    }

                    return r;
                }
                catch (Exception ex)
                {
                    return null;
                }
            }
        }

        /// <summary>
        ///     生成秘钥对
        /// </summary>
        /// <returns></returns>
        public static Dictionary<string, string> GenerateKeyPair()
        {
            string[] param =
            {
                "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF", // p,0
                "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC", // a,1
                "28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", // b,2
                "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", // n,3
                "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", // gx,4
                "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0" // gy,5
            };
            var eccParam = param;

            var eccP = new BigInteger(eccParam[0], 16);
            var eccA = new BigInteger(eccParam[1], 16);
            var eccB = new BigInteger(eccParam[2], 16);
            var eccN = new BigInteger(eccParam[3], 16);
            var eccGx = new BigInteger(eccParam[4], 16);
            var eccGy = new BigInteger(eccParam[5], 16);
            ECFieldElement element = new FpFieldElement(eccP, eccGx);
            ECFieldElement ecFieldElement = new FpFieldElement(eccP, eccGy);

            ECCurve eccCurve = new FpCurve(eccP, eccA, eccB);
            ECPoint eccPointG = new FpPoint(eccCurve, element, ecFieldElement);

            var bcSpec = new ECDomainParameters(eccCurve, eccPointG, eccN);
            var ecgenparam = new ECKeyGenerationParameters(bcSpec, new SecureRandom());
            var generator = new ECKeyPairGenerator();
            generator.Init(ecgenparam);

            var key = generator.GenerateKeyPair();
            var ecpriv = (ECPrivateKeyParameters) key.Private;
            var ecpub = (ECPublicKeyParameters) key.Public;
            var privateKey = ecpriv.D;
            var publicKey = ecpub.Q;
            var dic = new Dictionary<string, string>();
            dic.Add("pubkey", Encoding.Default.GetString(Hex.Encode(publicKey.GetEncoded())).ToUpper());
            dic.Add("prikey", Encoding.Default.GetString(Hex.Encode(privateKey.ToByteArray())).ToUpper());
            return dic;
        }

        public byte[] Decrypt(byte[] data)
        {
            try
            {
                if (_mode == Mode.C1C3C2)
                    data = C132ToC123(data);
                var sm2 = new SM2Engine(new SM3Digest());
                sm2.Init(false, PrivateKeyParameters);
                return sm2.ProcessBlock(data, 0, data.Length);
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        public byte[] Encrypt(byte[] data)
        {
            try
            {
                var sm2 = new SM2Engine(new SM3Digest());
                sm2.Init(true, new ParametersWithRandom(PublicKeyParameters));
                data = sm2.ProcessBlock(data, 0, data.Length);
                if (_mode == Mode.C1C3C2)
                    data = C123ToC132(data);
                return data;
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        private static byte[] C123ToC132(byte[] c1c2c3)
        {
            var gn = GMNamedCurves.GetByName("SM2P256V1");
            var c1Len = (gn.Curve.FieldSize + 7) / 8 * 2 + 1;
            var c3Len = 32;
            var result = new byte[c1c2c3.Length];
            Array.Copy(c1c2c3, 0, result, 0, c1Len); //c1
            Array.Copy(c1c2c3, c1c2c3.Length - c3Len, result, c1Len, c3Len); //c3
            Array.Copy(c1c2c3, c1Len, result, c1Len + c3Len, c1c2c3.Length - c1Len - c3Len); //c2
            return result;
        }

        private static byte[] C132ToC123(byte[] c1c3c2)
        {
            var gn = GMNamedCurves.GetByName("SM2P256V1");
            var c1Len = (gn.Curve.FieldSize + 7) / 8 * 2 + 1;
            var c3Len = 32;
            var result = new byte[c1c3c2.Length];
            Array.Copy(c1c3c2, 0, result, 0, c1Len); //c1: 0->65
            Array.Copy(c1c3c2, c1Len + c3Len, result, c1Len, c1c3c2.Length - c1Len - c3Len); //c2
            Array.Copy(c1c3c2, c1Len, result, c1c3c2.Length - c3Len, c3Len); //c3
            return result;
        }

        /// <summary>
        ///     字节数组转16进制原码字符串
        /// </summary>
        /// <param name="bytes"></param>
        /// <returns></returns>
        public static string BytesToHexStr(byte[] bytes)
        {
            var str = "";
            if (bytes != null)
                for (var i = 0; i < bytes.Length; i++)
                    str += bytes[i].ToString("X2");
            return str;
        }

        /// <summary>
        ///     16进制原码字符串转字节数组
        /// </summary>
        /// <param name="hexStr">"AABBCC"或"AA BB CC"格式的字符串</param>
        /// <returns></returns>
        public static byte[] HexStrToBytes(string hexStr)
        {
            hexStr = hexStr.Replace(" ", "");
            if (hexStr.Length % 2 != 0) throw new ArgumentException("参数长度不正确,必须是偶数位。");
            var bytes = new byte[hexStr.Length / 2];
            for (var i = 0; i < bytes.Length; i++)
            {
                var b = Convert.ToByte(hexStr.Substring(i * 2, 2), 16);
                bytes[i] = b;
            }

            return bytes;
        }
    }
}