package de.svws_nrw.base.crypto;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Diese Klasse beinhaltet allgemeine Hilfsmethoden für die Nutzung
 * von Kennwörtern.
 */
public final class Passwords {

	private static final Random random = new SecureRandom();

	private Passwords() {
		throw new IllegalStateException("Instantiation of " + Passwords.class.getName() + " not allowed");
	}


	/**
	 * Erstellt ein zufälliges Kennwort mit der angegebenen Länge, welches
	 * Ziffern sowie Groß- und Kleinbuchstaben beinhaltet
	 *
	 * @param length   die Länge des Kennwortes
	 *
	 * @return das Kennwort
	 */
	public static String generateRandomPasswordWithoutSpecialChars(final int length) {
		// define char types and order
		final List<Integer> types = new ArrayList<>();
		types.add(1); // digit
		types.add(2); // lowercase
		types.add(6); // uppercase
		for (int i = 3; i < length; i++)
			types.add(random.nextInt(10));
		Collections.shuffle(types);
		// randomly create chars by type and order
		final char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = switch (types.get(i)) {
				case 0, 1 -> (char) random.nextInt(48, 58); // digit
				case 2, 3, 4, 5 -> (char) random.nextInt(97, 123); // lowercase
				default -> (char) random.nextInt(65, 91); // uppercase
			};
		}
		return new String(chars);
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
		for (int i = 4; i < length; i++)
			types.add(random.nextInt(10));
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


}
