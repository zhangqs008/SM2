package src.framework.security;

import java.util.Arrays;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.encoders.Hex;

public class SM3Test {

	public static void main(String[] args) {

		String plainText = "abc";
		System.out.println("1.普通加密");
		System.out.println("明文: " + plainText);
		String cipherText = encrypt(plainText);
		System.out.println("密文: " + cipherText.toUpperCase());
		System.out.println("校验：" + verify(plainText, cipherText));
		System.out.println("");

		System.out.println("2.自定义秘钥加密");
		System.out.println("明文: " + plainText);
		String key = "1234567890";
		System.out.println("秘钥: " + key);
		cipherText = new String(Hex.encode(hmac(key.getBytes(), plainText.getBytes())));
		System.out.println("密文: " + cipherText.toUpperCase()); 
		System.out.println("");

	}

	// 加密
	public static byte[] hash(byte[] srcdata) {
		SM3Digest digest = new SM3Digest();
		digest.update(srcdata, 0, srcdata.length);
		byte[] hash = new byte[digest.getDigestSize()];
		digest.doFinal(hash, 0);
		return hash;
	}

	/**
	 * sm3算法加密
	 * 
	 * @explain
	 * @param paramStr 待加密字符串
	 * @return 返回加密后，固定长度=32的16进制字符串
	 */
	public static String encrypt(String srcdata) {
		return new String(Hex.encode(hash(srcdata.getBytes())));
	}

	// 自定义秘钥进行加密
	public static byte[] hmac(byte[] key, byte[] srcdata) {
		KeyParameter keypar = new KeyParameter(key);
		SM3Digest digest = new SM3Digest();
		HMac mac = new HMac(digest);
		mac.init(keypar);
		mac.update(srcdata, 0, srcdata.length);
		digest.update(srcdata, 0, srcdata.length);
		byte[] hash = new byte[mac.getMacSize()];
		mac.doFinal(hash, 0);
		return hash;
	}

	public static boolean verify(String org, String sm3sString) {
		byte[] src = org.getBytes();
		byte[] sm3Hash = ByteUtils.fromHexString(sm3sString);
		byte[] newHash = hash(src);
		return Arrays.equals(newHash, sm3Hash);
	}
}
