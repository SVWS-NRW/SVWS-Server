package de.svws_nrw.base.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prüft die gemeinsamen Regeln für E-Mail-Adressen und den HTML-Body einer E-Mail.
 */
class EmailUtilsTests {

	@Test
	@DisplayName("normalizeAddress entfernt Leerraum am Rand, setzt Kleinbuchstaben und macht aus null einen leeren String")
	void testNormalizeAddress() {
		assertEquals("max.mustermann@schule.de", EmailUtils.normalizeAddress("  Max.Mustermann@Schule.DE\t"));
		assertEquals("", EmailUtils.normalizeAddress(null));
	}

	@Test
	@DisplayName("isValidEmail akzeptiert nur eine einzelne Adresse in einfacher Form mit einem Punkt in der Domain")
	void testIsValidEmail() {
		assertTrue(EmailUtils.isValidEmail("max.mustermann@schule.de"));
		assertTrue(EmailUtils.isValidEmail("max+eltern@mail.schule.nrw.de"));
		assertTrue(EmailUtils.isValidEmail("max@müller.de"));
		assertFalse(EmailUtils.isValidEmail(null));
		assertFalse(EmailUtils.isValidEmail("  "));
		assertFalse(EmailUtils.isValidEmail("keine-adresse"));
		assertFalse(EmailUtils.isValidEmail("max@"));
		assertFalse(EmailUtils.isValidEmail(" max@schule.de"));
		assertFalse(EmailUtils.isValidEmail("mumpitz@pluemper"));
		assertFalse(EmailUtils.isValidEmail("max@schule.de."));
		assertFalse(EmailUtils.isValidEmail("max@schule..de"));
		assertFalse(EmailUtils.isValidEmail("max@[127.0.0.1]"));
		assertFalse(EmailUtils.isValidEmail("Max <max@schule.de>"));
		assertFalse(EmailUtils.isValidEmail("max@schule.de (Max)"));
		assertFalse(EmailUtils.isValidEmail("a@schule.de,b@schule.de"));
		assertFalse(EmailUtils.isValidEmail("gruppe:a@schule.de;"));
		assertFalse(EmailUtils.isValidEmail("\"a b\"@schule.de"));
		assertFalse(EmailUtils.isValidEmail("\"a,b\"@schule.de"));
	}

	@Test
	@DisplayName("isValidEmail lässt vor dem @ nur sichtbare ASCII-Zeichen zu")
	void testIsValidEmailLokalerTeil() {
		assertTrue(EmailUtils.isValidEmail("max_m.!#$%&'*+-/=?^`{|}~9@schule.de"));
		assertFalse(EmailUtils.isValidEmail("müller@schule.de"));
		// Das untere Byte dieser Zeichen ist 'a', '@', '>', ein Leerzeichen und ein Zeilenumbruch.
		assertFalse(EmailUtils.isValidEmail("mšx@schule.de"));
		assertFalse(EmailUtils.isValidEmail("opferŀsmail.deľĠx@schule.de"));
		assertFalse(EmailUtils.isValidEmail("ačĊb@schule.de"));
		assertFalse(EmailUtils.isValidEmail("max\u0085@schule.de"));
		assertFalse(EmailUtils.isValidEmail("max\u007F@schule.de"));
	}

	@Test
	@DisplayName("isValidEmail lehnt Adressen über 254 Zeichen und Domains ohne ASCII-Form ab")
	void testIsValidEmailLaengeUndDomain() {
		final String domain = "@" + "s".repeat(60) + "." + "c".repeat(60) + "." + "h".repeat(60) + ".de";
		assertTrue(EmailUtils.isValidEmail("m".repeat(254 - domain.length()) + domain));
		assertFalse(EmailUtils.isValidEmail("m".repeat(255 - domain.length()) + domain));
		assertFalse(EmailUtils.isValidEmail("max@" + "a".repeat(64) + ".de"));
	}

	@Test
	@DisplayName("toAsciiDomain wandelt nur die Domain in die ASCII-Form um")
	void testToAsciiDomain() {
		assertEquals("Müller@xn--mller-kva.de", EmailUtils.toAsciiDomain("Müller@müller.de"));
		assertEquals("max@schule.de", EmailUtils.toAsciiDomain("max@schule.de"));
	}

	@Test
	@DisplayName("isBlockedDomain erkennt die Domains der SchILD-Anonymisierung, ihre Subdomains und Unicode-Schreibweisen")
	void testIsBlockedDomain() {
		assertTrue(EmailUtils.isBlockedDomain("max@smail.de"));
		assertTrue(EmailUtils.isBlockedDomain("max@lmail.de"));
		assertFalse(EmailUtils.isBlockedDomain("max@schule.de"));
		assertTrue(EmailUtils.isBlockedDomain("max@mail.smail.de"));
		assertFalse(EmailUtils.isBlockedDomain("max@nosmail.de"));
		assertTrue(EmailUtils.isValidEmail("max@ｓmail.de"));
		assertTrue(EmailUtils.isBlockedDomain("max@ｓmail.de"));
		assertTrue(EmailUtils.isBlockedDomain("max@mx.ｌmail.de"));
	}

	@Test
	@DisplayName("isValidEmail lehnt Unicode-Punkte in der Domain ab, weil sie als Punkt am Ende der ASCII-Form die Sperre umgingen")
	void testIsValidEmailUnicodePunkt() {
		assertFalse(EmailUtils.isValidEmail("max@smail.de。"));
		assertFalse(EmailUtils.isValidEmail("max@smail.de．"));
		assertFalse(EmailUtils.isValidEmail("max@smail.de｡"));
		assertFalse(EmailUtils.isValidEmail("max@mx.smail.de。"));
	}

	@Test
	@DisplayName("createHtmlBody maskiert den Text, trennt Absätze an Leerzeilen und setzt Zeilenumbrüche und Tabs um")
	void testCreateHtmlBody() {
		assertEquals("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"></head><body>"
				+ "<p>Hallo &lt;b&gt;Welt&lt;/b&gt; &amp; &quot;Co&quot;<br>Zeile 2</p><p>Absatz    2</p></body></html>",
				EmailUtils.createHtmlBody("  Hallo <b>Welt</b> & \"Co\"\r\nZeile 2  \r \n\n\nAbsatz\t2"));
	}

}
