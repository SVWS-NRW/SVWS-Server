import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { NotenKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_NotenKatalogEintrag } from '../../core/data/schule/NotenKatalogEintrag';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';

export class Note extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Note> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Note> = new Map<string, Note>();

	/**
	 * leerer Noteneintrag
	 */
	public static readonly KEINE : Note = new Note("KEINE", 0, -1, -1, null, "", "", "--------------------", null, null);

	/**
	 * Die Note "ungenügend" mit 0 Punkten.
	 */
	public static readonly UNGENUEGEND : Note = new Note("UNGENUEGEND", 1, 0, 600, 0, "6", "ungenuegend", "ungenuegend", null, null);

	/**
	 * Die Note "mangelhaft minus" mit 1 Punkt.
	 */
	public static readonly MANGELHAFT_MINUS : Note = new Note("MANGELHAFT_MINUS", 2, 1, 501, 1, "5-", "mangelhaft (minus)", "mangelhaft", null, null);

	/**
	 * Die Note "mangelhaft" mit 2 Punkten.
	 */
	public static readonly MANGELHAFT : Note = new Note("MANGELHAFT", 3, 2, 500, 2, "5", "mangelhaft", "mangelhaft", null, null);

	/**
	 * Die Note "mangelhaft plus" mit 3 Punkten.
	 */
	public static readonly MANGELHAFT_PLUS : Note = new Note("MANGELHAFT_PLUS", 4, 3, 499, 3, "5+", "mangelhaft (plus)", "mangelhaft", null, null);

	/**
	 * Die Note "ausreichend minus" mit 4 Punkten.
	 */
	public static readonly AUSREICHEND_MINUS : Note = new Note("AUSREICHEND_MINUS", 5, 4, 401, 4, "4-", "ausreichend (minus)", "ausreichend", null, null);

	/**
	 * Die Note "ausreichend " mit 5 Punkten.
	 */
	public static readonly AUSREICHEND : Note = new Note("AUSREICHEND", 6, 5, 400, 5, "4", "ausreichend", "ausreichend", null, null);

	/**
	 * Die Note "ausreichend plus" mit 6 Punkten.
	 */
	public static readonly AUSREICHEND_PLUS : Note = new Note("AUSREICHEND_PLUS", 7, 6, 399, 6, "4+", "ausreichend (plus)", "ausreichend", null, null);

	/**
	 * Die Note "befriedigend minus" mit 7 Punkten.
	 */
	public static readonly BEFRIEDIGEND_MINUS : Note = new Note("BEFRIEDIGEND_MINUS", 8, 7, 301, 7, "3-", "befriedigend (minus)", "befriedigend", null, null);

	/**
	 * Die Note "befriedigend" mit 8 Punkten.
	 */
	public static readonly BEFRIEDIGEND : Note = new Note("BEFRIEDIGEND", 9, 8, 300, 8, "3", "befriedigend", "befriedigend", null, null);

	/**
	 * Die Note "befriedigend plus" mit 9 Punkten.
	 */
	public static readonly BEFRIEDIGEND_PLUS : Note = new Note("BEFRIEDIGEND_PLUS", 10, 9, 299, 9, "3+", "befriedigend (plus)", "befriedigend", null, null);

	/**
	 * Die Note "gut minus" mit 10 Punkten.
	 */
	public static readonly GUT_MINUS : Note = new Note("GUT_MINUS", 11, 10, 201, 10, "2-", "gut (minus)", "gut", null, null);

	/**
	 * Die Note "gut" mit 111 Punkten.
	 */
	public static readonly GUT : Note = new Note("GUT", 12, 11, 200, 11, "2", "gut", "gut", null, null);

	/**
	 * Die Note "gut plus" mit 12 Punkten.
	 */
	public static readonly GUT_PLUS : Note = new Note("GUT_PLUS", 13, 12, 199, 12, "2+", "gut (plus)", "gut", null, null);

	/**
	 * Die Note "sehr gut mnus" mit 13 Punkten.
	 */
	public static readonly SEHR_GUT_MINUS : Note = new Note("SEHR_GUT_MINUS", 14, 13, 101, 13, "1-", "sehr gut (minus)", "sehr gut", null, null);

	/**
	 * Die Note "sehr gut" mit 14 Punkten.
	 */
	public static readonly SEHR_GUT : Note = new Note("SEHR_GUT", 15, 14, 100, 14, "1", "sehr gut", "sehr gut", null, null);

	/**
	 * Die Note "sehr gut plus" mit 15 Punkten.
	 */
	public static readonly SEHR_GUT_PLUS : Note = new Note("SEHR_GUT_PLUS", 16, 15, 99, 15, "1+", "sehr gut (plus)", "sehr gut", null, null);

	/**
	 * Der Noteneintrag "ärtzliches Attest" mit dem Wert AT.
	 */
	public static readonly ATTEST : Note = new Note("ATTEST", 17, 16, 2500, null, "AT", "(ärtzliches Attest)", "--------------------", null, null);

	/**
	 * Der Noteneintrag "mit besonderem Erfolg teilgenommen" mit dem Wert E1.
	 */
	public static readonly E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN : Note = new Note("E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN", 18, 17, 1000, null, "E1", "mit besonderem Erfolg teilgenommen", "mit besonderem Erfolg teilgenommen", null, null);

	/**
	 * Der Noteneintrag "mit Erfolg teilgenommen" mit dem Wert E2.
	 */
	public static readonly E2_MIT_ERFOLG_TEILGENOMMEN : Note = new Note("E2_MIT_ERFOLG_TEILGENOMMEN", 19, 18, 1001, null, "E2", "mit Erfolg teilgenommen", "mit Erfolg teilgenommen", null, null);

	/**
	 * Der Noteneintrag "teilgenommen" mit dem Wert E3.
	 */
	public static readonly E3_TEILGENOMMEN : Note = new Note("E3_TEILGENOMMEN", 20, 19, 1002, null, "E3", "teilgenommen", "teilgenommen", null, null);

	/**
	 * Der Noteneintrag "nicht teilgenommen" mit dem Wert NT.
	 */
	public static readonly NICHT_TEILGENOMMEN : Note = new Note("NICHT_TEILGENOMMEN", 21, 20, 4010, null, "NT", "(nicht teilgenommen)", "--------------------", null, null);

	/**
	 * Der Noteneintrag "kann nicht beurteilt werdent" mit dem Wert NB.
	 */
	public static readonly NICHT_BEURTEILT : Note = new Note("NICHT_BEURTEILT", 22, 21, 4000, null, "NB", "(kann nicht beurteilt werden)", "--------------------", null, null);

	/**
	 * Der Noteneintrag "nicht erteilt" mit dem Wert NE.
	 */
	public static readonly NICHT_ERTEILT : Note = new Note("NICHT_ERTEILT", 23, 22, 4020, null, "NE", "(nicht erteilt)", "--------------------", null, null);

	/**
	 * Der Noteneintrag "kein Unterricht wegen Lehrermangel" mit dem Wert LM.
	 */
	public static readonly LEHRERMANGEL : Note = new Note("LEHRERMANGEL", 24, 23, 9000, null, "LM", "(kein Unterricht wegen Lehrermangel)", "--------------------", null, null);

	/**
	 * Der Noteneintrag "abgemeldet" mit dem Wert AM.
	 */
	public static readonly ABGEMELDET : Note = new Note("ABGEMELDET", 25, 24, 2000, null, "AM", "(abgemeldet)", "--------------------", null, null);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Die eindeutige ID der Note
	 */
	public readonly id : number;

	/**
	 * Eine ID, die der Sortierung der Noteneinträge in einer Anwendung vorgibt
	 */
	public readonly sortierung : number;

	/**
	 * Die Notenpunkte, die dieser Note zugeordnet sind
	 */
	public readonly notenpunkte : number | null;

	/**
	 * Die Kurzschreibweise der Note als Zahl ggf. mit Tendenz (+/-)
	 */
	public readonly kuerzel : string;

	/**
	 * Die Note in ausführlicher Textform ggf. mit Tendenz (plus/minus)
	 */
	public readonly text : string;

	/**
	 * Die Note in ausführlicher Textform, wie sie auf einem Zeugnis dargestellt wird.
	 */
	public readonly textZeugnis : string;

	/**
	 * Gibt an, in welchem Schuljahr die Note einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public readonly gueltigVon : number | null;

	/**
	 * Gibt an, bis zu welchem Schuljahr die Note gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public readonly gueltigBis : number | null;

	/**
	 * Der Noten-Katalog-Eintrag
	 */
	private katalogEintrag : NotenKatalogEintrag | null = null;

	/**
	 * Erzeugt ein neues Element der Aufzählung
	 *  
	 * @param id              die eindeutige ID der Note
	 * @param sortierung      eine ID, die der Sortierung der Noteneinträge in einer Anwendung vorgibt  
	 * @param notenpunkte     die Notenpunkte, die dieser Note zugeordnet sind
	 * @param kuerzel         die Kurzschreibweise der Note als Zahl ggf. mit Tendenz (+/-)
	 * @param text            die Note in ausführlicher Textform ggf. mit Tendenz (plus/minus)
	 * @param textZeugnis     die Note in ausführlicher Textform, wie sie auf einem Zeugnis dargestellt wird
	 * @param gueltigVon      gibt an, in welchem Schuljahr die Note einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 * @param gueltigBis      gibt an, bis zu welchem Schuljahr die Note gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	private constructor(name : string, ordinal : number, id : number, sortierung : number, notenpunkte : number | null, kuerzel : string, text : string, textZeugnis : string, gueltigVon : number | null, gueltigBis : number | null) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Note.all_values_by_ordinal.push(this);
		Note.all_values_by_name.set(name, this);
		this.id = id;
		this.sortierung = sortierung;
		this.notenpunkte = notenpunkte;
		this.kuerzel = kuerzel;
		this.text = text;
		this.textZeugnis = textZeugnis;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

	/**
	 * Gibt zurück, ob es sich um eine echte Note oder nur um eine "Pseudonote" handelt
	 * 
	 * @return true, wenn es sich bei der Note um eine echte Note handelt
	 */
	public istNote() : boolean {
		return this.notenpunkte !== null;
	}

	/**
	 * Bestimmt die Note anhand des übergebenen Integer-Wert, welcher eine
	 * Note ohne Tendenz darstellt.
	 *  
	 * @param noteSekI    die Note ohne Tendenz
	 * 
	 * @return die Note
	 */
	public static fromNoteSekI(noteSekI : number | null) : Note | null {
		if (noteSekI === null)
			return null;
		switch (noteSekI) {
			case 1: 
				return Note.SEHR_GUT;
			case 2: 
				return Note.GUT;
			case 3: 
				return Note.BEFRIEDIGEND;
			case 4: 
				return Note.AUSREICHEND;
			case 5: 
				return Note.MANGELHAFT;
			case 6: 
				return Note.UNGENUEGEND;
			default: 
				return null;
		}
	}

	/**
	 * Gibt die Note anhand der angebenen Notenpunkte zurück.
	 * 
	 * @param notenpunkte   die Notenpunkte anhand derer die Note ermittelt wird
	 * 
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	public static fromNotenpunkte(notenpunkte : number | null) : Note {
		if (notenpunkte === null)
			return Note.KEINE;
		switch (notenpunkte) {
			case 0: 
				return Note.UNGENUEGEND;
			case 1: 
				return Note.MANGELHAFT_MINUS;
			case 2: 
				return Note.MANGELHAFT;
			case 3: 
				return Note.MANGELHAFT_PLUS;
			case 4: 
				return Note.AUSREICHEND_MINUS;
			case 5: 
				return Note.AUSREICHEND;
			case 6: 
				return Note.AUSREICHEND_PLUS;
			case 7: 
				return Note.BEFRIEDIGEND_MINUS;
			case 8: 
				return Note.BEFRIEDIGEND;
			case 9: 
				return Note.BEFRIEDIGEND_PLUS;
			case 10: 
				return Note.GUT_MINUS;
			case 11: 
				return Note.GUT;
			case 12: 
				return Note.GUT_PLUS;
			case 13: 
				return Note.SEHR_GUT_MINUS;
			case 14: 
				return Note.SEHR_GUT;
			case 15: 
				return Note.SEHR_GUT_PLUS;
		}
		return Note.KEINE;
	}

	/**
	 * Gibt die Note bzw. Pseudonote anhand des angebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel anhand derer die Note ermittelt wird
	 * 
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	public static fromKuerzel(kuerzel : string | null) : Note {
		if (kuerzel === null)
			return Note.KEINE;
		let kuerzelUppercase : string | null = kuerzel.toUpperCase();
		switch (kuerzelUppercase) {
			case "6": 
				return Note.UNGENUEGEND;
			case "5-": 
				return Note.MANGELHAFT_MINUS;
			case "5": 
				return Note.MANGELHAFT;
			case "5+": 
				return Note.MANGELHAFT_PLUS;
			case "4-": 
				return Note.AUSREICHEND_MINUS;
			case "4": 
				return Note.AUSREICHEND;
			case "4+": 
				return Note.AUSREICHEND_PLUS;
			case "3-": 
				return Note.BEFRIEDIGEND_MINUS;
			case "3": 
				return Note.BEFRIEDIGEND;
			case "3+": 
				return Note.BEFRIEDIGEND_PLUS;
			case "2-": 
				return Note.GUT_MINUS;
			case "2": 
				return Note.GUT;
			case "2+": 
				return Note.GUT_PLUS;
			case "1-": 
				return Note.SEHR_GUT_MINUS;
			case "1": 
				return Note.SEHR_GUT;
			case "1+": 
				return Note.SEHR_GUT_PLUS;
			case "E1": 
				return Note.E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN;
			case "E2": 
				return Note.E2_MIT_ERFOLG_TEILGENOMMEN;
			case "E3": 
				return Note.E3_TEILGENOMMEN;
			case "AT": 
				return Note.ATTEST;
			case "AM": 
				return Note.ABGEMELDET;
			case "NB": 
				return Note.NICHT_BEURTEILT;
			case "NT": 
				return Note.NICHT_TEILGENOMMEN;
			case "NE": 
				return Note.NICHT_ERTEILT;
			case "LM": 
				return Note.LEHRERMANGEL;
		}
		return Note.KEINE;
	}

	/**
	 * Gibt die Note anhand der angebenen Notenpunkte zurück, welche als String 
	 * übergeben werden.
	 * 
	 * @param notenpunkte   die Notenpunkte anhand derer die Note ermittelt wird als String
	 * 
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	public static fromNotenpunkteString(notenpunkte : string | null) : Note {
		if (notenpunkte === null)
			return Note.KEINE;
		switch (notenpunkte) {
			case "0": 
				return Note.UNGENUEGEND;
			case "1": 
				return Note.MANGELHAFT_MINUS;
			case "2": 
				return Note.MANGELHAFT;
			case "3": 
				return Note.MANGELHAFT_PLUS;
			case "4": 
				return Note.AUSREICHEND_MINUS;
			case "5": 
				return Note.AUSREICHEND;
			case "6": 
				return Note.AUSREICHEND_PLUS;
			case "7": 
				return Note.BEFRIEDIGEND_MINUS;
			case "8": 
				return Note.BEFRIEDIGEND;
			case "9": 
				return Note.BEFRIEDIGEND_PLUS;
			case "10": 
				return Note.GUT_MINUS;
			case "11": 
				return Note.GUT;
			case "12": 
				return Note.GUT_PLUS;
			case "13": 
				return Note.SEHR_GUT_MINUS;
			case "14": 
				return Note.SEHR_GUT;
			case "15": 
				return Note.SEHR_GUT_PLUS;
		}
		return Note.KEINE;
	}

	/**
	 * Gibt an, ob es sich um eine Note mit Tendenz handelt oder nicht.
	 * 
	 * @return true, falls die Note eine Tendenz hat
	 */
	public hatTendenz() : boolean {
		if (this.notenpunkte === null)
			return false;
		switch (this.notenpunkte) {
			case 0: 
			case 2: 
			case 5: 
			case 8: 
			case 11: 
			case 14: 
				return false;
		}
		return true;
	}

	/**
	 * Ermittelt die zu der Note gehörende Note ohne Tendenz (z.B. 3+ wird zu 3)
	 * 
	 * @return die entsprechende Note ohne Tendenz
	 */
	public ohneTendenz() : Note {
		if (this.notenpunkte === null)
			return Note.KEINE;
		switch (this.notenpunkte) {
			case 0: 
				return Note.UNGENUEGEND;
			case 1: 
			case 2: 
			case 3: 
				return Note.MANGELHAFT;
			case 4: 
			case 5: 
			case 6: 
				return Note.AUSREICHEND;
			case 7: 
			case 8: 
			case 9: 
				return Note.BEFRIEDIGEND;
			case 10: 
			case 11: 
			case 12: 
				return Note.GUT;
			case 13: 
			case 14: 
			case 15: 
				return Note.SEHR_GUT;
		}
		return Note.KEINE;
	}

	public toString() : string {
		return this.kuerzel;
	}

	/**
	 * Gibt die Note als Note der Sekundarstufe I ohne Tendenz als Zahl zurück.
	 * 
	 * @return die Noten 1-6 oder im Fehlerfall null  
	 */
	public getNoteSekI() : number | null {
		switch (this.notenpunkte) {
			case 15: 
			case 14: 
			case 13: 
				return 1;
			case 12: 
			case 11: 
			case 10: 
				return 2;
			case 9: 
			case 8: 
			case 7: 
				return 3;
			case 6: 
			case 5: 
			case 4: 
				return 4;
			case 3: 
			case 2: 
			case 1: 
				return 5;
			case 0: 
				return 6;
			default: 
				return null;
		}
	}

	/**
	 * Gibt den Noten-Katalog-Eintrag zu dieser Note zurück.
	 *  
	 * @return der Noten-Katalog-Eintrag
	 */
	public getKatalogEintrag() : NotenKatalogEintrag {
		if (this.katalogEintrag === null)
			this.katalogEintrag = new NotenKatalogEintrag(this.id, this.sortierung, this.notenpunkte, this.kuerzel, this.text, this.textZeugnis, this.gueltigVon, this.gueltigBis);
		return this.katalogEintrag;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof Note))
			return false;
		return this === other;
	}

	/**
	 * Returns the ordinal value as hashcode, since the ordinal value is unique.
	 *
	 * @returns the ordinal value as hashcode
	 */
	public hashCode() : number {
		return this.__ordinal;
	}

	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	public compareTo(other : Note) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Note> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Note | null {
		let tmp : Note | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.Note'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_Note(obj : unknown) : Note {
	return obj as Note;
}
