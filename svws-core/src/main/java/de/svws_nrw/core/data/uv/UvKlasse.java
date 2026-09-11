package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer UV-Klasse.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer UV-Klasse.")
@TranspilerDTO
public class UvKlasse {

	/**
	 * Die ID der Klasse (generiert, planungsspezifisch).
	 */
	@Schema(description = "die ID der Klasse", example = "4711")
	public long id;

	/**
	 * Die ID des Planungsabschnitts.
	 */
	@Schema(description = "die ID des Planungsabschnitts", example = "815")
	public long idPlanungsabschnitt;

	/**
	 * Die ID des Schuljahresabschnitts.
	 */
	@Schema(description = "die ID des Schuljahresabschnitts", example = "42")
	public long idSchuljahresabschnitt;

	/**
	 * Bezeichnender Text für die Klasse.
	 */
	@Schema(description = "die Bezeichnung der Klasse", example = "5a")
	public String bezeichnung;

	/**
	 * Das Kürzel der Klasse.
	 */
	@Schema(description = "das Klassenkürzel", example = "5a")
	public @NotNull String kuerzel = "";

	/**
	 * Die Parallelität (z. B. a/b/c).
	 */
	@Schema(description = "die Parallelität der Klasse", example = "b")
	public @NotNull String parallelitaet = "";

	/**
	 * Die ID der Stundentafel.
	 */
	@Schema(description = "die ID der Stundentafel", example = "17")
	public Long idStundentafel;

	/**
	 * Die ID der zugehörigen Schülergruppe.
	 */
	@Schema(description = "die ID der Schülergruppe", example = "815")
	public long idSchuelergruppe;

	/**
	 * Das Kürzel der Organisationsform.
	 */
	@Schema(description = "das Kürzel der Organisationsform", example = "G8")
	public String orgFormKrz;

	/**
	 * Die ID der Fachklasse (nur BK SBK).
	 */
	@Schema(description = "die ID der Fachklasse", example = "12")
	public Long idFachklasse;

	/**
	 * Die Schulgliederungsnummer (ASD-Schulform-Nr).
	 */
	@Schema(description = "die Schulgliederungsnummer (ASD-Schulform-Nr)", example = "03")
	public String asdSchulformNr;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvKlasse() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des UvKlasse-Objekts zurück.
	 *
	 * @return eine String-Repräsentation
	 */
	@Override
	public String toString() {
		return "UvLerngruppenLehrer{id=" + id
				+ ", idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", idSchuljahresabschnitt=" + idSchuljahresabschnitt
				+ ", bezeichnung=" + bezeichnung
				+ ", kuerzel=" + kuerzel
				+ ", parallelitaet=" + parallelitaet
				+ ", idStundentafel=" + idStundentafel
				+ ", idSchuelergruppe=" + idSchuelergruppe
				+ ", orgFormKrz=" + orgFormKrz
				+ ", idFachklasse=" + idFachklasse
				+ ", asdSchulformNr=" + asdSchulformNr
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
		return (another instanceof final UvKlasse ano) && (this.id == ano.id);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der ID.
	 *
	 * @return den Hashcode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
