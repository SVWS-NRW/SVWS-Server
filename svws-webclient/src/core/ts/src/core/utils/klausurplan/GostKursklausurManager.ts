import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { GostKursklausur, cast_de_nrw_schule_svws_core_data_gost_klausuren_GostKursklausur } from '../../../core/data/gost/klausuren/GostKursklausur';
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
	private readonly _termine : List<GostKlausurtermin> = new Vector();

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
			this.helpKonstruktor();
			for (let t of termine) {
				this.addTermin(t);
			}
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.util.List'))) || (__param0 === null)) && (typeof __param1 === "undefined")) {
			let klausuren : List<GostKursklausur> = cast_java_util_List(__param0);
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
	 * 
	 * @param klausur das GostKursklausur-Objekt
	 */
	public updateKursklausur(klausur : GostKursklausur) : void {
		let terminNeuKlausuren : List<GostKursklausur | null> | null = this._mapTerminKursklausuren.get(klausur.idTermin === null ? -1 : klausur.idTermin);
		if (terminNeuKlausuren === null || !terminNeuKlausuren.contains(klausur)) {
			let oldTerminId : number = -2;
			for (let key of this._mapTerminKursklausuren.keySet()) {
				let entry : Vector<GostKursklausur> | null = this._mapTerminKursklausuren.get(key);
				if (entry === null) {
				} else {
					if (entry.contains(klausur)) {
						oldTerminId = key.valueOf();
						entry.remove(klausur);
					}
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
	 * 
	 * @param termin das GostKlausurtermin-Objekt
	 */
	public addTermin(termin : GostKlausurtermin) : void {
		this._termine.add(termin);
		this._mapIdKlausurtermin.put(termin.id, termin);
		let listKlausurtermineMapQuartalKlausurtermine : Vector<GostKlausurtermin> | null = this._mapQuartalKlausurtermine.get(termin.quartal);
		if (listKlausurtermineMapQuartalKlausurtermine === null) {
			listKlausurtermineMapQuartalKlausurtermine = new Vector();
			this._mapQuartalKlausurtermine.put(termin.quartal, listKlausurtermineMapQuartalKlausurtermine);
		}
		listKlausurtermineMapQuartalKlausurtermine.add(termin);
	}

	/**
	 * Fügt den internen Strukturen eine neue Kursklausur hinzu.
	 * 
	 * @param klausur das GostKursklausur-Objekt
	 */
	public addKlausur(klausur : GostKursklausur) : void {
		this._klausuren.add(klausur);
		this._mapIdKursklausur.put(klausur.id, klausur);
		this.addKlausurToInternalMaps(klausur);
	}

	/**
	 * Fügt den internen Strukturen neue Kursklausuren hinzu.
	 * 
	 * @param klausuren die Liste von GostKursklausur-Objekten
	 */
	public addKlausuren(klausuren : List<GostKursklausur>) : void {
		for (let klausur of klausuren) {
			this.addKlausur(klausur);
		}
	}

	/**
	 * Löscht einen Klausurtermin aus den internen Strukturen
	 * 
	 * @param termin das GostKlausurtermin-Objekt
	 */
	public removeTermin(termin : GostKlausurtermin) : void {
		let listKlausurtermineMapQuartalKlausurtermine : Vector<GostKlausurtermin> | null = this._mapQuartalKlausurtermine.get(termin.quartal);
		if (listKlausurtermineMapQuartalKlausurtermine === null) {
			return;
		}
		listKlausurtermineMapQuartalKlausurtermine.remove(termin);
		let listKlausurenZuTermin : List<GostKursklausur> | null = this.getKursklausuren(termin.id);
		if (listKlausurenZuTermin !== null) {
			listKlausurenZuTermin = new Vector(listKlausurenZuTermin);
			for (let k of listKlausurenZuTermin) {
				k.idTermin = null;
				this.updateKursklausur(k);
			}
		}
		this._termine.remove(termin);
		this._mapIdKlausurtermin.remove(termin.id);
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
	public getKursklausurenOhneTermin(quartal : number) : List<GostKursklausur>;

	/**
	 * Implementation for method overloads of 'getKursklausurenOhneTermin'
	 */
	public getKursklausurenOhneTermin(__param0? : number) : List<GostKursklausur> {
		if ((typeof __param0 === "undefined")) {
			return this.getKursklausurenOhneTermin(-1);
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			let quartal : number = __param0 as number;
			let mapTerminKursklausuren : HashMap<number, Vector<GostKursklausur>> | null = this._mapQuartalTerminKursklausuren.get(quartal <= 0 ? -1 : quartal);
			if (mapTerminKursklausuren === null) {
				return new Vector();
			}
			let klausuren : List<GostKursklausur> | null = mapTerminKursklausuren.get(-1);
			return klausuren !== null ? klausuren : new Vector();
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
	public getKlausurtermine(quartal : number) : List<GostKlausurtermin>;

	/**
	 * Implementation for method overloads of 'getKlausurtermine'
	 */
	public getKlausurtermine(__param0? : number) : List<GostKlausurtermin> {
		if ((typeof __param0 === "undefined")) {
			return this._termine;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			let quartal : number = __param0 as number;
			let termine : List<GostKlausurtermin> | null = this._mapQuartalKlausurtermine.get(quartal <= 0 ? -1 : quartal);
			return termine !== null ? termine : new Vector();
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
	 * Prüft, ob es innerhalb eines bestehenden Klausurtermins Konflikte gibt. Es
	 * wird die Anzahl der Konflikte zurückgegeben..
	 * 
	 * @param idTermin die ID des zu prüfenden Klausurtermins
	 * 
	 * @return die Anzahl der Konflikte innerhalb des Termins.
	 */
	public gibAnzahlKonflikteZuTermin(idTermin : number) : number {
		let anzahl : number = 0;
		let listKlausurenZuTermin : List<GostKursklausur> | null = this.getKursklausuren(idTermin);
		if (listKlausurenZuTermin !== null) {
			let copyListKlausurenZuTermin : List<GostKursklausur> | null = new Vector(listKlausurenZuTermin);
			for (let k1 of listKlausurenZuTermin) {
				copyListKlausurenZuTermin.remove(k1);
				for (let k2 of copyListKlausurenZuTermin) 
					anzahl += this.gibKonfliktKursklausurKursklausur(k1.id, k2.id).size();
			}
		}
		return anzahl;
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
	public gibKonfliktKursklausurKursklausur(idKursklausur1 : number, idKursklausur2 : number) : List<number>;

	/**
	 * Prüft, ob die Schülermengen zweier Kursklausuren disjunkt sind. Es werden die
	 * Schüler-IDs, die beide Klausuren schreiben, als Liste zurückgegeben. Wenn die
	 * zurückgegebene Liste leer ist, gibt es keine Übereinstimmungen.
	 * 
	 * @param klausur1 die erste zu prüfende Kursklausur
	 * @param klausur2 die zweite zu prüfende Kursklausur
	 * 
	 * @return die Liste der Schüler-IDs, die beide Klausuren schreiben.
	 */
	public gibKonfliktKursklausurKursklausur(klausur1 : GostKursklausur | null, klausur2 : GostKursklausur | null) : List<number>;

	/**
	 * Implementation for method overloads of 'gibKonfliktKursklausurKursklausur'
	 */
	public gibKonfliktKursklausurKursklausur(__param0 : GostKursklausur | null | number, __param1 : GostKursklausur | null | number) : List<number> {
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			let idKursklausur1 : number = __param0 as number;
			let idKursklausur2 : number = __param1 as number;
			let klausur1 : GostKursklausur | null = this._mapIdKursklausur.get(idKursklausur1);
			let klausur2 : GostKursklausur | null = this._mapIdKursklausur.get(idKursklausur2);
			return this.gibKonfliktKursklausurKursklausur(klausur1, klausur2);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.klausuren.GostKursklausur'))) || (__param0 === null)) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.klausuren.GostKursklausur'))) || (__param1 === null))) {
			let klausur1 : GostKursklausur | null = cast_de_nrw_schule_svws_core_data_gost_klausuren_GostKursklausur(__param0);
			let klausur2 : GostKursklausur | null = cast_de_nrw_schule_svws_core_data_gost_klausuren_GostKursklausur(__param1);
			if (klausur1 === null || klausur2 === null) {
				return new Vector();
			}
			let konflikte : List<number> | null = new Vector(klausur1.schuelerIds);
			konflikte.retainAll(klausur2.schuelerIds);
			return konflikte;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.klausurplan.GostKursklausurManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_klausurplan_GostKursklausurManager(obj : unknown) : GostKursklausurManager {
	return obj as GostKursklausurManager;
}
