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
 * Testklasse für vCards in der Version V3
 *
 */
public class VCardV3Test {
	private static final String ERIKA_MUSTERMANN = "BEGIN:VCARD\r\n" + "VERSION:3.0\r\n" + "FN:Dr. Erika Mustermann\r\n"
			+ "N:Mustermann;Erika;;Dr.;\r\n" + "ORG:Wikimedia\r\n" + "ROLE:Kommunikation\r\n"
			+ "TITLE:Redaktion & Gestaltung\r\n" + "TEL;TYPE=WORK,VOICE:+49 221 9999123\r\n"
			+ "TEL;TYPE=HOME,VOICE:+49 221 1234567\r\n" + "ADR;TYPE=HOME:;;Heidestraße 17;Köln;;51147;Germany\r\n"
			+ "EMAIL;TYPE=PREF,INTERNET:erika@mustermann.de\r\n" + "URL:http://de.wikipedia.org/\r\n"
			+ "REV:2014-03-01T22:11:10Z\r\n" + "END:VCARD";
	private static final String MAX_MUSTERMANN_VCF = "BEGIN:VCARD\r\n" + "VERSION:3.0\r\n" + "FN:Max Mustermann\r\n"
			+ "N:Mustermann;Max;;;\r\n" + "ADR:;;Musterstraße 1b;Musterstadt;;1111;\r\n" + "EMAIL:mail@server.de\r\n"
			+ "URL:maxmustermann.de\r\n" + "CATEGORIES:Klasse-9k-2018-2\r\n" + "TEL;TYPE=VOICE:+49 123 45 678\r\n"
			+ "TEL;TYPE=FAX:+49 987 654 321\r\n" + "TEL;TYPE=CELL:+49 555 555 55555\r\n"
			+ "UID:3fdfc8af-135c-3331-8d8b-ebed54ee74db\r\n" + "END:VCARD";

	private static final String FULLNAME_VCF = "BEGIN:VCARD\r\n" + "VERSION:3.0\r\n" + "FN:Der Fullname der VCard\r\n"
			+ "END:VCARD";

	private static final String NAME_VCF = "BEGIN:VCARD\r\n" + "VERSION:3.0\r\n" + "FN:Hnr Prfx Given Family\r\n"
			+ "N:Family;Given;Additional1,Additional2;Hnr,Prfx;\r\n" + "END:VCARD";

	/**
	 * Testet die Serialisierung einer VCard mit gegebenen Parametern und gleicht
	 * sie ab.
	 */
	@Test
	void serializationTest() {
		NameProperty np = new NameProperty();
		np.setFamilyName("Mustermann");
		np.setGivenName("Erika");
		np.getHonorificPrefixes().add("Dr.");
		VCard vc = new VCard(np);
		vc.setVersion(Version.V3);
		vc.addProperty(new SimpleProperty("ORG", "Wikimedia"));
		vc.addProperty(new SimpleProperty("ROLE", "Kommunikation"));
		vc.addProperty(new SimpleProperty("TITLE", "Redaktion & Gestaltung"));
		vc.addProperty(new PhoneProperty("WORK,VOICE", "+49 221 9999123"));
		vc.addProperty(new PhoneProperty("HOME,VOICE", "+49 221 1234567"));
		AddressProperty ap = new AddressProperty();
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

		String serialized = vc.serialize();
		assertEquals(ERIKA_MUSTERMANN, serialized);
	}

	/**
	 * Testet die Serialisierung einer VCard mit einem gegebenen Kontakt und gleicht
	 * den serialisierten String ab
	 */
	@Test
	void serializationFromKontaktTest() {
		AdressbuchKontakt k = new AdressbuchKontakt();
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
		VCard vc = VCard.createVCard(k);
		vc.setVersion(Version.V3);
		String serialized = vc.serialize();
		assertEquals(MAX_MUSTERMANN_VCF, serialized);
	}

	/**
	 * Testet die Serialisierung einer VCard die ausschließlich aus dem
	 * {@link FullnameProperty} besteht und gleicht den serialisierten String ab
	 */
	@Test
	void serializeFullnameVCF() {
		FullnameProperty fn = new FullnameProperty("Der Fullname der VCard");
		VCard vc = new VCard(fn);
		vc.setVersion(Version.V3);
		String serialized = vc.serialize();
		assertEquals(FULLNAME_VCF, serialized);
	}

	/**
	 * Testet die Serialisierung einer VCard die aus dem {@link NameProperty}
	 * erstellt wurde und gleicht den serialisierten String ab
	 */
	@Test
	void serializeNameVCF() {
		NameProperty np = new NameProperty();
		np.setFamilyName("Family");
		np.setGivenName("Given");
		np.getAdditionalNames().add("Additional1");
		np.getAdditionalNames().add("Additional2");
		np.getHonorificPrefixes().add("Hnr");
		np.getHonorificPrefixes().add("Prfx");
		VCard vCard = new VCard(np);
		vCard.setVersion(Version.V3);
		String serialize = vCard.serialize();
		assertEquals(NAME_VCF, serialize);
	}
}
