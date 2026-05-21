package de.svws_nrw.schulbescheinigung;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import digital.xschule.def.xschule._1_1.xsd.XSchuleSchuelerSchulbescheinigung0004;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

/**
 * Serialisiert eine {@link XSchuleSchuelerSchulbescheinigung0004} in eine XSchule-konforme XML-Darstellung.
 * Beim Marshalling wird das XML gegen das XSchule-Schema validiert.
 */
public final class SchulbescheinigungSerializer {

	private static final String SCHEMA_LOCATION = "/xschule/";
	private static final String SCHEMA_PATH = SCHEMA_LOCATION + "xschule.xsd";

	private final JAXBContext context;
	private final Schema schema;

	/**
	 * Erstellt einen neuen {@code SchulbescheinigungSerializer}.
	 *
	 * @throws IllegalStateException wenn der JAXBContext oder das Schema nicht initialisiert werden kann
	 */
	public SchulbescheinigungSerializer() {
		try {
			this.context = JAXBContext.newInstance(XSchuleSchuelerSchulbescheinigung0004.class);
			this.schema = ladeSchema();
		} catch (final JAXBException e) {
			throw new IllegalStateException("JAXBContext konnte nicht initialisiert werden", e);
		}
	}

	/**
	 * Serialisiert die übergebene Schulbescheinigung als XML in den übergebenen {@link OutputStream}.
	 * Das Ergebnis wird gegen das XSchule-Schema validiert – fehlende Pflichtfelder führen zu einer Exception.
	 *
	 * @param bescheinigung die zu serialisierende Schulbescheinigung
	 * @param out der Ziel-Stream
	 * @throws IllegalArgumentException wenn {@code bescheinigung} oder {@code out} null ist
	 * @throws SchulbescheinigungSerializerException wenn die Serialisierung oder Validierung fehlschlägt
	 */
	public void serialize(final XSchuleSchuelerSchulbescheinigung0004 bescheinigung, final OutputStream out) {
		Objects.requireNonNull(bescheinigung, "bescheinigung darf nicht null sein");
		Objects.requireNonNull(out, "out darf nicht null sein");
		try {
			final var marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
			marshaller.setSchema(schema);
			marshaller.marshal(bescheinigung, out);
		} catch (final JAXBException e) {
			throw new SchulbescheinigungSerializerException("Serialisierung der Schulbescheinigung fehlgeschlagen", e);
		}
	}

	/**
	 * Lädt das XSchule-Schema aus dem Classpath. Die inkludierten XSD-Dateien werden über relative schemaLocation-Angaben
	 * in der xschule.xsd aufgelöst – sie müssen im gleichen Classpath-Verzeichnis liegen.
	 *
	 * @return das geladene {@link Schema}
	 * @throws IllegalStateException wenn das Schema nicht geladen werden kann
	 */
	private Schema ladeSchema() {
		final URL schemaUrl = SchulbescheinigungSerializer.class.getResource(SCHEMA_PATH);
		if (schemaUrl == null) {
			throw new IllegalStateException("XSchule-Schema nicht gefunden: " + SCHEMA_PATH);
		}
		try {
			final var schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			// Externe DTDs verbieten
			schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			// Generelle externe Schema-Dateien verbieten
			schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
			// Eigener Resource-Resolver zur Auflösung inkludierter lokaler Schema-Dateien
			schemaFactory.setResourceResolver(new ClasspathResourceResolver(SCHEMA_LOCATION));
			return schemaFactory.newSchema(schemaUrl);
		} catch (final SAXException e) {
			throw new IllegalStateException("XSchule-Schema konnte nicht geladen werden: " + SCHEMA_PATH, e);
		}
	}


	private static final class ClasspathResourceResolver implements LSResourceResolver {

		private static final Pattern VALID_SCHEMA_NAME = Pattern.compile("^[A-Za-z0-9_-]+\\.xsd$");

		private final String basePath;

		private ClasspathResourceResolver(final String basePath) {
			this.basePath = basePath;
		}

		@Override
		public LSInput resolveResource(final String type, final String namespaceURI, final String publicId,
				final String systemId, final String baseURI) {
			if (systemId == null) {
				return null;
			}
			if (!VALID_SCHEMA_NAME.matcher(systemId).matches()) {
				throw new IllegalArgumentException("Ungültige oder unerlaubte Schema-Referenz: " + systemId);
			}
			final var stream = ClasspathResourceResolver.class.getResourceAsStream(basePath + systemId);
			if (stream == null) {
				return null;
			}
			return new LSInputImpl(stream, systemId);
		}
	}


	private record LSInputImpl(InputStream byteStream, String systemId) implements LSInput {

		@Override public InputStream getByteStream() {
			return byteStream(); }
		@Override public String getSystemId() {
			return systemId(); }

		@Override public Reader getCharacterStream() {
			return null; }
		@Override public String getStringData() {
			return null; }
		@Override public String getPublicId() {
			return null; }
		@Override public String getBaseURI() {
			return null; }
		@Override public String getEncoding() {
			return null; }
		@Override public boolean getCertifiedText() {
			return false; }
		@Override public void setByteStream(final InputStream byteStream) {
			throw new UnsupportedOperationException(); }
		@Override public void setSystemId(final String systemId) {
			throw new UnsupportedOperationException(); }
		@Override public void setCharacterStream(final Reader characterStream) {
			throw new UnsupportedOperationException(); }
		@Override public void setStringData(final String stringData) {
			throw new UnsupportedOperationException(); }
		@Override public void setPublicId(final String publicId) {
			throw new UnsupportedOperationException(); }
		@Override public void setBaseURI(final String baseURI) {
			throw new UnsupportedOperationException(); }
		@Override public void setEncoding(final String encoding) {
			throw new UnsupportedOperationException(); }
		@Override public void setCertifiedText(final boolean certifiedText) {
			throw new UnsupportedOperationException(); }
	}
}
