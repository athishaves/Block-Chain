package blockChain.bChain;

import org.apache.commons.codec.binary.Hex;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Hash {

    public static final String KEY_ALGO = "EC";
    public static final int KEY_SIZE = 256;

    public static final String SIG_ALGO = "SHA256withECDSA";

    public static final String HASH_ALGO = "SHA-256";

    public static final String CHARSET = "UTF-8";



    public static String sha_256 (String text) {
        try {
            byte[] hash = MessageDigest.getInstance(HASH_ALGO).digest(text.getBytes(CHARSET));
            StringBuilder sbr = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) sbr.append('0');
                sbr.append(hex);
            }
            return sbr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static KeyPair generateKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGO);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static String keyToHex(byte[] key) {
        StringBuilder resKey = new StringBuilder();
        for (byte b : key) resKey.append(String.format("%02x", b));
        return resKey.toString();
    }


    public static PrivateKey hexToPrivateKey(String key) {
        try {
            byte[] keyData = Hex.decodeHex(key.toCharArray());
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGO);
            PrivateKey privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(keyData));
            return privateKey;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static PublicKey hexToPublicKey(String key) {
        try {
            byte[] keyData = Hex.decodeHex(key.toCharArray());
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGO);
            PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(keyData));
            return publicKey;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
