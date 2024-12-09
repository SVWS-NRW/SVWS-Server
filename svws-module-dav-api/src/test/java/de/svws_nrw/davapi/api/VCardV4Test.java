package de.svws_nrw.davapi.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.svws_nrw.core.data.adressbuch.AdressbuchKontakt;
import de.svws_nrw.core.data.adressbuch.Telefonnummer;
import de.svws_nrw.davapi.util.vcard.AddressProperty;
import de.svws_nrw.davapi.util.vcard.EMailProperty;
import de.svws_nrw.davapi.util.vcard.FullnameProperty;
import de.svws_nrw.davapi.util.vcard.NameProperty;
import de.svws_nrw.davapi.util.vcard.PhoneProperty;
import de.svws_nrw.davapi.util.vcard.SimpleProperty;
import de.svws_nrw.davapi.util.vcard.VCard;
import de.svws_nrw.davapi.util.vcard.VCard.Version;

import org.junit.jupiter.api.Test;

/**
 * Testklasse für vCards in der Version 4
 *
 */
class VCardV4Test {

	private static final String ERIKA_MUSTERMANN = """
		BEGIN:VCARD\r
		VERSION:4.0\r
		KIND:individual\r
		FN:Dr. Erika Mustermann\r
		N:Mustermann;Erika;;Dr.;\r
		ORG:Wikimedia\r
		ROLE:Kommunikation\r
		TITLE:Redaktion & Gestaltung\r
		TEL;TYPE=WORK,VOICE:+49 221 9999123\r
		TEL;TYPE=HOME,VOICE:+49 221 1234567\r
		ADR;TYPE=HOME:;;Heidestraße 17;Köln;;51147;Germany\r
		EMAIL;TYPE=PREF,INTERNET:erika@mustermann.de\r
		URL:http://de.wikipedia.org/\r
		REV:2014-03-01T22:11:10Z\r
		END:VCARD""";

	private static final String MAX_MUSTERMANN_VCF = """
		BEGIN:VCARD\r
		VERSION:4.0\r
		KIND:individual\r
		FN:Max Mustermann\r
		N:Mustermann;Max;;;\r
		ADR:;;Musterstraße 1b;Musterstadt;;1111;\r
		EMAIL:mail@server.de\r
		URL:maxmustermann.de\r
		CATEGORIES:Klasse-9k-2018-2\r
		TEL;TYPE=VOICE:+49 123 45 678\r
		TEL;TYPE=FAX:+49 987 654 321\r
		TEL;TYPE=CELL:+49 555 555 55555\r
		UID:3fdfc8af-135c-3331-8d8b-ebed54ee74db\r
		END:VCARD""";

	private static final String FULLNAME_VCF = """
		BEGIN:VCARD\r
		VERSION:4.0\r
		KIND:individual\r
		FN:Der Fullname der VCard\r
		END:VCARD""";

	private static final String NAME_VCF = """
		BEGIN:VCARD\r
		VERSION:4.0\r
		KIND:individual\r
		FN:Hnr Prfx Given Family\r
		N:Family;Given;Additional1,Additional2;Hnr,Prfx;\r
		END:VCARD""";

	/**
	 * Testet die Serialisierung einer VCard mit gegebenen Parametern und gleicht
	 * sie ab.
	 */
	@Test
	void serializationTest() {
		final NameProperty np = new NameProperty();
		np.setFamilyName("Mustermann");
		np.setGivenName("Erika");
		np.getHonorificPrefixes().add("Dr.");
		final VCard vc = new VCard(np);
		vc.setVersion(Version.V4);
		vc.addProperty(new SimpleProperty("ORG", "Wikimedia"));
		vc.addProperty(new SimpleProperty("ROLE", "Kommunikation"));
		vc.addProperty(new SimpleProperty("TITLE", "Redaktion & Gestaltung"));
		vc.addProperty(new PhoneProperty("WORK,VOICE", "+49 221 9999123"));
		vc.addProperty(new PhoneProperty("HOME,VOICE", "+49 221 1234567"));
		final AddressProperty ap = new AddressProperty();
		ap.setAddressType("HOME");
		ap.setCity("Köln");
		ap.setCountry("Germany");
		ap.setHouseNumber("17");
		ap.setPostalCode("51147");
		ap.setStreet("Heidestraße");
		vc.addProperty(ap);
		vc.addProperty(new EMailProperty("PREF,INTERNET", "erika@mustermann.de"));
		vc.addProperty(new SimpleProperty("URL", "http://de.wikipedia.org/"));
		vc.addProperty(new SimpleProperty("REV", "2014-03-01T22:11:10Z"));

		final String serialized = vc.serialize();
		assertEquals(ERIKA_MUSTERMANN, serialized);
	}

	/**
	 * Testet die Serialisierung einer VCard mit einem gegebenen Kontakt und gleicht
	 * den serialisierten String ab
	 */
	@Test
	void serializationFromKontaktTest() {
		final AdressbuchKontakt k = new AdressbuchKontakt();
		k.email = "mail@server.de";
		k.hausnummer = "1";
		k.hausnummerZusatz = "b";
		k.nachname = "Mustermann";
		k.ort = "Musterstadt";
		k.plz = "1111";
		k.strassenname = "Musterstraße";
		Telefonnummer tel = new Telefonnummer();
		tel.number = "+49 123 45 678";
		tel.type = "VOICE";
		k.telefonnummern.add(tel);
		tel = new Telefonnummer();
		tel.number = "+49 987 654 321";
		tel.type = "FAX";
		k.telefonnummern.add(tel);
		tel = new Telefonnummer();
		tel.number = "+49 555 555 55555";
		tel.type = "CELL";
		k.telefonnummern.add(tel);
		k.vorname = "Max";
		k.webAdresse = "maxmustermann.de";
		k.adressbuchId = ("Klasse-9k-2018-2");
		k.id = ("S_123");
		k.uri = ("https://example.com/db/gymabi/carddav/S_123");
		k.kategorien.add("Klasse-9k-2018-2");
		// FIXME telefonnummern zum test wieder zufügen.
		final VCard vc = VCard.createVCard(k);
		vc.setVersion(Version.V4);
		final String serialized = vc.serialize();
		assertEquals(MAX_MUSTERMANN_VCF, serialized);
	}

	/**
	 * Testet die Serialisierung einer VCard die ausschließlich aus dem
	 * {@link FullnameProperty} besteht und gleicht den serialisierten String ab
	 */
	@Test
	void serializeFullnameVCF() {
		final FullnameProperty fn = new FullnameProperty("Der Fullname der VCard");
		final VCard vc = new VCard(fn);
		vc.setVersion(Version.V4);
		final String serialized = vc.serialize();
		assertEquals(FULLNAME_VCF, serialized);
	}

	/**
	 * Testet die Serialisierung einer VCard die aus dem {@link NameProperty}
	 * erstellt wurde und gleicht den serialisierten String ab
	 */
	@Test
	void serializeNameVCF() {
		final NameProperty np = new NameProperty();
		np.setFamilyName("Family");
		np.setGivenName("Given");
		np.getAdditionalNames().add("Additional1");
		np.getAdditionalNames().add("Additional2");
		np.getHonorificPrefixes().add("Hnr");
		np.getHonorificPrefixes().add("Prfx");
		final VCard vc = new VCard(np);
		vc.setVersion(Version.V4);
		final String serialize = vc.serialize();
		assertEquals(NAME_VCF, serialize);
	}
}
