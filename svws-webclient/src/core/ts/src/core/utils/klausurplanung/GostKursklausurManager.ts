import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostKursklausur } from '../../../core/data/gost/klausurplanung/GostKursklausur';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanManager } from '../../../core/utils/stundenplan/StundenplanManager';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { MapUtils } from '../../../core/utils/MapUtils';
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { Map2DUtils } from '../../../core/utils/Map2DUtils';
import type { Comparator } from '../../../java/util/Comparator';
import { Map3DUtils } from '../../../core/utils/Map3DUtils';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { Wochentag } from '../../../core/types/Wochentag';
import type { JavaMap } from '../../../java/util/JavaMap';
import { GostKlausurtermin } from '../../../core/data/gost/klausurplanung/GostKlausurtermin';
import { HashMap3D } from '../../../core/adt/map/HashMap3D';

export class GostKursklausurManager extends JavaObject {

	/**
	 * Die Kursklausuren, die im Manager vorhanden sind
	 */
	private readonly _klausuren : List<GostKursklausur>;

	/**
	 * Eine Map id -> GostKursklausur
	 */
	private readonly _mapIdKursklausur : JavaMap<number, GostKursklausur> = new HashMap();

	/**
	 * Eine Map quartal -> Liste von GostKursklausuren
	 */
	private readonly _mapQuartalKursKlausuren : JavaMap<number, List<GostKursklausur>> = new HashMap();

	/**
	 * Eine Map idTermin -> Liste von GostKursklausuren
	 */
	private readonly _mapTerminKursklausuren : JavaMap<number, List<GostKursklausur>> = new HashMap();

	/**
	 * Eine Map quartal, idTermin -> Liste von GostKursklausuren
	 */
	private readonly _mapQuartalTerminKursklausuren : HashMap2D<number, number, List<GostKursklausur>> = new HashMap2D();

	/**
	 * Eine Map quartal, kursart, idTermin -> Liste von GostKursklausuren
	 */
	private readonly _mapQuartalKursartTerminKursklausuren : HashMap3D<number, string, number, List<GostKursklausur>> = new HashMap3D();

	/**
	 * Die Klausurtermine, die im Manager vorhanden sind
	 */
	private readonly _termine : List<GostKlausurtermin> = new ArrayList();

	/**
	 * Eine Map quartal -> Liste von GostKlausurterminen
	 */
	private readonly _mapQuartalKlausurtermine : JavaMap<number, List<GostKlausurtermin>> = new HashMap();

	/**
	 * Eine Map idTermin -> Liste von Schüler-IDs
	 */
	private readonly _mapTerminSchuelerids : JavaMap<number, List<number>> = new HashMap();

	/**
	 * Eine Map idTermin -> GostKlausurtermin
	 */
	private readonly _mapIdKlausurtermin : JavaMap<number, GostKlausurtermin> = new HashMap();

	/**
	 * Eine Map date -> GostKlausurtermin
	 */
	private readonly _mapDateKlausurtermin : JavaMap<string, List<GostKlausurtermin>> = new HashMap();

