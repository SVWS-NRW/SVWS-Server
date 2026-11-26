package de.svws_nrw.base.email;

import java.net.IDN;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.validation.constraints.NotNull;

/**
 * Hilfsmethoden, die jeder E-Mail-Versand des Servers vor dem Einreihen eines {@link EmailJob} braucht: Adressen bereinigen und prüfen,
 * gesperrte Domains erkennen und den Text einer E-Mail als HTML setzen. Sie liegen hier, damit API und Reporting dieselben Regeln anwenden.
 */
public final class EmailUtils {

	/** Domains des Anonymisierungs-Tools von SchILD-NRW. Sie existieren real und stehen in vielen Beispieldatenbanken. Ihre Subdomains sind mitgesperrt. */
	private static final Set<String> BLOCKED_EMAIL_DOMAINS = Set.of("smail.de", "lmail.de");

	/** Die höchste Länge einer Adresse, die SMTP zulässt. */
	private static final int MAX_ADDRESS_LENGTH = 254;

	/**
	 * Lokaler Teil und Domain ohne Leerraum und ohne Zeichen mit Sonderbedeutung, die Domain mit mindestens einem Punkt. Der lokale Teil besteht
	 * nur aus sichtbaren ASCII-Zeichen, weil der Versand jedes andere Zeichen auf ein Byte kürzt und die E-Mail so an eine andere Adresse ginge.
	 */
	private static final Pattern SIMPLE_ADDRESS =
			Pattern.compile("[\\x21-\\x7E&&[^\"(),:;<>@\\[\\]\\\\]]+@[^\\s\"(),:;<>@\\[\\]\\\\.]+(\\.[^\\s\"(),:;<>@\\[\\]\\\\.]+)+");


	private EmailUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/**
	 * Entfernt Leerraum am Rand und setzt die Adresse in Kleinbuchstaben.
	 *
	 * @param address   die Adresse, null wird wie eine leere Adresse behandelt
	 *
	 * @return die bereinigte Adresse oder ein leerer String
	 */
	public static @NotNull String normalizeAddress(final String address) {
		return (address == null) ? "" : address.strip().toLowerCase(Locale.ROOT);
	}


	/**
	 * Prüft, ob eine Adresse eine einzelne gültige E-Mail-Adresse in einfacher Form ist: ohne Anzeigenamen, Gruppe, Kommentar oder
	 * Anführungszeichen, vor dem @ nur mit ASCII-Zeichen und mit einem Punkt in der Domain.
	 *
	 * @param address   die zu prüfende Adresse
	 *
	 * @return true, wenn die Adresse gültig ist, und ansonsten false
	 */
	public static boolean isValidEmail(final String address) {
		// Die SMTP-Session entfernt Leerzeichen und trennt an Kommas, verfälscht also jede Form jenseits der einfachen. Eine Domain ohne Punkt
		// erreicht außerhalb eines lokalen Netzes keinen Empfänger.
		if ((address == null) || (address.length() > MAX_ADDRESS_LENGTH) || !SIMPLE_ADDRESS.matcher(address).matches()) {
			return false;
		}
		try {
			// Eine Domain ohne ASCII-Form lässt sich per SMTP nicht erreichen.
			toAsciiDomain(address);
			final InternetAddress internetAddress = new InternetAddress(address, true);
			return !internetAddress.isGroup() && address.equals(internetAddress.getAddress());
		} catch (@SuppressWarnings("unused") final AddressException | IllegalArgumentException e) {
			return false;
		}
	}


	/**
	 * Wandelt nur die Domain einer Adresse in die ASCII-Form um, die SMTP verlangt. IDN gilt nur für Domainnamen, auf die ganze Adresse angewendet
	 * entstünde eine andere Adresse.
	 *
	 * @param address   die Adresse
	 *
	 * @return die Adresse mit der Domain in ASCII-Form
	 *
	 * @throws IllegalArgumentException   wenn die Domain keine gültige ASCII-Form hat
	 */
	public static @NotNull String toAsciiDomain(final @NotNull String address) {
		final int at = address.lastIndexOf('@');
		if (at < 0) {
			return address;
		}
		return address.substring(0, at + 1) + IDN.toASCII(address.substring(at + 1));
	}


