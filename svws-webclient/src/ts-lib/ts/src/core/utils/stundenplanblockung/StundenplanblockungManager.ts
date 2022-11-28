import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { StundenplanblockungLehrkraft, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanblockungLehrkraft } from '../../../core/data/stundenplanblockung/StundenplanblockungLehrkraft';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { StundenplanblockungKlasse, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanblockungKlasse } from '../../../core/data/stundenplanblockung/StundenplanblockungKlasse';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { StundenplanblockungFach, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanblockungFach } from '../../../core/data/stundenplanblockung/StundenplanblockungFach';
import { StundenplanblockungInput, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanblockungInput } from '../../../core/data/stundenplanblockung/StundenplanblockungInput';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { Comparator, cast_java_util_Comparator } from '../../../java/util/Comparator';

export class StundenplanblockungManager extends JavaObject {

	private readonly _daten : StundenplanblockungInput;

	private readonly _map_id_lehrkraft : HashMap<Number, StundenplanblockungLehrkraft> = new HashMap();

	private static readonly _comp_lehrkraft_kuerzel : Comparator<StundenplanblockungLehrkraft> = { compare : (a: StundenplanblockungLehrkraft, b: StundenplanblockungLehrkraft) => {
		let cmpKuerzel : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (cmpKuerzel !== 0) 
			return cmpKuerzel;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _map_id_klasse : HashMap<Number, StundenplanblockungKlasse> = new HashMap();

	private static readonly _comp_klasse_kuerzel : Comparator<StundenplanblockungKlasse> = { compare : (a: StundenplanblockungKlasse, b: StundenplanblockungKlasse) => {
		let kuerzelA : String | null = a.kuerzel.trim();
		let kuerzelB : String | null = b.kuerzel.trim();
		while (kuerzelA.startsWith("0")) 
			kuerzelA = kuerzelA.substring(1);
		while (kuerzelB.startsWith("0")) 
			kuerzelB = kuerzelB.substring(1);
		let cmpKuerzel : number = JavaString.compareTo(kuerzelA, kuerzelB);
		if (cmpKuerzel !== 0) 
			return cmpKuerzel;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _map_id_fach : HashMap<Number, StundenplanblockungFach> = new HashMap();

	private static readonly _comp_fach_sortiernummer : Comparator<StundenplanblockungFach> = { compare : (a: StundenplanblockungFach, b: StundenplanblockungFach) => {
		let cmpSortiernummer : number = JavaInteger.compare(a.sortierung, b.sortierung);
		if (cmpSortiernummer !== 0) 
			return cmpSortiernummer;
		let cmpKuerzel : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (cmpKuerzel !== 0) 
			return cmpKuerzel;
		return JavaLong.compare(a.id, b.id);
	} };


	/**
	 * Erzeugt einen neuen Manager mit einem leeren {@link StundenplanblockungInput}-Objekt.
	 */
	public constructor() {
		super();
		this._daten = new StundenplanblockungInput();
	}

	/**
	 * Erzeugt eine Lehrkraft anhand der übergebenen Daten. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID bereits existiert.
	 * 
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @param pKuerzel               Das Kürzel der Lehrkraft.
	 * @throws NullPointerException  Falls die Lehrkraft-ID bereits existiert.
	 */
	public lehrkraftCreate(pLehrkraftID : number, pKuerzel : String) : void {
		if (this._map_id_lehrkraft.containsKey(pLehrkraftID)) 
			throw new NullPointerException("Lehrkraft-ID " + pLehrkraftID + " existiert bereits!")
		let le : StundenplanblockungLehrkraft | null = new StundenplanblockungLehrkraft();
		le.id = pLehrkraftID;
		le.kuerzel = pKuerzel;
		this._map_id_lehrkraft.put(pLehrkraftID, le);
		this._daten.lehrkraefte.add(le);
		this._daten.lehrkraefte.sort(StundenplanblockungManager._comp_lehrkraft_kuerzel);
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft-ID existiert. 
	 * 
	 * @param pLehrkraftID  Die Datenbank-ID der Lehrkraft.
	 * @return              TRUE, falls die Lehrkraft-ID existiert.
	 */
	public lehrkraftExists(pLehrkraftID : number) : boolean {
		return this._map_id_lehrkraft.containsKey(pLehrkraftID);
	}

	/**
	 * Liefert das {@link StundenplanblockungLehrkraft}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 * 
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @return                       Das {@link StundenplanblockungLehrkraft}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public lehrkraftGet(pLehrkraftID : number) : StundenplanblockungLehrkraft {
		let lehrkraft : StundenplanblockungLehrkraft | null = this._map_id_lehrkraft.get(pLehrkraftID);
		if (lehrkraft === null) 
			throw new NullPointerException("Lehrkraft-ID " + pLehrkraftID + " unbekannt!")
		return lehrkraft;
	}

	/**
	 * Liefert die Menge aller Lehrkräfte sortiert nach dem Kürzel.
	 * 
	 * @return Die Menge aller Lehrkräfte sortiert nach dem Kürzel.
	 */
	public lehrkraftGetMengeSortiertNachKuerzel() : Vector<StundenplanblockungLehrkraft | null> | null {
		return this._daten.lehrkraefte;
	}

	/**
	 * Löscht die übergebene Lehrkraft. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 * 
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public lehrkraftRemove(pLehrkraftID : number) : void {
		let lehrkraft : StundenplanblockungLehrkraft = this.lehrkraftGet(pLehrkraftID);
		this._map_id_lehrkraft.remove(pLehrkraftID);
		this._daten.lehrkraefte.remove(lehrkraft);
	}

	/**
	 * Ändert das Kürzel der Lehrkraft. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 * 
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @param pKuerzel               Das neue Kürzel der Lehrkraft.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public lehrkraftSetKuerzel(pLehrkraftID : number, pKuerzel : String) : void {
		let lehrkraft : StundenplanblockungLehrkraft = this.lehrkraftGet(pLehrkraftID);
		lehrkraft.kuerzel = pKuerzel;
		this._daten.lehrkraefte.sort(StundenplanblockungManager._comp_lehrkraft_kuerzel);
	}

	/**
	 * Ändert, ob die Lehrkraft prinzipiell vertreten dürfte. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID unbekannt ist.
	 * 
	 * @param pLehrkraftID           Die Datenbank-ID der Lehrkraft.
	 * @param pDarfVertreten         TRUE, falls die Lehrkraft prinzipiell vertreten dürfte.
	 * @throws NullPointerException  Falls die Lehrkraft-ID unbekannt ist.
	 */
	public lehrkraftSetDarfVertreten(pLehrkraftID : number, pDarfVertreten : boolean) : void {
		let lehrkraft : StundenplanblockungLehrkraft = this.lehrkraftGet(pLehrkraftID);
		lehrkraft.darfVertreten = pDarfVertreten;
	}

	/**
	 * Erzeugt eine Klasse anhand der übergebenen Daten. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID bereits existiert.
	 * 
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @param pKuerzel               Das Kürzel der Klasse.
	 * @throws NullPointerException  Falls die Klasse-ID bereits existiert.
	 */
	public klasseCreate(pKlasseID : number, pKuerzel : String) : void {
		if (this._map_id_klasse.containsKey(pKlasseID)) 
			throw new NullPointerException("Klasse-ID " + pKlasseID + " existiert bereits!")
		let kl : StundenplanblockungKlasse | null = new StundenplanblockungKlasse();
		kl.id = pKlasseID;
		kl.kuerzel = pKuerzel;
		this._map_id_klasse.put(pKlasseID, kl);
		this._daten.klassen.add(kl);
		this._daten.klassen.sort(StundenplanblockungManager._comp_klasse_kuerzel);
	}

	/**
	 * Liefert TRUE, falls die Klasse-ID existiert. 
	 * 
	 * @param pKlasseID  Die Datenbank-ID der Klasse.
	 * @return           TRUE, falls die Klasse-ID existiert.
	 */
	public klasseExists(pKlasseID : number) : boolean {
		return this._map_id_klasse.containsKey(pKlasseID);
	}

	/**
	 * Liefert das {@link StundenplanblockungKlasse}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID unbekannt ist.
	 * 
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @return                       Das {@link StundenplanblockungKlasse}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Klasse-ID unbekannt ist.
	 */
	public klasseGet(pKlasseID : number) : StundenplanblockungKlasse {
		let klasse : StundenplanblockungKlasse | null = this._map_id_klasse.get(pKlasseID);
		if (klasse === null) 
			throw new NullPointerException("Klasse-ID " + pKlasseID + " unbekannt!")
		return klasse;
	}

	/**
	 * Liefert die Menge aller Klassen sortiert nach dem Kürzel.
	 * 
	 * @return Die Menge aller Klassen sortiert nach dem Kürzel.
	 */
	public klasseGetMengeSortiertNachKuerzel() : Vector<StundenplanblockungKlasse | null> | null {
		return this._daten.klassen;
	}

	/**
	 * Löscht die übergebene Klasse. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID unbekannt ist.
	 * 
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @throws NullPointerException  Falls die Klasse-ID unbekannt ist.
	 */
	public klasseRemove(pKlasseID : number) : void {
		let klasse : StundenplanblockungKlasse = this.klasseGet(pKlasseID);
		this._map_id_klasse.remove(pKlasseID);
		this._daten.klassen.remove(klasse);
	}

	/**
	 * Ändert das Kürzel der Klasse. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID unbekannt ist.
	 * 
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @param pKuerzel               Das neue Kürzel der Klasse.
	 * @throws NullPointerException  Falls die Klasse-ID unbekannt ist.
	 */
	public klasseSetKuerzel(pKlasseID : number, pKuerzel : String) : void {
		let klasse : StundenplanblockungKlasse = this.klasseGet(pKlasseID);
		klasse.kuerzel = pKuerzel;
		this._daten.klassen.sort(StundenplanblockungManager._comp_klasse_kuerzel);
	}

	/**
	 * Erzeugt ein Fach anhand der übergebenen Daten. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID bereits existiert.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @param pKuerzel               Das Kürzel des Faches.
	 * @param pSortierung            Eine Zahlenwert Als Grundlage zur Sortierung der Fächer. 
	 * @throws NullPointerException  Falls die Fach-ID bereits existiert.
	 */
	public fachCreate(pFachID : number, pKuerzel : String, pSortierung : number) : void {
		if (this._map_id_fach.containsKey(pFachID)) 
			throw new NullPointerException("Fach-ID " + pFachID + " existiert bereits!")
		let fa : StundenplanblockungFach | null = new StundenplanblockungFach();
		fa.id = pFachID;
		fa.kuerzel = pKuerzel;
		fa.sortierung = pSortierung;
		this._map_id_fach.put(pFachID, fa);
		this._daten.faecher.add(fa);
		this._daten.faecher.sort(StundenplanblockungManager._comp_fach_sortiernummer);
	}

	/**
	 * Liefert TRUE, falls die Fach-ID existiert. 
	 * 
	 * @param pFachID  Die Datenbank-ID des Faches.
	 * @return         TRUE, falls die Fach-ID existiert.
	 */
	public fachExists(pFachID : number) : boolean {
		return this._map_id_fach.containsKey(pFachID);
	}

	/**
	 * Liefert das {@link StundenplanblockungFach}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID unbekannt ist.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @return                       Das {@link StundenplanblockungFach}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Fach-ID unbekannt ist.
	 */
	public fachGet(pFachID : number) : StundenplanblockungFach {
		let fach : StundenplanblockungFach | null = this._map_id_fach.get(pFachID);
		if (fach === null) 
			throw new NullPointerException("Fach-ID " + pFachID + " unbekannt!")
		return fach;
	}

	/**
	 * Liefert die Menge aller Fächer sortiert nach der Sortiernummer.
	 * 
	 * @return Die Menge aller Fächer sortiert nach der Sortiernummer.
	 */
	public fachGetMengeSortiertNachSortiernummer() : Vector<StundenplanblockungFach | null> | null {
		return this._daten.faecher;
	}

	/**
	 * Löscht das übergebene Fach. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID unbekannt ist.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @throws NullPointerException  Falls die Fach-ID unbekannt ist.
	 */
	public fachRemove(pFachID : number) : void {
		let fach : StundenplanblockungFach = this.fachGet(pFachID);
		this._map_id_fach.remove(pFachID);
		this._daten.faecher.remove(fach);
	}

	/**
	 * Ändert das Kürzel des Faches. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID unbekannt ist.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @param pKuerzel               Das neue Kürzel des Faches.
	 * @throws NullPointerException  Falls die Fach-ID unbekannt ist.
	 */
	public fachSetKuerzel(pFachID : number, pKuerzel : String) : void {
		let fach : StundenplanblockungFach = this.fachGet(pFachID);
		fach.kuerzel = pKuerzel;
		this._daten.faecher.sort(StundenplanblockungManager._comp_fach_sortiernummer);
	}

	/**
	 * Ändert die Sortiernummer des Faches. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID unbekannt ist.
	 * 
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @param pSortiernummer         Die neue Sortiernummer des Faches.
	 * @throws NullPointerException  Falls die Fach-ID unbekannt ist.
	 */
	public fachSetSortiernummer(pFachID : number, pSortiernummer : number) : void {
		let fach : StundenplanblockungFach = this.fachGet(pFachID);
		fach.sortierung = pSortiernummer;
		this._daten.faecher.sort(StundenplanblockungManager._comp_fach_sortiernummer);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManager(obj : unknown) : StundenplanblockungManager {
	return obj as StundenplanblockungManager;
}
