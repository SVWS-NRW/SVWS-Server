package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zur Zuordnung eines Unterrichts zu einem Lerngruppenlehrer
 * innerhalb eines Planungsabschnitts der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "die Informationen zur Zuordnung eines Unterrichts zu einem Lerngruppenlehrer innerhalb eines Planungsabschnitts.")
@TranspilerDTO
public class UvUnterrichtLerngruppenlehrer {

	/** Die ID des Planungsabschnitts, in dem die Zuordnung gilt. */
	@Schema(description = "die ID des Planungsabschnitts, in dem die Zuordnung gilt", example = "101")
	public long idPlanungsabschnitt = -1;

	/** Die ID des Unterrichts, der dem Lerngruppenlehrer zugeordnet ist. */
	@Schema(description = "die ID des Unterrichts, der dem Lerngruppenlehrer zugeordnet ist", example = "2001")
	public long idUnterricht = -1;

	/** Die ID des Lerngruppenlehrers, dem der Unterricht zugeordnet ist. */
	@Schema(description = "die ID des Lerngruppenlehrers, dem der Unterricht zugeordnet ist", example = "3001")
	public long idLerngruppenLehrer = -1;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvUnterrichtLerngruppenlehrer() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvUnterrichtLerngruppenlehrer-Objekts zurück.
	 *
	 * @return die String-Darstellung der Unterricht-Lerngruppenlehrer-Zuordnung
	 */
	@Override
	public String toString() {
		return "UvUnterrichtLerngruppenlehrer{idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", idUnterricht=" + idUnterricht
				+ ", idLerngruppenLehrer=" + idLerngruppenLehrer
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
		return (another instanceof final UvUnterrichtLerngruppenlehrer a)
				&& (this.idPlanungsabschnitt == a.idPlanungsabschnitt)
				&& (this.idUnterricht == a.idUnterricht)
				&& (this.idLerngruppenLehrer == a.idLerngruppenLehrer);
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
		result = 31 * result + Long.hashCode(idLerngruppenLehrer);
		return result;
	}

}
