package se.ifmo.shared.communication;

import java.io.Serializable;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;

public record Credentials(String username, String password) implements Serializable {

    public Credentials {
        if (password != null) password = sha1Hex(password);
    }

    public static Credentials empty() {
        return new Credentials(null, null);
    }
}
