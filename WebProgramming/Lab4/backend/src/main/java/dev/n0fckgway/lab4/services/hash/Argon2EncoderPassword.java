package dev.n0fckgway.lab4.services.hash;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2EncoderPassword {
    private final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    public String hashPassword(String password) {
        return argon2.hash(3, 65536, 1, password.toCharArray());
    }

    public boolean checkPass(String hashPassword, String password) {
        return argon2.verify(hashPassword, password.toCharArray());
    }

}
