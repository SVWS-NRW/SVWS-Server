package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer Raumgruppe innerhalb der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Raumgruppe innerhalb der Unterrichtsverteilung.")
@TranspilerDTO
public class UvRaumgruppe {

	/** Die eindeutige ID der Raumgruppe. */
	@Schema(description = "die eindeutige ID der Raumgruppe", example = "4711")
	public long id = -1;

	/** Die Bezeichnung der Raumgruppe. */
	@Schema(description = "das Bezeichnung der Raumgruppe", example = "Computerräume")
	public @NotNull String bezeichnung = "";

	/** Das Datum, ab dem die Raumgruppe gültig ist. */
	@Schema(description = "das Datum, ab dem die Raumgruppe gültig ist", example = "2025-08-01")
	public @NotNull String gueltigVon = "";

	/** Das Datum, bis wann die Raumgruppe gültig ist. Ist kein Datum gesetzt, gilt die Raumgruppe unbegrenzt weiter. */
	@Schema(description = "das Datum, bis wann die Raumgruppe gültig ist. Ist kein Datum gesetzt, gilt die Raumgruppe unbegrenzt weiter.", example = "2026-07-31")
	public String gueltigBis = null;

	/** Die Beschreibung der Raumgruppe. */
	@Schema(description = "das Beschreibung der Raumgruppe", example = "Computerräume")
	public String beschreibung = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvRaumgruppe() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvRaumgruppe-Objekts zurück.
	 *
	 * @return die String-Darstellung der Raumgruppe
	 */
	@Override
	public String toString() {
		return "UvRaumgruppe{id=" + id
				+ ", bezeichnung=" + bezeichnung
				+ ", gueltigVon=" + gueltigVon
				+ ", gueltigBis=" + gueltigBis
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
		return (another instanceof final UvRaumgruppe a) && (this.id == a.id);
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
