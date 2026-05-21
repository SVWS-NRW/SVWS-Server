package de.svws_nrw.schulbescheinigung;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import digital.xschule.def.xschule._1_1.xsd.XSchuleSchuelerSchulbescheinigung0004;

public final class SchulbescheinigungService {

	private final SchulbescheinigungSerializer serializer;

	/**
	 * @param serializer Serializer für die Erstellung von Xml.
	 */
	public SchulbescheinigungService(final SchulbescheinigungSerializer serializer) {
		this.serializer = Objects.requireNonNull(serializer, "serializer darf nicht null sein");
	}

	/**
	 * Erwartet ein Schulbescheinigungs-Objekt und serialisiert diese in einen Xml-String.
	 *
	 * @param schulbescheinigung im XSchule-Format als Basis für die Xml
	 * @return das Xml-Dokument als String
	 */
	public String createSchulbescheinigungXml(final XSchuleSchuelerSchulbescheinigung0004 schulbescheinigung) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		serializer.serialize(schulbescheinigung, out);
		return out.toString(StandardCharsets.UTF_8);
	}
}
