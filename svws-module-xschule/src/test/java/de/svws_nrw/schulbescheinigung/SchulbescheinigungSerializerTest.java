package de.svws_nrw.schulbescheinigung;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathFactory;

import digital.xschule.def.xschule._1_2.xsd.XSchuleSchuelerSchulbescheinigung0004;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests für {@link SchulbescheinigungSerializer}.
 */
@DisplayName("Diese Klasse testet die Klasse SchulbescheinigungSerializer")
class SchulbescheinigungSerializerTest {

	private static final String OUTDATED_VERSION = "1.1";
	private static final String EXPECTED_VERSION = "1.2";

	private static final String XSCHULE_NAMESPACE = "http://xschule.digital/def/xschule/" + EXPECTED_VERSION + "/xsd";
	private static final String XBILDUNG_NAMESPACE = "http://xbildung.de/def/xbildung/" + EXPECTED_VERSION + "/xsd";

	private static final String XSD_PATH = "/xschule/xschule.xsd";

	private SchulbescheinigungSerializer serializer;
	private XPathFactory xPathFactory;

	@BeforeEach
	void setUp() {
		serializer = new SchulbescheinigungSerializer();
		xPathFactory = XPathFactory.newInstance();
	}

	@Test
	@DisplayName("serialize | Erfolg - XML nicht leer")
	void serializeErzeugtNichtLeeresXml() {
		final var bescheinigungBuilder = defaultBuilder();
		assertThat(serialize(bescheinigungBuilder)).isNotEmpty();
	}

	@Test
	@DisplayName("serialize | Erfolg - XPath enthält Nachname")
	void serializeEnthaeltNachname() throws Exception {
		final var doc = toDocument(serialize(defaultBuilder()), false);

		assertThat(xpath(doc, "//familienname/name")).isEqualTo("Musterfrau");
	}

	@Test
	@DisplayName("serialize | Erfolg - XPath enthält Vorname")
	void serializeEnthaeltVorname() throws Exception {
		final var doc = toDocument(serialize(defaultBuilder()), false);

		assertThat(xpath(doc, "//vorname/name")).isEqualTo("Marianne");
	}

	@Test
	@DisplayName("serialize | Erfolg - XPath enthält Geburtsdatum")
	void serializeEnthaeltGeburtsdatum() throws Exception {
		final var doc = toDocument(serialize(defaultBuilder()), false);

		assertThat(xpath(doc, "//geburt/datum/jahrMonatTag")).isEqualTo("2012-10-23");
	}

	@Test
	@DisplayName("serialize | Erfolg - XPath enthält Schulname")
	void serializeEnthaeltSchulname() throws Exception {
		final var doc = toDocument(serialize(defaultBuilder()), false);

		assertThat(xpath(doc, "//schule/name/name")).isEqualTo("Test-Gymnasium");
	}

	@Test
	@DisplayName("serialize | Erfolg - XPath enthält Bildungsgang-Enddatum")
	void serializeEnthaeltBildungsgangEnddatum() throws Exception {
		final var doc = toDocument(serialize(defaultBuilder()), false);

		assertThat(xpath(doc, "//schulbesuch/zeitraum/ende")).isEqualTo("2031-07-31");
	}

