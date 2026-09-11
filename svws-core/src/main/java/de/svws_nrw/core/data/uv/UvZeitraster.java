package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Zeitraster innerhalb der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einem Zeitraster der Unterrichtsverteilung.")
@TranspilerDTO
public class UvZeitraster {

	/** Die ID des Zeitrasters. */
	@Schema(description = "die ID des Zeitrasters", example = "4711")
	public long id = -1;

	/** Das Datum, ab dem das Zeitraster gültig ist (ISO-Datum als String, z. B. 2025-08-01). */
	@Schema(description = "das Datum, ab dem das Zeitraster gültig ist (ISO-Datum)", example = "2025-08-01")
	public @NotNull String gueltigVon = "";

	/** Das Datum, bis zu dem das Zeitraster gültig ist (oder null, falls unbegrenzt gültig). */
	@Schema(description = "das Datum, bis zu dem das Zeitraster gültig ist (oder null, falls unbegrenzt gültig)", example = "2026-07-31")
	public String gueltigBis;

	/** Die Bezeichnung des Zeitrasters. */
	@Schema(description = "die Bezeichnung des Zeitrasters", example = "Zeitraster Schuljahr 2025/26")
	public String bezeichnung;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvZeitraster() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvZeitraster-Objekts zurück.
	 *
	 * @return die String-Darstellung
	 */
	@Override
	public String toString() {
		return "UvZeitraster{id=" + id
				+ ", gueltigVon=" + gueltigVon
				+ ", gueltigBis=" + gueltigBis
				+ ", bezeichnung=" + bezeichnung
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
		return (another instanceof final UvZeitraster a)
				&& (this.id == a.id);
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
