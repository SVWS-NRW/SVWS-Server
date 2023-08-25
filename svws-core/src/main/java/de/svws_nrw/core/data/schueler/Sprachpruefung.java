package de.svws_nrw.core.data.schueler;

import de.svws_nrw.core.transpiler.TranspilerDTO;

/**
 * Dieser Core-DTO beinhaltet die Sprachbelegungsinformationen eines Schülers.
 */
@TranspilerDTO
public class Sprachpruefung {

	/** Das einstellige Sprachkürzel des geprüften Faches */
	public String sprache = null;

	/** Gibt an, in welchem ASD-Jahrgang die Prüfung abgelegt wurde */
	public String jahrgang;

	/** ID der Bezeichnung des am Schulabschluss orientierte Anspruchsniveau der Sprachprüfung */
	public Integer anspruchsniveauId;

	/** Gibt das Datum an, an dem die Prüfung abgelegt wurde */
	public String pruefungsdatum;

	/** Sprache, die durch die Prüfung ersetzt wird */
	public String ersetzteSprache;

    /** Prüfung ist eine Prüfung im herkunftssprachlichen Unterricht */
    public boolean istHSUPruefung;

    /** Prüfung ist eine Sprachfeststellungsprüfung */
    public boolean istFeststellungspruefung;

	/** Durch die Prüfung kann die erste Pflichtfremdsprache ersetzt werden */
	public boolean kannErstePflichtfremdspracheErsetzen;

	/** Durch die Prüfung kann die zweite Pflichtfremdsprache ersetzt werden */
	public boolean kannZweitePflichtfremdspracheErsetzen;

	/** Durch die Prüfung kann die Wahlpflichtfremdsprache ersetzt werden */
	public boolean kannWahlpflichtfremdspracheErsetzen;

    /** Durch die Prüfung kann die Sprache als fortgeführte Fremdsprache in der GOSt belegt werden.*/
    public boolean kannBelegungAlsFortgefuehrteSpracheErlauben;

	/** Das Kürzel des GeR-Referenzniveaus, welches durch die Prüfung erreicht wurde */
	public String referenzniveau;

    /** Die Note, die in der Sprachprüfung erreicht wurde (1,2,3,4,5,6 oder null, wenn keine Note angegeben ist) */
    public Integer note;

}
