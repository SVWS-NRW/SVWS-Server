package de.svws_nrw.core.data.uv;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zur Zuordnung eines Zeitrasters zu einem Planungsabschnitt innerhalb der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "die Informationen zur Zuordnung eines Zeitrasters zu einem Planungsabschnitt innerhalb der Unterrichtsverteilung.")
@TranspilerDTO
public class UvPlanungsabschnittZeitraster {

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts", example = "42")
	public long idPlanungsabschnitt = -1;

	/** Die ID des Zeitrasters. */
	@Schema(description = "die ID des Zeitrasters", example = "4711")
	public long idZeitraster = -1;

	/** Ein Array mit den IDs der zugeordneten Jahrgänge. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "ein Array mit den IDs der zugeordneten Jahrgänge."))
	public @NotNull List<Long> idsJahrgaenge = new ArrayList<>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvPlanungsabschnittZeitraster() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvPlanungsabschnittZeitraster-Objekts zurück.
	 *
	 * @return die String-Darstellung der Zuordnung
	 */
	@Override
	public String toString() {
		return "UvPlanungsabschnittZeitraster{idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", idZeitraster=" + idZeitraster
				+ "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return (another instanceof final UvPlanungsabschnittZeitraster a) && (this.idPlanungsabschnitt == a.idPlanungsabschnitt) && (this.idZeitraster == a.idZeitraster);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(idPlanungsabschnitt);
		result = prime * result + Long.hashCode(idZeitraster);
		return result;
	}

}
