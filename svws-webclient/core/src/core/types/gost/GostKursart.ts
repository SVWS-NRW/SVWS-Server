import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayMap } from '../../../core/adt/map/ArrayMap';
import { GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ZulaessigeKursart } from '../../../asd/types/kurse/ZulaessigeKursart';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { Arrays } from '../../../java/util/Arrays';
import type { JavaMap } from '../../../java/util/JavaMap';
import { GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';

export class GostKursart extends JavaEnum<GostKursart> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GostKursart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GostKursart> = new Map<string, GostKursart>();

	/**
	 * Leistungskurs = LK
	 */
	public static readonly LK : GostKursart = new GostKursart("LK", 0, 1, "LK", "Leistungskurs", Arrays.asList(ZulaessigeKursart.LK1, ZulaessigeKursart.LK2));

	/**
	 * Grundkurs = GK
	 */
	public static readonly GK : GostKursart = new GostKursart("GK", 1, 2, "GK", "Grundkurs", Arrays.asList(ZulaessigeKursart.GKM, ZulaessigeKursart.GKS, ZulaessigeKursart.AB3, ZulaessigeKursart.AB4, ZulaessigeKursart.EFSP));

	/**
	 * Zusatzkurs = ZK
	 */
	public static readonly ZK : GostKursart = new GostKursart("ZK", 2, 3, "ZK", "Zusatzkurs", Arrays.asList(ZulaessigeKursart.ZK));

	/**
	 * Projektkurs = PJK
	 */
	public static readonly PJK : GostKursart = new GostKursart("PJK", 3, 4, "PJK", "Projektkurs", Arrays.asList(ZulaessigeKursart.PJK));

	/**
	 * Vertiefungskurs = VTF
	 */
	public static readonly VTF : GostKursart = new GostKursart("VTF", 4, 5, "VTF", "Vertiefungskurs", Arrays.asList(ZulaessigeKursart.VTF));

	private static readonly FACHART_ID_FAKTOR : number = 1000;

	/**
	 * Die Zuordnung der Kursarten zu dem Kürzel der Kursart
	 */
	private static readonly _mapKuerzel : HashMap<string, GostKursart> = new HashMap<string, GostKursart>();

	/**
	 * Die Zuordnung der Kursarten zu der jeweiligen zulässigen Kursart
	 */
	private static readonly _mapZulKursart : JavaMap<ZulaessigeKursart, GostKursart> = new ArrayMap<ZulaessigeKursart, GostKursart>(ZulaessigeKursart.values());

	/**
	 * Die eindeutige ID der Kursart der Gymnasialen Oberstufe
	 */
	public readonly id : number;

	/**
	 * Das Kürzel der Kursart der Gymnasialen Oberstufe
	 */
	public readonly kuerzel : string;

	/**
	 * Die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe
	 */
	public readonly beschreibung : string;

	/**
	 * Die Liste der Kursarten, welche zu dieser Gost-Kursart gehören
	 */
	private readonly kursarten : List<ZulaessigeKursart>;

	/**
	 * Erzeugt eine neue Kursart für die Aufzählung.
	 *
	 * @param id             die eindeutige ID der Kursart der Gymnasialen Oberstufe
	 * @param kuerzel        das Kürzel der Kursart der Gymnasialen Oberstufe
	 * @param beschreibung   die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe
	 * @param kursarten      die zulässigen Kursarten, die dieser Kursart der gymnasialen Oberstufe zugeordnet sind
	 */
	private constructor(name : string, ordinal : number, id : number, kuerzel : string, beschreibung : string, kursarten : List<ZulaessigeKursart>) {
		super(name, ordinal);
		GostKursart.all_values_by_ordinal.push(this);
		GostKursart.all_values_by_name.set(name, this);
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		this.kursarten = kursarten;
	}

	/**
	 * Prüft die Anzahl der Wochenstunden zu der Kursart.
	 *
	 * @param anzahl   Anzahl der Wochenstunden
	 *
	 * @return         Anzahl der Wochenstunden der Kursart korrekt, true oder false
	 */
	public pruefeWochenstunden(anzahl : number) : boolean {
		switch (this.kuerzel) {
			case "GK": {
				return (anzahl === 3) || (anzahl === 4);
			}
			case "LK": {
				return (anzahl === 5);
			}
			case "PJK": {
				return (anzahl === 2) || (anzahl === 3);
			}
			case "VTF": {
				return (anzahl === 2);
			}
			case "ZK": {
				return (anzahl === 3);
			}
			default: {
				return false;
			}
		}
	}

	/**
	 * Gibt den Default-Wert für die Anzahl der Wochenstunden bei der Kursart zurück.
	 *
	 * @param istFSNeu   gibt an, ob es sich um eine neu einsetzende Fremdsprache handelt oder nicht.
	 *
	 * @return die Anzahl der Wochenstunden
	 */
	public getWochenstunden(istFSNeu : boolean) : number {
		switch (this.kuerzel) {
			case "GK": {
				return istFSNeu ? 4 : 3;
			}
			case "LK": {
				return 5;
			}
			case "PJK": {
				return 2;
			}
			case "VTF": {
				return 2;
			}
			case "ZK": {
				return 3;
			}
			default: {
				return 3;
			}
		}
	}

	/**
	 * Gibt eine Map von den Kürzeln auf die Gost-Kursart zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die Gost-Kursarten
	 */
	private static getMapByKuerzel() : HashMap<string, GostKursart> {
		if (GostKursart._mapKuerzel.size() === 0)
			for (const k of GostKursart.values())
				GostKursart._mapKuerzel.put(k.kuerzel, k);
		return GostKursart._mapKuerzel;
	}

	/**
	 * Gibt eine Map von den zulässigen Kursarten auf die Gost-Kursart zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den zulässigen Kursarten auf die Gost-Kursarten
	 */
	private static getMapByZulKursart() : JavaMap<ZulaessigeKursart, GostKursart> {
		if (GostKursart._mapZulKursart.size() === 0)
			for (const k of GostKursart.values())
				for (const zulKursart of k.kursarten)
					GostKursart._mapZulKursart.put(zulKursart, k);
		return GostKursart._mapZulKursart;
	}

	/**
	 * Gibt die Liste der zulässigen Kursarten zurück.
	 *
	 * @return die Liste der zulässigen Kursarten
	 */
	public getKursarten() : List<ZulaessigeKursart> {
		return this.kursarten;
	}

	/**
	 * Gibt die Kursart aus der ID Kursart zurück.
	 *
	 * @param id    die ID der Kursart
	 *
	 * @return die Kursart
	 *
	 * @throws DeveloperNotificationException falls die ID ungültig ist
	 */
	public static fromID(id : number) : GostKursart {
		switch (id) {
			case 1: {
				return GostKursart.LK;
			}
			case 2: {
				return GostKursart.GK;
			}
			case 3: {
				return GostKursart.ZK;
			}
			case 4: {
				return GostKursart.PJK;
			}
			case 5: {
				return GostKursart.VTF;
			}
			default: {
				throw new DeveloperNotificationException("Invalid ID value.")
			}
		}
	}

	/**
	 * Liefert die Kursart anhand der Kursart-ID der Fachwahl.
	 *
	 * @param pFachwahl Das Fachwahl-Objekt.
	 * @return die Kursart anhand der Kursart-ID der Fachwahl.
	 * @throws DeveloperNotificationException falls die ID ungültig ist
	 */
	public static fromFachwahlOrException(pFachwahl : GostFachwahl) : GostKursart {
		return GostKursart.fromID(pFachwahl.kursartID);
	}

	/**
	 * Gibt die Kursart aus der ID Kursart zurück.
	 *
	 * @param id    die ID der Kursart
	 *
	 * @return die Kursart oder null falls die ID ungültig ist
	 */
	public static fromIDorNull(id : number) : GostKursart | null {
		switch (id) {
			case 1: {
				return GostKursart.LK;
			}
			case 2: {
				return GostKursart.GK;
			}
			case 3: {
				return GostKursart.ZK;
			}
			case 4: {
				return GostKursart.PJK;
			}
			case 5: {
				return GostKursart.VTF;
			}
			default: {
				return null;
			}
		}
	}

	/**
	 * Gibt die Gost-Kursart aus dem Kürzel der Kursart zurück.
	 *
	 * @param kuerzel    das Kürzel der Kursart
	 *
	 * @return die Kursart oder null, falls das Kürzel ungültig ist
	 */
	public static fromKuerzel(kuerzel : string | null) : GostKursart | null {
		return GostKursart.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Gibt die Gost-Kursart aus dem Kürzel der Kursart zurück.
	 *
	 * @param kuerzel    das Kürzel der Kursart
	 *
	 * @return die Kursart oder null, falls das Kürzel ungültig ist
	 */
	public static fromKuerzelOrException(kuerzel : string | null) : GostKursart {
		const gk : GostKursart | null = GostKursart.getMapByKuerzel().get(kuerzel);
		if (gk === null)
			throw new DeveloperNotificationException("Invalid value for kurzel: " + kuerzel)
		return gk;
	}

	/**
	 * Bestimmt die Gost-Kursart anhand der übergebenen zulässigen Kursart
	 *
	 * @param kursart   die Kursart
	 *
	 * @return die Gost-Kursart
	 */
	public static fromKursart(kursart : ZulaessigeKursart | null) : GostKursart | null {
		return GostKursart.getMapByZulKursart().get(kursart);
	}

	/**
	 * Berechnet mit der Formel pFachID * {@link #FACHART_ID_FAKTOR} + pKursartID die ID der Fachart.
	 *
	 * @param  pFachID    Die DatenbankID des Faches.
	 * @param  pKursartID Die DatenbankID der Kursart.
	 *
	 * @return pFachID * {@link #FACHART_ID_FAKTOR} + pKursartID
	 */
	public static getFachartID(pFachID : number, pKursartID : number) : number {
		return (pFachID * GostKursart.FACHART_ID_FAKTOR) + pKursartID;
	}

	/**
	 * Berechnet anhand des Fachwahl-Objektes die FachartID.
	 * @param pFachwahl Das Fachwahl-Objekt.
	 *
	 * @return pFachwahl.fachID * {@link #FACHART_ID_FAKTOR} + pFachwahl.kursartID
	 */
	public static getFachartIDByFachwahl(pFachwahl : GostFachwahl) : number {
		return GostKursart.getFachartID(pFachwahl.fachID, pFachwahl.kursartID);
	}

	/**
	 * Berechnet anhand des Kurs-Objektes die FachartID.
	 *
	 * @param pKurs Das Kurs-Objekt.
	 *
	 * @return pKurs.fachID * {@link #FACHART_ID_FAKTOR} + pKurs.kursartID
	 */
	public static getFachartIDByKurs(pKurs : GostBlockungKurs) : number {
		return GostKursart.getFachartID(pKurs.fach_id, pKurs.kursart);
	}

	/**
	 * Berechnet anhand der Fachart-ID die Fach-ID.
	 *
	 * @param pFachartID Die ID der Fachart, welche das Fach und die Kursart kodiert.
	 *
	 * @return Ganzzahlige Division von pFachartID durch {@link #FACHART_ID_FAKTOR}
	 */
	public static getFachID(pFachartID : number) : number {
		return Math.trunc(pFachartID / GostKursart.FACHART_ID_FAKTOR);
	}

	/**
	 * Berechnet anhand der Fachart-ID die Kursart-ID.
	 *
	 * @param pFachartID Die ID der Fachart, welche das Fach und die Kursart kodiert.
	 *
	 * @return Rest der ganzzahligen Division von pFachartID durch {@link #FACHART_ID_FAKTOR}
	 */
	public static getKursartID(pFachartID : number) : number {
		return (pFachartID % GostKursart.FACHART_ID_FAKTOR) as number;
	}

	public toString() : string {
		return this.kuerzel;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostKursart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostKursart | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.gost.GostKursart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.GostKursart', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<GostKursart>('de.svws_nrw.core.types.gost.GostKursart');

}

export function cast_de_svws_nrw_core_types_gost_GostKursart(obj : unknown) : GostKursart {
	return obj as GostKursart;
}
