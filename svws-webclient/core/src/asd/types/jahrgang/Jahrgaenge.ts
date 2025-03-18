import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JahrgaengeKatalogEintrag } from '../../../asd/data/jahrgang/JahrgaengeKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { SchulformKatalogEintrag } from '../../../asd/data/schule/SchulformKatalogEintrag';
import { Schulgliederung } from '../../../asd/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class Jahrgaenge extends JavaEnum<Jahrgaenge> implements CoreType<JahrgaengeKatalogEintrag, Jahrgaenge> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Jahrgaenge> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Jahrgaenge> = new Map<string, Jahrgaenge>();

	/**
	 * Jahrgang 90: Hausfrüherziehung für Hör- bzw. Sehgeschädigte
	 */
	public static readonly HAUSFRUEHERZIEHUNG : Jahrgaenge = new Jahrgaenge("HAUSFRUEHERZIEHUNG", 0, );

	/**
	 * Jahrgang 00: Frühkindliche Förderung, Förderschulkindergarten
	 */
	public static readonly JAHRGANG_00 : Jahrgaenge = new Jahrgaenge("JAHRGANG_00", 1, );

	/**
	 * 1. Jahrgang
	 */
	public static readonly JAHRGANG_01 : Jahrgaenge = new Jahrgaenge("JAHRGANG_01", 2, );

	/**
	 * 2. Jahrgang
	 */
	public static readonly JAHRGANG_02 : Jahrgaenge = new Jahrgaenge("JAHRGANG_02", 3, );

	/**
	 * 3. Jahrgang
	 */
	public static readonly JAHRGANG_03 : Jahrgaenge = new Jahrgaenge("JAHRGANG_03", 4, );

	/**
	 * 4. Jahrgang
	 */
	public static readonly JAHRGANG_04 : Jahrgaenge = new Jahrgaenge("JAHRGANG_04", 5, );

	/**
	 * 5. Jahrgang
	 */
	public static readonly JAHRGANG_05 : Jahrgaenge = new Jahrgaenge("JAHRGANG_05", 6, );

	/**
	 * 6. Jahrgang
	 */
	public static readonly JAHRGANG_06 : Jahrgaenge = new Jahrgaenge("JAHRGANG_06", 7, );

	/**
	 * 7. Jahrgang
	 */
	public static readonly JAHRGANG_07 : Jahrgaenge = new Jahrgaenge("JAHRGANG_07", 8, );

	/**
	 * 8. Jahrgang
	 */
	public static readonly JAHRGANG_08 : Jahrgaenge = new Jahrgaenge("JAHRGANG_08", 9, );

	/**
	 * 9. Jahrgang
	 */
	public static readonly JAHRGANG_09 : Jahrgaenge = new Jahrgaenge("JAHRGANG_09", 10, );

	/**
	 * 10. Jahrgang
	 */
	public static readonly JAHRGANG_10 : Jahrgaenge = new Jahrgaenge("JAHRGANG_10", 11, );

	/**
	 * 11. Jahrgang
	 */
	public static readonly JAHRGANG_11 : Jahrgaenge = new Jahrgaenge("JAHRGANG_11", 12, );

	/**
	 * 12. Jahrgang
	 */
	public static readonly JAHRGANG_12 : Jahrgaenge = new Jahrgaenge("JAHRGANG_12", 13, );

	/**
	 * 13. Jahrgang
	 */
	public static readonly JAHRGANG_13 : Jahrgaenge = new Jahrgaenge("JAHRGANG_13", 14, );

	/**
	 * Jahrgang EF: Gymnasiale Oberstufe - Einführungsphase
	 */
	public static readonly EF : Jahrgaenge = new Jahrgaenge("EF", 15, );

	/**
	 * Jahrgang Q1: Gymnasiale Oberstufe - Qualifikationsphase 1. Jahr
	 */
	public static readonly Q1 : Jahrgaenge = new Jahrgaenge("Q1", 16, );

	/**
	 * Jahrgang Q2: Gymnasiale Oberstufe - Qualifikationsphase 2. Jahr
	 */
	public static readonly Q2 : Jahrgaenge = new Jahrgaenge("Q2", 17, );

	/**
	 * Jahrgang Berufspraxisstufe laut AO-SF für den Förderschwerpunkt Geistige Entwicklung (je nach Organisationsform in Teilzeit (86) und Vollzeit (85) möglich)
	 */
	public static readonly BERUFSPRAXISSTUFE : Jahrgaenge = new Jahrgaenge("BERUFSPRAXISSTUFE", 18, );

	/**
	 * 91: Abendrealschule Vorkurs, 1. Semester
	 */
	public static readonly REALSCHULE_VORKURS_SEMESTER_1 : Jahrgaenge = new Jahrgaenge("REALSCHULE_VORKURS_SEMESTER_1", 19, );

	/**
	 * 92: Abendrealschule Vorkurs, 2. Semester
	 */
	public static readonly REALSCHULE_VORKURS_SEMESTER_2 : Jahrgaenge = new Jahrgaenge("REALSCHULE_VORKURS_SEMESTER_2", 20, );

	/**
	 * Abendrealschule 1. Semester
	 */
	public static readonly REALSCHULE_SEMESTER_01 : Jahrgaenge = new Jahrgaenge("REALSCHULE_SEMESTER_01", 21, );

	/**
	 * Abendrealschule 2. Semester
	 */
	public static readonly REALSCHULE_SEMESTER_02 : Jahrgaenge = new Jahrgaenge("REALSCHULE_SEMESTER_02", 22, );

	/**
	 * Abendrealschule 3. Semester
	 */
	public static readonly REALSCHULE_SEMESTER_03 : Jahrgaenge = new Jahrgaenge("REALSCHULE_SEMESTER_03", 23, );

	/**
	 * Abendrealschule 4. Semester
	 */
	public static readonly REALSCHULE_SEMESTER_04 : Jahrgaenge = new Jahrgaenge("REALSCHULE_SEMESTER_04", 24, );

	/**
	 * Abendgymnasium/Kolleg: Vorkurs, 1. Semester
	 */
	public static readonly VORKURS_SEMESTER_1 : Jahrgaenge = new Jahrgaenge("VORKURS_SEMESTER_1", 25, );

	/**
	 * Abendgymnasium/Kolleg: Vorkurs, 2. Semester
	 */
	public static readonly VORKURS_SEMESTER_2 : Jahrgaenge = new Jahrgaenge("VORKURS_SEMESTER_2", 26, );

	/**
	 * Abendgymnasium/Kolleg: 1. Semester (EF.1)
	 */
	public static readonly SEMESTER_01 : Jahrgaenge = new Jahrgaenge("SEMESTER_01", 27, );

	/**
	 * Abendgymnasium/Kolleg: 2. Semester (EF.2)
	 */
	public static readonly SEMESTER_02 : Jahrgaenge = new Jahrgaenge("SEMESTER_02", 28, );

	/**
	 * Abendgymnasium/Kolleg: 3. Semester (Q1.1)
	 */
	public static readonly SEMESTER_03 : Jahrgaenge = new Jahrgaenge("SEMESTER_03", 29, );

	/**
	 * Abendgymnasium/Kolleg: 4. Semester (Q1.2)
	 */
	public static readonly SEMESTER_04 : Jahrgaenge = new Jahrgaenge("SEMESTER_04", 30, );

	/**
	 * Abendgymnasium/Kolleg: 5. Semester (Q2.1)
	 */
	public static readonly SEMESTER_05 : Jahrgaenge = new Jahrgaenge("SEMESTER_05", 31, );

	/**
	 * Abendgymnasium/Kolleg: 6. Semester (Q2.2)
	 */
	public static readonly SEMESTER_06 : Jahrgaenge = new Jahrgaenge("SEMESTER_06", 32, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Jahrgaenge.all_values_by_ordinal.push(this);
		Jahrgaenge.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<JahrgaengeKatalogEintrag, Jahrgaenge>) : void {
		CoreTypeDataManager.putManager(Jahrgaenge.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<JahrgaengeKatalogEintrag, Jahrgaenge> {
		return CoreTypeDataManager.getManager(Jahrgaenge.class);
	}

	/**
	 * Prüft, ob die Schulform bei diesem Core-Type-Wert in dem angegeben Schuljahr zulässig ist oder nicht.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @param sf          die Schulform, auf die geprüft wird
	 *
	 * @return true, falls die Schulform zulässig ist, und ansonsten false
	 */
	public hatSchulform(schuljahr : number, sf : Schulform) : boolean {
		return Jahrgaenge.data().hatSchulform(schuljahr, sf, this);
	}

	/**
	 * Gibt den Katalog-Eintrag des Jahrgangs für die übergenene Schulform in dem übergebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param sf          die Schulform
	 *
	 * @return der Katalog-Eintrag oder null, wenn keiner gefunden wird
	 */
	public getBySchulform(schuljahr : number, sf : Schulform) : JahrgaengeKatalogEintrag | null {
		return Jahrgaenge.data().getBySchulform(schuljahr, sf, this);
	}

	/**
	 * Gibt zurück, ob es sich bei diesem Jahrgang um einen Jahrgang der gymnasialen Oberstufe
	 * handelt oder nicht.
	 *
	 * @return true, wenn dieser ein Jahrgang der gymnasialen Oberstufe ist, und ansonsten false
	 */
	public istGymOb() : boolean {
		let _sevar_627718607 : any;
		const _seexpr_627718607 = (this);
		if (_seexpr_627718607 === Jahrgaenge.EF) {
			_sevar_627718607 = true;
		} else if (_seexpr_627718607 === Jahrgaenge.Q1) {
			_sevar_627718607 = true;
		} else if (_seexpr_627718607 === Jahrgaenge.Q2) {
			_sevar_627718607 = true;
		} else if (_seexpr_627718607 === Jahrgaenge.JAHRGANG_11) {
			_sevar_627718607 = true;
		} else if (_seexpr_627718607 === Jahrgaenge.JAHRGANG_12) {
			_sevar_627718607 = true;
		} else if (_seexpr_627718607 === Jahrgaenge.JAHRGANG_13) {
			_sevar_627718607 = true;
		} else {
			_sevar_627718607 = false;
		}
		return _sevar_627718607;
	}

	/**
	 * Prüft, ob der angegebene Jahrgang bei der angegebenen Schulform und Gliederung ein gültiger Vorgänger-Jahrgang
	 * dieses Jahrgangs ist.
	 *
	 * @param schuljahr            das Schuljahr, welches für den Zugriff auf die Core-Type-Daten benötigt wird
	 * @param vergleichsjahrgang   der zu prüfende Jahrgang
	 * @param schulform            die Schulform
	 * @param gliederung           die Schulgliederung
	 *
	 * @return true, falls jgVorher ein gültiger Vorgänger-Jahrgang dieses Jahrgangs ist.
	 */
	public isNachfolgerVon(schuljahr : number, vergleichsjahrgang : Jahrgaenge | null, schulform : Schulform | null, gliederung : Schulgliederung | null) : boolean {
		if (schulform === null)
			return false;
		if (!this.hatSchulform(schuljahr, schulform) || ((vergleichsjahrgang !== null) && (!vergleichsjahrgang.hatSchulform(schuljahr, schulform))))
			return false;
		const gl : Schulgliederung | null = (gliederung === null) ? Schulgliederung.getDefault(schulform) : gliederung;
		let _sevar_1019613782 : any;
		const _seexpr_1019613782 = (this);
		if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_00) {
			_sevar_1019613782 = (vergleichsjahrgang === null);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_01) {
			_sevar_1019613782 = (vergleichsjahrgang === null);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_02) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_01 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_03) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_02 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_04) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_03 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_05) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_04 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_06) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_05 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_07) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_06 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_08) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_07 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_09) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_08 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_10) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_09 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_11) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_12) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_11 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.JAHRGANG_13) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_12 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.BERUFSPRAXISSTUFE) {
			_sevar_1019613782 = (vergleichsjahrgang === null);
		} else if (_seexpr_1019613782 === Jahrgaenge.HAUSFRUEHERZIEHUNG) {
			_sevar_1019613782 = (vergleichsjahrgang === null);
		} else if (_seexpr_1019613782 === Jahrgaenge.VORKURS_SEMESTER_1) {
			_sevar_1019613782 = (vergleichsjahrgang === null);
		} else if (_seexpr_1019613782 === Jahrgaenge.VORKURS_SEMESTER_2) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.VORKURS_SEMESTER_1 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.EF) {
			_sevar_1019613782 = (gl as unknown === Schulgliederung.GY8 as unknown) ? (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_09 as unknown) : (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.Q1) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.EF as unknown);
		} else if (_seexpr_1019613782 === Jahrgaenge.Q2) {
			_sevar_1019613782 = (vergleichsjahrgang as unknown === Jahrgaenge.Q1 as unknown);
		} else {
			_sevar_1019613782 = false;
		}
		return _sevar_1019613782;
	}

	/**
	 * Prüft, ob der angegebene Jahrgang bei der angegebenen Schulform und Gliederung ein gültiger Nachfolger-Jahrgang
	 * dieses Jahrgangs ist.
	 *
	 * @param schuljahr            das Schuljahr, welches für den Zugriff auf die Core-Type-Daten benötigt wird
	 * @param vergleichsjahrgang    der zu prüfende Jahrgang
	 * @param schulform    die Schulform
	 * @param gliederung   die Schulgliederung
	 *
	 * @return true, falls jgNachher ein gültiger Nachfolger-Jahrgang dieses Jahrgangs ist.
	 */
	public isVorgaengerVon(schuljahr : number, vergleichsjahrgang : Jahrgaenge | null, schulform : Schulform | null, gliederung : Schulgliederung | null) : boolean {
		if (schulform === null)
			return false;
		if (!this.hatSchulform(schuljahr, schulform) || ((vergleichsjahrgang !== null) && (!vergleichsjahrgang.hatSchulform(schuljahr, schulform))))
			return false;
		const ske : SchulformKatalogEintrag | null = schulform.daten(schuljahr);
		if (ske === null)
			return false;
		const gl : Schulgliederung | null = (gliederung === null) ? Schulgliederung.getDefault(schulform) : gliederung;
		let _sevar_412687505 : any;
		const _seexpr_412687505 = (this);
		if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_00) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_01 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_01) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_02 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_02) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_03 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_03) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_04 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_04) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_05 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_05) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_06 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_06) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_07 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_07) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_08 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_08) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_09 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_09) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_10 as unknown) || ((schulform as unknown === Schulform.GY as unknown) && ((gl as unknown === Schulgliederung.GY8 as unknown) || (gl as unknown === Schulgliederung.DEFAULT as unknown)) && (vergleichsjahrgang as unknown === Jahrgaenge.EF as unknown));
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_10) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_11 as unknown) || ((ske.hatGymOb) && (vergleichsjahrgang as unknown === Jahrgaenge.EF as unknown));
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_11) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_12 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_12) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_13 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.JAHRGANG_13) {
			_sevar_412687505 = (vergleichsjahrgang === null);
		} else if (_seexpr_412687505 === Jahrgaenge.BERUFSPRAXISSTUFE) {
			_sevar_412687505 = (vergleichsjahrgang === null);
		} else if (_seexpr_412687505 === Jahrgaenge.HAUSFRUEHERZIEHUNG) {
			_sevar_412687505 = (vergleichsjahrgang === null);
		} else if (_seexpr_412687505 === Jahrgaenge.VORKURS_SEMESTER_1) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.VORKURS_SEMESTER_2 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.VORKURS_SEMESTER_2) {
			_sevar_412687505 = (vergleichsjahrgang === null);
		} else if (_seexpr_412687505 === Jahrgaenge.EF) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.Q1 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.Q1) {
			_sevar_412687505 = (vergleichsjahrgang as unknown === Jahrgaenge.Q2 as unknown);
		} else if (_seexpr_412687505 === Jahrgaenge.Q2) {
			_sevar_412687505 = (vergleichsjahrgang === null);
		} else {
			_sevar_412687505 = false;
		}
		return _sevar_412687505;
	}

	/**
	 * Prüft, ob dieser Jahrgang ein möglicher Nachfolger des übergebenen Vergleichsjahrgangs ist.
	 * Da durch den Wechsel von G8 und G9 und den Wechsel von Sek-I (10) in die Sek-II eine Zuordnung unter Umständen
	 * nur Schüler individuell entschieden werden kann, wird hier nur auf mögliche Nachfolger geprüft.
	 *
	 * Diese Methode berücksichtigt keine Schulformen und -gliederungen und liefert daher nur ein grobes Ergebnis
	 *
	 * @param vergleichsjahrgang    der Jahrgang für den Vergleich zum gegebenen Jahrgang.
	 *
	 * @return true, falls dieser Jahrgang ein möglicher Vorgänger des übergebenen Vergleichsjahrgangs ist.
	 */
	public isMoeglicherNachfolgerVon(vergleichsjahrgang : Jahrgaenge | null) : boolean {
		let _sevar_1139221792 : any;
		const _seexpr_1139221792 = (this);
		if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_00) {
			_sevar_1139221792 = (vergleichsjahrgang === null);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_01) {
			_sevar_1139221792 = (vergleichsjahrgang === null);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_02) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_01 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_03) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_02 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_04) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_03 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_05) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_04 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_06) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_05 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_07) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_06 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_08) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_07 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_09) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_08 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_10) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_09 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_11) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_12) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_11 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.JAHRGANG_13) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_12 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.BERUFSPRAXISSTUFE) {
			_sevar_1139221792 = (vergleichsjahrgang === null);
		} else if (_seexpr_1139221792 === Jahrgaenge.HAUSFRUEHERZIEHUNG) {
			_sevar_1139221792 = (vergleichsjahrgang === null);
		} else if (_seexpr_1139221792 === Jahrgaenge.VORKURS_SEMESTER_1) {
			_sevar_1139221792 = (vergleichsjahrgang === null);
		} else if (_seexpr_1139221792 === Jahrgaenge.VORKURS_SEMESTER_2) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.VORKURS_SEMESTER_1 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.EF) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_09 as unknown) || (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.Q1) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.EF as unknown);
		} else if (_seexpr_1139221792 === Jahrgaenge.Q2) {
			_sevar_1139221792 = (vergleichsjahrgang as unknown === Jahrgaenge.Q1 as unknown);
		} else {
			_sevar_1139221792 = false;
		}
		return _sevar_1139221792;
	}

	/**
	 * Prüft, ob dieser Jahrgang ein möglicher Vorgänger des übergebenen Vergleichsjahrgangs ist.
	 * Da durch den Wechsel von G8 und G9 und den Wechsel von Sek-I (10) in die Sek-II eine Zuordnung unter Umständen
	 * nur Schüler individuell entschieden werden kann, wird hier nur auf mögliche Vorgänger geprüft.
	 *
	 * Diese Methode berücksichtigt keine Schulformen und -gliederungen und liefert daher nur ein grobes Ergebnis
	 *
	 * @param vergleichsjahrgang    der Jahrgang für den Vergleich zum gegebenen Jahrgang.
	 *
	 * @return true, falls dieser Jahrgang ein möglicher Vorgänger des übergebenen Vergleichsjahrgangs ist.
	 */
	public isMoeglicherVorgaengerVon(vergleichsjahrgang : Jahrgaenge | null) : boolean {
		let _sevar_1901634875 : any;
		const _seexpr_1901634875 = (this);
		if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_00) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_01 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_01) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_02 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_02) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_03 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_03) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_04 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_04) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_05 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_05) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_06 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_06) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_07 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_07) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_08 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_08) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_09 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_09) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_10) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_11 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_11) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_12 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_12) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.JAHRGANG_13 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.JAHRGANG_13) {
			_sevar_1901634875 = (vergleichsjahrgang === null);
		} else if (_seexpr_1901634875 === Jahrgaenge.BERUFSPRAXISSTUFE) {
			_sevar_1901634875 = (vergleichsjahrgang === null);
		} else if (_seexpr_1901634875 === Jahrgaenge.HAUSFRUEHERZIEHUNG) {
			_sevar_1901634875 = (vergleichsjahrgang === null);
		} else if (_seexpr_1901634875 === Jahrgaenge.VORKURS_SEMESTER_1) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.VORKURS_SEMESTER_2 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.VORKURS_SEMESTER_2) {
			_sevar_1901634875 = (vergleichsjahrgang === null);
		} else if (_seexpr_1901634875 === Jahrgaenge.EF) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.Q1 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.Q1) {
			_sevar_1901634875 = (vergleichsjahrgang as unknown === Jahrgaenge.Q2 as unknown);
		} else if (_seexpr_1901634875 === Jahrgaenge.Q2) {
			_sevar_1901634875 = (vergleichsjahrgang === null);
		} else {
			_sevar_1901634875 = false;
		}
		return _sevar_1901634875;
	}

	/**
	 * Gibt zurück, ob bei diesem Jahrgang für die angebebene Schulform und Schulgliederung in dem Schuljahr eine
	 * Lernbereichsnote 1 angebeben werden kann.
	 *
	 * @param schulform        die Schulform
	 * @param schulgliederung  die Schulgliederung
	 * @param schuljahr        das Schuljahr
	 *
	 * @return true, falls eine Lernbereichsnote vorhanden ist.
	 */
	public hatLernbereichsnote1(schulform : Schulform, schulgliederung : Schulgliederung | null, schuljahr : number) : boolean {
		let _sevar_434506116 : any;
		const _seexpr_434506116 = (schulform);
		if (_seexpr_434506116 === Schulform.R) {
			_sevar_434506116 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_434506116 === Schulform.SR) {
			_sevar_434506116 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_434506116 === Schulform.H) {
			_sevar_434506116 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_434506116 === Schulform.S) {
			_sevar_434506116 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_434506116 === Schulform.FW) {
			_sevar_434506116 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_434506116 === Schulform.WF) {
			_sevar_434506116 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_434506116 === Schulform.GY) {
			_sevar_434506116 = ((schulgliederung as unknown === Schulgliederung.GY8 as unknown) || (schulgliederung as unknown === Schulgliederung.DEFAULT as unknown)) ? (this as unknown === Jahrgaenge.EF as unknown) : (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_434506116 === Schulform.SG) {
			_sevar_434506116 = ((schulgliederung as unknown === Schulgliederung.GY8 as unknown) || (schulgliederung as unknown === Schulgliederung.DEFAULT as unknown)) ? (this as unknown === Jahrgaenge.EF as unknown) : (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_434506116 === Schulform.GM) {
			_sevar_434506116 = ((this as unknown === Jahrgaenge.JAHRGANG_10 as unknown) && (schuljahr <= 2024)) || ((this as unknown === Jahrgaenge.JAHRGANG_09 as unknown) && (schuljahr <= 2023)) || ((this as unknown === Jahrgaenge.JAHRGANG_08 as unknown) && (schuljahr <= 2022));
		} else if (_seexpr_434506116 === Schulform.GE) {
			_sevar_434506116 = ((this as unknown === Jahrgaenge.JAHRGANG_10 as unknown) && (schuljahr <= 2024)) || ((this as unknown === Jahrgaenge.JAHRGANG_09 as unknown) && (schuljahr <= 2023)) || ((this as unknown === Jahrgaenge.JAHRGANG_08 as unknown) && (schuljahr <= 2022));
		} else if (_seexpr_434506116 === Schulform.PS) {
			_sevar_434506116 = ((this as unknown === Jahrgaenge.JAHRGANG_10 as unknown) && (schuljahr <= 2024)) || ((this as unknown === Jahrgaenge.JAHRGANG_09 as unknown) && (schuljahr <= 2023)) || ((this as unknown === Jahrgaenge.JAHRGANG_08 as unknown) && (schuljahr <= 2022));
		} else if (_seexpr_434506116 === Schulform.SK) {
			_sevar_434506116 = ((this as unknown === Jahrgaenge.JAHRGANG_10 as unknown) && (schuljahr <= 2024)) || ((this as unknown === Jahrgaenge.JAHRGANG_09 as unknown) && (schuljahr <= 2023)) || ((this as unknown === Jahrgaenge.JAHRGANG_08 as unknown) && (schuljahr <= 2022));
		} else if (_seexpr_434506116 === Schulform.HI) {
			_sevar_434506116 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_434506116 === Schulform.KS) {
			_sevar_434506116 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_434506116 === Schulform.V) {
			_sevar_434506116 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_434506116 === Schulform.BK) {
			_sevar_434506116 = false;
		} else if (_seexpr_434506116 === Schulform.SB) {
			_sevar_434506116 = false;
		} else if (_seexpr_434506116 === Schulform.WB) {
			_sevar_434506116 = false;
		} else if (_seexpr_434506116 === Schulform.G) {
			_sevar_434506116 = false;
		}
		return _sevar_434506116;
	}

	/**
	 * Gibt die Bezeichnung der Lernbereichtsnote 1 zurück, sofern bei diesem Jahrgang für die angebebene Schulform
	 * und Schulgliederung in dem Schuljahr eine Lernbereichsnote 1 angebeben werden kann.
	 *
	 * @param schulform        die Schulform
	 * @param schulgliederung  die Schulgliederung
	 * @param schuljahr        das Schuljahr
	 *
	 * @return die Bezeichnung der Lernbereichsnote, falls eine vorhanden ist und ansonsten null.
	 */
	public getLernbereichsnote1Bezeichnung(schulform : Schulform, schulgliederung : Schulgliederung | null, schuljahr : number) : string | null {
		if (!this.hatLernbereichsnote1(schulform, schulgliederung, schuljahr))
			return null;
		let _sevar_825399400 : any;
		const _seexpr_825399400 = (schulform);
		if (_seexpr_825399400 === Schulform.H) {
			_sevar_825399400 = "Arbeitslehre";
		} else if (_seexpr_825399400 === Schulform.GM) {
			_sevar_825399400 = "Arbeitslehre";
		} else if (_seexpr_825399400 === Schulform.GE) {
			_sevar_825399400 = "Arbeitslehre";
		} else if (_seexpr_825399400 === Schulform.PS) {
			_sevar_825399400 = "Arbeitslehre";
		} else if (_seexpr_825399400 === Schulform.SK) {
			_sevar_825399400 = "Arbeitslehre";
		} else {
			_sevar_825399400 = "Gesellschaftslehre";
		}
		return _sevar_825399400;
	}

	/**
	 * Gibt zurück, ob bei diesem Jahrgang für die angebebene Schulform und Schulgliederung in dem Schuljahr eine
	 * Lernbereichsnote 2 angebeben werden kann.
	 *
	 * @param schulform        die Schulform
	 * @param schulgliederung  die Schulgliederung
	 * @param schuljahr        das Schuljahr
	 *
	 * @return true, falls eine Lernbereichsnote vorhanden ist.
	 */
	public hatLernbereichsnote2(schulform : Schulform, schulgliederung : Schulgliederung | null, schuljahr : number) : boolean {
		let _sevar_1863460395 : any;
		const _seexpr_1863460395 = (schulform);
		if (_seexpr_1863460395 === Schulform.R) {
			_sevar_1863460395 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1863460395 === Schulform.SR) {
			_sevar_1863460395 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1863460395 === Schulform.H) {
			_sevar_1863460395 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1863460395 === Schulform.S) {
			_sevar_1863460395 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1863460395 === Schulform.FW) {
			_sevar_1863460395 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1863460395 === Schulform.WF) {
			_sevar_1863460395 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1863460395 === Schulform.GY) {
			_sevar_1863460395 = ((schulgliederung as unknown === Schulgliederung.GY8 as unknown) || (schulgliederung as unknown === Schulgliederung.DEFAULT as unknown)) ? (this as unknown === Jahrgaenge.EF as unknown) : (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1863460395 === Schulform.SG) {
			_sevar_1863460395 = ((schulgliederung as unknown === Schulgliederung.GY8 as unknown) || (schulgliederung as unknown === Schulgliederung.DEFAULT as unknown)) ? (this as unknown === Jahrgaenge.EF as unknown) : (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1863460395 === Schulform.GM) {
			_sevar_1863460395 = ((this as unknown === Jahrgaenge.JAHRGANG_10 as unknown) || (this as unknown === Jahrgaenge.JAHRGANG_09 as unknown) || (this as unknown === Jahrgaenge.JAHRGANG_08 as unknown));
		} else if (_seexpr_1863460395 === Schulform.GE) {
			_sevar_1863460395 = ((this as unknown === Jahrgaenge.JAHRGANG_10 as unknown) || (this as unknown === Jahrgaenge.JAHRGANG_09 as unknown) || (this as unknown === Jahrgaenge.JAHRGANG_08 as unknown));
		} else if (_seexpr_1863460395 === Schulform.PS) {
			_sevar_1863460395 = ((this as unknown === Jahrgaenge.JAHRGANG_10 as unknown) || (this as unknown === Jahrgaenge.JAHRGANG_09 as unknown) || (this as unknown === Jahrgaenge.JAHRGANG_08 as unknown));
		} else if (_seexpr_1863460395 === Schulform.SK) {
			_sevar_1863460395 = ((this as unknown === Jahrgaenge.JAHRGANG_10 as unknown) || (this as unknown === Jahrgaenge.JAHRGANG_09 as unknown) || (this as unknown === Jahrgaenge.JAHRGANG_08 as unknown));
		} else if (_seexpr_1863460395 === Schulform.HI) {
			_sevar_1863460395 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1863460395 === Schulform.KS) {
			_sevar_1863460395 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1863460395 === Schulform.V) {
			_sevar_1863460395 = (this as unknown === Jahrgaenge.JAHRGANG_10 as unknown);
		} else if (_seexpr_1863460395 === Schulform.BK) {
			_sevar_1863460395 = false;
		} else if (_seexpr_1863460395 === Schulform.SB) {
			_sevar_1863460395 = false;
		} else if (_seexpr_1863460395 === Schulform.WB) {
			_sevar_1863460395 = false;
		} else if (_seexpr_1863460395 === Schulform.G) {
			_sevar_1863460395 = false;
		}
		return _sevar_1863460395;
	}

	/**
	 * Gibt die Bezeichnung der Lernbereichtsnote 2 zurück, sofern bei diesem Jahrgang für die angebebene Schulform
	 * und Schulgliederung in dem Schuljahr eine Lernbereichsnote 2 angebeben werden kann.
	 *
	 * @param schulform        die Schulform
	 * @param schulgliederung  die Schulgliederung
	 * @param schuljahr        das Schuljahr
	 *
	 * @return die Bezeichnung der Lernbereichsnote, falls eine vorhanden ist und ansonsten null.
	 */
	public getLernbereichsnote2Bezeichnung(schulform : Schulform, schulgliederung : Schulgliederung | null, schuljahr : number) : string | null {
		if (!this.hatLernbereichsnote2(schulform, schulgliederung, schuljahr))
			return null;
		return "Naturwissenschaft";
	}

	/**
	 * Liefert alle zulässigen Jahrgänge für die angegebene Schulform in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform in dem angegebenen Schuljahr zulässigen Jahrgänge
	 */
	public static getListBySchuljahrAndSchulform(schuljahr : number, schulform : Schulform) : List<Jahrgaenge> {
		return Jahrgaenge.data().getListBySchuljahrAndSchulform(schuljahr, schulform);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Jahrgaenge> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Jahrgaenge | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<JahrgaengeKatalogEintrag, Jahrgaenge> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : JahrgaengeKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<JahrgaengeKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.jahrgang.Jahrgaenge';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.jahrgang.Jahrgaenge', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Jahrgaenge>('de.svws_nrw.asd.types.jahrgang.Jahrgaenge');

}

export function cast_de_svws_nrw_asd_types_jahrgang_Jahrgaenge(obj : unknown) : Jahrgaenge {
	return obj as Jahrgaenge;
}
