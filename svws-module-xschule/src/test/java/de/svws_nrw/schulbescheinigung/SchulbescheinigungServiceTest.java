package de.svws_nrw.schulbescheinigung;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;

import digital.xschule.def.xschule._1_1.xsd.XSchuleSchuelerSchulbescheinigung0004;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Diese Klasse testet die Klasse SchulbescheinigungService")
class SchulbescheinigungServiceTest {

	private SchulbescheinigungService service;
	private XPathFactory xPathFactory;

	@BeforeEach
	void setUp() {
		final var serializer = new SchulbescheinigungSerializer();
		service = new SchulbescheinigungService(serializer);
		xPathFactory = XPathFactory.newInstance();
	}

	private XSchuleSchuelerSchulbescheinigung0004 defaultSchulbescheinigung() {
		return new SchulbescheinigungBuilder()
				.titel("Schulbescheinigung")
				.sprache("deu")
				.ausstellungOrt("Testort")
				.ausstellungDatum("2026-01-01")
				.schuelerNachname("Musterfrau")
				.schuelerVorname("Marianne")
				.schuelerGeburtsdatum("2012-10-23")
				.bildungsgangEnddatum("2031-07-31")
				.schuleName("Test-Gymnasium")
				.build();
	}

	@Test
	@DisplayName("createSchulbescheinigungXml | Erfolg - Liefert nicht leeren String")
	void createSchulbescheinigungXmlLiefertNichtLeerenString() {
		final var xml = service.createSchulbescheinigungXml(defaultSchulbescheinigung());

		assertThat(xml).isNotBlank();
	}

	@Test
	@DisplayName("createSchulbescheinigungXml | Erfolg - Enthält UTF-8 Deklaration")
	void createSchulbescheinigungXmlEnthaeltUtf8Deklaration() {
		final var xml = service.createSchulbescheinigungXml(defaultSchulbescheinigung());

		assertThat(xml).contains("UTF-8");
	}

	@Test
	@DisplayName("createSchulbescheinigungXml | Erfolg - Smoke-Test XPath enthält Nachname")
	void createSchulbescheinigungXmlSmokeTestEnthaeltNachname() throws Exception {
		final var xml = service.createSchulbescheinigungXml(defaultSchulbescheinigung());
		final var doc = toDocument(xml);

		assertThat(xpath(doc, "//familienname/name")).isEqualTo("Musterfrau");
	}

	@Test
	@DisplayName("createSchulbescheinigungXml | Schulbescheinigung null")
	void createSchulbescheinigungXmlMitNullWirftException() {
		assertThatThrownBy(() -> service.createSchulbescheinigungXml(null))
				.isInstanceOf(NullPointerException.class);
	}

	@Test
	@DisplayName("Konstruktor | Serializer null")
	void konstruktorMitNullSerializerWirftException() {
		assertThatThrownBy(() -> new SchulbescheinigungService(null))
				.isInstanceOf(NullPointerException.class);
	}

	private Document toDocument(final String xml) throws Exception {
		final var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
	}

	private String xpath(final Document doc, final String expression) throws Exception {
		return xPathFactory.newXPath().evaluate(expression, doc);
	}

}
