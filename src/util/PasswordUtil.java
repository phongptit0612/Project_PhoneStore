package util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // bam mk
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // kiem tra mk voi mk da bam
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
