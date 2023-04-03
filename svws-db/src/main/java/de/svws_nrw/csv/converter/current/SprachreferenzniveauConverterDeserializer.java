package de.svws_nrw.csv.converter.current;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.svws_nrw.core.types.fach.Sprachreferenzniveau;

import java.io.IOException;

/**
 * Diese Klasse ist ein Deserialisierer für das Sprachreferenzniveau (z.B. A2/B1). Sie deserialisiert
 * die Datenbankdarstellung als String in einen Wert der Aufzählung {@link Sprachreferenzniveau}.
 */
public final class SprachreferenzniveauConverterDeserializer extends StdDeserializer<Sprachreferenzniveau> {

    private static final long serialVersionUID = 2214600102039874189L;

    /**
     * Erzeugt einen neuen Deserialisierer
     */
    public SprachreferenzniveauConverterDeserializer() {
        super(Sprachreferenzniveau.class);
    }

    /**
     * Erzeugt einen neuen Deserialisierer unter Angabe der {@link Class}
     *
     * @param t   das Klassen-Objekt
     */
    protected SprachreferenzniveauConverterDeserializer(final Class<Sprachreferenzniveau> t) {
        super(t);
    }

    @Override
    public Sprachreferenzniveau deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return Sprachreferenzniveau.getByKuerzel(p.getText());
    }

}
