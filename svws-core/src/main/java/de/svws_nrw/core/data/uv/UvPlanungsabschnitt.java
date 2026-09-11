package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem UV-Planungsabschnitt.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einem UV-Planungsabschnitt.")
@TranspilerDTO
public class UvPlanungsabschnitt {

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts", example = "815")
	public long id = -1;

	/** Das Schuljahr, zu dem der Planungsabschnitt gehört. */
	@Schema(description = "das Schuljahr, zu dem der Planungsabschnitt gehört.", example = "2025")
	public int schuljahr = -1;

	/** Gibt an, ob der Planungsabschnitt aktiv ist. An einem Stichtag darf nur ein Planungsabschnitt aktiv sein. */
	@Schema(description = "gibt an, ob der Planungsabschnitt aktiv ist. An einem Stichtag darf nur ein Planungsabschnitt aktiv sein.", example = "true")
	public boolean aktiv = false;

	/** Das Datum des Gültigkeitsbeginns des Planungsabschnitts. */
	@Schema(description = "das Datum des Gültigkeitsbeginns des Planungsabschnitts", example = "2025-02-09")
	public @NotNull String gueltigVon = "";

	/** Das Datum des Gültigkeitsendes des Planungsabschnitts. */
	@Schema(description = "das Datum des Gültigkeitsendes des Planungsabschnitts", example = "2025-04-13")
	public String gueltigBis = "";

	/** Die optionale Beschreibung oder Kommentar zum Planungsabschnitt. */
	@Schema(description = "die optionale Beschreibung oder Kommentar zum Planungsabschnitt.", example = "Weggang Q2")
	public String beschreibung = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvPlanungsabschnitt() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvPlanungsabschnitt-Objekts zurück.
	 */
	@Override
	public String toString() {
		return "UvPlanungsabschnitt{id=" + id
				+ ", schuljahr=" + schuljahr
				+ ", aktiv=" + aktiv
				+ ", gueltigVon=" + gueltigVon
				+ ", gueltigBis=" + gueltigBis
				+ ", beschreibung=" + beschreibung
				+ "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return (another instanceof final UvPlanungsabschnitt ano) && (this.id == ano.id);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
