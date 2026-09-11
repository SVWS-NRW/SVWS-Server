package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Lehrer innerhalb der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einem Lehrer innerhalb der Unterrichtsverteilung.")
@TranspilerDTO
public class UvLehrer {

	/** Die eindeutige ID des Lehrers im Planungsabschnitt (planungsspezifisch). */
	@Schema(description = "die eindeutige ID des Lehrers im Planungsabschnitt (planungsspezifisch)", example = "4711")
	public long id = -1;

	/** Die ID des Lehrers als Fremdschlüssel auf die Tabelle K_Lehrer. */
	@Schema(description = "die ID des Lehrers als Fremdschlüssel auf die Tabelle K_Lehrer", example = "102")
	public Long idKLehrer = null;

	/** Das Lehrer-Kürzel für eine eindeutige Identifikation. */
	@Schema(description = "das Lehrer-Kürzel für eine eindeutige Identifikation", example = "ABC")
	public @NotNull String kuerzel = "";

	/** Der Nachname des Lehrers. */
	@Schema(description = "der Nachname des Lehrers", example = "Mustermann")
	public String nachname = null;

	/** Der Vorname (bzw. Rufname) des Lehrers. */
	@Schema(description = "der Vorname (bzw. Rufname) des Lehrers", example = "Max")
	public String vorname = null;

	/** Das Datum, wann der Lehrer an die Schule gekommen ist. */
	@Schema(description = "das Datum, wann der Lehrer an die Schule gekommen ist.", example = "2000-11-11")
	public String datumZugang = null;

	/** Das Datum, wann der Lehrer die Schule verlassen hat. */
	@Schema(description = "das Datum, wann der Lehrer die Schule verlassen hat.", example = "2026-07-31")
	public String datumAbgang = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvLehrer() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvLehrer-Objekts zurück.
	 *
	 * @return die String-Darstellung des Lehrers
	 */
	@Override
	public String toString() {
		return "UvLehrer{id=" + id
				+ ", idKLehrer=" + idKLehrer
				+ ", kuerzel=" + kuerzel
				+ ", nachname=" + nachname
				+ ", vorname=" + vorname
				+ "}";	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return (another instanceof final UvLehrer a) && (this.id == a.id);
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