	@Test
	@DisplayName("serialize | Erfolg - XPath enthält Sprache")
	void serializeEnthaeltSprache() throws Exception {
		final var doc = toDocument(serialize(defaultBuilder()), false);
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

	// -------------------------------------------------------------------------
	// Namespace-Tests
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("serialize | Namespace - Root-Element trägt korrekten XSchule-Namespace")
	void serialisiertesXmlTraegtKorrektenNamespace() throws Exception {
		final var doc = toDocument(serialize(defaultBuilder()), true);

		assertThat(doc.getDocumentElement().getNamespaceURI()).isEqualTo(XSCHULE_NAMESPACE);
	}

	@Test
	@DisplayName("serialize | Namespace - Root-Element trägt nicht den veralteten " + OUTDATED_VERSION + "-Namespace")
	void serialisiertesXmlTraegtNichtVeraltetenNamespace() throws Exception {
		final var doc = toDocument(serialize(defaultBuilder()), true);

		assertThat(doc.getDocumentElement().getNamespaceURI())
				.isNotEqualTo("http://xschule.digital/def/xschule/%s/xsd".formatted(OUTDATED_VERSION));
	}

	@Test
	@DisplayName("serialize | Namespace - Namespace ist im serialisierten XML-String enthalten")
	void serialisiertesXmlEnthaeltNamespaceAlsString() {
		final var out = new ByteArrayOutputStream();
		serializer.serialize(defaultBuilder().build(), out);

		assertThat(out.toString()).contains(XSCHULE_NAMESPACE);
	}

	// -------------------------------------------------------------------------
	// XSD-Validierungstests
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("serialize | XSD-Validierung - Vollständige Schulbescheinigung ist schema-konform")
	void vollstaendigeSchulbescheinigungIstSchemaKonform() {
		final var xmlBytes = serialize(defaultBuilder());

		assertThatNoException().isThrownBy(() -> validateGegenXsd(xmlBytes));
	}

	@Test
	@DisplayName("serialize | XSD-Validierung - Nur Pflichtfelder gesetzt ist schema-konform")
	void nurPflichtfelderIstSchemaKonform() {
		final var xmlBytes = serialize(
				new SchulbescheinigungBuilder()
						.schuelerNachname("Musterfrau")
						.schuelerVorname("Marianne")
						.schuleName("Test-Gymnasium")
						.bildungsgangEnddatum("2031-07-31")
						.sprache("deu")
		);

		assertThatNoException().isThrownBy(() -> validateGegenXsd(xmlBytes));
	}

	@Test
	@DisplayName("validateGegenXsd | XSD-Validierung - Schema-invalides XML wird abgelehnt")
	void schemaInvalidesXmlWirdAbgelehnt() {
		// XML mit korrektem Namespace, aber fehlendem Pflichtfeld "schueler" —
		// schema-invalide gemäß xschule-nachweise.xsd
		final var invalidXml = ("""
				<?xml version="1.0" encoding="UTF-8"?>
				<xsc:schueler.schulbescheinigung.0004xmlns:xsc="https://xschule.digital/def/xschule/%s/xsd">
				</xsc:schueler.schulbescheinigung.0004>
				""".formatted(EXPECTED_VERSION)).getBytes(UTF_8);

		assertThatThrownBy(() -> validateGegenXsd(invalidXml))
				.isInstanceOf(SAXException.class);
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

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

	private byte[] serialize(final SchulbescheinigungBuilder builder) {
		final var out = new ByteArrayOutputStream();
		serializer.serialize(builder.build(), out);
		return out.toByteArray();
	}

	private Document toDocument(final byte[] xmlBytes, final boolean isNamespaceAware) throws Exception {
		final var factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(isNamespaceAware);
		return factory.newDocumentBuilder().parse(new ByteArrayInputStream(xmlBytes));
	}

	/**
	 * Validiert die übergebenen XML-Bytes gegen die XSD aus dem Classpath.
	 * Wirft {@link SAXException} bei Schema-Verletzung.
	 *
	 * @param xmlBytes die zu validierenden XML-Bytes
	 * @throws Exception bei IO- oder Parse-Fehlern
	 */
	private void validateGegenXsd(final byte[] xmlBytes) throws Exception {

		final var xsdUrl = getClass().getResource(XSD_PATH);
		assertThat(xsdUrl).as("XSD nicht im Classpath gefunden: %s".formatted(XSD_PATH)).isNotNull();

		final var schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		final var schema = schemaFactory.newSchema(xsdUrl);
		final var validator = schema.newValidator();
		validator.validate(new StreamSource(new ByteArrayInputStream(xmlBytes)));
	}

	private String xpath(final Document doc, final String expression) throws Exception {
		final var xPath = xPathFactory.newXPath();
		xPath.setNamespaceContext(new XSchuleNamespaceContext());
		return xPath.evaluate(expression, doc);
	}

	private record XSchuleNamespaceContext() implements NamespaceContext {
		@Override
		public String getNamespaceURI(final String prefix) {
			return switch (prefix) {
				case "xsc" -> XSCHULE_NAMESPACE;
				case "xb"  -> XBILDUNG_NAMESPACE;
				default    -> XMLConstants.NULL_NS_URI;
			};
		}
		@Override public String getPrefix(final String ns) {
			return null;
		}
		@Override public java.util.Iterator<String> getPrefixes(final String ns) {
			return null;
		}
	}
}