	/**
	 * Ein Comparator für die GostKlausurtermine.
	 */
	private static readonly _compDatum : Comparator<GostKlausurtermin> = { compare : (a: GostKlausurtermin, b: GostKlausurtermin) => {
		if (a.datum === null)
			return +1;
		if (b.datum === null)
			return -1;
		return JavaString.compareTo(a.datum, b.datum);
	} };


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
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param0 === null)) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param1 === null))) {
			const klausuren : List<GostKursklausur> = cast_java_util_List(__param0);
			const termine : List<GostKlausurtermin> = cast_java_util_List(__param1);
			this._klausuren = klausuren;
			this.helpKonstruktor();
			for (const t of termine) {
				this.addKlausurtermin(t);
			}
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param0 === null)) && (typeof __param1 === "undefined")) {
			const klausuren : List<GostKursklausur> = cast_java_util_List(__param0);
			this._klausuren = klausuren;
			this.helpKonstruktor();
		} else throw new Error('invalid method overload');
	}

	private helpKonstruktor() : void {
		for (const kk of this._klausuren) {
			DeveloperNotificationException.ifMapPutOverwrites(this._mapIdKursklausur, kk.id, kk);
			this.addKlausurToInternalMaps(kk);
			DeveloperNotificationException.ifListAddsDuplicate("_mapQuartalKursKlausurenList", MapUtils.getOrCreateArrayList(this._mapQuartalKursKlausuren, kk.quartal), kk);
			if (kk.idTermin !== null)
				MapUtils.getOrCreateArrayList(this._mapTerminSchuelerids, kk.idTermin).addAll(kk.schuelerIds);
		}
	}

	private addKlausurToInternalMaps(kk : GostKursklausur) : void {
		DeveloperNotificationException.ifListAddsDuplicate("_mapTerminKursklausurenList", MapUtils.getOrCreateArrayList(this._mapTerminKursklausuren, kk.idTermin !== null ? kk.idTermin : -1), kk);
		DeveloperNotificationException.ifListAddsDuplicate("_mapQuartalTerminKursklausurenList", Map2DUtils.getOrCreateArrayList(this._mapQuartalTerminKursklausuren, kk.quartal, kk.idTermin !== null ? kk.idTermin : -1), kk);
		DeveloperNotificationException.ifListAddsDuplicate("_mapQuartalKursartTerminKursklausurenList", Map3DUtils.getOrCreateArrayList(this._mapQuartalKursartTerminKursklausuren, kk.quartal, kk.kursart, kk.idTermin !== null ? kk.idTermin : -1), kk);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich z.B. das Datum eines
	 * Termins geändert hat.
	 *
	 * @param id    die ID des GostKlausurtermin-Objekts
	 * @param datum das neue Datum der Klausur
	 */
	public patchKlausurterminDatum(id : number, datum : string | null) : void {
		const termin : GostKlausurtermin = DeveloperNotificationException.ifMapGetIsNull(this._mapIdKlausurtermin, id);
		if (termin.datum !== null)
			DeveloperNotificationException.ifListRemoveFailes("_mapDateKlausurterminList", DeveloperNotificationException.ifMapGetIsNull(this._mapDateKlausurtermin, termin.datum), termin);
		termin.datum = datum;
		if (termin.datum !== null)
			DeveloperNotificationException.ifListAddsDuplicate("_mapDateKlausurterminList", MapUtils.getOrCreateArrayList(this._mapDateKlausurtermin, termin.datum), termin);
	}

	/**
	 * Löscht mehrere GostKlausurtermin-Objekte aus den internen Strukturen.
	 *
	 * @param terminIds die IDs des GostKlausurtermin-Objekte
	 */
	public removeKlausurtermine(terminIds : List<number>) : void {
		for (const id of terminIds) {
			this.removeKlausurtermin(id);
		}
	}

	/**
	 * Löscht ein GostKlausurtermin-Objekt aus den internen Strukturen.
	 *
	 * @param id die ID des GostKlausurtermin-Objekts
	 */
	public removeKlausurtermin(id : number) : void {
		const termin : GostKlausurtermin = DeveloperNotificationException.ifMapGetIsNull(this._mapIdKlausurtermin, id);
		this._mapIdKlausurtermin.remove(termin.id);
		DeveloperNotificationException.ifListRemoveFailes("_termine", this._termine, termin);
		DeveloperNotificationException.ifListRemoveFailes("listKlausurtermineMapQuartalKlausurtermine", DeveloperNotificationException.ifMapGetIsNull(this._mapQuartalKlausurtermine, termin.quartal), termin);
		const listKlausurenZuTermin : List<GostKursklausur> = DeveloperNotificationException.ifMapGetIsNull(this._mapTerminKursklausuren, id);
		for (const k of new ArrayList(listKlausurenZuTermin)) {
			k.idTermin = null;
			this.updateKursklausur(k);
		}
		if (termin.datum !== null)
			DeveloperNotificationException.ifListRemoveFailes("_mapDateKlausurterminList", DeveloperNotificationException.ifMapGetIsNull(this._mapDateKlausurtermin, termin.datum), termin);
		DeveloperNotificationException.ifMapRemoveFailes(this._mapTerminSchuelerids, termin.id);
		DeveloperNotificationException.ifMapRemoveFailes(this._mapTerminKursklausuren, termin.id);
		const klausuren : List<GostKursklausur> = this._mapQuartalTerminKursklausuren.getNonNullOrException(termin.quartal, termin.id);
		this._mapQuartalTerminKursklausuren.removeOrException(termin.quartal, termin.id);
		for (const klausur of klausuren)
			this._mapQuartalKursartTerminKursklausuren.removeOrException(termin.quartal, klausur.kursart, termin.id);
	}

	/**
	 * Fügt den internen Strukturen einen neuen Klausurtermin hinzu.
	 *
	 * @param termin das GostKlausurtermin-Objekt
	 */
	public addKlausurtermin(termin : GostKlausurtermin) : void {
		DeveloperNotificationException.ifListAddsDuplicate("_termine", this._termine, termin);
		DeveloperNotificationException.ifMapPutOverwrites(this._mapIdKlausurtermin, termin.id, termin);
		MapUtils.getOrCreateArrayList(this._mapTerminKursklausuren, termin.id);
		MapUtils.getOrCreateArrayList(this._mapTerminSchuelerids, termin.id);
		MapUtils.getOrCreateArrayList(this._mapQuartalKlausurtermine, termin.quartal).add(termin);
		if (termin.datum !== null)
			DeveloperNotificationException.ifListAddsDuplicate("_mapDateKlausurterminList", MapUtils.getOrCreateArrayList(this._mapDateKlausurtermin, termin.datum), termin);
		Map2DUtils.getOrCreateArrayList(this._mapQuartalTerminKursklausuren, termin.quartal, termin.id);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Termin einer Klausur
	 * geändert hat.
	 *
	 * @param klausur das GostKursklausur-Objekt
	 */
	public updateKursklausur(klausur : GostKursklausur) : void {
		const terminNeuKlausuren : List<GostKursklausur | null> | null = this._mapTerminKursklausuren.get(klausur.idTermin !== null ? klausur.idTermin : -1);
		if (terminNeuKlausuren === null || !terminNeuKlausuren.contains(klausur)) {
			let oldTerminId : number | null = -2;
			for (const e of this._mapTerminKursklausuren.entrySet()) {
				const list : List<GostKursklausur> = e.getValue();
				if (list.contains(klausur)) {
					oldTerminId = e.getKey();
					list.remove(klausur);
				}
			}
			DeveloperNotificationException.ifListRemoveFailes("_mapQuartalTerminKursklausurenList", this._mapQuartalTerminKursklausuren.getNonNullOrException(klausur.quartal, oldTerminId), klausur);
			DeveloperNotificationException.ifListRemoveFailes("_mapQuartalKursartTerminKursklausurenList", DeveloperNotificationException.ifMap3DGetIsNull(this._mapQuartalKursartTerminKursklausuren, klausur.quartal, klausur.kursart, oldTerminId), klausur);
			this.addKlausurToInternalMaps(klausur);
			this.updateSchuelerIdsZuTermin(oldTerminId!);
			if (klausur.idTermin !== null)
				this.updateSchuelerIdsZuTermin(klausur.idTermin);
		}
	}

	private updateSchuelerIdsZuTermin(idTermin : number) : void {
		const listSchuelerIds : ArrayList<number> | null = new ArrayList();
		this._mapTerminSchuelerids.put(idTermin, listSchuelerIds);
		const listKlausurenZuTermin : List<GostKursklausur> = DeveloperNotificationException.ifMapGetIsNull(this._mapTerminKursklausuren, idTermin);
		for (const k of listKlausurenZuTermin) {
			listSchuelerIds.addAll(k.schuelerIds);
		}
	}

	/**
	 * Fügt den internen Strukturen eine neue Kursklausur hinzu.
	 *
	 * @param klausur das GostKursklausur-Objekt
	 */
	public addKlausur(klausur : GostKursklausur) : void {
		DeveloperNotificationException.ifListAddsDuplicate("_klausuren", this._klausuren, klausur);
		DeveloperNotificationException.ifMapPutOverwrites(this._mapIdKursklausur, klausur.id, klausur);
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
	 * Liefert das GostKursklausur-Objekt zur übergebenen Id
	 *
	 * @param id die ID der Kursklausur
	 *
	 * @return das GostKursklausur-Objekt
	 */
	public getKursklausurById(id : number) : GostKursklausur {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapIdKursklausur, id);
	}

	/**
	 * Liefert das GostKursklausur-Objekt zum übergebenen Termin und Kurs
	 *
	 * @param idTermin die ID des Klausurtermins
	 * @param idKurs   die ID des Kurses
	 *
	 * @return das GostKursklausur-Objekt
	 */
	public getKursklausurByTerminKurs(idTermin : number | null, idKurs : number | null) : GostKursklausur | null {
		const klausuren : List<GostKursklausur> | null = this.getKursklausurenByTermin(idTermin);
		for (const klaus of klausuren) {
			if (klaus.idKurs === idKurs)
				return klaus;
		}
		return null;
	}

	/**
	 * Liefert den Klausurtermin zur übergebenen ID.
	 *
	 * @param id die Id des Klausurtermins
	 *
	 * @return das Klausurtermin-Objekt
	 */
	public getKlausurterminById(id : number) : GostKlausurtermin {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapIdKlausurtermin, id);
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum das Datum der Klausurtermine
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public getKlausurtermineByDatum(datum : string) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = this._mapDateKlausurtermin.get(datum);
		return termine !== null ? termine : new ArrayList();
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum   das Datum der Klausurtermine
	 * @param zr      Zeitraster
	 * @param manager Manager
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public getKlausurtermineByDatumUhrzeit(datum : string, zr : StundenplanZeitraster, manager : StundenplanManager) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = this.getKlausurtermineByDatum(datum);
		const retList : List<GostKlausurtermin> | null = new ArrayList();
		for (const termin of termine) {
			const zrsTermin : List<StundenplanZeitraster> | null = manager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(zr.wochentag), DeveloperNotificationException.ifNull("Startzeit des Klausurtermins", termin.startzeit)!, this.getMaxKlausurdauerZuTermin(termin.id));
			for (const zrTermin of zrsTermin)
				if (zrTermin !== null && zrTermin.id === zr.id)
					retList.add(termin);
		}
		return retList;
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Termin
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public getKursklausurenByTermin(idTermin : number | null) : List<GostKursklausur> {
		const klausuren : List<GostKursklausur> | null = this._mapTerminKursklausuren.get(idTermin !== null ? idTermin : -1);
		return klausuren !== null ? klausuren : new ArrayList();
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten des Halbjahres
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public getKursklausuren() : List<GostKursklausur> {
		return this._klausuren;
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal
	 *
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public getKursklausurenByQuartal(quartal : number) : List<GostKursklausur> | null {
		return this._mapQuartalKursKlausuren.get(quartal);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten für die noch kein Termin /
	 * Schiene gesetzt wurde
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public getKursklausurenOhneTermin() : List<GostKursklausur> {
		return this.getKursklausurenByTermin(-1);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal für
	 * die noch kein Termin / Schiene gesetzt wurde
	 *
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public getKursklausurenOhneTerminByQuartal(quartal : number) : List<GostKursklausur> {
		if (quartal > 0) {
			const klausuren : List<GostKursklausur> | null = this._mapQuartalTerminKursklausuren.getOrNull(quartal, -1);
			return klausuren !== null ? klausuren : new ArrayList();
		}
		const klausuren : List<GostKursklausur> | null = new ArrayList();
		const klausurListen : List<List<GostKursklausur>> | null = this._mapQuartalTerminKursklausuren.getNonNullValuesAsList();
		for (const kl of klausurListen) {
			klausuren.addAll(kl);
		}
		return klausuren;
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
		const mapKursartTerminKursklausuren : JavaMap<string, JavaMap<number, List<GostKursklausur> | null>> | null = this._mapQuartalKursartTerminKursklausuren.getMap2OrNull(quartal <= 0 ? -1 : quartal);
		if (mapKursartTerminKursklausuren !== null) {
			for (const mapKursarten of mapKursartTerminKursklausuren.values()) {
				if (mapKursarten !== null)
					retList.add(mapKursarten.get(-1));
			}
		}
		return retList;
	}

	/**
	 * Liefert die maximale Klausurdauer innerhalb eines Klausurtermins
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die maximale Klausurdauer innerhalb des Termins
	 */
	public getMaxKlausurdauerZuTermin(idTermin : number) : number {
		const klausuren : List<GostKursklausur> = DeveloperNotificationException.ifMapGetIsNull(this._mapTerminKursklausuren, idTermin);
		let maxDauer : number = -1;
		for (const klausur of klausuren)
			maxDauer = klausur.dauer > maxDauer ? klausur.dauer : maxDauer;
		return maxDauer;
	}

	/**
	 * Gibt das GostKlausurtermin-Objekt zur übergebenen id zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return das GostKlausurtermin-Objekt
	 */
	public gibKlausurtermin(idTermin : number) : GostKlausurtermin {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapIdKlausurtermin, idTermin);
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
	public gibKursklausurById(idKursklausur : number) : GostKursklausur {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapIdKursklausur, idKursklausur);
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten des Halbjahres
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public getKlausurtermine() : List<GostKlausurtermin> {
		return this._termine;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Quartal
	 *
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public getKlausurtermineByQuartal(quartal : number) : List<GostKlausurtermin> {
		if (quartal === 0)
			return this.getKlausurtermine();
		const termine : List<GostKlausurtermin> | null = this._mapQuartalKlausurtermine.get(quartal);
		return termine !== null ? termine : new ArrayList();
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten des Halbjahres, bei denen
	 * ein Datum gesetzt ist
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public getKlausurtermineMitDatum() : List<GostKlausurtermin> {
		const termineMitDatum : List<GostKlausurtermin> | null = new ArrayList();
		for (const termin of this._termine)
			if (termin.datum !== null)
				termineMitDatum.add(termin);
		termineMitDatum.sort(GostKursklausurManager._compDatum);
		return termineMitDatum;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten des Quartals, bei denen ein
	 * Datum gesetzt ist
	 *
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public getKlausurtermineMitDatumByQuartal(quartal : number) : List<GostKlausurtermin> {
		const termineMitDatum : List<GostKlausurtermin> | null = new ArrayList();
		for (const termin of this.getKlausurtermineByQuartal(quartal))
			if (termin.datum !== null)
				termineMitDatum.add(termin);
		termineMitDatum.sort(GostKursklausurManager._compDatum);
		return termineMitDatum;
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
		const listKlausurenZuTermin : List<GostKursklausur> | null = this.getKursklausurenByTermin(termin.id);
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
	public gibKonfliktTerminKursklausur(termin : GostKlausurtermin, klausur : GostKursklausur) : List<number> {
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
	public gibKonfliktTerminKursklausurIds(idTermin : number, idKursklausur : number) : List<number> {
		const klausur : GostKursklausur | null = DeveloperNotificationException.ifMapGetIsNull(this._mapIdKursklausur, idKursklausur);
		const termin : GostKlausurtermin | null = DeveloperNotificationException.ifMapGetIsNull(this._mapIdKlausurtermin, idTermin);
		return this.gibKonfliktTerminKursklausur(termin, klausur);
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
		const listKlausurenZuTermin : List<GostKursklausur> | null = this.getKursklausurenByTermin(idTermin);
		if (listKlausurenZuTermin !== null) {
			const copyListKlausurenZuTermin : List<GostKursklausur> | null = new ArrayList(listKlausurenZuTermin);
			for (const k1 of listKlausurenZuTermin) {
				copyListKlausurenZuTermin.remove(k1);
				for (const k2 of copyListKlausurenZuTermin)
					anzahl += this.gibKonfliktKursklausurKursklausurIds(k1.id, k2.id).size();
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
	public gibKonfliktKursklausurKursklausurIds(idKursklausur1 : number, idKursklausur2 : number) : List<number> {
		const klausur1 : GostKursklausur | null = DeveloperNotificationException.ifMapGetIsNull(this._mapIdKursklausur, idKursklausur1);
		const klausur2 : GostKursklausur | null = DeveloperNotificationException.ifMapGetIsNull(this._mapIdKursklausur, idKursklausur2);
		return this.gibKonfliktKursklausurKursklausur(klausur1, klausur2);
	}

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
	public gibKonfliktKursklausurKursklausur(klausur1 : GostKursklausur, klausur2 : GostKursklausur) : List<number> {
		if (klausur1 as unknown === klausur2 as unknown) {
			return new ArrayList();
		}
		const konflikte : List<number> | null = new ArrayList(klausur1.schuelerIds);
		konflikte.retainAll(klausur2.schuelerIds);
		return konflikte;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplanung.GostKursklausurManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplanung_GostKursklausurManager(obj : unknown) : GostKursklausurManager {
	return obj as GostKursklausurManager;
}
