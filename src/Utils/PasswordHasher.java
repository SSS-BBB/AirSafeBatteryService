package Utils;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHasher {

	// From Chat GPT: ;-;

	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final String FORMAT = "pbkdf2-sha256";

	private static final int ITERATIONS = 600_000;
	private static final int SALT_BYTES = 16;
	private static final int HASH_BYTES = 32;

	// Limits accepted when reading stored hashes.
	private static final int MIN_ITERATIONS = 100_000;
	private static final int MAX_ITERATIONS = 2_000_000;

	private static final SecureRandom RANDOM = new SecureRandom();

	private PasswordHasher() {
	}

	public static String hashPassword(char[] password) throws GeneralSecurityException {

		if (password == null) {
			throw new IllegalArgumentException("Password cannot be null");
		}

		byte[] salt = new byte[SALT_BYTES];
		RANDOM.nextBytes(salt);

		byte[] hash = deriveKey(password, salt, ITERATIONS);

		try {
			Base64.Encoder encoder = Base64.getEncoder();

			return FORMAT + "$" + ITERATIONS + "$" + encoder.encodeToString(salt) + "$" + encoder.encodeToString(hash);
		} finally {
			Arrays.fill(hash, (byte) 0);
			Arrays.fill(salt, (byte) 0);
		}

	}

	public static boolean verifyPassword(char[] password, String storedHash) throws GeneralSecurityException {
		if (password == null || storedHash == null) {
			return false;
		}

		String[] parts = storedHash.split("\\$", -1);

		if (parts.length != 4 || !FORMAT.equals(parts[0])) {
			return false;
		}

		final int iterations;
		final byte[] salt;
		final byte[] expectedHash;
		
		try {
            iterations = Integer.parseInt(parts[1]);
            salt = Base64.getDecoder().decode(parts[2]);
            expectedHash = Base64.getDecoder().decode(parts[3]);
        } catch (IllegalArgumentException ex) {
            return false;
        }
		
		if (iterations < MIN_ITERATIONS
                || iterations > MAX_ITERATIONS
                || salt.length != SALT_BYTES
                || expectedHash.length != HASH_BYTES) {
            return false;
        }
		
		byte[] compareHash = deriveKey(password, salt, iterations);
		
		try {
            return MessageDigest.isEqual(
                expectedHash, compareHash);
        } finally {
            Arrays.fill(compareHash, (byte) 0);
            Arrays.fill(expectedHash, (byte) 0);
            Arrays.fill(salt, (byte) 0);
        }
	}

	private static byte[] deriveKey(char[] password, byte[] salt, int iterations) throws GeneralSecurityException {
		PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, HASH_BYTES * 8);

		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);

			return factory.generateSecret(spec).getEncoded();
		} finally {
			spec.clearPassword();
		}
	}
}
