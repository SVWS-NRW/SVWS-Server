import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostKursklausur } from '../../../core/data/gost/klausurplanung/GostKursklausur';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanManager } from '../../../core/utils/stundenplan/StundenplanManager';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { MapUtils } from '../../../core/utils/MapUtils';
import { GostKursart } from '../../../core/types/gost/GostKursart';
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

	private static readonly _compTermin : Comparator<GostKlausurtermin> = { compare : (a: GostKlausurtermin, b: GostKlausurtermin) => {
		if (a.datum === null && b.datum !== null)
			return +1;
		if (b.datum === null && a.datum !== null)
			return -1;
		if (a.datum !== null && b.datum !== null)
			return JavaString.compareTo(a.datum, b.datum);
		if (a.quartal !== b.quartal)
			return a.quartal - b.quartal;
		return a.id > b.id ? +1 : -1;
	} };

	private static readonly _compKursklausur : Comparator<GostKursklausur> = { compare : (a: GostKursklausur, b: GostKursklausur) => {
		if (a.halbjahr !== b.halbjahr)
			return a.halbjahr - b.halbjahr;
		if (a.quartal !== b.quartal)
			return a.quartal - b.quartal;
		if (a.idFach !== b.idFach)
			return a.idFach > b.idFach ? +1 : -1;
		if (a.kursart as unknown !== b.kursart as unknown)
			return GostKursart.fromKuerzelOrException(a.kursart).compareTo(GostKursart.fromKuerzelOrException(b.kursart));
		return a.id > b.id ? +1 : -1;
	} };

	private readonly _kursklausur_by_id : JavaMap<number, GostKursklausur> = new HashMap();

	private readonly _kursklausurmenge : List<GostKursklausur> = new ArrayList();

	private readonly _kursklausurmenge_by_quartal : JavaMap<number, List<GostKursklausur>> = new HashMap();

	private readonly _kursklausurmenge_by_idTermin : JavaMap<number, List<GostKursklausur>> = new HashMap();

	private readonly _kursklausurmenge_by_quartal_and_idTermin : HashMap2D<number, number, List<GostKursklausur>> = new HashMap2D();

	private readonly _kursklausurmenge_by_quartal_and_kursart_and_idTermin : HashMap3D<number, string, number, List<GostKursklausur>> = new HashMap3D();

	private readonly _termin_by_id : JavaMap<number, GostKlausurtermin> = new HashMap();

	private readonly _terminmenge : List<GostKlausurtermin> = new ArrayList();

	private readonly _terminmenge_by_quartal : JavaMap<number, List<GostKlausurtermin>> = new HashMap();

	private readonly _terminmenge_by_datum : JavaMap<string, List<GostKlausurtermin>> = new HashMap();

	private readonly _schuelerIds_by_idTermin : JavaMap<number, List<number>> = new HashMap();


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param listKlausuren die Liste der GostKursklausuren eines Abiturjahrgangs
	 *                      und Gost-Halbjahres
	 * @param listTermine   die Liste der GostKlausurtermine eines Abiturjahrgangs
	 *                      und Gost-Halbjahres
	 */
	public constructor(listKlausuren : List<GostKursklausur>, listTermine : List<GostKlausurtermin>);

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und erzeugt die privaten Attribute.
	 *
	 * @param listKlausuren die Liste der GostKursklausuren eines Abiturjahrgangs
	 *                      und Gost-Halbjahres
	 */
	public constructor(listKlausuren : List<GostKursklausur>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : List<GostKursklausur>, __param1? : List<GostKlausurtermin>) {
		super();
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param0 === null)) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param1 === null))) {
			const listKlausuren : List<GostKursklausur> = cast_java_util_List(__param0);
			const listTermine : List<GostKlausurtermin> = cast_java_util_List(__param1);
			this.initAll(listKlausuren, listTermine);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param0 === null)) && (typeof __param1 === "undefined")) {
			const listKlausuren : List<GostKursklausur> = cast_java_util_List(__param0);
			this.initAll(listKlausuren, new ArrayList());
		} else throw new Error('invalid method overload');
	}

	private initAll(listKlausuren : List<GostKursklausur>, listTermine : List<GostKlausurtermin>) : void {
		this.kursklausurAddAll(listKlausuren);
		this.terminAddAll(listTermine);
		this.update_all();
	}

	private update_all() : void {
		this.update_kursklausurmenge();
		this.update_terminmenge();
		this.update_kursklausurmenge_by_quartal();
		this.update_kursklausurmenge_by_idTermin();
		this.update_kursklausurmenge_by_quartal_and_idTermin();
		this.update_kursklausurmenge_by_quartal_and_kursart_and_idTermin();
		this.update_terminmenge_by_quartal();
		this.update_terminmenge_by_datum();
		this.update_schuelerIds_by_idTermin();
	}

	private update_kursklausurmenge_by_quartal() : void {
		this._kursklausurmenge_by_quartal.clear();
		for (const kk of this._kursklausurmenge)
			MapUtils.getOrCreateArrayList(this._kursklausurmenge_by_quartal, kk.quartal).add(kk);
	}

	private update_kursklausurmenge_by_idTermin() : void {
		this._kursklausurmenge_by_idTermin.clear();
		for (const kk of this._kursklausurmenge)
			MapUtils.getOrCreateArrayList(this._kursklausurmenge_by_idTermin, kk.idTermin !== null ? kk.idTermin : -1).add(kk);
	}

	private update_kursklausurmenge_by_quartal_and_idTermin() : void {
		this._kursklausurmenge_by_quartal_and_idTermin.clear();
		for (const kk of this._kursklausurmenge)
			Map2DUtils.getOrCreateArrayList(this._kursklausurmenge_by_quartal_and_idTermin, kk.quartal, kk.idTermin !== null ? kk.idTermin : -1).add(kk);
	}

	private update_kursklausurmenge_by_quartal_and_kursart_and_idTermin() : void {
		this._kursklausurmenge_by_quartal_and_kursart_and_idTermin.clear();
		for (const kk of this._kursklausurmenge)
			Map3DUtils.getOrCreateArrayList(this._kursklausurmenge_by_quartal_and_kursart_and_idTermin, kk.quartal, kk.kursart, kk.idTermin !== null ? kk.idTermin : -1).add(kk);
	}

	private update_terminmenge_by_quartal() : void {
		this._terminmenge_by_quartal.clear();
		for (const t of this._terminmenge)
			MapUtils.getOrCreateArrayList(this._terminmenge_by_quartal, t.quartal).add(t);
	}

	private update_terminmenge_by_datum() : void {
		this._terminmenge_by_datum.clear();
		for (const t of this._terminmenge)
			MapUtils.getOrCreateArrayList(this._terminmenge_by_datum, t.datum).add(t);
	}

	private update_schuelerIds_by_idTermin() : void {
		this._schuelerIds_by_idTermin.clear();
		for (const t of this._terminmenge) {
			const listSchuelerIds : ArrayList<number> | null = new ArrayList();
			this._schuelerIds_by_idTermin.put(t.id, listSchuelerIds);
			const klausurenZuTermin : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(t.id);
			if (klausurenZuTermin !== null)
				for (const k of klausurenZuTermin)
					listSchuelerIds.addAll(k.schuelerIds);
		}
	}

	private update_kursklausurmenge() : void {
		this._kursklausurmenge.clear();
		this._kursklausurmenge.addAll(this._kursklausur_by_id.values());
		this._kursklausurmenge.sort(GostKursklausurManager._compKursklausur);
	}

	private kursklausurAddOhneUpdate(kursklausur : GostKursklausur) : void {
		GostKursklausurManager.kursklausurCheck(kursklausur);
		DeveloperNotificationException.ifMapPutOverwrites(this._kursklausur_by_id, kursklausur.id, kursklausur);
	}

	/**
	 * Fügt ein {@link GostKursklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostKursklausur}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public kursklausurAdd(kursklausur : GostKursklausur) : void {
		this.kursklausurAddOhneUpdate(kursklausur);
		this.update_all();
	}

	private kursklausurAddAllOhneUpdate(listKursklausuren : List<GostKursklausur>) : void {
		for (const kursklausur of listKursklausuren)
			this.kursklausurAddOhneUpdate(kursklausur);
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public kursklausurAddAll(listKursklausuren : List<GostKursklausur>) : void {
		this.kursklausurAddAllOhneUpdate(listKursklausuren);
		this.update_all();
	}

	private static kursklausurCheck(kursklausur : GostKursklausur) : void {
		DeveloperNotificationException.ifInvalidID("kursklausur.id", kursklausur.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKursklausur}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idKursklausur Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKursklausur}-Objekt.
	 */
	public kursklausurGetByIdOrException(idKursklausur : number) : GostKursklausur {
		return DeveloperNotificationException.ifMapGetIsNull(this._kursklausur_by_id, idKursklausur);
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public kursklausurGetMengeAsList() : List<GostKursklausur> {
		return this._kursklausurmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausur Das neue {@link GostKursklausur}-Objekt.
	 */
	public kursklausurPatchAttributes(kursklausur : GostKursklausur) : void {
		GostKursklausurManager.kursklausurCheck(kursklausur);
		DeveloperNotificationException.ifMapRemoveFailes(this._kursklausur_by_id, kursklausur.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._kursklausur_by_id, kursklausur.id, kursklausur);
		this.update_all();
	}

	private kursklausurRemoveOhneUpdateById(idKursklausur : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._kursklausur_by_id, idKursklausur);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param idKursklausur Die ID des {@link GostKursklausur}-Objekts.
	 */
	public kursklausurRemoveById(idKursklausur : number) : void {
		this.kursklausurRemoveOhneUpdateById(idKursklausur);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKursklausur}-Objekte.
	 *
	 * @param listKursklausuren Die Liste der zu entfernenden
	 *                          {@link GostKursklausur}-Objekte.
	 */
	public kursklausurRemoveAll(listKursklausuren : List<GostKursklausur>) : void {
		for (const kursklausur of listKursklausuren)
			this.kursklausurRemoveOhneUpdateById(kursklausur.id);
		this.update_all();
	}

	private update_terminmenge() : void {
		this._terminmenge.clear();
		this._terminmenge.addAll(this._termin_by_id.values());
		this._terminmenge.sort(GostKursklausurManager._compTermin);
	}

	private terminAddOhneUpdate(termin : GostKlausurtermin) : void {
		GostKursklausurManager.terminCheck(termin);
		DeveloperNotificationException.ifMapPutOverwrites(this._termin_by_id, termin.id, termin);
	}

	/**
	 * Fügt ein {@link GostKlausurtermin}-Objekt hinzu.
	 *
	 * @param termin Das {@link GostKlausurtermin}-Objekt, welches hinzugefügt
	 *               werden soll.
	 */
	public terminAdd(termin : GostKlausurtermin) : void {
		this.terminAddOhneUpdate(termin);
		this.update_all();
	}

	private terminAddAllOhneUpdate(listTermine : List<GostKlausurtermin>) : void {
		for (const termin of listTermine)
			this.terminAddOhneUpdate(termin);
	}

	/**
	 * Fügt alle {@link GostKlausurtermin}-Objekte hinzu.
	 *
	 * @param listTermine Die Menge der {@link GostKlausurtermin}-Objekte, welche
	 *                    hinzugefügt werden soll.
	 */
	public terminAddAll(listTermine : List<GostKlausurtermin>) : void {
		this.terminAddAllOhneUpdate(listTermine);
		this.update_all();
	}

	private static terminCheck(termin : GostKlausurtermin) : void {
		DeveloperNotificationException.ifInvalidID("kursklausur.id", termin.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurtermin}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idTermin Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurtermin}-Objekt.
	 */
	public terminGetByIdOrException(idTermin : number) : GostKlausurtermin {
		return DeveloperNotificationException.ifMapGetIsNull(this._termin_by_id, idTermin);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurtermin}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurtermin}-Objekte.
	 */
	public terminGetMengeAsList() : List<GostKlausurtermin> {
		return this._terminmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurtermin}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param termin Das neue {@link GostKlausurtermin}-Objekt.
	 */
	public terminPatchAttributes(termin : GostKlausurtermin) : void {
		GostKursklausurManager.terminCheck(termin);
		DeveloperNotificationException.ifMapRemoveFailes(this._termin_by_id, termin.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._termin_by_id, termin.id, termin);
		this.update_all();
	}

	private terminRemoveOhneUpdateById(idTermin : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._termin_by_id, idTermin);
		const klausurenZuTermin : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(idTermin);
		if (klausurenZuTermin !== null)
			for (const k of klausurenZuTermin)
				k.idTermin = null;
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurtermin}-Objekt.
	 *
	 * @param idTermin Die ID des {@link GostKlausurtermin}-Objekts.
	 */
	public terminRemoveById(idTermin : number) : void {
		this.terminRemoveOhneUpdateById(idTermin);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurtermin}-Objekte.
	 *
	 * @param listTermine Die Liste der zu entfernenden
	 *                    {@link GostKlausurtermin}-Objekte.
	 */
	public terminRemoveAll(listTermine : List<GostKlausurtermin>) : void {
		for (const termin of listTermine)
			this.terminRemoveOhneUpdateById(termin.id);
		this.update_all();
	}

	/**
	 * Liefert das GostKursklausur-Objekt zum übergebenen Termin und Kurs
	 *
	 * @param idTermin die ID des Klausurtermins
	 * @param idKurs   die ID des Kurses
	 *
	 * @return das GostKursklausur-Objekt
	 */
	public kursklausurGetByTerminidAndKursid(idTermin : number, idKurs : number) : GostKursklausur | null {
		const klausuren : List<GostKursklausur> | null = this.kursklausurGetMengeByTerminid(idTermin);
		for (const klaus of klausuren) {
			if (klaus.idKurs === idKurs)
				return klaus;
		}
		return null;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Datum
	 *
	 * @param datum das Datum der Klausurtermine
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminGetMengeByDatum(datum : string) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = this._terminmenge_by_datum.get(datum);
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
	public terminGetMengeByDatumAndZeitraster(datum : string, zr : StundenplanZeitraster, manager : StundenplanManager) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = this.terminGetMengeByDatum(datum);
		const retList : List<GostKlausurtermin> | null = new ArrayList();
		for (const termin of termine) {
			const zrsTermin : List<StundenplanZeitraster> | null = manager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(zr.wochentag), DeveloperNotificationException.ifNull("Startzeit des Klausurtermins", termin.startzeit)!, this.maxKlausurdauerGetByTerminid(termin.id));
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
	public kursklausurGetMengeByTerminid(idTermin : number | null) : List<GostKursklausur> {
		const klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(idTermin !== null ? idTermin : -1);
		return klausuren !== null ? klausuren : new ArrayList();
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal
	 *
	 * @param quartal die Nummer des Quartals
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public kursklausurGetMengeByQuartal(quartal : number) : List<GostKursklausur> | null {
		return this._kursklausurmenge_by_quartal.get(quartal);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten für die noch kein Termin /
	 * Schiene gesetzt wurde
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public kursklausurOhneTerminGetMenge() : List<GostKursklausur> {
		return this.kursklausurGetMengeByTerminid(-1);
	}

	/**
	 * Liefert eine Liste von GostKursklausur-Objekten zum übergebenen Quartal für
	 * die noch kein Termin / Schiene gesetzt wurde
	 *
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public kursklausurOhneTerminGetMengeByQuartal(quartal : number) : List<GostKursklausur> {
		if (quartal > 0) {
			const klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_quartal_and_idTermin.getOrNull(quartal, -1);
			return klausuren !== null ? klausuren : new ArrayList();
		}
		return this.kursklausurOhneTerminGetMenge();
	}

	/**
	 * Liefert die maximale Klausurdauer innerhalb eines Klausurtermins
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die maximale Klausurdauer innerhalb des Termins
	 */
	public maxKlausurdauerGetByTerminid(idTermin : number) : number {
		const klausuren : List<GostKursklausur> = DeveloperNotificationException.ifMapGetIsNull(this._kursklausurmenge_by_idTermin, idTermin);
		let maxDauer : number = -1;
		for (const klausur of klausuren)
			maxDauer = klausur.dauer > maxDauer ? klausur.dauer : maxDauer;
		return maxDauer;
	}

	/**
	 * Gibt eine Liste von Schüler-IDs zurück, die vom übergebenen Termin betroffen
	 * sind.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste der betroffenen Schüler-IDs
	 */
	public schueleridsGetMengeByTerminid(idTermin : number) : List<number> {
		const schuelerIds : List<number> | null = this._schuelerIds_by_idTermin.get(idTermin);
		return schuelerIds !== null ? schuelerIds : new ArrayList();
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten zum übergebenen Quartal
	 *
	 * @param quartal             die Nummer des Quartals, 0 für alle Quartale
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminGetMengeByQuartal(quartal : number, includeMultiquartal : boolean) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = new ArrayList();
		if (quartal > 0) {
			if (this._terminmenge_by_quartal.get(quartal) !== null)
				termine.addAll(this._terminmenge_by_quartal.get(quartal));
			if (includeMultiquartal && this._terminmenge_by_quartal.get(0) !== null)
				termine.addAll(this._terminmenge_by_quartal.get(0));
			return termine;
		}
		return this.terminGetMengeAsList();
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten des Halbjahres, bei denen
	 * ein Datum gesetzt ist
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminMitDatumGetMenge() : List<GostKlausurtermin> {
		const termineMitDatum : List<GostKlausurtermin> | null = new ArrayList();
		for (const termin of this._terminmenge)
			if (termin.datum !== null)
				termineMitDatum.add(termin);
		termineMitDatum.sort(GostKursklausurManager._compTermin);
		return termineMitDatum;
	}

	/**
	 * Liefert eine Liste von GostKlausurtermin-Objekten des Quartals, bei denen ein
	 * Datum gesetzt ist
	 *
	 * @param quartal             die Nummer des Quartals
	 * @param includeMultiquartal true, wenn auch für mehrere Quartale geöffnete
	 *                            Termine geliefert werden sollen, sonst false
	 *
	 * @return die Liste von GostKlausurtermin-Objekten
	 */
	public terminMitDatumGetMengeByQuartal(quartal : number, includeMultiquartal : boolean) : List<GostKlausurtermin> {
		const termineMitDatum : List<GostKlausurtermin> | null = new ArrayList();
		for (const termin of this.terminGetMengeByQuartal(quartal, includeMultiquartal))
			if (termin.datum !== null)
				termineMitDatum.add(termin);
		termineMitDatum.sort(GostKursklausurManager._compTermin);
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
	public konfliktTermininternSchueleridsGetMengeByTerminAndKursklausur(termin : GostKlausurtermin, klausur : GostKursklausur) : List<number> {
		const konflikte : List<number> = new ArrayList();
		const listKlausurenZuTermin : List<GostKursklausur> | null = this.kursklausurGetMengeByTerminid(termin.id);
		if (listKlausurenZuTermin === null)
			return konflikte;
		for (const klausurInTermin of listKlausurenZuTermin)
			konflikte.addAll(this.konfliktSchueleridsGetMengeByKursklausurAndKursklausur(klausur, klausurInTermin));
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
	public konfliktSchueleridsGetMengeByTerminAndKursklausur(termin : GostKlausurtermin, klausur : GostKursklausur) : List<number> {
		if (klausur.idTermin === termin.id) {
			return new ArrayList();
		}
		const schuelerIds : List<number> | null = this.schueleridsGetMengeByTerminid(termin.id);
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
	public konfliktSchueleridsGetMengeByTerminidAndKursklausurid(idTermin : number, idKursklausur : number) : List<number> {
		const klausur : GostKursklausur | null = DeveloperNotificationException.ifMapGetIsNull(this._kursklausur_by_id, idKursklausur);
		const termin : GostKlausurtermin | null = DeveloperNotificationException.ifMapGetIsNull(this._termin_by_id, idTermin);
		return this.konfliktSchueleridsGetMengeByTerminAndKursklausur(termin, klausur);
	}

	/**
	 * Prüft, ob es innerhalb eines bestehenden Klausurtermins Konflikte gibt. Es
	 * wird die Anzahl der Konflikte zurückgegeben.
	 *
	 * @param idTermin die ID des zu prüfenden Klausurtermins
	 *
	 * @return die Anzahl der Konflikte innerhalb des Termins.
	 */
	public konflikteAnzahlGetByTerminid(idTermin : number) : number {
		let anzahl : number = 0;
		const listKlausurenZuTermin : List<GostKursklausur> | null = this.kursklausurGetMengeByTerminid(idTermin);
		if (listKlausurenZuTermin !== null) {
			const copyListKlausurenZuTermin : List<GostKursklausur> | null = new ArrayList(listKlausurenZuTermin);
			for (const k1 of listKlausurenZuTermin) {
				copyListKlausurenZuTermin.remove(k1);
				for (const k2 of copyListKlausurenZuTermin)
					anzahl += this.konfliktSchueleridsGetMengeByKursklausuridAndKursklausurid(k1.id, k2.id).size();
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
	public konfliktSchueleridsGetMengeByKursklausuridAndKursklausurid(idKursklausur1 : number, idKursklausur2 : number) : List<number> {
		const klausur1 : GostKursklausur | null = DeveloperNotificationException.ifMapGetIsNull(this._kursklausur_by_id, idKursklausur1);
		const klausur2 : GostKursklausur | null = DeveloperNotificationException.ifMapGetIsNull(this._kursklausur_by_id, idKursklausur2);
		return this.konfliktSchueleridsGetMengeByKursklausurAndKursklausur(klausur1, klausur2);
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
	public konfliktSchueleridsGetMengeByKursklausurAndKursklausur(klausur1 : GostKursklausur, klausur2 : GostKursklausur) : List<number> {
		if (klausur1 as unknown === klausur2 as unknown) {
			return new ArrayList();
		}
		const konflikte : List<number> | null = new ArrayList(klausur1.schuelerIds);
		konflikte.retainAll(klausur2.schuelerIds);
		return konflikte;
	}

	/**
	 * Liefert das Quartal der Kursklausuren innerhalb des Klausurtermins, sofern alle identisch sind, sonst -1.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return das Quartal aller Klausuren, sofern identisch, sonst -1.
	 */
	public quartalGetByTerminid(idTermin : number) : number {
		const klausuren : List<GostKursklausur> | null = this._kursklausurmenge_by_idTermin.get(idTermin);
		if (klausuren === null)
			return DeveloperNotificationException.ifMapGetIsNull(this._termin_by_id, idTermin).quartal;
		let quartal : number = -1;
		for (const k of klausuren) {
			if (quartal === -1)
				quartal = k.quartal;
			if (quartal !== k.quartal)
				return -1;
		}
		return quartal;
	}

	/**
	 * Liefert die Anzahl der Schülerklausuren zu einem bestimmten Klausurtermin.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Anzahl der Schülerklausuren des Termins.
	 */
	public schuelerklausurAnzahlGetByTerminid(idTermin : number) : number {
		return this.schueleridsGetMengeByTerminid(idTermin).size();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplanung.GostKursklausurManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplanung_GostKursklausurManager(obj : unknown) : GostKursklausurManager {
	return obj as GostKursklausurManager;
}
