package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird für die Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt eine Unterrichtseinheit innerhalb des Unterrichtsverteilungsmoduls.
 */
@XmlRootElement
@Schema(description = "die Daten einer Unterrichtseinheit innerhalb des Unterrichtsverteilungsmoduls.")
@TranspilerDTO
public class UvUnterricht {

	/** Die ID des Unterrichts. */
	@Schema(description = "die ID des Unterrichts", example = "4711")
	public long id = -1;

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des zugehörigen Planungsabschnitts", example = "1201")
	public long idPlanungsabschnitt = -1;

	/** Die ID des Zeitraster-Eintrags (kann null sein). */
	@Schema(description = "die ID des Zeitraster-Eintrags", example = "2301")
	public Long idZeitrasterEintrag = null;

	/** Die ID der Lerngruppe. */
	@Schema(description = "die ID der Lerngruppe", example = "3101")
	public long idLerngruppe = -1;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvUnterricht() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvUnterricht-Objekts zurück.
	 *
	 * @return die String-Darstellung
	 */
	@Override
	public String toString() {
		return "UvUnterricht{id=" + id
				+ ", idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", idZeitrasterEintrag=" + idZeitrasterEintrag
				+ ", idLerngruppe=" + idLerngruppe
				+ "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param obj das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object obj) {
		return (obj instanceof final UvUnterricht other) && (this.id == other.id);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der ID.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
