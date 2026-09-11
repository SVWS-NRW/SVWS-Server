package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem UV-Fach.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einem UV-Fach.")
@TranspilerDTO
public class UvFach {

	/** Die ID des UV-Fachs. */
	@Schema(description = "die ID des UV-Fachs", example = "4711")
	public long id = -1;

	/** Die ID des Faches (Fremdschlüssel auf die Tabelle EigeneSchule_Faecher). */
	@Schema(description = "die ID des Faches", example = "17")
	public long idFach = -1;

	/** Das Datum, ab dem das Fach gültig ist. */
	@Schema(description = "das Datum, ab dem das Fach gültig ist", example = "2024-08-01")
	public @NotNull String gueltigVon = "";

	/** Das Datum, bis wann das Fach gültig ist (falls vorhanden). */
	@Schema(description = "das Datum, bis wann das Fach gültig ist (falls vorhanden)", example = "2025-07-31")
	public String gueltigBis;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvFach() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvFach-Objekts zurück.
	 *
	 * @return die String-Darstellung des UV-Fachs
	 */
	@Override
	public String toString() {
		return "UvFach{id=" + id
				+ ", idFach=" + idFach
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
		return (another instanceof final UvFach a) && (this.id == a.id);
	}

	/**
	 * Erzeugt den Hashcode des Objekts auf Basis der ID.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
