package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zur Zuordnung eines Unterrichts zu einem Raum
 * innerhalb eines Planungsabschnitts der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "die Informationen zur Zuordnung eines Unterrichts zu einem Raum innerhalb eines Planungsabschnitts.")
@TranspilerDTO
public class UvUnterrichtRaum {

	/** Die ID des Planungsabschnitts, in dem die Zuordnung gilt. */
	@Schema(description = "die ID des Planungsabschnitts, in dem die Zuordnung gilt", example = "101")
	public long idPlanungsabschnitt = -1;

	/** Die ID des Unterrichts, der dem Raum zugeordnet ist. */
	@Schema(description = "die ID des Unterrichts, der dem Raum zugeordnet ist", example = "2001")
	public long idUnterricht = -1;

	/** Die ID des Raums, dem der Unterricht zugeordnet ist. */
	@Schema(description = "die ID des Raums, dem der Unterricht zugeordnet ist", example = "3001")
	public long idRaum = -1;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvUnterrichtRaum() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvUnterrichtRaum-Objekts zurück.
	 *
	 * @return die String-Darstellung der Unterricht-Raum-Zuordnung
	 */
	@Override
	public String toString() {
		return "UvUnterrichtRaum{idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", idUnterricht=" + idUnterricht
				+ ", idRaum=" + idRaum
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
		return (another instanceof final UvUnterrichtRaum a)
				&& (this.idPlanungsabschnitt == a.idPlanungsabschnitt)
				&& (this.idUnterricht == a.idUnterricht)
				&& (this.idRaum == a.idRaum);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der IDs.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		int result = Long.hashCode(idPlanungsabschnitt);
		result = 31 * result + Long.hashCode(idUnterricht);
		result = 31 * result + Long.hashCode(idRaum);
		return result;
	}

}