	/**
	 * Prüft, ob die Domain der Adresse oder eine ihrer übergeordneten Domains für den Versand gesperrt ist. Verglichen wird die ASCII-Form, weil
	 * auch Unicode-Schreibweisen wie Vollbreiten-Buchstaben beim Versand zur gesperrten Domain werden.
	 *
	 * @param address   eine gültige Adresse in Kleinbuchstaben
	 *
	 * @return true, wenn die Domain gesperrt ist, und ansonsten false
	 */
	public static boolean isBlockedDomain(final @NotNull String address) {
		final String asciiAddress = toAsciiDomain(address).toLowerCase(Locale.ROOT);
		final String domain = asciiAddress.substring(asciiAddress.lastIndexOf('@') + 1);
		for (final String blocked : BLOCKED_EMAIL_DOMAINS) {
			if (domain.equals(blocked) || domain.endsWith("." + blocked)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Setzt den reinen Text als HTML. Er wird maskiert, damit der Text keine eigenen HTML-Elemente einschleusen kann. Leerzeilen trennen Absätze,
	 * einfache Zeilenumbrüche werden zu {@code <br>}.
	 *
	 * @param text   der reine Text
	 *
	 * @return der HTML-Body der E-Mail
	 */
	public static @NotNull String createHtmlBody(final @NotNull String text) {
		final String escaped = htmlEscape(normalizePlainText(text));
		// Ein minimales HTML-Grundgerüst, damit E-Mail-Programme den Text als HTML darstellen.
		final StringBuilder html = new StringBuilder(escaped.length() + 128);
		html.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"></head><body>");
		for (final String paragraph : escaped.split("\\r\\n\\r\\n")) {
			final String paragraphWithBr = paragraph.replace("\r\n", "<br>");
			if (paragraphWithBr.isBlank()) {
				html.append("<p><br></p>");
			} else {
				html.append("<p>").append(paragraphWithBr).append("</p>");
			}
		}
		html.append("</body></html>");
		return html.toString();
	}


	/**
	 * Vereinheitlicht den Text: Zeilen ohne Leerraum am Rand, höchstens eine Leerzeile am Stück, Tabs als vier Leerzeichen und Zeilenumbrüche
	 * im CRLF-Format, das alle E-Mail-Programme verstehen.
	 *
	 * @param plainText   der ursprüngliche Text
	 *
	 * @return der vereinheitlichte Text
	 */
	private static @NotNull String normalizePlainText(final @NotNull String plainText) {
		final String[] lines = plainText.replace("\r\n", "\n").replace("\r", "\n").split("\n", -1);
		final StringBuilder result = new StringBuilder(plainText.length());
		boolean lastLineEmpty = false;
		for (final String line : lines) {
			final String stripped = line.strip();
			if (stripped.isEmpty()) {
				if (!lastLineEmpty) {
					result.append('\n');
					lastLineEmpty = true;
				}
			} else {
				result.append(stripped.replace("\t", "    ")).append('\n');
				lastLineEmpty = false;
			}
		}
		if (!result.toString().endsWith("\n\n")) {
			result.append(result.toString().endsWith("\n") ? "\n" : "\n\n");
		}
		return result.toString().replace("\n", "\r\n");
	}


	/**
	 * Ersetzt die Zeichen, die in HTML eine Bedeutung haben, durch ihre Entities.
	 *
	 * @param text   der zu maskierende Text
	 *
	 * @return der maskierte Text
	 */
	private static @NotNull String htmlEscape(final @NotNull String text) {
		final StringBuilder result = new StringBuilder(text.length());
		for (int i = 0; i < text.length(); i++) {
			final char c = text.charAt(i);
			switch (c) {
				case '&' -> result.append("&amp;");
				case '<' -> result.append("&lt;");
				case '>' -> result.append("&gt;");
				case '"' -> result.append("&quot;");
				case '\'' -> result.append("&#39;");
				default -> result.append(c);
			}
		}
		return result.toString();
	}

}
