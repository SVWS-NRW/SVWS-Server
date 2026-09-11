package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zur Zuordnung eines Lehrers zu einer Lerngruppe
 * innerhalb eines Planungsabschnitts der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "die Informationen zur Zuordnung eines Lehrers zu einer Lerngruppe innerhalb eines Planungsabschnitts.")
@TranspilerDTO
public class UvLerngruppenLehrer {

	/** Die ID der Zuordnung (planungsspezifisch). */
	@Schema(description = "die ID der Zuordnung (planungsspezifisch)", example = "4711")
	public long id = -1;

	/** Die ID des Planungsabschnitts, in dem die Zuordnung gilt. */
	@Schema(description = "die ID des Planungsabschnitts, in dem die Zuordnung gilt", example = "101")
	public long idPlanungsabschnitt = -1;

	/** Die ID der Lerngruppe, der der Lehrer zugeordnet ist. */
	@Schema(description = "die ID der Lerngruppe, der der Lehrer zugeordnet ist", example = "2001")
	public long idLerngruppe = -1;

	/** Die ID des Lehrers, der der Lerngruppe zugeordnet ist. */
	@Schema(description = "die ID des Lehrers, der der Lerngruppe zugeordnet ist", example = "3001")
	public long idLehrer = -1;

	/** Die Reihenfolge der Zuordnung (z. B. 1 = Hauptlehrkraft, 2 = Zweitkraft). */
	@Schema(description = "die Reihenfolge der Zuordnung (z. B. 1 = Hauptlehrkraft, 2 = Zweitkraft)", example = "1")
	public int reihenfolge = 1;

	/** Die Anzahl der Wochenstunden, in denen der Lehrer in dieser Lerngruppe eingesetzt ist. */
	@Schema(description = "die Anzahl der Wochenstunden, in denen der Lehrer in dieser Lerngruppe eingesetzt ist", example = "4.0")
	public double wochenstunden = 0.0;

	/** Die Anzahl der Wochenstunden, die auf das Deputat angerechnet werden. */
	@Schema(description = "die Anzahl der Wochenstunden, die auf das Deputat angerechnet werden", example = "2.0")
	public double wochenstundenAngerechnet = 0.0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvLerngruppenLehrer() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvLerngruppenLehrer-Objekts zurück.
	 *
	 * @return die String-Darstellung der Lehrer-Lerngruppen-Zuordnung
	 */
	@Override
	public String toString() {
		return "UvLerngruppenLehrer{id=" + id
				+ ", idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", idLerngruppe=" + idLerngruppe
				+ ", idLehrer=" + idLehrer
				+ ", reihenfolge=" + reihenfolge
				+ ", wochenstunden=" + wochenstunden
				+ ", wochenstundenAngerechnet=" + wochenstundenAngerechnet
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
		return (another instanceof final UvLerngruppenLehrer a)
				&& (this.id == a.id);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der ID.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
