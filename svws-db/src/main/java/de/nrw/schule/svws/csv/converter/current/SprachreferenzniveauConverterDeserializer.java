package de.nrw.schule.svws.csv.converter.current;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.nrw.schule.svws.core.types.fach.Sprachreferenzniveau;

import java.io.IOException;

/**
 * Diese Klasse ist ein Deserialisierer für das Sprachreferenzniveau (z.B. A2/B1). Sie deserialisiert
 * die Datenbankdarstellung als String in einen Wert der Aufzählung {@link Sprachreferenzniveau}.
 */
public class SprachreferenzniveauConverterDeserializer extends StdDeserializer<Sprachreferenzniveau> {

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
    protected SprachreferenzniveauConverterDeserializer(Class<Sprachreferenzniveau> t) {
        super(t);
    }

    @Override
    public Sprachreferenzniveau deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return Sprachreferenzniveau.getByBezeichnung(p.getText());
    }

}