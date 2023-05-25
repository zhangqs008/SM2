package src;

import src.framework.security.SM2Utils;
import src.framework.security.Util;

public class Main {

    public static void main(final String[] args) {
        try {  
            
            //1.测试生成密钥对
            SM2Utils.generateKeyPair();
            System.out.println("1.测试生成密钥对" );
            System.out.println("公钥: " + SM2Utils.pubk);
            System.out.println("私钥: " + SM2Utils.prik);
            System.out.println("");


            //2.测试加密
            System.out.println("2.测试加密" );
            String plainText = "abc123中国";
            String pubk = "04F59485B23304990ED45E42521BE504D0DE358B9E4031A172EF48700071AF985A8EA8B12BB479E24152814EE61840932BFFF5B3B1657C9CF50A61756B1D901E1C";
            System.out.println("公钥: " + pubk);
            System.out.println("加密: ");            
            String cipherText = SM2Utils.encrypt(Util.hexToByte(pubk), plainText.getBytes());
            System.out.println(cipherText);
            System.out.println("");
    
            //3.测试解密
            System.out.println("3.测试解密" );
            String prik = "78AEBAE7DE025B6954357DB327F4AE412B3657B1E1ED36F89927C065155DBA9A";
            System.out.println("私钥: " + prik);
            System.out.println("解密: ");
            plainText = new String(SM2Utils.decrypt(Util.hexToByte(prik), Util.hexToByte(cipherText)));
            System.out.println(plainText);
            System.out.println("");
    
            //4.测试解密其他加密密文（如js，C#加密的密文）
            System.out.println("4.测试解密其他加密密文（如js，C#加密的密文）" ); 
            cipherText = "04682E007F7D922ED855E29A5E5AB257C920A5A2CEA613BB129A643AA52687C2A3BF4E729ECD0C8C1C508EE5DE6F01296756ACE13F7831185319B1F775457DA68E2F31EC033BBF52ED2D00088F4FE7712C302B2D10BF1A998F1C3675E6EDFDBD9E5EE8A4018659EC3D90E639DE85CE971727DB4553FEB4115A05721616";
            plainText = new String(SM2Utils.decrypt(Util.hexToByte(prik), Util.hexToByte(cipherText)));
            System.out.println(plainText);
            

        } catch (final Exception e) {
            System.out.print("系统异常：" + e.getMessage());
            e.printStackTrace();
        } 
    }
}
