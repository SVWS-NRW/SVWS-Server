import { JavaObject } from '../../../java/lang/JavaObject';
import { GostKursklausur, cast_de_svws_nrw_core_data_gost_klausuren_GostKursklausur } from '../../../core/data/gost/klausuren/GostKursklausur';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { List, cast_java_util_List } from '../../../java/util/List';
import { GostKlausurtermin, cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurtermin } from '../../../core/data/gost/klausuren/GostKlausurtermin';

export class GostKursklausurManager extends JavaObject {

	/**
	 * Die Kursklausuren, die im Manager vorhanden sind
	 */
	private readonly _klausuren : List<GostKursklausur>;

	/**
	 * Die Klausurtermine, die im Manager vorhanden sind
	 */
	private readonly _termine : List<GostKlausurtermin> = new ArrayList();

	/**
	 * Eine Map quartal -> Liste von GostKlausurterminen
	 */
	private readonly _mapQuartalKlausurtermine : HashMap<number, ArrayList<GostKlausurtermin>> = new HashMap();

	/**
	 * Eine Map id -> GostKursklausur
	 */
	private readonly _mapIdKursklausur : HashMap<number, GostKursklausur> = new HashMap();

	/**
	 * Eine Map idTermin -> Liste von GostKursklausuren
	 */
	private readonly _mapTerminKursklausuren : HashMap<number, ArrayList<GostKursklausur>> = new HashMap();

	/**
	 * Eine Map quartal, idTermin -> Liste von GostKursklausuren
	 */
	private readonly _mapQuartalTerminKursklausuren : HashMap<number, HashMap<number, ArrayList<GostKursklausur>>> = new HashMap();

	/**
	 * Eine Map quartal, kursart, idTermin -> Liste von GostKursklausuren
	 */
	private readonly _mapQuartalKursartTerminKursklausuren : HashMap<number, HashMap<string, HashMap<number, ArrayList<GostKursklausur>>>> = new HashMap();

	/**
	 * Eine Map quartal -> Liste von GostKursklausuren
	 */
	private readonly _mapQuartalKursKlausuren : HashMap<number, ArrayList<GostKursklausur>> = new HashMap();

