package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zur Zuordnung einer Lerngruppe zu einer Schiene
 * innerhalb eines Planungsabschnitts der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "die Informationen zur Zuordnung einer Lerngruppe zu einer Schiene innerhalb eines Planungsabschnitts.")
@TranspilerDTO
public class UvLerngruppenSchiene {

	/** Die ID des Planungsabschnitts, in dem die Zuordnung gilt. */
	@Schema(description = "die ID des Planungsabschnitts, in dem die Zuordnung gilt", example = "101")
	public long idPlanungsabschnitt = -1;

	/** Die ID der Lerngruppe, die der Schiene zugeordnet ist. */
	@Schema(description = "die ID der Lerngruppe, die der Schiene zugeordnet ist", example = "2001")
	public long idLerngruppe = -1;

	/** Die ID der Schiene, der die Lerngruppe zugeordnet ist. */
	@Schema(description = "die ID der Schiene, der die Lerngruppe zugeordnet ist", example = "3001")
	public long idSchiene = -1;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvLerngruppenSchiene() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvLerngruppenSchiene-Objekts zurück.
	 *
	 * @return die String-Darstellung der Lerngruppe-Schiene-Zuordnung
	 */
	@Override
	public String toString() {
		return "UvLerngruppenSchiene{idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", idLerngruppe=" + idLerngruppe
				+ ", idSchiene=" + idSchiene
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
		return (another instanceof final UvLerngruppenSchiene a)
				&& (this.idPlanungsabschnitt == a.idPlanungsabschnitt)
				&& (this.idLerngruppe == a.idLerngruppe)
				&& (this.idSchiene == a.idSchiene);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der IDs.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		int result = Long.hashCode(idPlanungsabschnitt);
		result = 31 * result + Long.hashCode(idLerngruppe);
		result = 31 * result + Long.hashCode(idSchiene);
		return result;
	}

}
