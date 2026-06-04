package de.svws_nrw.core.data.gost.laufbahnplanung.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.svws_nrw.transpiler.TranspilerDTO;
import jakarta.validation.constraints.NotNull;

/**
 * Datenaustauschformat für die Laufbahnplanung der gymnasialen Oberstufe:
 *
 * - DTO für eine Sprachbelegung.
 */
@TranspilerDTO
@JsonIgnoreProperties(ignoreUnknown = true)
public class GostLaufbahnplanungExportV1Sprachbelegung {

	/** Das einstellige Sprachkürzel des belegten Faches */
	public @NotNull String sprache = "";

	/** Für WbK: Gibt an, ob die Sprachbelegung einer zweiten Fremdsprache durch Nachweis erfolgt (siehe §34 Abst 3,4 APO-WbK) */
	public boolean istNachweis = false;

	/** Gibt an, an welcher Stelle in der Sprachenfolge die Sprache begonnen wurde */
	public Integer reihenfolge = null;

	/** Der Jahrgang, in dem die Sprache zum ersten mal belegt wurde */
	public String belegungVonJahrgang = null;

	/** Der Abschnitt des Jahrganges, in welchem die Sprache zum ersten mal belegt wurde */
	public Integer belegungVonAbschnitt = null;

	/** Der Jahrgang, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde */
	public String belegungBisJahrgang = null;

	/** Der Abschnitt des Jahrgangs, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde */
	public Integer belegungBisAbschnitt = null;

	/** Das Referenzniveau, welches bisher erreicht wurde */
	public String referenzniveau = null;

	/** Gibt an, ob das kleine Latinum erreicht wurde oder nicht. */
	public boolean hatKleinesLatinum = false;

	/** Gibt an, ob das Latinum erreicht wurde oder nicht. */
	public boolean hatLatinum = false;

	/** Gibt an, ob das Graecum erreicht wurde oder nicht. */
	public boolean hatGraecum = false;

	/** Gibt an, ob das Hebraicum erreicht wurde oder nicht. */
	public boolean hatHebraicum = false;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostLaufbahnplanungExportV1Sprachbelegung() {
		// leer
	}

}
