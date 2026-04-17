package de.svws_nrw.csv.converter.current;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.db.converter.current.ReportingBildDefinitionConverter;

/**
 * Diese Klasse ist einen Serialisierer von ReportingBildDefinition-Objekten.
 */
public final class ReportingBildDefinitionConverterSerializer extends StdSerializer<ReportingBildDefinition> {

	private static final long serialVersionUID = -1745427357127293925L;

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 */
	public ReportingBildDefinitionConverterSerializer() {
		super(ReportingBildDefinition.class);
	}

	/**
	 * Erzeugt ein neues Objekt zur Serialisierung
	 *
	 * @param t   ein Klassenobjekt für die ReportingBildDefinition-Klasse
	 */
	public ReportingBildDefinitionConverterSerializer(final Class<ReportingBildDefinition> t) {
		super(t);
	}

	@Override
	public void serialize(final ReportingBildDefinition value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
		gen.writeString(ReportingBildDefinitionConverter.instance.convertToDatabaseColumn(value));
	}

}
