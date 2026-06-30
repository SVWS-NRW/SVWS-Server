package de.svws_nrw.service.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Diese Utility Klasse stellt Funktionalität zur Verfügung, um Hashes zu erzeugen.
 */
public final class HashUtils {

	private static final String ALGORITHM_SHA_256 = "SHA-256";

	private HashUtils() {
	}

	/**
	 * Erzeugt einen SHA-256 Hash aus dem übergebenen Input.
	 *
	 * @param input der Input
	 * @return der SHA-256 Hash
	 */
	public static String sha256AsHex(final byte[] input) {
		try {
			final MessageDigest digest = MessageDigest.getInstance(ALGORITHM_SHA_256);
			return HexFormat.of().formatHex(digest.digest(input));
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 algorithm not available", e);
		}
	}
}
