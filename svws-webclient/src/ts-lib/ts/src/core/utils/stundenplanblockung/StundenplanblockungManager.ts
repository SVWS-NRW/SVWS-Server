import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { StundenplanblockungFach, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanblockungFach } from '../../../core/data/stundenplanblockung/StundenplanblockungFach';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Comparator, cast_java_util_Comparator } from '../../../java/util/Comparator';
import { StundenplanblockungLehrkraft, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanblockungLehrkraft } from '../../../core/data/stundenplanblockung/StundenplanblockungLehrkraft';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { StundenplanblockungKopplung, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanblockungKopplung } from '../../../core/data/stundenplanblockung/StundenplanblockungKopplung';
import { StundenplanblockungKlasse, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanblockungKlasse } from '../../../core/data/stundenplanblockung/StundenplanblockungKlasse';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { StundenplanblockungInput, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanblockungInput } from '../../../core/data/stundenplanblockung/StundenplanblockungInput';
import { List, cast_java_util_List } from '../../../java/util/List';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { StundenplanblockungRaum, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanblockungRaum } from '../../../core/data/stundenplanblockung/StundenplanblockungRaum';

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

	private readonly _map_id_raum : HashMap<Number, StundenplanblockungRaum> = new HashMap();

	private static readonly _comp_raum_kuerzel : Comparator<StundenplanblockungRaum> = { compare : (a: StundenplanblockungRaum, b: StundenplanblockungRaum) => {
		let cmpKuerzel : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (cmpKuerzel !== 0) 
			return cmpKuerzel;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _map_id_kopplung : HashMap<Number, StundenplanblockungKopplung> = new HashMap();

	private static readonly _comp_kopplung_kuerzel : Comparator<StundenplanblockungKopplung> = { compare : (a: StundenplanblockungKopplung, b: StundenplanblockungKopplung) => {
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

	private lehrkraftAddOhneSortierung(pLehrkraft : StundenplanblockungLehrkraft) : void {
		if (this._map_id_lehrkraft.containsKey(pLehrkraft.id)) 
			throw new NullPointerException("Lehrkraft-ID " + pLehrkraft.id + " existiert bereits!")
		this._map_id_lehrkraft.put(pLehrkraft.id, pLehrkraft);
		this._daten.lehrkraefte.add(pLehrkraft);
	}

	/**
	 * Fügt die Lehrkraft hinzu. <br>
	 * Wirft eine NullPointerException, falls die Lehrkraft-ID bereits existiert.
	 * 
	 * @param pLehrkraft             Das Lehrkraft-Objekt.
	 * @throws NullPointerException  Falls die Lehrkraft-ID bereits existiert.
	 */
	public lehrkraftAdd(pLehrkraft : StundenplanblockungLehrkraft) : void {
		this.lehrkraftAddOhneSortierung(pLehrkraft);
		this._daten.lehrkraefte.sort(StundenplanblockungManager._comp_lehrkraft_kuerzel);
	}

	/**
	 * Fügt alle Lehrkräfte hinzu. <br>
	 * Wirft eine NullPointerException, falls eine der Lehrkräfte bereits existiert. 
	 * 
	 * @param pLehrkraefte           Die Menge der Lehrkräfte, die hinzugefügt werden soll.
	 * @throws NullPointerException  Falls eine der Lehrkräfte bereits existiert.
	 */
	public lehrkraftAddAll(pLehrkraefte : List<StundenplanblockungLehrkraft>) : void {
		for (let lehrkraft of pLehrkraefte) 
			this.lehrkraftAddOhneSortierung(lehrkraft);
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

	private klasseAddOhneSortierung(pKlasse : StundenplanblockungKlasse) : void {
		if (this._map_id_klasse.containsKey(pKlasse.id)) 
			throw new NullPointerException("Klasse-ID " + pKlasse.id + " existiert bereits!")
		this._map_id_klasse.put(pKlasse.id, pKlasse);
		this._daten.klassen.add(pKlasse);
	}

	/**
	 * Fügt die Klasse hinzu. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID bereits existiert.
	 * 
	 * @param pKlasse                Das Klassen-Objekt.
	 * @throws NullPointerException  Falls die Klasse-ID bereits existiert.
	 */
	public klasseAdd(pKlasse : StundenplanblockungKlasse) : void {
		this.klasseAddOhneSortierung(pKlasse);
		this._daten.klassen.sort(StundenplanblockungManager._comp_klasse_kuerzel);
	}

	/**
	 * Fügt alle Klassen hinzu. <br>
	 * Wirft eine NullPointerException, falls eine der Klassen bereits existiert. 
	 * 
	 * @param pKlassen               Die Menge der Klassen, die hinzugefügt werden soll.
	 * @throws NullPointerException  Falls eine der Klassen bereits existiert.
	 */
	public klasseAddAll(pKlassen : List<StundenplanblockungKlasse>) : void {
		for (let klasse of pKlassen) 
			this.klasseAddOhneSortierung(klasse);
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

	private fachAddOhneSortierung(pFach : StundenplanblockungFach) : void {
		if (this._map_id_fach.containsKey(pFach.id)) 
			throw new NullPointerException("Fach-ID " + pFach.id + " existiert bereits!")
		this._map_id_fach.put(pFach.id, pFach);
		this._daten.faecher.add(pFach);
	}

	/**
	 * Fügt das Fach hinzu. <br>
	 * Wirft eine NullPointerException, falls die Fach-ID bereits existiert.
	 * 
	 * @param pFach                  Das Fach-Objekt.
	 * @throws NullPointerException  Falls die Fach-ID bereits existiert.
	 */
	public fachAdd(pFach : StundenplanblockungFach) : void {
		this.fachAddOhneSortierung(pFach);
		this._daten.faecher.sort(StundenplanblockungManager._comp_fach_sortiernummer);
	}

	/**
	 * Fügt alle Fächer hinzu. <br>
	 * Wirft eine NullPointerException, falls eines der Fächer bereits existiert. 
	 * 
	 * @param pFaecher               Die Menge der Fächer, die hinzugefügt werden soll.
	 * @throws NullPointerException  Falls eines der Fächer bereits existiert.
	 */
	public fachAddAll(pFaecher : List<StundenplanblockungFach>) : void {
		for (let fach of pFaecher) 
			this.fachAddOhneSortierung(fach);
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

	private raumAddOhneSortierung(pRaum : StundenplanblockungRaum) : void {
		if (this._map_id_raum.containsKey(pRaum.id)) 
			throw new NullPointerException("Raum-ID " + pRaum.id + " existiert bereits!")
		this._map_id_raum.put(pRaum.id, pRaum);
		this._daten.raeume.add(pRaum);
	}

	/**
	 * Fügt den Raum hinzu. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID bereits existiert.
	 * 
	 * @param pRaum                  Das Raum-Objekt.
	 * @throws NullPointerException  Falls die Raum-ID bereits existiert.
	 */
	public raumAdd(pRaum : StundenplanblockungRaum) : void {
		this.raumAddOhneSortierung(pRaum);
		this._daten.raeume.sort(StundenplanblockungManager._comp_raum_kuerzel);
	}

	/**
	 * Fügt alle Räume hinzu. <br>
	 * Wirft eine NullPointerException, falls einer der Räume bereits existiert. 
	 * 
	 * @param pRaeume                Die Menge der Räume, die hinzugefügt werden soll.
	 * @throws NullPointerException  Falls einer der Räume bereits existiert.
	 */
	public raumAddAll(pRaeume : List<StundenplanblockungRaum>) : void {
		for (let raum of pRaeume) 
			this.raumAddOhneSortierung(raum);
		this._daten.raeume.sort(StundenplanblockungManager._comp_raum_kuerzel);
	}

	/**
	 * Liefert TRUE, falls die Raum-ID existiert. 
	 * 
	 * @param pRaumID  Die Datenbank-ID des Raumes.
	 * @return         TRUE, falls die Raum-ID existiert.
	 */
	public raumExists(pRaumID : number) : boolean {
		return this._map_id_raum.containsKey(pRaumID);
	}

	/**
	 * Liefert das {@link StundenplanblockungRaum}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID unbekannt ist.
	 * 
	 * @param pRaumID                Die Datenbank-ID des Raumes.
	 * @return                       Das {@link StundenplanblockungRaum}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Raum-ID unbekannt ist.
	 */
	public raumGet(pRaumID : number) : StundenplanblockungRaum {
		let raum : StundenplanblockungRaum | null = this._map_id_raum.get(pRaumID);
		if (raum === null) 
			throw new NullPointerException("Raum-ID " + pRaumID + " unbekannt!")
		return raum;
	}

	/**
	 * Liefert die Menge aller Räume sortiert nach dem Kürzel.
	 * 
	 * @return Die Menge aller Räume sortiert nach dem Kürzel.
	 */
	public raumGetMengeSortiertNachKuerzel() : Vector<StundenplanblockungRaum | null> | null {
		return this._daten.raeume;
	}

	/**
	 * Löscht den übergebenen Raum. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID unbekannt ist.
	 * 
	 * @param pRaumID                Die Datenbank-ID des Raumes.
	 * @throws NullPointerException  Falls die Raum-ID unbekannt ist.
	 */
	public raumRemove(pRaumID : number) : void {
		let raum : StundenplanblockungRaum = this.raumGet(pRaumID);
		this._map_id_raum.remove(pRaumID);
		this._daten.raeume.remove(raum);
	}

	/**
	 * Ändert das Kürzel des Raumes. <br>
	 * Wirft eine NullPointerException, falls die Raum-ID unbekannt ist.
	 * 
	 * @param pRaumID                Die Datenbank-ID des Raumes.
	 * @param pKuerzel               Das neue Kürzel des Raumes.
	 * @throws NullPointerException  Falls die Raum-ID unbekannt ist.
	 */
	public raumSetKuerzel(pRaumID : number, pKuerzel : String) : void {
		let raum : StundenplanblockungRaum = this.raumGet(pRaumID);
		raum.kuerzel = pKuerzel;
		this._daten.raeume.sort(StundenplanblockungManager._comp_raum_kuerzel);
	}

	private kopplungAddOhneSortierung(pKopplung : StundenplanblockungKopplung) : void {
		if (this._map_id_kopplung.containsKey(pKopplung.id)) 
			throw new NullPointerException("Kopplung-ID " + pKopplung.id + " existiert bereits!")
		this._map_id_kopplung.put(pKopplung.id, pKopplung);
		this._daten.kopplungen.add(pKopplung);
	}

	/**
	 * Fügt die Kopplung hinzu. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID bereits existiert.
	 * 
	 * @param pKopplung              Das Kopplung-Objekt.
	 * @throws NullPointerException  Falls die Kopplung-ID bereits existiert.
	 */
	public kopplungAdd(pKopplung : StundenplanblockungKopplung) : void {
		this.kopplungAddOhneSortierung(pKopplung);
		this._daten.kopplungen.sort(StundenplanblockungManager._comp_kopplung_kuerzel);
	}

	/**
	 * Fügt alle Kopplungen hinzu. <br>
	 * Wirft eine NullPointerException, falls eine der Kopplungen bereits existiert. 
	 * 
	 * @param pKopplungen            Die Menge der Kopplungen, die hinzugefügt werden soll.
	 * @throws NullPointerException  Falls eine der Kopplungen bereits existiert.
	 */
	public kopplungAddAll(pKopplungen : List<StundenplanblockungKopplung>) : void {
		for (let kopplung of pKopplungen) 
			this.kopplungAddOhneSortierung(kopplung);
		this._daten.kopplungen.sort(StundenplanblockungManager._comp_kopplung_kuerzel);
	}

	/**
	 * Liefert TRUE, falls die Kopplung-ID existiert. 
	 * 
	 * @param pKopplungID  Die Datenbank-ID der Kopplung.
	 * @return             TRUE, falls die Kopplung-ID existiert.
	 */
	public kopplungExists(pKopplungID : number) : boolean {
		return this._map_id_kopplung.containsKey(pKopplungID);
	}

	/**
	 * Liefert das {@link StundenplanblockungKopplung}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID unbekannt ist.
	 * 
	 * @param pKopplungID            Die Datenbank-ID der Kopplung.
	 * @return                       Das {@link StundenplanblockungKopplung}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Kopplung-ID unbekannt ist.
	 */
	public kopplungGet(pKopplungID : number) : StundenplanblockungKopplung {
		let kopplung : StundenplanblockungKopplung | null = this._map_id_kopplung.get(pKopplungID);
		if (kopplung === null) 
			throw new NullPointerException("Kopplung-ID " + pKopplungID + " unbekannt!")
		return kopplung;
	}

	/**
	 * Liefert die Menge aller Kopplungen sortiert nach dem Kürzel.
	 * 
	 * @return Die Menge aller Kopplungen sortiert nach dem Kürzel.
	 */
	public kopplungGetMengeSortiertNachKuerzel() : Vector<StundenplanblockungKopplung | null> | null {
		return this._daten.kopplungen;
	}

	/**
	 * Löscht die übergebenen Kopplung. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID unbekannt ist.
	 * 
	 * @param pKopplungID            Die Datenbank-ID der Kopplung.
	 * @throws NullPointerException  Falls die Kopplung-ID unbekannt ist.
	 */
	public kopplungRemove(pKopplungID : number) : void {
		let kopplung : StundenplanblockungKopplung = this.kopplungGet(pKopplungID);
		this._map_id_kopplung.remove(pKopplungID);
		this._daten.kopplungen.remove(kopplung);
	}

	/**
	 * Ändert das Kürzel der Kopplung. <br>
	 * Wirft eine NullPointerException, falls die Kopplung-ID unbekannt ist.
	 * 
	 * @param pKopplungID            Die Datenbank-ID der Kopplung.
	 * @param pKuerzel               Das neue Kürzel der Kopplung.
	 * @throws NullPointerException  Falls die Kopplung-ID unbekannt ist.
	 */
	public kopplungSetKuerzel(pKopplungID : number, pKuerzel : String) : void {
		let kopplung : StundenplanblockungKopplung = this.kopplungGet(pKopplungID);
		kopplung.kuerzel = pKuerzel;
		this._daten.kopplungen.sort(StundenplanblockungManager._comp_kopplung_kuerzel);
	}

	/**
	 * Diese Methode überprüft alle Datenstrukturen auf ihre Konsistenz.
	 * Liefert TRUE, falls die Daten in Ordnung (konsistent) sind.
	 * 
	 * @return TRUE, falls die Daten in Ordnung (konsistent) sind.
	 */
	public miscCheckConsistency() : boolean {
		if (this._daten.lehrkraefte.size() !== this._map_id_lehrkraft.size()) 
			return false;
		for (let lehrkraft of this._daten.lehrkraefte) 
			if (this._map_id_lehrkraft.get(lehrkraft.id) as unknown !== lehrkraft as unknown) 
				return false;
		for (let i : number = 1; i < this._daten.lehrkraefte.size(); i++)
			if (StundenplanblockungManager._comp_lehrkraft_kuerzel.compare(this._daten.lehrkraefte.get(i - 1), this._daten.lehrkraefte.get(i)) > 0) 
				return false;
		if (this._daten.klassen.size() !== this._map_id_klasse.size()) 
			return false;
		for (let klasse of this._daten.klassen) 
			if (this._map_id_klasse.get(klasse.id) as unknown !== klasse as unknown) 
				return false;
		for (let i : number = 1; i < this._daten.klassen.size(); i++)
			if (StundenplanblockungManager._comp_klasse_kuerzel.compare(this._daten.klassen.get(i - 1), this._daten.klassen.get(i)) > 0) 
				return false;
		if (this._daten.faecher.size() !== this._map_id_fach.size()) 
			return false;
		for (let fach of this._daten.faecher) 
			if (this._map_id_fach.get(fach.id) as unknown !== fach as unknown) 
				return false;
		for (let i : number = 1; i < this._daten.faecher.size(); i++)
			if (StundenplanblockungManager._comp_fach_sortiernummer.compare(this._daten.faecher.get(i - 1), this._daten.faecher.get(i)) > 0) 
				return false;
		if (this._daten.raeume.size() !== this._map_id_raum.size()) 
			return false;
		for (let raum of this._daten.raeume) 
			if (this._map_id_raum.get(raum.id) as unknown !== raum as unknown) 
				return false;
		for (let i : number = 1; i < this._daten.raeume.size(); i++)
			if (StundenplanblockungManager._comp_raum_kuerzel.compare(this._daten.raeume.get(i - 1), this._daten.raeume.get(i)) > 0) 
				return false;
		if (this._daten.kopplungen.size() !== this._map_id_kopplung.size()) 
			return false;
		for (let kopplung of this._daten.kopplungen) 
			if (this._map_id_kopplung.get(kopplung.id) as unknown !== kopplung as unknown) 
				return false;
		for (let i : number = 1; i < this._daten.kopplungen.size(); i++)
			if (StundenplanblockungManager._comp_kopplung_kuerzel.compare(this._daten.kopplungen.get(i - 1), this._daten.kopplungen.get(i)) > 0) 
				return false;
		return true;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManager(obj : unknown) : StundenplanblockungManager {
	return obj as StundenplanblockungManager;
}
