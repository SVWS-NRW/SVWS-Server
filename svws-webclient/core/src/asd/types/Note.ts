import { JavaEnum } from '../../java/lang/JavaEnum';
import { NoteKatalogEintrag } from '../../asd/data/NoteKatalogEintrag';
import { CoreTypeDataManager } from '../../asd/utils/CoreTypeDataManager';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import type { CoreType } from '../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../asd/types/CoreType';

export class Note extends JavaEnum<Note> implements CoreType<NoteKatalogEintrag, Note> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Note> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Note> = new Map<string, Note>();

	/**
	 * leerer Noteneintrag
	 */
	public static readonly KEINE : Note = new Note("KEINE", 0, );

	/**
	 * Die Note "ungenügend" mit 0 Punkten.
	 */
	public static readonly UNGENUEGEND : Note = new Note("UNGENUEGEND", 1, );

	/**
	 * Die Note "mangelhaft minus" mit 1 Punkt.
	 */
	public static readonly MANGELHAFT_MINUS : Note = new Note("MANGELHAFT_MINUS", 2, );

	/**
	 * Die Note "mangelhaft" mit 2 Punkten.
	 */
	public static readonly MANGELHAFT : Note = new Note("MANGELHAFT", 3, );

	/**
	 * Die Note "mangelhaft plus" mit 3 Punkten.
	 */
	public static readonly MANGELHAFT_PLUS : Note = new Note("MANGELHAFT_PLUS", 4, );

	/**
	 * Die Note "ausreichend minus" mit 4 Punkten.
	 */
	public static readonly AUSREICHEND_MINUS : Note = new Note("AUSREICHEND_MINUS", 5, );

	/**
	 * Die Note "ausreichend " mit 5 Punkten.
	 */
	public static readonly AUSREICHEND : Note = new Note("AUSREICHEND", 6, );

	/**
	 * Die Note "ausreichend plus" mit 6 Punkten.
	 */
	public static readonly AUSREICHEND_PLUS : Note = new Note("AUSREICHEND_PLUS", 7, );

	/**
	 * Die Note "befriedigend minus" mit 7 Punkten.
	 */
	public static readonly BEFRIEDIGEND_MINUS : Note = new Note("BEFRIEDIGEND_MINUS", 8, );

	/**
	 * Die Note "befriedigend" mit 8 Punkten.
	 */
	public static readonly BEFRIEDIGEND : Note = new Note("BEFRIEDIGEND", 9, );

	/**
	 * Die Note "befriedigend plus" mit 9 Punkten.
	 */
	public static readonly BEFRIEDIGEND_PLUS : Note = new Note("BEFRIEDIGEND_PLUS", 10, );

	/**
	 * Die Note "gut minus" mit 10 Punkten.
	 */
	public static readonly GUT_MINUS : Note = new Note("GUT_MINUS", 11, );

	/**
	 * Die Note "gut" mit 11 Punkten.
	 */
	public static readonly GUT : Note = new Note("GUT", 12, );

	/**
	 * Die Note "gut plus" mit 12 Punkten.
	 */
	public static readonly GUT_PLUS : Note = new Note("GUT_PLUS", 13, );

	/**
	 * Die Note "sehr gut minus" mit 13 Punkten.
	 */
	public static readonly SEHR_GUT_MINUS : Note = new Note("SEHR_GUT_MINUS", 14, );

	/**
	 * Die Note "sehr gut" mit 14 Punkten.
	 */
	public static readonly SEHR_GUT : Note = new Note("SEHR_GUT", 15, );

	/**
	 * Die Note "sehr gut plus" mit 15 Punkten.
	 */
	public static readonly SEHR_GUT_PLUS : Note = new Note("SEHR_GUT_PLUS", 16, );

	/**
	 * Der Noteneintrag "ärztliches Attest" mit dem Wert AT.
	 */
	public static readonly ATTEST : Note = new Note("ATTEST", 17, );

	/**
	 * Der Noteneintrag "mit besonderem Erfolg teilgenommen" mit dem Wert E1.
	 */
	public static readonly E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN : Note = new Note("E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN", 18, );

	/**
	 * Der Noteneintrag "mit Erfolg teilgenommen" mit dem Wert E2.
	 */
	public static readonly E2_MIT_ERFOLG_TEILGENOMMEN : Note = new Note("E2_MIT_ERFOLG_TEILGENOMMEN", 19, );

	/**
	 * Der Noteneintrag "teilgenommen" mit dem Wert E3.
	 */
	public static readonly E3_TEILGENOMMEN : Note = new Note("E3_TEILGENOMMEN", 20, );

	/**
	 * Der Noteneintrag "nicht teilgenommen" mit dem Wert NT.
	 */
	public static readonly NICHT_TEILGENOMMEN : Note = new Note("NICHT_TEILGENOMMEN", 21, );

	/**
	 * Der Noteneintrag "kann nicht beurteilt werden" mit dem Wert NB.
	 */
	public static readonly NICHT_BEURTEILT : Note = new Note("NICHT_BEURTEILT", 22, );

	/**
	 * Der Noteneintrag "nicht erteilt" mit dem Wert NE.
	 */
	public static readonly NICHT_ERTEILT : Note = new Note("NICHT_ERTEILT", 23, );

	/**
	 * Der Noteneintrag "kein Unterricht wegen Lehrermangel" mit dem Wert LM.
	 */
	public static readonly LEHRERMANGEL : Note = new Note("LEHRERMANGEL", 24, );

	/**
	 * Der Noteneintrag "abgemeldet" mit dem Wert AM.
	 */
	public static readonly ABGEMELDET : Note = new Note("ABGEMELDET", 25, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Note.all_values_by_ordinal.push(this);
		Note.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<NoteKatalogEintrag, Note>) : void {
		CoreTypeDataManager.putManager(Note.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<NoteKatalogEintrag, Note> {
		return CoreTypeDataManager.getManager(Note.class);
	}

	/**
	 * Gibt zurück, ob es sich um eine echte Note oder nur um eine "Pseudonote" handelt
	 *
	 * @param schuljahr	Prüfung im angegebenen Schuljahr
	 *
	 * @return true, wenn es sich bei der Note um eine echte Note handelt
	 */
	public istNote(schuljahr : number) : boolean {
		const nke : NoteKatalogEintrag | null = this.daten(schuljahr);
		return ((nke !== null) && (nke.notenpunkte !== null));
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
			case 1: {
				return Note.SEHR_GUT;
			}
			case 2: {
				return Note.GUT;
			}
			case 3: {
				return Note.BEFRIEDIGEND;
			}
			case 4: {
				return Note.AUSREICHEND;
			}
			case 5: {
				return Note.MANGELHAFT;
			}
			case 6: {
				return Note.UNGENUEGEND;
			}
			default: {
				return null;
			}
		}
	}

	/**
	 * Gibt die Note anhand der angegebenen Notenpunkte zurück.
	 *
	 * @param notenpunkte   die Notenpunkte anhand derer die Note ermittelt wird
	 *
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	public static fromNotenpunkte(notenpunkte : number | null) : Note {
		if (notenpunkte === null)
			return Note.KEINE;
		switch (notenpunkte) {
			case 0: {
				return Note.UNGENUEGEND;
			}
			case 1: {
				return Note.MANGELHAFT_MINUS;
			}
			case 2: {
				return Note.MANGELHAFT;
			}
			case 3: {
				return Note.MANGELHAFT_PLUS;
			}
			case 4: {
				return Note.AUSREICHEND_MINUS;
			}
			case 5: {
				return Note.AUSREICHEND;
			}
			case 6: {
				return Note.AUSREICHEND_PLUS;
			}
			case 7: {
				return Note.BEFRIEDIGEND_MINUS;
			}
			case 8: {
				return Note.BEFRIEDIGEND;
			}
			case 9: {
				return Note.BEFRIEDIGEND_PLUS;
			}
			case 10: {
				return Note.GUT_MINUS;
			}
			case 11: {
				return Note.GUT;
			}
			case 12: {
				return Note.GUT_PLUS;
			}
			case 13: {
				return Note.SEHR_GUT_MINUS;
			}
			case 14: {
				return Note.SEHR_GUT;
			}
			case 15: {
				return Note.SEHR_GUT_PLUS;
			}
			default: {
				return Note.KEINE;
			}
		}
	}

	/**
	 * Gibt die Note bzw. Pseudonote anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel anhand derer die Note ermittelt wird
	 *
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	public static fromKuerzel(kuerzel : string | null) : Note {
		if (kuerzel === null)
			return Note.KEINE;
		const kuerzelUppercase : string | null = kuerzel.toUpperCase();
		switch (kuerzelUppercase) {
			case "6": {
				return Note.UNGENUEGEND;
			}
			case "5-": {
				return Note.MANGELHAFT_MINUS;
			}
			case "5": {
				return Note.MANGELHAFT;
			}
			case "5+": {
				return Note.MANGELHAFT_PLUS;
			}
			case "4-": {
				return Note.AUSREICHEND_MINUS;
			}
			case "4": {
				return Note.AUSREICHEND;
			}
			case "4+": {
				return Note.AUSREICHEND_PLUS;
			}
			case "3-": {
				return Note.BEFRIEDIGEND_MINUS;
			}
			case "3": {
				return Note.BEFRIEDIGEND;
			}
			case "3+": {
				return Note.BEFRIEDIGEND_PLUS;
			}
			case "2-": {
				return Note.GUT_MINUS;
			}
			case "2": {
				return Note.GUT;
			}
			case "2+": {
				return Note.GUT_PLUS;
			}
			case "1-": {
				return Note.SEHR_GUT_MINUS;
			}
			case "1": {
				return Note.SEHR_GUT;
			}
			case "1+": {
				return Note.SEHR_GUT_PLUS;
			}
			case "E1": {
				return Note.E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN;
			}
			case "E2": {
				return Note.E2_MIT_ERFOLG_TEILGENOMMEN;
			}
			case "E3": {
				return Note.E3_TEILGENOMMEN;
			}
			case "AT": {
				return Note.ATTEST;
			}
			case "AM": {
				return Note.ABGEMELDET;
			}
			case "NB": {
				return Note.NICHT_BEURTEILT;
			}
			case "NT": {
				return Note.NICHT_TEILGENOMMEN;
			}
			case "NE": {
				return Note.NICHT_ERTEILT;
			}
			case "LM": {
				return Note.LEHRERMANGEL;
			}
			default: {
				return Note.KEINE;
			}
		}
	}

	/**
	 * Gibt die Note anhand der angegebenen Notenpunkte zurück, welche als String
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
			case "0": {
				return Note.UNGENUEGEND;
			}
			case "1": {
				return Note.MANGELHAFT_MINUS;
			}
			case "2": {
				return Note.MANGELHAFT;
			}
			case "3": {
				return Note.MANGELHAFT_PLUS;
			}
			case "4": {
				return Note.AUSREICHEND_MINUS;
			}
			case "5": {
				return Note.AUSREICHEND;
			}
			case "6": {
				return Note.AUSREICHEND_PLUS;
			}
			case "7": {
				return Note.BEFRIEDIGEND_MINUS;
			}
			case "8": {
				return Note.BEFRIEDIGEND;
			}
			case "9": {
				return Note.BEFRIEDIGEND_PLUS;
			}
			case "10": {
				return Note.GUT_MINUS;
			}
			case "11": {
				return Note.GUT;
			}
			case "12": {
				return Note.GUT_PLUS;
			}
			case "13": {
				return Note.SEHR_GUT_MINUS;
			}
			case "14": {
				return Note.SEHR_GUT;
			}
			case "15": {
				return Note.SEHR_GUT_PLUS;
			}
			default: {
				return Note.KEINE;
			}
		}
	}

	/**
	 * Gibt an, ob es sich um eine Note mit Tendenz handelt oder nicht.
	 *
	 * @param schuljahr	Prüfung im angegebenen Schuljahr
	 *
	 * @return true, falls die Note eine Tendenz hat
	 */
	public hatTendenz(schuljahr : number) : boolean {
		return Note.eintragHatTendenz(this.daten(schuljahr));
	}

	/**
	 * Gibt an, ob es sich um eine Note mit Tendenz handelt oder nicht.
	 *
	 * @param nke	der NotenKatalogEintrag
	 *
	 * @return true, falls die Note eine Tendenz hat
	 */
	public static eintragHatTendenz(nke : NoteKatalogEintrag | null) : boolean {
		return (nke !== null) && (nke.notenpunkte !== null) && (nke.notenpunkte !== 0) && (nke.notenpunkte % 3 !== 2);
	}

	/**
	 * Ermittelt die zu der Note gehörende Note ohne Tendenz (z.B. 3+ wird zu 3)
	 *
	 * @param schuljahr	Schuljahr, für das der Wert abgefragt wird.
	 *
	 * @return die entsprechende Note ohne Tendenz
	 */
	public ohneTendenz(schuljahr : number) : Note {
		const nke : NoteKatalogEintrag | null = this.daten(schuljahr);
		if (nke === null || nke.notenpunkte === null)
			return Note.KEINE;
		switch (nke.notenpunkte) {
			case 0: {
				return Note.UNGENUEGEND;
			}
			case 1:
			case 2:
			case 3: {
				return Note.MANGELHAFT;
			}
			case 4:
			case 5:
			case 6: {
				return Note.AUSREICHEND;
			}
			case 7:
			case 8:
			case 9: {
				return Note.BEFRIEDIGEND;
			}
			case 10:
			case 11:
			case 12: {
				return Note.GUT;
			}
			case 13:
			case 14:
			case 15: {
				return Note.SEHR_GUT;
			}
			default: {
				return Note.KEINE;
			}
		}
	}

	/**
	 * Gibt alle Noten ohne eine Tendenz zurück.
	 *
	 * @return die Noten ohne Tendenz
	 */
	public static getNotenOhneTendenz() : List<Note> {
		const result : List<Note> = new ArrayList<Note>();
		result.add(Note.SEHR_GUT);
		result.add(Note.GUT);
		result.add(Note.BEFRIEDIGEND);
		result.add(Note.AUSREICHEND);
		result.add(Note.MANGELHAFT);
		result.add(Note.UNGENUEGEND);
		return result;
	}

	public toString() : string;

	/**
	 * Gibt das Notenkürzel zurück
	 *
	 * @param schuljahr - Schuljahr, für das das Kürzel gilt
	 * @return String - das Notenkürzel
	 */
	public toString(schuljahr : number) : string;

	/**
	 * Implementation for method overloads of 'toString'
	 */
	public toString(__param0? : number) : string {
		if ((__param0 === undefined)) {
			return this.toString(0);
		} else if (((__param0 !== undefined) && typeof __param0 === "number")) {
			const schuljahr : number = __param0 as number;
			const nke : NoteKatalogEintrag | null = this.daten(schuljahr);
			if (nke !== null)
				return nke.kuerzel;
			return "";
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt die Note als Note der Sekundarstufe I ohne Tendenz als Zahl zurück.
	 *
	 * @param schuljahr	Schuljahr, für das der Wert abgefragt wird.
	 *
	 * @return die Noten 1-6 oder im Fehlerfall null
	 */
	public getNoteSekI(schuljahr : number) : number | null {
		const nke : NoteKatalogEintrag | null = this.daten(schuljahr);
		if (nke !== null) {
			switch (nke.notenpunkte) {
				case 15:
				case 14:
				case 13: {
					return 1;
				}
				case 12:
				case 11:
				case 10: {
					return 2;
				}
				case 9:
				case 8:
				case 7: {
					return 3;
				}
				case 6:
				case 5:
				case 4: {
					return 4;
				}
				case 3:
				case 2:
				case 1: {
					return 5;
				}
				case 0: {
					return 6;
				}
				default: {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * Gibt den Noten-Katalog-Eintrag zu dieser Note zurück.
	 *
	 * @param schuljahr	Schuljahr, für das der Wert abgefragt wird.
	 *
	 * @return der Noten-Katalog-Eintrag
	 */
	public getKatalogEintrag(schuljahr : number) : NoteKatalogEintrag | null {
		return this.daten(schuljahr);
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<NoteKatalogEintrag, Note> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : NoteKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<NoteKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.Note';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.Note', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Note>('de.svws_nrw.asd.types.Note');

}

export function cast_de_svws_nrw_asd_types_Note(obj : unknown) : Note {
	return obj as Note;
}
