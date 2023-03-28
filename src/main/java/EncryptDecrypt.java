import org.apache.commons.codec.binary.Base64;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;

public class EncryptDecrypt {

    private static final String SECRET_KEY_1 = "getFatalityDamn";
    public String SECRET_KEY_2;

    private IvParameterSpec ivParameterSpec;
    private SecretKeySpec secretKeySpec;
    private Cipher cipher;

    public EncryptDecrypt(String SECRET_KEY_2) throws Exception {
        ivParameterSpec = new IvParameterSpec(SECRET_KEY_1.getBytes("UTF-8"));
        secretKeySpec = new SecretKeySpec(SECRET_KEY_2.getBytes("UTF-8"), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    }

    public String encrypt(String toBeEncrypt) throws Exception{
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(toBeEncrypt.getBytes());
        return Base64.encodeBase64String(encrypted);
    }

    public String decrypt(String encrypted) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encrypted));
        return new String(decryptedBytes);
    }
}
