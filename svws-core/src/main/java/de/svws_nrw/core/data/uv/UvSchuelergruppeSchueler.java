package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zur Zuordnung eines Schülers zu einer Schülergruppe innerhalb der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "die Informationen zur Zuordnung eines Schülers zu einer Schülergruppe innerhalb der Unterrichtsverteilung.")
@TranspilerDTO
public class UvSchuelergruppeSchueler {

	/** Die ID der Schülergruppe. */
	@Schema(description = "die ID der Schülergruppe", example = "42")
	public long idSchuelergruppe = -1;

	/** Die ID des Schülers. */
	@Schema(description = "die ID des Schülers", example = "4711")
	public long idSchueler = -1;

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts", example = "1")
	public long idPlanungsabschnitt = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvSchuelergruppeSchueler() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvSchuelergruppeSchueler-Objekts zurück.
	 *
	 * @return die String-Darstellung der Zuordnung
	 */
	@Override
	public String toString() {
		return "UvSchuelergruppeSchueler{idSchuelergruppe=" + idSchuelergruppe
				+ ", idSchueler=" + idSchueler
				+ ", idPlanungsabschnitt=" + idPlanungsabschnitt
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
		return (another instanceof final UvSchuelergruppeSchueler a) && (this.idSchuelergruppe == a.idSchuelergruppe) && (this.idSchueler == a.idSchueler);
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
		result = prime * result + Long.hashCode(idSchuelergruppe);
		result = prime * result + Long.hashCode(idSchueler);
		return result;
	}

}
