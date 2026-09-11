package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Fach einer UV-Stundentafel.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einem Fach einer UV-Stundentafel.")
@TranspilerDTO
public class UvStundentafelFach {

	/** Die ID des Stundentafel_Fachs. */
	@Schema(description = "die ID des Stundentafel_Fachs", example = "4711")
	public long id = -1;

	/** Die ID der Stundentafel, zu der das Fach gehört. */
	@Schema(description = "die ID der Stundentafel, zu der das Fach gehört", example = "1001")
	public long idStundentafel = -1;

	/** Der Abschnitt des Schuljahres (z. B. 1 oder 2). */
	@Schema(description = "der Abschnitt des Schuljahres (z. B. 1 oder 2)", example = "1")
	public int abschnitt = 1;

	/** Die ID des Faches (Fremdschlüssel auf die Tabelle UV_Faecher). */
	@Schema(description = "die ID des Faches", example = "4711")
	public long idFach = -1;

	/** Die Anzahl der Wochenstunden für das Fach. */
	@Schema(description = "die Anzahl der Wochenstunden für das Fach", example = "4.0")
	public double wochenstunden = 0.0;

	/** Die Anzahl der Ergänzungsstunden für das Fach (in den Wochenstunden enthalten). */
	@Schema(description = "die Anzahl der Ergänzungsstunden für das Fach (in den Wochenstunden enthalten)", example = "1.0")
	public double davonErgaenzungsstunden = 0.0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvStundentafelFach() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvStundentafelFach-Objekts zurück.
	 *
	 * @return die String-Darstellung des Fach-Eintrags der Stundentafel
	 */
	@Override
	public String toString() {
		return "UvStundentafelFach{id=" + id
				+ ", idStundentafel=" + idStundentafel
				+ ", abschnitt=" + abschnitt
				+ ", idFach=" + idFach
				+ ", wochenstunden=" + wochenstunden
				+ ", davonErgaenzungsstunden=" + davonErgaenzungsstunden
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
		return (another instanceof final UvStundentafelFach a)
				&& (this.idStundentafel == a.idStundentafel)
				&& (this.idFach == a.idFach)
				&& (this.abschnitt == a.abschnitt);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der ID-Kombination.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		int result = Long.hashCode(idStundentafel);
		result = 31 * result + Long.hashCode(idFach);
		return result;
	}

}
