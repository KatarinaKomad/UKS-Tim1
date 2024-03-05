package uns.ac.rs.uks.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Base64;

public class EncryptionUtil {

    public static String decodeHex(String encoded){
        try {
            return new String(Hex.decodeHex(encoded));
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeHex(String toEncode) {
        return Hex.encodeHexString(toEncode.getBytes());
    }

    public static String decodeBase64(String encoded){
        return new String(Base64.getDecoder().decode(encoded));
    }
}