	/**
	 * Eine Map idTermin -> Liste von Schüler-IDs
	 */
	private readonly _mapTerminSchuelerids : HashMap<number, ArrayList<number>> = new HashMap();

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
			const klausuren : List<GostKursklausur> = cast_java_util_List(__param0);
			const termine : List<GostKlausurtermin> = cast_java_util_List(__param1);
			this._klausuren = klausuren;
			this.helpKonstruktor();
			for (const t of termine) {
				this.addTermin(t);
			}
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.util.List'))) || (__param0 === null)) && (typeof __param1 === "undefined")) {
			const klausuren : List<GostKursklausur> = cast_java_util_List(__param0);
			this._klausuren = klausuren;
			this.helpKonstruktor();
		} else throw new Error('invalid method overload');
	}

	private helpKonstruktor() : void {
		for (const kk of this._klausuren) {
			this._mapIdKursklausur.put(kk.id, kk);
			this.addKlausurToInternalMaps(kk);
			let listKursklausurenMapQuartalKursKlausuren : ArrayList<GostKursklausur> | null = this._mapQuartalKursKlausuren.get(kk.quartal);
			if (listKursklausurenMapQuartalKursKlausuren === null) {
				listKursklausurenMapQuartalKursKlausuren = new ArrayList();
				this._mapQuartalKursKlausuren.put(kk.quartal, listKursklausurenMapQuartalKursKlausuren);
			}
			listKursklausurenMapQuartalKursKlausuren.add(kk);
			if (kk.idTermin !== null) {
				let listSchuelerIds : ArrayList<number> | null = this._mapTerminSchuelerids.get(kk.idTermin);
				if (listSchuelerIds === null) {
					listSchuelerIds = new ArrayList();
					this._mapTerminSchuelerids.put(kk.idTermin, listSchuelerIds);
				}
				listSchuelerIds.addAll(kk.schuelerIds);
			}
		}
	}

	private addKlausurToInternalMaps(kk : GostKursklausur) : void {
		let listKursklausurenMapTermine : ArrayList<GostKursklausur> | null = this._mapTerminKursklausuren.get(kk.idTermin === null ? -1 : kk.idTermin);
		if (listKursklausurenMapTermine === null) {
			listKursklausurenMapTermine = new ArrayList();
			this._mapTerminKursklausuren.put(kk.idTermin === null ? -1 : kk.idTermin, listKursklausurenMapTermine);
		}
		listKursklausurenMapTermine.add(kk);
		let mapTerminKursklausuren : HashMap<number, ArrayList<GostKursklausur>> | null = this._mapQuartalTerminKursklausuren.get(kk.quartal);
		if (mapTerminKursklausuren === null) {
			mapTerminKursklausuren = new HashMap();
			this._mapQuartalTerminKursklausuren.put(kk.quartal, mapTerminKursklausuren);
		}
		let listKursklausurenMapQuartalmapTermine : ArrayList<GostKursklausur> | null = mapTerminKursklausuren.get(kk.idTermin === null ? -1 : kk.idTermin);
		if (listKursklausurenMapQuartalmapTermine === null) {
			listKursklausurenMapQuartalmapTermine = new ArrayList();
			mapTerminKursklausuren.put(kk.idTermin === null ? -1 : kk.idTermin, listKursklausurenMapQuartalmapTermine);
		}
		listKursklausurenMapQuartalmapTermine.add(kk);
		let mapKursartTerminKursklausuren : HashMap<string, HashMap<number, ArrayList<GostKursklausur>>> | null = this._mapQuartalKursartTerminKursklausuren.get(kk.quartal);
		if (mapKursartTerminKursklausuren === null) {
			mapKursartTerminKursklausuren = new HashMap();
			this._mapQuartalKursartTerminKursklausuren.put(kk.quartal, mapKursartTerminKursklausuren);
		}
		let mapKursklausurenMapQuartalKursartTerminKursKlausuren : HashMap<number, ArrayList<GostKursklausur>> | null = mapKursartTerminKursklausuren.get(kk.kursart);
		if (mapKursklausurenMapQuartalKursartTerminKursKlausuren === null) {
			mapKursklausurenMapQuartalKursartTerminKursKlausuren = new HashMap();
			mapKursartTerminKursklausuren.put(kk.kursart, mapKursklausurenMapQuartalKursartTerminKursKlausuren);
		}
		let listKursklausurenMapQuartalKursartmapTermine : ArrayList<GostKursklausur> | null = mapKursklausurenMapQuartalKursartTerminKursKlausuren.get(kk.idTermin === null ? -1 : kk.idTermin);
		if (listKursklausurenMapQuartalKursartmapTermine === null) {
			listKursklausurenMapQuartalKursartmapTermine = new ArrayList();
			mapKursklausurenMapQuartalKursartTerminKursKlausuren.put(kk.idTermin === null ? -1 : kk.idTermin, listKursklausurenMapQuartalKursartmapTermine);
		}
		listKursklausurenMapQuartalKursartmapTermine.add(kk);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Termin einer Klausur
	 * geändert hat.
	 *
	 * @param klausur das GostKursklausur-Objekt
	 */
	public updateKursklausur(klausur : GostKursklausur) : void {
		const terminNeuKlausuren : List<GostKursklausur | null> | null = this._mapTerminKursklausuren.get(klausur.idTermin === null ? -1 : klausur.idTermin);
		if (terminNeuKlausuren === null || !terminNeuKlausuren.contains(klausur)) {
			let oldTerminId : number = -2;
			for (const key of this._mapTerminKursklausuren.keySet()) {
				const entry : ArrayList<GostKursklausur> | null = this._mapTerminKursklausuren.get(key);
				if (entry === null) {
					continue;
				}
				if (entry.contains(klausur)) {
					oldTerminId = key.valueOf();
					entry.remove(klausur);
				}
			}
			const quartalMap : HashMap<number, ArrayList<GostKursklausur>> | null = this._mapQuartalTerminKursklausuren.get(klausur.quartal);
			if (quartalMap !== null) {
				const listOldQuartalTerminKursklausuren : List<GostKursklausur> | null = quartalMap.get(oldTerminId);
				if (listOldQuartalTerminKursklausuren !== null)
					listOldQuartalTerminKursklausuren.remove(klausur);
			}
			const quartalKursartMap : HashMap<string, HashMap<number, ArrayList<GostKursklausur>>> | null = this._mapQuartalKursartTerminKursklausuren.get(klausur.quartal);
			if (quartalKursartMap !== null) {
				const kursartMap : HashMap<number, ArrayList<GostKursklausur>> | null = quartalKursartMap.get(klausur.kursart);
				if (kursartMap !== null) {
					const listOldQuartalTerminKursklausuren : List<GostKursklausur> | null = kursartMap.get(oldTerminId);
					if (listOldQuartalTerminKursklausuren !== null)
						listOldQuartalTerminKursklausuren.remove(klausur);
				}
			}
			this.addKlausurToInternalMaps(klausur);
			this.updateSchuelerIdsZuTermin(oldTerminId);
			if (klausur.idTermin !== null)
				this.updateSchuelerIdsZuTermin(klausur.idTermin);
		}
	}

	private updateSchuelerIdsZuTermin(idTermin : number) : void {
		const listSchuelerIds : ArrayList<number> | null = new ArrayList();
		this._mapTerminSchuelerids.put(idTermin, listSchuelerIds);
		const listKlausurenZuTermin : List<GostKursklausur> | null = this._mapTerminKursklausuren.get(idTermin);
		if (listKlausurenZuTermin === null)
			return;
		for (const k of listKlausurenZuTermin) {
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
		let listKlausurtermineMapQuartalKlausurtermine : ArrayList<GostKlausurtermin> | null = this._mapQuartalKlausurtermine.get(termin.quartal);
		if (listKlausurtermineMapQuartalKlausurtermine === null) {
			listKlausurtermineMapQuartalKlausurtermine = new ArrayList();
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
		for (const klausur of klausuren) {
			this.addKlausur(klausur);
		}
	}

	/**
	 * Löscht einen Klausurtermin aus den internen Strukturen
	 *
	 * @param termin das GostKlausurtermin-Objekt
	 */
	public removeTermin(termin : GostKlausurtermin) : void {
		const listKlausurtermineMapQuartalKlausurtermine : ArrayList<GostKlausurtermin> | null = this._mapQuartalKlausurtermine.get(termin.quartal);
		if (listKlausurtermineMapQuartalKlausurtermine === null) {
			return;
		}
		listKlausurtermineMapQuartalKlausurtermine.remove(termin);
		let listKlausurenZuTermin : List<GostKursklausur> | null = this.getKursklausuren(termin.id);
		if (listKlausurenZuTermin !== null) {
			listKlausurenZuTermin = new ArrayList(listKlausurenZuTermin);
			for (const k of listKlausurenZuTermin) {
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
			const idTermin : number | null = __param0;
			const klausuren : List<GostKursklausur> | null = this._mapTerminKursklausuren.get(idTermin === null ? -1 : idTermin);
			return klausuren !== null ? klausuren : new ArrayList();
		} else if ((typeof __param0 === "undefined")) {
			return this._klausuren;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			const quartal : number = __param0 as number;
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
			return this.getKursklausuren(-1);
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			const quartal : number = __param0 as number;
			const mapTerminKursklausuren : HashMap<number, ArrayList<GostKursklausur>> | null = this._mapQuartalTerminKursklausuren.get(quartal <= 0 ? -1 : quartal);
			if (mapTerminKursklausuren === null) {
				return new ArrayList();
			}
			const klausuren : List<GostKursklausur> | null = mapTerminKursklausuren.get(-1);
			return klausuren !== null ? klausuren : new ArrayList();
		} else throw new Error('invalid method overload');
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal für
	 * die noch kein Termin / Schiene gesetzt wurde
	 *
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public getKursklausurenKursartOhneTermin(quartal : number) : List<List<GostKursklausur>> {
		const retList : List<List<GostKursklausur>> | null = new ArrayList();
		const mapKursartTerminKursklausuren : HashMap<string, HashMap<number, ArrayList<GostKursklausur>>> | null = this._mapQuartalKursartTerminKursklausuren.get(quartal <= 0 ? -1 : quartal);
		if (mapKursartTerminKursklausuren !== null) {
			for (const mapKursarten of mapKursartTerminKursklausuren.values()) {
				if (mapKursarten !== null)
					retList.add(mapKursarten.get(-1));
			}
		}
		return retList;
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
		const schuelerIds : List<number> | null = this._mapTerminSchuelerids.get(idTermin);
		return schuelerIds !== null || !this._mapIdKlausurtermin.containsKey(idTermin) ? schuelerIds : new ArrayList();
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
			const quartal : number = __param0 as number;
			const termine : List<GostKlausurtermin> | null = this._mapQuartalKlausurtermine.get(quartal <= 0 ? -1 : quartal);
			return termine !== null ? termine : new ArrayList();
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
	 * @param termin  der zu prüfende Klausurtermin
	 * @param klausur die zu prüfende Kursklausur
	 *
	 * @return die Liste der Schüler-IDs, die einen Konflikt verursachen.
	 */
	public gibKonfliktTerminInternKursklausur(termin : GostKlausurtermin, klausur : GostKursklausur) : List<number> {
		const konflikte : List<number> = new ArrayList();
		const listKlausurenZuTermin : List<GostKursklausur> | null = this.getKursklausuren(termin.id);
		if (listKlausurenZuTermin === null)
			return konflikte;
		for (const klausurInTermin of listKlausurenZuTermin) {
			konflikte.addAll(this.gibKonfliktKursklausurKursklausur(klausur, klausurInTermin));
		}
		return konflikte;
	}

	/**
	 * Prüft, ob eine Kursklausur konfliktfrei zu einem bestehenden Klausurtermin
	 * hinzugefügt werden kann. Es werden die Schüler-IDs, die den Konflikt
	 * verursachen, als Liste zurückgegeben. Wenn die zurückgegebene Liste leer ist,
	 * gibt es keinen Konflikt.
	 *
	 * @param termin  der zu prüfende Klausurtermin
	 * @param klausur die zu prüfende Kursklausur
	 *
	 * @return die Liste der Schüler-IDs, die einen Konflikt verursachen.
	 */
	public gibKonfliktTerminKursklausur(termin : GostKlausurtermin, klausur : GostKursklausur) : List<number>;

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
	public gibKonfliktTerminKursklausur(idTermin : number, idKursklausur : number) : List<number>;

	/**
	 * Implementation for method overloads of 'gibKonfliktTerminKursklausur'
	 */
	public gibKonfliktTerminKursklausur(__param0 : GostKlausurtermin | number, __param1 : GostKursklausur | number) : List<number> {
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausuren.GostKursklausur'))))) {
			const termin : GostKlausurtermin = cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurtermin(__param0);
			const klausur : GostKursklausur = cast_de_svws_nrw_core_data_gost_klausuren_GostKursklausur(__param1);
			if (klausur.idTermin === termin.id) {
				return new ArrayList();
			}
			const schuelerIds : List<number> | null = this.gibSchuelerIDsZuTermin(termin.id);
			if (schuelerIds === null) {
				return new ArrayList();
			}
			const konflikte : List<number> = new ArrayList(schuelerIds);
			konflikte.retainAll(klausur.schuelerIds);
			return konflikte;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			const idTermin : number = __param0 as number;
			const idKursklausur : number = __param1 as number;
			const klausur : GostKursklausur | null = this._mapIdKursklausur.get(idKursklausur);
			const termin : GostKlausurtermin | null = this._mapIdKlausurtermin.get(idTermin);
			if (klausur === null || termin === null) {
				return new ArrayList();
			}
			return this.gibKonfliktTerminKursklausur(termin, klausur);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Prüft, ob es innerhalb eines bestehenden Klausurtermins Konflikte gibt. Es
	 * wird die Anzahl der Konflikte zurückgegeben.
	 *
	 * @param idTermin die ID des zu prüfenden Klausurtermins
	 *
	 * @return die Anzahl der Konflikte innerhalb des Termins.
	 */
	public gibAnzahlKonflikteZuTermin(idTermin : number) : number {
		let anzahl : number = 0;
		const listKlausurenZuTermin : List<GostKursklausur> | null = this.getKursklausuren(idTermin);
		if (listKlausurenZuTermin !== null) {
			const copyListKlausurenZuTermin : List<GostKursklausur> | null = new ArrayList(listKlausurenZuTermin);
			for (const k1 of listKlausurenZuTermin) {
				copyListKlausurenZuTermin.remove(k1);
				for (const k2 of copyListKlausurenZuTermin)
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
	public gibKonfliktKursklausurKursklausur(klausur1 : GostKursklausur, klausur2 : GostKursklausur) : List<number>;

	/**
	 * Implementation for method overloads of 'gibKonfliktKursklausurKursklausur'
	 */
	public gibKonfliktKursklausurKursklausur(__param0 : GostKursklausur | number, __param1 : GostKursklausur | number) : List<number> {
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			const idKursklausur1 : number = __param0 as number;
			const idKursklausur2 : number = __param1 as number;
			const klausur1 : GostKursklausur | null = this._mapIdKursklausur.get(idKursklausur1);
			const klausur2 : GostKursklausur | null = this._mapIdKursklausur.get(idKursklausur2);
			if (klausur1 === null || klausur2 === null) {
				return new ArrayList();
			}
			return this.gibKonfliktKursklausurKursklausur(klausur1, klausur2);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausuren.GostKursklausur')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausuren.GostKursklausur'))))) {
			const klausur1 : GostKursklausur = cast_de_svws_nrw_core_data_gost_klausuren_GostKursklausur(__param0);
			const klausur2 : GostKursklausur = cast_de_svws_nrw_core_data_gost_klausuren_GostKursklausur(__param1);
			if (klausur1 as unknown === klausur2 as unknown) {
				return new ArrayList();
			}
			const konflikte : List<number> | null = new ArrayList(klausur1.schuelerIds);
			konflikte.retainAll(klausur2.schuelerIds);
			return konflikte;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplan.GostKursklausurManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplan_GostKursklausurManager(obj : unknown) : GostKursklausurManager {
	return obj as GostKursklausurManager;
}
