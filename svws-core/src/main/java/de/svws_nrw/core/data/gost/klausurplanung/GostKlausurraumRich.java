package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die mit Daten des zugehörigen Stundenplan-Raums angereicherten Informationen zu einem Gost-Klausurraum.
 */
@XmlRootElement
@Schema(description = "die mit Daten des zugehörigen {@link StundenplanRaum} angereicherten Informationen zu einem {@link GostKlausurraum}.")
@TranspilerDTO
public class GostKlausurraumRich {

	/** Die ID des Klausurraums. */
	@Schema(description = "die ID des Klausurraums", example = "815")
	public long id = -1;

	/** Die Grösse des Raumes an Arbeitsplätzen für Schüler. */
	@Schema(description = "die Grösse des Raumes an Arbeitsplätzen für Schüler", example = "30")
	public int groesse = -1;

	/** Ein Array mit den IDs der im Raum enthaltenen Schülerklausurterminen. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den IDs der im Raum enthaltenen Schülerklausurterminen."))
	public @NotNull List<Long> schuelerklausurterminIDs = new ArrayList<>();

	/**
	 * Konstruktor zur Erstellung des Rich-Objekts.
	 *
	 * @param klausurraum      Das zugehörige {@link GostKlausurraum}-Objekt.
	 * @param stundenplanraum  Das zugehörige {@link StundenplanRaum}-Objekt.
	 *
	 */
	public GostKlausurraumRich(final @NotNull GostKlausurraum klausurraum, final StundenplanRaum stundenplanraum) {
		id = klausurraum.id;
		if (stundenplanraum != null) {
			groesse = stundenplanraum.groesse;
		}
	}

	/**
	 * Default-Konstruktor
	 */
	public GostKlausurraumRich() {
	}

	/**
	 * Vergleicht, ob das aktuelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return (another != null) && (another instanceof GostKlausurraumRich) && (this.id == ((GostKlausurraumRich) another).id);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
