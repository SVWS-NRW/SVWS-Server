package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zur Zuordnung eines Lehrers zu einem Planungsabschnitt innerhalb der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "die Informationen zur Zuordnung eines Lehrers zu einem Planungsabschnitt innerhalb der Unterrichtsverteilung.")
@TranspilerDTO
public class UvPlanungsabschnittLehrer {

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts", example = "42")
	public long idPlanungsabschnitt = -1;

	/** Die ID des UV-Lehrers. */
	@Schema(description = "die ID des UV-Lehrers", example = "4711")
	public long idLehrer = -1;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvPlanungsabschnittLehrer() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvPlanungsabschnittLehrer-Objekts zurück.
	 *
	 * @return die String-Darstellung der Zuordnung
	 */
	@Override
	public String toString() {
		return "UvPlanungsabschnittLehrer{idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", idLehrer=" + idLehrer
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
		return (another instanceof final UvPlanungsabschnittLehrer a) && (this.idPlanungsabschnitt == a.idPlanungsabschnitt) && (this.idLehrer == a.idLehrer);
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
		result = prime * result + Long.hashCode(idLehrer);
		return result;
	}

}
