package DmData.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashUtil {
    private static final String ALGO = "SHA-256";

    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGO);
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 - not available", e);
        }
    }
}
