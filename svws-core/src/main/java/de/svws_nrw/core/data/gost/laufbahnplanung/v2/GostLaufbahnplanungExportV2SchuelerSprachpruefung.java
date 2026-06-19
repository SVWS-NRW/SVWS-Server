package de.svws_nrw.core.data.gost.laufbahnplanung.v2;

import de.svws_nrw.transpiler.TranspilerDTO;
import jakarta.validation.constraints.NotNull;

/**
 * Datenaustauschformat für die Laufbahnplanung der gymnasialen Oberstufe:
 *
 * - Eine Sprachprüfung eines Schülers.
 */
@TranspilerDTO
public class GostLaufbahnplanungExportV2SchuelerSprachpruefung {

	/** Das einstellige Sprachkürzel des geprüften Faches */
	public @NotNull String sprache = "";

	/** Gibt an, in welchem ASD-Jahrgang die Prüfung abgelegt wurde */
	public String jahrgang = null;

	/** ID der Bezeichnung des am Schulabschluss orientierten Anspruchsniveau der Sprachprüfung */
	public Integer anspruchsniveauId = null;

	/** Gibt das Datum an, an dem die Prüfung abgelegt wurde */
	public String pruefungsdatum = null;

	/** Sprache, die durch die Prüfung ersetzt wird */
	public String ersetzteSprache = null;

	/** Prüfung ist eine Prüfung im herkunftssprachlichen Unterricht */
	public boolean istHSUPruefung = false;

	/** Prüfung ist eine Sprachfeststellungsprüfung */
	public boolean istFeststellungspruefung = false;

	/** Durch die Prüfung kann die erste Pflichtfremdsprache ersetzt werden */
	public boolean kannErstePflichtfremdspracheErsetzen = false;

	/** Durch die Prüfung kann die zweite Pflichtfremdsprache ersetzt werden */
	public boolean kannZweitePflichtfremdspracheErsetzen = false;

	/** Durch die Prüfung kann die Wahlpflichtfremdsprache ersetzt werden */
	public boolean kannWahlpflichtfremdspracheErsetzen = false;

	/** Durch die Prüfung kann die Sprache als fortgeführte Fremdsprache in der GOSt belegt werden */
	public boolean kannBelegungAlsFortgefuehrteSpracheErlauben = false;

	/** Das Kürzel des GeR-Referenzniveaus, welches durch die Prüfung erreicht wurde */
	public String referenzniveau = null;

	/** Die Note, die in der Sprachprüfung erreicht wurde (1,2,3,4,5,6 oder null, wenn keine Note angegeben ist) */
	public Integer note = null;

	/** Die Bezeichnung der Sprache auf dem Zeugnis (z.B. nötig für einen Eintrag "Sonstige Sprache") */
	public @NotNull String zeugnisbezeichnung = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostLaufbahnplanungExportV2SchuelerSprachpruefung() {
		// leer
	}

}
