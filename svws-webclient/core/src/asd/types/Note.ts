import { JavaEnum } from '../../java/lang/JavaEnum';
import { NoteKatalogEintrag } from '../../asd/data/NoteKatalogEintrag';
import { CoreTypeDataManager } from '../../asd/utils/CoreTypeDataManager';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import type { CoreType } from '../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../asd/types/CoreType';

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
		let _sevar_928693309 : any;
		const _seexpr_928693309 = (noteSekI);
		if (_seexpr_928693309 === 1) {
			_sevar_928693309 = Note.SEHR_GUT;
		} else if (_seexpr_928693309 === 2) {
			_sevar_928693309 = Note.GUT;
		} else if (_seexpr_928693309 === 3) {
			_sevar_928693309 = Note.BEFRIEDIGEND;
		} else if (_seexpr_928693309 === 4) {
			_sevar_928693309 = Note.AUSREICHEND;
		} else if (_seexpr_928693309 === 5) {
			_sevar_928693309 = Note.MANGELHAFT;
		} else if (_seexpr_928693309 === 6) {
			_sevar_928693309 = Note.UNGENUEGEND;
		} else {
			_sevar_928693309 = null;
		}
		return _sevar_928693309;
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
		let _sevar_1649314226 : any;
		const _seexpr_1649314226 = (notenpunkte);
		if (_seexpr_1649314226 === 0) {
			_sevar_1649314226 = Note.UNGENUEGEND;
		} else if (_seexpr_1649314226 === 1) {
			_sevar_1649314226 = Note.MANGELHAFT_MINUS;
		} else if (_seexpr_1649314226 === 2) {
			_sevar_1649314226 = Note.MANGELHAFT;
		} else if (_seexpr_1649314226 === 3) {
			_sevar_1649314226 = Note.MANGELHAFT_PLUS;
		} else if (_seexpr_1649314226 === 4) {
			_sevar_1649314226 = Note.AUSREICHEND_MINUS;
		} else if (_seexpr_1649314226 === 5) {
			_sevar_1649314226 = Note.AUSREICHEND;
		} else if (_seexpr_1649314226 === 6) {
			_sevar_1649314226 = Note.AUSREICHEND_PLUS;
		} else if (_seexpr_1649314226 === 7) {
			_sevar_1649314226 = Note.BEFRIEDIGEND_MINUS;
		} else if (_seexpr_1649314226 === 8) {
			_sevar_1649314226 = Note.BEFRIEDIGEND;
		} else if (_seexpr_1649314226 === 9) {
			_sevar_1649314226 = Note.BEFRIEDIGEND_PLUS;
		} else if (_seexpr_1649314226 === 10) {
			_sevar_1649314226 = Note.GUT_MINUS;
		} else if (_seexpr_1649314226 === 11) {
			_sevar_1649314226 = Note.GUT;
		} else if (_seexpr_1649314226 === 12) {
			_sevar_1649314226 = Note.GUT_PLUS;
		} else if (_seexpr_1649314226 === 13) {
			_sevar_1649314226 = Note.SEHR_GUT_MINUS;
		} else if (_seexpr_1649314226 === 14) {
			_sevar_1649314226 = Note.SEHR_GUT;
		} else if (_seexpr_1649314226 === 15) {
			_sevar_1649314226 = Note.SEHR_GUT_PLUS;
		} else {
			_sevar_1649314226 = Note.KEINE;
		}
		return _sevar_1649314226;
	}

	/**
	 * Gibt die Pseudonote anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel anhand derer die Pseudonote ermittelt wird
	 *
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	private static pseudoNoteFromKuerzel(kuerzel : string) : Note {
		let _sevar_709055086 : any;
		const _seexpr_709055086 = (kuerzel);
		if (_seexpr_709055086 === "E1") {
			_sevar_709055086 = Note.E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN;
		} else if (_seexpr_709055086 === "E2") {
			_sevar_709055086 = Note.E2_MIT_ERFOLG_TEILGENOMMEN;
		} else if (_seexpr_709055086 === "E3") {
			_sevar_709055086 = Note.E3_TEILGENOMMEN;
		} else if (_seexpr_709055086 === "AT") {
			_sevar_709055086 = Note.ATTEST;
		} else if (_seexpr_709055086 === "AM") {
			_sevar_709055086 = Note.ABGEMELDET;
		} else if (_seexpr_709055086 === "NB") {
			_sevar_709055086 = Note.NICHT_BEURTEILT;
		} else if (_seexpr_709055086 === "NT") {
			_sevar_709055086 = Note.NICHT_TEILGENOMMEN;
		} else if (_seexpr_709055086 === "NE") {
			_sevar_709055086 = Note.NICHT_ERTEILT;
		} else if (_seexpr_709055086 === "LM") {
			_sevar_709055086 = Note.LEHRERMANGEL;
		} else {
			_sevar_709055086 = Note.KEINE;
		}
		return _sevar_709055086;
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
		let _sevar_1799037231 : any;
		const _seexpr_1799037231 = (kuerzelUppercase);
		if (_seexpr_1799037231 === "6") {
			_sevar_1799037231 = Note.UNGENUEGEND;
		} else if (_seexpr_1799037231 === "5-") {
			_sevar_1799037231 = Note.MANGELHAFT_MINUS;
		} else if (_seexpr_1799037231 === "5") {
			_sevar_1799037231 = Note.MANGELHAFT;
		} else if (_seexpr_1799037231 === "5+") {
			_sevar_1799037231 = Note.MANGELHAFT_PLUS;
		} else if (_seexpr_1799037231 === "4-") {
			_sevar_1799037231 = Note.AUSREICHEND_MINUS;
		} else if (_seexpr_1799037231 === "4") {
			_sevar_1799037231 = Note.AUSREICHEND;
		} else if (_seexpr_1799037231 === "4+") {
			_sevar_1799037231 = Note.AUSREICHEND_PLUS;
		} else if (_seexpr_1799037231 === "3-") {
			_sevar_1799037231 = Note.BEFRIEDIGEND_MINUS;
		} else if (_seexpr_1799037231 === "3") {
			_sevar_1799037231 = Note.BEFRIEDIGEND;
		} else if (_seexpr_1799037231 === "3+") {
			_sevar_1799037231 = Note.BEFRIEDIGEND_PLUS;
		} else if (_seexpr_1799037231 === "2-") {
			_sevar_1799037231 = Note.GUT_MINUS;
		} else if (_seexpr_1799037231 === "2") {
			_sevar_1799037231 = Note.GUT;
		} else if (_seexpr_1799037231 === "2+") {
			_sevar_1799037231 = Note.GUT_PLUS;
		} else if (_seexpr_1799037231 === "1-") {
			_sevar_1799037231 = Note.SEHR_GUT_MINUS;
		} else if (_seexpr_1799037231 === "1") {
			_sevar_1799037231 = Note.SEHR_GUT;
		} else if (_seexpr_1799037231 === "1+") {
			_sevar_1799037231 = Note.SEHR_GUT_PLUS;
		} else {
			_sevar_1799037231 = Note.pseudoNoteFromKuerzel(kuerzelUppercase);
		}
		return _sevar_1799037231;
	}

	/**
	 * Gibt die Note anhand der angegebenen Notenpunkte zurück, welche als String
	 * übergeben werden. Bei Pseudonoten wird die Note anhand des Kürzels ermittelt
	 *
	 * @param notenpunkte   die Notenpunkte anhand derer die Note ermittelt wird als String
	 *
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	public static fromNotenpunkteString(notenpunkte : string | null) : Note {
		if (notenpunkte === null)
			return Note.KEINE;
		let _sevar_397605981 : any;
		const _seexpr_397605981 = (notenpunkte);
		if (_seexpr_397605981 === "0") {
			_sevar_397605981 = Note.UNGENUEGEND;
		} else if (_seexpr_397605981 === "00") {
			_sevar_397605981 = Note.UNGENUEGEND;
		} else if (_seexpr_397605981 === "1") {
			_sevar_397605981 = Note.MANGELHAFT_MINUS;
		} else if (_seexpr_397605981 === "01") {
			_sevar_397605981 = Note.MANGELHAFT_MINUS;
		} else if (_seexpr_397605981 === "2") {
			_sevar_397605981 = Note.MANGELHAFT;
		} else if (_seexpr_397605981 === "02") {
			_sevar_397605981 = Note.MANGELHAFT;
		} else if (_seexpr_397605981 === "3") {
			_sevar_397605981 = Note.MANGELHAFT_PLUS;
		} else if (_seexpr_397605981 === "03") {
			_sevar_397605981 = Note.MANGELHAFT_PLUS;
		} else if (_seexpr_397605981 === "4") {
			_sevar_397605981 = Note.AUSREICHEND_MINUS;
		} else if (_seexpr_397605981 === "04") {
			_sevar_397605981 = Note.AUSREICHEND_MINUS;
		} else if (_seexpr_397605981 === "5") {
			_sevar_397605981 = Note.AUSREICHEND;
		} else if (_seexpr_397605981 === "05") {
			_sevar_397605981 = Note.AUSREICHEND;
		} else if (_seexpr_397605981 === "6") {
			_sevar_397605981 = Note.AUSREICHEND_PLUS;
		} else if (_seexpr_397605981 === "06") {
			_sevar_397605981 = Note.AUSREICHEND_PLUS;
		} else if (_seexpr_397605981 === "7") {
			_sevar_397605981 = Note.BEFRIEDIGEND_MINUS;
		} else if (_seexpr_397605981 === "07") {
			_sevar_397605981 = Note.BEFRIEDIGEND_MINUS;
		} else if (_seexpr_397605981 === "8") {
			_sevar_397605981 = Note.BEFRIEDIGEND;
		} else if (_seexpr_397605981 === "08") {
			_sevar_397605981 = Note.BEFRIEDIGEND;
		} else if (_seexpr_397605981 === "9") {
			_sevar_397605981 = Note.BEFRIEDIGEND_PLUS;
		} else if (_seexpr_397605981 === "09") {
			_sevar_397605981 = Note.BEFRIEDIGEND_PLUS;
		} else if (_seexpr_397605981 === "10") {
			_sevar_397605981 = Note.GUT_MINUS;
		} else if (_seexpr_397605981 === "11") {
			_sevar_397605981 = Note.GUT;
		} else if (_seexpr_397605981 === "12") {
			_sevar_397605981 = Note.GUT_PLUS;
		} else if (_seexpr_397605981 === "13") {
			_sevar_397605981 = Note.SEHR_GUT_MINUS;
		} else if (_seexpr_397605981 === "14") {
			_sevar_397605981 = Note.SEHR_GUT;
		} else if (_seexpr_397605981 === "15") {
			_sevar_397605981 = Note.SEHR_GUT_PLUS;
		} else {
			_sevar_397605981 = Note.pseudoNoteFromKuerzel(notenpunkte);
		}
		return _sevar_397605981;
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
		const np : number | null = nke.notenpunkte;
		let _sevar_240724349 : any;
		const _seexpr_240724349 = (np);
		if (_seexpr_240724349 === 0) {
			_sevar_240724349 = Note.UNGENUEGEND;
		} else if (_seexpr_240724349 === 1) {
			_sevar_240724349 = Note.MANGELHAFT;
		} else if (_seexpr_240724349 === 2) {
			_sevar_240724349 = Note.MANGELHAFT;
		} else if (_seexpr_240724349 === 3) {
			_sevar_240724349 = Note.MANGELHAFT;
		} else if (_seexpr_240724349 === 4) {
			_sevar_240724349 = Note.AUSREICHEND;
		} else if (_seexpr_240724349 === 5) {
			_sevar_240724349 = Note.AUSREICHEND;
		} else if (_seexpr_240724349 === 6) {
			_sevar_240724349 = Note.AUSREICHEND;
		} else if (_seexpr_240724349 === 7) {
			_sevar_240724349 = Note.BEFRIEDIGEND;
		} else if (_seexpr_240724349 === 8) {
			_sevar_240724349 = Note.BEFRIEDIGEND;
		} else if (_seexpr_240724349 === 9) {
			_sevar_240724349 = Note.BEFRIEDIGEND;
		} else if (_seexpr_240724349 === 10) {
			_sevar_240724349 = Note.GUT;
		} else if (_seexpr_240724349 === 11) {
			_sevar_240724349 = Note.GUT;
		} else if (_seexpr_240724349 === 12) {
			_sevar_240724349 = Note.GUT;
		} else if (_seexpr_240724349 === 13) {
			_sevar_240724349 = Note.SEHR_GUT;
		} else if (_seexpr_240724349 === 14) {
			_sevar_240724349 = Note.SEHR_GUT;
		} else if (_seexpr_240724349 === 15) {
			_sevar_240724349 = Note.SEHR_GUT;
		} else {
			_sevar_240724349 = Note.KEINE;
		}
		return _sevar_240724349;
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
	 * Gibt zurück, ob es sich bei der Note um ein Defizit im Sinne der gymnasialen Oberstufe handelt oder nicht.
	 *
	 * @return true, wenn es sich um ein Defizit handelt, und ansonsten false
	 */
	public istDefizitSekII() : boolean {
		return (this as unknown === Note.AUSREICHEND_MINUS as unknown) || (this as unknown === Note.MANGELHAFT_PLUS as unknown) || (this as unknown === Note.MANGELHAFT as unknown) || (this as unknown === Note.MANGELHAFT_MINUS as unknown) || (this as unknown === Note.UNGENUEGEND as unknown);
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
		if (nke === null)
			return null;
		const np : number | null = nke.notenpunkte;
		let _sevar_606963213 : any;
		const _seexpr_606963213 = (np);
		if (_seexpr_606963213 === 15) {
			_sevar_606963213 = 1;
		} else if (_seexpr_606963213 === 14) {
			_sevar_606963213 = 1;
		} else if (_seexpr_606963213 === 13) {
			_sevar_606963213 = 1;
		} else if (_seexpr_606963213 === 12) {
			_sevar_606963213 = 2;
		} else if (_seexpr_606963213 === 11) {
			_sevar_606963213 = 2;
		} else if (_seexpr_606963213 === 10) {
			_sevar_606963213 = 2;
		} else if (_seexpr_606963213 === 9) {
			_sevar_606963213 = 3;
		} else if (_seexpr_606963213 === 8) {
			_sevar_606963213 = 3;
		} else if (_seexpr_606963213 === 7) {
			_sevar_606963213 = 3;
		} else if (_seexpr_606963213 === 6) {
			_sevar_606963213 = 4;
		} else if (_seexpr_606963213 === 5) {
			_sevar_606963213 = 4;
		} else if (_seexpr_606963213 === 4) {
			_sevar_606963213 = 4;
		} else if (_seexpr_606963213 === 3) {
			_sevar_606963213 = 5;
		} else if (_seexpr_606963213 === 2) {
			_sevar_606963213 = 5;
		} else if (_seexpr_606963213 === 1) {
			_sevar_606963213 = 5;
		} else if (_seexpr_606963213 === 0) {
			_sevar_606963213 = 6;
		} else {
			_sevar_606963213 = null;
		}
		return _sevar_606963213;
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

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
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
