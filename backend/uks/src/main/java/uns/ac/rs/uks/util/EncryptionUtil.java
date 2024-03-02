package uns.ac.rs.uks.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class EncryptionUtil {

    public static String decode(String encoded){
        try {
            return new String(Hex.decodeHex(encoded));
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encode(String toEncode) {
        return Hex.encodeHexString(toEncode.getBytes());
    }
}
