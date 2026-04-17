package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.db.converter.current.ReportingBildDefinitionConverter;

/**
 * Diese Klasse ist einen Deserialisierer von ReportingBildDefinition-Objekten.
 */
public final class ReportingBildDefinitionConverterDeserializer extends StdDeserializer<ReportingBildDefinition> {

	private static final long serialVersionUID = -1745427357127293925L;

	/**
	 * Erzeugt ein neues Objekt zur Deserialisierung
	 */
	public ReportingBildDefinitionConverterDeserializer() {
		super(ReportingBildDefinition.class);
	}

	/**
	 * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
	 *
	 * @param t   ein Klassenobjekt für die ReportingBildDefinition-Klasse
	 */
	protected ReportingBildDefinitionConverterDeserializer(final Class<ReportingBildDefinition> t) {
		super(t);
	}

	@Override
	public ReportingBildDefinition deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		return ReportingBildDefinitionConverter.instance.convertToEntityAttribute(p.getText());
	}

}
