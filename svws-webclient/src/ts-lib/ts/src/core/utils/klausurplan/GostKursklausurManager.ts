import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { GostKursklausur, cast_de_nrw_schule_svws_core_data_gost_klausuren_GostKursklausur } from '../../../core/data/gost/klausuren/GostKursklausur';
import { JavaMapEntry, cast_java_util_Map_Entry } from '../../../java/util/JavaMapEntry';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../java/util/List';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { GostKlausurtermin, cast_de_nrw_schule_svws_core_data_gost_klausuren_GostKlausurtermin } from '../../../core/data/gost/klausuren/GostKlausurtermin';

export class GostKursklausurManager extends JavaObject {

	/**
	 * Die Kursklausuren, die im Manager vorhanden sind 
	 */
	private readonly _klausuren : List<GostKursklausur>;

	/**
	 * Die Klausurtermine, die im Manager vorhanden sind 
	 */
	private readonly _termine : List<GostKlausurtermin> | null;

	/**
	 * Eine Map quartal -> Liste von GostKlausurterminen 
	 */
	private readonly _mapQuartalKlausurtermine : HashMap<number, Vector<GostKlausurtermin>> = new HashMap();

	/**
	 * Eine Map id -> GostKursklausur 
	 */
	private readonly _mapIdKursklausur : HashMap<number, GostKursklausur> = new HashMap();

	/**
	 * Eine Map idTermin -> Liste von GostKursklausuren 
	 */
	private readonly _mapTerminKursklausuren : HashMap<number, Vector<GostKursklausur>> = new HashMap();

	/**
	 * Eine Map quartal, idTermin -> Liste von GostKursklausuren 
	 */
	private readonly _mapQuartalTerminKursklausuren : HashMap<number, HashMap<number, Vector<GostKursklausur>>> = new HashMap();

	/**
	 * Eine Map quartal -> Liste von GostKursklausuren 
	 */
	private readonly _mapQuartalKursKlausuren : HashMap<number, Vector<GostKursklausur>> = new HashMap();

	/**
	 * Eine Map idTermin -> Liste von Schüler-IDs 
	 */
	private readonly _mapTerminSchuelerids : HashMap<number, Vector<number>> = new HashMap();

