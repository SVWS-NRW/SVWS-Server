package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer Lerngruppe innerhalb eines UV-Planungsabschnitts.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Lerngruppe innerhalb eines UV-Planungsabschnitts.")
@TranspilerDTO
public class UvLerngruppe {

	/** Die eindeutige ID der Lerngruppe. */
	@Schema(description = "die eindeutige ID der Lerngruppe", example = "4711")
	public long id = -1;

	/** Die ID der Klasse, zu der die Lerngruppe gehört (optional). */
	@Schema(description = "die ID der Klasse, zu der die Lerngruppe gehört (optional)", example = "101")
	public Long idKlasse = null;

	/** Die ID des Faches, das in der Lerngruppe unterrichtet wird (optional). */
	@Schema(description = "die ID des Faches, das in der Lerngruppe unterrichtet wird (optional)", example = "32")
	public Long idFach = null;

	/** Die ID des Kurses, der mit dieser Lerngruppe verknüpft ist (optional). */
	@Schema(description = "die ID des Kurses, der mit dieser Lerngruppe verknüpft ist (optional)", example = "55")
	public Long idKurs = null;

	/** Die ID des Planungsabschnitts, zu dem die Lerngruppe gehört. */
	@Schema(description = "die ID des Planungsabschnitts, zu dem die Lerngruppe gehört", example = "7")
	public long idPlanungsabschnitt = -1;

	/** Die Anzahl der vorgesehenen Wochenstunden für die Lerngruppe. */
	@Schema(description = "die Anzahl der vorgesehenen Wochenstunden für die Lerngruppe", example = "4.0")
	public double wochenstunden = 0.0;

	/** Die Anzahl der tatsächlich unterrichteten Wochenstunden der Lerngruppe. */
	@Schema(description = "die Anzahl der tatsächlich unterrichteten Wochenstunden der Lerngruppe", example = "3.5")
	public double wochenstundenUnterrichtet = 0.0;

	/** Die Schulnummer einer möglichen Koop-Schule, null falls keine Kooperation besteht. */
	@Schema(description = "die Schulnummer einer möglichen Koop-Schule, null falls keine Kooperation besteht", example = "178902")
	public String koopSchulNr = null;

	/** Die Anzahl der externen Schüler von Koop-Schulen. */
	@Schema(description = "die Anzahl der externen Schüler von Koop-Schulen", example = "2")
	public int koopAnzahlExterne = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvLerngruppe() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvLerngruppe-Objekts zurück.
	 *
	 * @return die String-Darstellung der Lerngruppe
	 */
	@Override
	public String toString() {
		return "UvLerngruppe{id=" + id
				+ ", idKlasse=" + idKlasse
				+ ", idFach=" + idFach
				+ ", idKurs=" + idKurs
				+ ", idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", wochenstunden=" + wochenstunden
				+ ", wochenstundenUnterrichtet=" + wochenstundenUnterrichtet
				+ ", koopSchulNr=" + koopSchulNr
				+ ", koopAnzahlExterne=" + koopAnzahlExterne
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
		return (another instanceof final UvLerngruppe a) && (this.id == a.id);
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
