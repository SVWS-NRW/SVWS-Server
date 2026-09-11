package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Raum innerhalb der Unterrichtsverteilung.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einem Raum innerhalb der Unterrichtsverteilung.")
@TranspilerDTO
public class UvRaum {

	/** Die eindeutige ID des Raums. */
	@Schema(description = "die eindeutige ID des Raums", example = "4711")
	public long id = -1;

	/** Das Kürzel des Raums. */
	@Schema(description = "das Kürzel des Raums", example = "R204")
	public @NotNull String kuerzel = "";

	/** Die Beschreibung des Raumes. */
	@Schema(description = "die Beschreibung des Raumes", example = "Klassenraum der Klasse 07b")
	public String beschreibung = "";

	/** Die Größe des Raumes an Arbeitsplätzen für Schüler. */
	@Schema(description = "die Größe des Raumes an Arbeitsplätzen für Schüler", example = "30")
	public int groesse = -1;

	/** Die ID der Raumgruppe, falls der Raum zu einer solchen gehört. */
	@Schema(description = "die ID der Raumgruppe, falls der Raum zu einer solchen gehört", example = "102")
	public Long idRaumgruppe = null;

	/** Das Datum, ab dem der Raum gültig ist. */
	@Schema(description = "das Datum, ab dem der Raum gültig ist", example = "2025-08-01")
	public @NotNull String gueltigVon = "";

	/** Das Datum, bis wann der Raum gültig ist. Ist kein Datum gesetzt, gilt der Raum unbegrenzt weiter. */
	@Schema(description = "das Datum, bis wann der Raum gültig ist. Ist kein Datum gesetzt, gilt der Raum unbegrenzt weiter.", example = "2026-07-31")
	public String gueltigBis = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvRaum() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvRaum-Objekts zurück.
	 *
	 * @return die String-Darstellung des Raums
	 */
	@Override
	public String toString() {
		return "UvRaum{id=" + id
				+ ", kuerzel=" + kuerzel
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
		return (another instanceof final UvRaum a) && (this.id == a.id);
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