	/**
	 * Eine Map idTermin -> GostKlausurtermin 
	 */
	private readonly _mapIdKlausurtermin : HashMap<number, GostKlausurtermin> = new HashMap();


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 * 
	 * @param klausuren die Liste der GostKursklausuren eines Abiturjahrgangs und
	 *                  Gost-Halbjahres
	 * @param termine   die Liste der GostKlausurtermine eines Abiturjahrgangs und
	 *                  Gost-Halbjahres
	 */
	public constructor(klausuren : List<GostKursklausur>, termine : List<GostKlausurtermin>);

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und erzeugt die privaten Attribute.
	 * 
	 * @param klausuren die Liste der GostKursklausuren eines Abiturjahrgangs und
	 *                  Gost-Halbjahres
	 */
	public constructor(klausuren : List<GostKursklausur>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : List<GostKursklausur>, __param1? : List<GostKlausurtermin>) {
		super();
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.util.List'))) || (__param0 === null)) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('java.util.List'))) || (__param1 === null))) {
			let klausuren : List<GostKursklausur> = cast_java_util_List(__param0);
			let termine : List<GostKlausurtermin> = cast_java_util_List(__param1);
			this._klausuren = klausuren;
			this._termine = termine;
			this.helpKonstruktor();
			for (let t of this._termine) {
				this.addTermin(t);
			}
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.util.List'))) || (__param0 === null)) && (typeof __param1 === "undefined")) {
			let klausuren : List<GostKursklausur> = cast_java_util_List(__param0);
			this._termine = null;
			this._klausuren = klausuren;
			this.helpKonstruktor();
		} else throw new Error('invalid method overload');
	}

	private helpKonstruktor() : void {
		for (let kk of this._klausuren) {
			this._mapIdKursklausur.put(kk.id, kk);
			this.addKlausurToInternalMaps(kk);
			let listKursklausurenMapQuartalKursKlausuren : Vector<GostKursklausur> | null = this._mapQuartalKursKlausuren.get(kk.quartal);
			if (listKursklausurenMapQuartalKursKlausuren === null) {
				listKursklausurenMapQuartalKursKlausuren = new Vector();
				this._mapQuartalKursKlausuren.put(kk.quartal, listKursklausurenMapQuartalKursKlausuren);
			}
			listKursklausurenMapQuartalKursKlausuren.add(kk);
			if (kk.idTermin !== null) {
				let listSchuelerIds : Vector<number> | null = this._mapTerminSchuelerids.get(kk.idTermin);
				if (listSchuelerIds === null) {
					listSchuelerIds = new Vector();
					this._mapTerminSchuelerids.put(kk.idTermin, listSchuelerIds);
				}
				listSchuelerIds.addAll(kk.schuelerIds);
			}
		}
	}

	private addKlausurToInternalMaps(kk : GostKursklausur) : void {
		let listKursklausurenMapTermine : Vector<GostKursklausur> | null = this._mapTerminKursklausuren.get(kk.idTermin === null ? -1 : kk.idTermin);
		if (listKursklausurenMapTermine === null) {
			listKursklausurenMapTermine = new Vector();
			this._mapTerminKursklausuren.put(kk.idTermin === null ? -1 : kk.idTermin, listKursklausurenMapTermine);
		}
		listKursklausurenMapTermine.add(kk);
		let mapTerminKursklausuren : HashMap<number, Vector<GostKursklausur>> | null = this._mapQuartalTerminKursklausuren.get(kk.quartal);
		if (mapTerminKursklausuren === null) {
			mapTerminKursklausuren = new HashMap();
			this._mapQuartalTerminKursklausuren.put(kk.quartal, mapTerminKursklausuren);
		}
		let listKursklausurenMapQuartalmapTermine : Vector<GostKursklausur> | null = mapTerminKursklausuren.get(kk.idTermin === null ? -1 : kk.idTermin);
		if (listKursklausurenMapQuartalmapTermine === null) {
			listKursklausurenMapQuartalmapTermine = new Vector();
			mapTerminKursklausuren.put(kk.idTermin === null ? -1 : kk.idTermin, listKursklausurenMapQuartalmapTermine);
		}
		listKursklausurenMapQuartalmapTermine.add(kk);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Termin einer Klausur
	 * geändert hat.
	 */
	public updateKursklausur(klausur : GostKursklausur) : void {
		let terminNeuKlausuren : List<GostKursklausur | null> | null = this._mapTerminKursklausuren.get(klausur.idTermin === null ? -1 : klausur.idTermin);
		if (terminNeuKlausuren === null || !terminNeuKlausuren.contains(klausur)) {
			let oldTerminId : number = -2;
			for (let entry of this._mapTerminKursklausuren.entrySet()) {
				if (entry.getValue().contains(klausur)) {
					oldTerminId = entry.getKey().valueOf();
					entry.getValue().remove(klausur);
				}
			}
			let quartalMap : HashMap<number, Vector<GostKursklausur>> | null = this._mapQuartalTerminKursklausuren.get(klausur.quartal);
			if (quartalMap !== null) {
				let listOldQuartalTerminKursklausuren : List<GostKursklausur> | null = quartalMap.get(oldTerminId);
				if (listOldQuartalTerminKursklausuren !== null) 
					listOldQuartalTerminKursklausuren.remove(klausur);
			} else {
			}
			this.updateSchuelerIdsZuTermin(oldTerminId);
			if (klausur.idTermin !== null) 
				this.updateSchuelerIdsZuTermin(klausur.idTermin);
			this.addKlausurToInternalMaps(klausur);
		}
	}

	private updateSchuelerIdsZuTermin(idTermin : number) : void {
		let listSchuelerIds : Vector<number> | null = new Vector();
		this._mapTerminSchuelerids.put(idTermin, listSchuelerIds);
		let listKlausurenZuTermin : List<GostKursklausur> | null = this._mapTerminKursklausuren.get(idTermin);
		if (listKlausurenZuTermin === null) 
			return;
		for (let k of listKlausurenZuTermin) {
			listSchuelerIds.addAll(k.schuelerIds);
		}
	}

	/**
	 * Fügt den internen Strukturen einen neuen Klausurtermin hinzu.
	 */
	public addTermin(t : GostKlausurtermin) : void {
		this._mapIdKlausurtermin.put(t.id, t);
		let listKlausurtermineMapQuartalKlausurtermine : Vector<GostKlausurtermin> | null = this._mapQuartalKlausurtermine.get(t.quartal);
		if (listKlausurtermineMapQuartalKlausurtermine === null) {
			listKlausurtermineMapQuartalKlausurtermine = new Vector();
			this._mapQuartalKlausurtermine.put(t.quartal, listKlausurtermineMapQuartalKlausurtermine);
		}
		listKlausurtermineMapQuartalKlausurtermine.add(t);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Termin
	 * 
	 * @param idTermin die ID des Klausurtermins
	 * 
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public getKursklausuren(idTermin : number | null) : List<GostKursklausur>;

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten des Halbjahres
	 * 
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public getKursklausuren() : List<GostKursklausur>;

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal
	 * 
	 * @param quartal die Nummer des Quartals
	 * 
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public getKursklausuren(quartal : number) : List<GostKursklausur> | null;

	/**
	 * Implementation for method overloads of 'getKursklausuren'
	 */
	public getKursklausuren(__param0? : null | number) : List<GostKursklausur> | null {
		if (((typeof __param0 !== "undefined") && (typeof __param0 === "number") || (__param0 === null))) {
			let idTermin : number | null = __param0;
			return this._mapTerminKursklausuren.get(idTermin === null ? -1 : idTermin);
		} else if ((typeof __param0 === "undefined")) {
			return this._klausuren;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			let quartal : number = __param0 as number;
			return this._mapQuartalKursKlausuren.get(quartal);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten für die noch kein Termin /
	 * Schiene gesetzt wurde
	 * 
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public getKursklausurenOhneTermin() : List<GostKursklausur>;

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal für
	 * die noch kein Termin / Schiene gesetzt wurde
	 * 
	 * @param quartal die Nummer des Quartals
	 * 
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public getKursklausurenOhneTermin(quartal : number) : List<GostKursklausur> | null;

	/**
	 * Implementation for method overloads of 'getKursklausurenOhneTermin'
	 */
	public getKursklausurenOhneTermin(__param0? : number) : List<GostKursklausur> | null {
		if ((typeof __param0 === "undefined")) {
			return this.getKursklausuren(-1);
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			let quartal : number = __param0 as number;
			let mapTerminKursklausuren : HashMap<number, Vector<GostKursklausur>> | null = this._mapQuartalTerminKursklausuren.get(quartal);
			if (mapTerminKursklausuren === null) {
				return new Vector();
			}
			return mapTerminKursklausuren.get(-1);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt das GostKlausurtermin-Objekt zur übergebenen id zurück.
	 * 
	 * @param idTermin die ID des Klausurtermins
	 * 
	 * @return das GostKlausurtermin-Objekt
	 */
	public gibGostKlausurtermin(idTermin : number) : GostKlausurtermin | null {
		return this._mapIdKlausurtermin.get(idTermin);
	}

	/**
	 * Gibt eine Liste von Schüler-IDs zurück, die vom übergebenen Termin betroffen
	 * sind.
	 * 
	 * @param idTermin die ID des Klausurtermins
	 * 
	 * @return die Liste der betroffenen Schüler-IDs
	 */
	public gibSchuelerIDsZuTermin(idTermin : number) : List<number> | null {
		let schuelerIds : List<number> | null = this._mapTerminSchuelerids.get(idTermin);
		return schuelerIds !== null || !this._mapIdKlausurtermin.containsKey(idTermin) ? schuelerIds : new Vector();
	}

	/**
	 * Gibt das GostKursklausur-Objekt zur übergebenen id zurück.
	 * 
	 * @param idKursklausur die ID der Kursklausur
	 * 
	 * @return das GostKursklausur-Objekt
	 */
	public gibKursklausur(idKursklausur : number) : GostKursklausur | null {
		return this._mapIdKursklausur.get(idKursklausur);
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten des Halbjahres
	 * 
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public getKlausurtermine() : List<GostKlausurtermin>;

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Quartal
	 * 
	 * @param quartal die Nummer des Quartals
	 * 
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public getKlausurtermine(quartal : number) : List<GostKlausurtermin> | null;

	/**
	 * Implementation for method overloads of 'getKlausurtermine'
	 */
	public getKlausurtermine(__param0? : number) : List<GostKlausurtermin> | null {
		if ((typeof __param0 === "undefined")) {
			return this._termine;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			let quartal : number = __param0 as number;
			return this._mapQuartalKlausurtermine.get(quartal);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt das GostKlausurtermin-Objekt zur übergebenen id zurück.
	 * 
	 * @param idTermin die ID des GostKlausurtermins
	 * 
	 * @return das GostKlausurtermin-Objekt
	 */
	public gibKlausurtermin(idTermin : number) : GostKlausurtermin | null {
		return this._mapIdKlausurtermin.get(idTermin);
	}

	/**
	 * Prüft, ob eine Kursklausur konfliktfrei zu einem bestehenden Klausurtermin
	 * hinzugefügt werden kann. Es werden die Schüler-IDs, die den Konflikt
	 * verursachen, als Liste zurückgegeben. Wenn die zurückgegebene Liste leer ist,
	 * gibt es keinen Konflikt.
	 * 
	 * @param idTermin      die ID des zu prüfenden Klausurtermins
	 * @param idKursklausur die ID der zu prüfenden Kursklausur
	 * 
	 * @return die Liste der Schüler-IDs, die einen Konflikt verursachen.
	 */
	public gibKonfliktTerminKursklausur(idTermin : number, idKursklausur : number) : List<number> {
		let schuelerIds : List<number> | null = this.gibSchuelerIDsZuTermin(idTermin);
		if (schuelerIds === null) {
			return new Vector();
		}
		let klausur : GostKursklausur | null = this._mapIdKursklausur.get(idKursklausur);
		if (klausur === null) {
			return new Vector();
		}
		let konflikte : List<number> = new Vector(schuelerIds);
		konflikte.retainAll(klausur.schuelerIds);
		return konflikte;
	}

	/**
	 * Prüft, ob die Schülermengen zweier Kursklausuren disjunkt sind. Es werden die
	 * Schüler-IDs, die beide Klausuren schreiben, als Liste zurückgegeben. Wenn die
	 * zurückgegebene Liste leer ist, gibt es keine Übereinstimmungen.
	 * 
	 * @param idKursklausur1 die ID der ersten zu prüfenden Kursklausur
	 * @param idKursklausur2 die ID der zweiten zu prüfenden Kursklausur
	 * 
	 * @return die Liste der Schüler-IDs, die beide Klausuren schreiben.
	 */
	public gibKonfliktKursklausurKursklausur(idKursklausur1 : number, idKursklausur2 : number) : List<number> {
		let klausur1 : GostKursklausur | null = this._mapIdKursklausur.get(idKursklausur1);
		let klausur2 : GostKursklausur | null = this._mapIdKursklausur.get(idKursklausur2);
		if (klausur1 === null || klausur2 === null) {
			return new Vector();
		}
		let konflikte : List<number> | null = new Vector(klausur1.schuelerIds);
		konflikte.retainAll(klausur2.schuelerIds);
		return konflikte;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.klausurplan.GostKursklausurManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_klausurplan_GostKursklausurManager(obj : unknown) : GostKursklausurManager {
	return obj as GostKursklausurManager;
}
