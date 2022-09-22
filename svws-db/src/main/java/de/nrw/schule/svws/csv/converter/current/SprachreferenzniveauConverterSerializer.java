package de.nrw.schule.svws.csv.converter.current;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.nrw.schule.svws.core.types.Sprachreferenzniveau;

import java.io.IOException;

/**
 * Diese Klasse ist ein Serialisierer für das Sprachreferenzniveau (z.B. A2/B1). Sie serialisiert
 * einen Wert der Aufzählung {@link Sprachreferenzniveau} in die Datenbankdarstellung als String.
 */
public class SprachreferenzniveauConverterSerializer extends StdSerializer<Sprachreferenzniveau> {

    private static final long serialVersionUID = -9145780250154825918L;

    /**
     * Erzeugt ein neues Objekt zur Serialisierung
     */
    public SprachreferenzniveauConverterSerializer() {
        super(Sprachreferenzniveau.class);
    }

    /**
     * Erzeugt einen neuen Serialisierer unter Angabe der {@link Class}
     *
     * @param t   das Klassen-Objekt
     */
    public SprachreferenzniveauConverterSerializer(Class<Sprachreferenzniveau> t) {
        super(t);
    }

    @Override
    public void serialize(Sprachreferenzniveau value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value == null ? null : value.bezeichnung);
    }

}
