package de.svws_nrw.base.crypto;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.core.utils.encoding.Base32;

/**
 * Diese Klasse beinhaltet allgemeine Hilfsmethoden für die Nutzung
 * von Kennwörtern.
 */
public final class Passwords {

	private static final SecureRandom random = new SecureRandom();

	/**
	 * Der Pool an Zeichen, welche vom Passwort-Generator verwendet wird. Ziel sind leichte Kennwörter,
	 * wo eine gute Entropie über eine Länge des Kennwortes erreicht wird. Es werden 54 unterschiedliche Zeichen
	 * verwendet.
	 * - 22 Klein-
	 * - 24 Großbuchstaben und
	 * - 8 Zahlen
	 * Verwechselbare Zeichen wie l, I, 1, 0, O werden nicht verwendet
	 */
	private static final String POOL = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";


	private Passwords() {
		throw new IllegalStateException("Instantiation of " + Passwords.class.getName() + " not allowed");
	}


	/**
	 * Erstellt ein zufälliges Kennwort mit der Länge 16, welches Ziffern sowie Groß- und Kleinbuchstaben
	 * beinhaltet.
	 * Z.B. ergibt eine Länge von 16 Zeichen mit der Pool-Größe von 54 Zeichen eine Entropie von ~92 Bit.
	 *
	 * Die Umsetzung ist analog zu der PHP-Implementierung des Projektes in Password.php.
	 *
	 * @param length   die Länge des Kennwortes
	 *
	 * @return das Kennwort
	 */
	public static char[] generateRandomPasswordWithoutSpecialChars(final int length) {
		final char[] password = new char[length];
		final int maxIndex = POOL.length();

		for (int i = 0; i < length; i++) {
			password[i] = POOL.charAt(random.nextInt(maxIndex));
		}

		return password;
	}


	/**
	 * Erstellt ein zufälliges Kennwort mit der Länge 16, welches Ziffern sowie Groß- und Kleinbuchstaben
	 * beinhaltet.
	 * Eine Länge von 16 Zeichen, ergibt mit der Pool-Größe von 54 Zeichen eine Entropie von ~92 Bit.
	 *
	 * Die Umsetzung ist analog zu der PHP-Implementierung des Projektes in Password.php.
	 *
	 * @return das Kennwort
	 */
	public static char[] generateRandomPasswordWithoutSpecialChars() {
		return generateRandomPasswordWithoutSpecialChars(16);
	}


	/**
	 * Erstellt ein zufälliges Kennwort mit der angegebenen Länge, welches
	 * Ziffern, Sonderzeichen (33-35) sowie Groß- und Kleinbuchstaben
	 * beinhaltet
	 *
	 * @param length   die Länge des Kennwortes
	 *
	 * @return das Kennwort
	 */
	public static String generateRandomPassword(final int length) {
		// define char types and order
		final List<Integer> types = new ArrayList<>();
		types.add(0); // special char
		types.add(1); // digit
		types.add(2); // lowercase
		types.add(6); // uppercase
		for (int i = 4; i < length; i++) {
			types.add(random.nextInt(10));
		}
		Collections.shuffle(types);
		// randomly create chars by type and order
		final char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = switch (types.get(i)) {
				case 0 -> (char) random.nextInt(33, 45); // special char
				case 1 -> (char) random.nextInt(48, 58); // digit
				case 2, 3, 4, 5 -> (char) random.nextInt(97, 123); // lowercase
				default -> (char) random.nextInt(65, 91); // uppercase
			};
		}
		return new String(chars);
	}

	/**
	 * Generiert ein zufälliges 160-Bit TOTP-Secret.
	 *
	 * @return das neue TOTP-Secret.
	 */
	public static String generateTotpSecret() {
		final byte[] secretBytes = new byte[20];
		random.nextBytes(secretBytes);
		return Base32.encode(secretBytes);
	}

}
