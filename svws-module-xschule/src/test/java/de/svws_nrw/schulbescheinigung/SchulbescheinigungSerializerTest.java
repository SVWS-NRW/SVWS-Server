package de.svws_nrw.schulbescheinigung;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;

import digital.xschule.def.xschule._1_1.xsd.XSchuleSchuelerSchulbescheinigung0004;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests für {@link SchulbescheinigungSerializer}.
 */
@DisplayName("Diese Klasse testet die Klasse SchulbescheinigungSerializer")
class SchulbescheinigungSerializerTest {

	private SchulbescheinigungSerializer serializer;
	private XPathFactory xPathFactory;

	@BeforeEach
	void setUp() {
		serializer = new SchulbescheinigungSerializer();
		xPathFactory = XPathFactory.newInstance();
	}

	private SchulbescheinigungBuilder defaultBuilder() {
		return new SchulbescheinigungBuilder()
				.titel("Schulbescheinigung")
				.sprache("deu")
				.ausstellungOrt("Testort")
				.ausstellungDatum("2026-01-01")
				.schuelerNachname("Musterfrau")
				.schuelerVorname("Marianne")
				.schuelerGeburtsdatum("2012-10-23")
				.bildungsgangEnddatum("2031-07-31")
				.schuleName("Test-Gymnasium");
	}

	private Document toDocument(final XSchuleSchuelerSchulbescheinigung0004 bescheinigung) throws Exception {
		final var out = new ByteArrayOutputStream();
		serializer.serialize(bescheinigung, out);
		final var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return builder.parse(new ByteArrayInputStream(out.toByteArray()));
	}

	private String xpath(final Document doc, final String expression) throws Exception {
		return xPathFactory.newXPath().evaluate(expression, doc);
	}

	@Test
	@DisplayName("serialize | Erfolg - XML nicht leer")
	void serializeErzeugtNichtLeeresXml() {
		final var bescheinigung = defaultBuilder().build();
		final var out = new ByteArrayOutputStream();

		serializer.serialize(bescheinigung, out);

		assertThat(out.toByteArray()).isNotEmpty();
	}

	@Test
	@DisplayName("serialize | Erfolg - XPath enthält Nachname")
	void serializeEnthaeltNachname() throws Exception {
		final var doc = toDocument(defaultBuilder().build());

		assertThat(xpath(doc, "//familienname/name")).isEqualTo("Musterfrau");
	}

	@Test
	@DisplayName("serialize | Erfolg - XPath enthält Vorname")
	void serializeEnthaeltVorname() throws Exception {
		final var doc = toDocument(defaultBuilder().build());

		assertThat(xpath(doc, "//vorname/name")).isEqualTo("Marianne");
	}

	@Test
	@DisplayName("serialize | Erfolg - XPath enthält Geburtsdatum")
	void serializeEnthaeltGeburtsdatum() throws Exception {
		final var doc = toDocument(defaultBuilder().build());

		assertThat(xpath(doc, "//geburt/datum/jahrMonatTag")).isEqualTo("2012-10-23");
	}

	@Test
	@DisplayName("serialize | Erfolg - XPath enthält Schulname")
	void serializeEnthaeltSchulname() throws Exception {
		final var doc = toDocument(defaultBuilder().build());

		assertThat(xpath(doc, "//schule/name/name")).isEqualTo("Test-Gymnasium");
	}

	@Test
	@DisplayName("serialize | Erfolg - XPath enthält Bildungsgang-Enddatum")
	void serializeEnthaeltBildungsgangEnddatum() throws Exception {
		final var doc = toDocument(defaultBuilder().build());

		assertThat(xpath(doc, "//schulbesuch/zeitraum/ende")).isEqualTo("2031-07-31");
	}

	@Test
	@DisplayName("serialize | Erfolg - XPath enthält Sprache")
	void serializeEnthaeltSprache() throws Exception {
		final var doc = toDocument(defaultBuilder().build());

		assertThat(xpath(doc, "//sprache/code")).isEqualTo("deu");
	}

	@Test
	@DisplayName("serialize | Bescheinigung null")
	void serializeMitNullBescheinigungWirftException() {
		final var out = new ByteArrayOutputStream();

		assertThatThrownBy(() -> serializer.serialize(null, out))
				.isInstanceOf(NullPointerException.class);
	}

	@Test
	@DisplayName("serialize | OutputStream null")
	void serializeMitNullOutputStreamWirftException() {
		final var bescheinigung = defaultBuilder().build();

		assertThatThrownBy(() -> serializer.serialize(bescheinigung, null))
				.isInstanceOf(NullPointerException.class);
	}

	@Test
	@DisplayName("serialize | Ungültiges Objekt")
	void serializeMitUngueltigemObjektWirftException() {
		final var bescheinigung = new XSchuleSchuelerSchulbescheinigung0004(); // leer, kein Pflichtfeld gesetzt
		final var out = new ByteArrayOutputStream();

		assertThatThrownBy(() -> serializer.serialize(bescheinigung, out))
				.isInstanceOf(SchulbescheinigungSerializerException.class);
	}

}
