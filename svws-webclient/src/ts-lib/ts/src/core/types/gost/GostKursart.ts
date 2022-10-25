import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { Collection, cast_java_util_Collection } from '../../../java/util/Collection';
import { GostFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { IllegalArgumentException, cast_java_lang_IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { GostBlockungKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';

export class GostKursart extends JavaObject {

	private static readonly FACHART_ID_FAKTOR : number = 1000;

	private static readonly map : HashMap<String, GostKursart> = new HashMap();

	public static readonly LK : GostKursart = new GostKursart(1, "LK", "Leistungskurs");

	public static readonly GK : GostKursart = new GostKursart(2, "GK", "Grundkurs");

	public static readonly ZK : GostKursart = new GostKursart(3, "ZK", "Zusatzkurs");

	public static readonly PJK : GostKursart = new GostKursart(4, "PJK", "Projektkurs");

	public static readonly VTF : GostKursart = new GostKursart(5, "VTF", "Vertiefungskurs");

	public readonly id : number;

	public readonly kuerzel : String;

	public readonly beschreibung : String;


	/**
	 * Erzeugt eine neue Kursart für die Aufzählung.
	 * 
	 * @param id             die eindeutige ID der Kursart der Gymnasialen Oberstufe
	 * @param kuerzel        das Kürzel der Kursart der Gymnasialen Oberstufe
	 * @param beschreibung   die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe
	 */
	private constructor(id : number, kuerzel : String, beschreibung : String) {
		super();
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		GostKursart.map.put(kuerzel, this);
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
			case "GK": 
				return (anzahl === 3) || (anzahl === 4);
			case "LK": 
				return (anzahl === 5);
			case "PJK": 
				return (anzahl === 2) || (anzahl === 3);
			case "VTF": 
				return (anzahl === 2);
			case "ZK": 
				return (anzahl === 3);
		}
		return false;
	}

	/**
	 * Gibt alle Kursarten der gymnasialen Oberstufe zurück.
	 * 
	 * @return eine {@link Collection} mit den Kursarten.
	 */
	public static values() : Collection<GostKursart> {
		return GostKursart.map.values();
	}

	/**
	 * Gibt die Kursart aus der ID Kursart zurück.
	 * 
	 * @param id    die ID der Kursart
	 * 
	 * @return die Kursart
	 * 
	 * @throws IllegalArgumentException falls die ID ungültig ist 
	 */
	public static fromID(id : number) : GostKursart {
		switch (id) {
			case 1: 
				return GostKursart.LK;
			case 2: 
				return GostKursart.GK;
			case 3: 
				return GostKursart.ZK;
			case 4: 
				return GostKursart.PJK;
			case 5: 
				return GostKursart.VTF;
		}
		throw new IllegalArgumentException("Invalid ID value.")
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
			case 1: 
				return GostKursart.LK;
			case 2: 
				return GostKursart.GK;
			case 3: 
				return GostKursart.ZK;
			case 4: 
				return GostKursart.PJK;
			case 5: 
				return GostKursart.VTF;
		}
		return null;
	}

	/**
	 * Gibt die Kursart aus dem Kürzel der Kursart zurück.
	 * 
	 * @param kuerzel    das Kürzel der Kursart
	 * 
	 * @return die Kursart oder null, falls das Kürzel ungültig ist 
	 */
	public static fromKuerzel(kuerzel : String | null) : GostKursart | null {
		return GostKursart.map.get(kuerzel);
	}

	/**
	 * Berechnet mit der Formel pFachID * {@link #FACHART_ID_FAKTOR} + pKursartID die ID der Fachart.
	 * 
	 * @param  pFachID    Die DatenbankID des Faches.
	 * @param  pKursartID Die DatenbankID der Kursart.
	 * 
	 * @return pFachID * {@link #FACHART_ID_FAKTOR} + pKursartID
	 */
	public static getFachartID(pFachID : number, pKursartID : number) : number;

	/**
	 * Berechnet anhand des Fachwahl-Objektes die FachartID.
	 * @param pFachwahl Das Fachwahl-Objekt.
	 * 
	 * @return pFachwahl.fachID * {@link #FACHART_ID_FAKTOR} + pFachwahl.kursartID
	 */
	public static getFachartID(pFachwahl : GostFachwahl) : number;

	/**
	 * Berechnet anhand des Kurs-Objektes die FachartID.
	 * @param pKurs Das Kurs-Objekt.
	 * 
	 * @return pKurs.fachID * {@link #FACHART_ID_FAKTOR} + pKurs.kursartID
	 */
	public static getFachartID(pKurs : GostBlockungKurs | null) : number;

	/**
	 * Implementation for method overloads of 'getFachartID'
	 */
	public static getFachartID(__param0 : GostBlockungKurs | GostFachwahl | null | number, __param1? : number) : number {
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			let pFachID : number = __param0 as number;
			let pKursartID : number = __param1 as number;
			return pFachID * GostKursart.FACHART_ID_FAKTOR + pKursartID;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.GostFachwahl')))) && (typeof __param1 === "undefined")) {
			let pFachwahl : GostFachwahl = cast_de_nrw_schule_svws_core_data_gost_GostFachwahl(__param0);
			return GostKursart.getFachartID(pFachwahl.fachID, pFachwahl.kursartID);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.GostBlockungKurs'))) || (__param0 === null)) && (typeof __param1 === "undefined")) {
			let pKurs : GostBlockungKurs | null = cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs(__param0);
			return GostKursart.getFachartID(pKurs.fach_id, pKurs.kursart);
		} else throw new Error('invalid method overload');
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

	public toString() : String {
		return this.kuerzel;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.gost.GostKursart'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_gost_GostKursart(obj : unknown) : GostKursart {
	return obj as GostKursart;
}
