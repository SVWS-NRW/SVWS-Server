package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer UV-Stundentafel.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer UV-Stundentafel.")
@TranspilerDTO
public class UvStundentafel {

    /** Die ID der Stundentafel. */
    @Schema(description = "die ID der Stundentafel", example = "4711")
    public long id = -1;

    /** Die ID des zugehörigen Jahrgangs. */
    @Schema(description = "die ID des zugehörigen Jahrgangs", example = "12")
    public long idJahrgang = -1;

	/** Die optionale Bezeichnung der Stundentafel. */
	@Schema(description = "die optionale Bezeichnung der Stundentafel.", example = "Stundentafel für Jahrgang 12")
	public @NotNull String bezeichnung = "";

    /** Das Datum, ab dem die Stundentafel gültig ist. */
    @Schema(description = "das Datum, ab dem die Stundentafel gültig ist", example = "2025-09-01")
    public @NotNull String gueltigVon = "";

    /** Das Datum, bis wann die Stundentafel gültig ist. */
    @Schema(description = "das Datum, bis wann die Stundentafel gültig ist", example = "2026-08-31")
    public String gueltigBis = "";

    /** Die optionale Beschreibung oder Kommentar zur Stundentafel. */
    @Schema(description = "die optionale Beschreibung oder Kommentar zur Stundentafel.", example = "Eine Beschreibung oder Kommentar zu dieser Stundentafel")
    public String beschreibung = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvStundentafel() {
		// leer
	}

    /**
     * Gibt eine String-Repräsentation des UvStundentafel-Objekts zurück.
     */
    @Override
    public String toString() {
		return "UvStundentafel{id=" + id
				+ ", jahrgangId=" + idJahrgang
				+ ", bezeichnung=" + bezeichnung
				+ ", gueltigVon=" + gueltigVon
				+ ", gueltigBis=" + gueltigBis
				+ ", beschreibung=" + beschreibung
				+ "}";
    }

	/**
	 * Vergleicht, ob das aktuelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
    @Override
    public boolean equals(final Object another) {
        return (another instanceof final UvStundentafel ano) && (this.id == ano.id);
    }

    /**
     * Erzeugt den Hashcode zum Objekt auf Basis des id-Attributs.
     *
     * @return den Hashcode zum Objekt auf Basis des id-Attributs.
     */
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

}
