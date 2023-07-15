import { JavaObject } from '../../../java/lang/JavaObject';
import { GostKursklausur } from '../../../core/data/gost/klausuren/GostKursklausur';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { GostKlausurraumstunde } from '../../../core/data/gost/klausuren/GostKlausurraumstunde';
import { GostSchuelerklausur } from '../../../core/data/gost/klausuren/GostSchuelerklausur';
import { MapUtils } from '../../../core/utils/MapUtils';
import type { Comparator } from '../../../java/util/Comparator';
import { GostKursklausurManager } from '../../../core/utils/klausurplan/GostKursklausurManager';
import { JavaLong } from '../../../java/lang/JavaLong';
import type { List } from '../../../java/util/List';
import { GostKlausurraum } from '../../../core/data/gost/klausuren/GostKlausurraum';
import type { JavaMap } from '../../../java/util/JavaMap';

export class GostKlausurraumManager extends JavaObject {

	/**
	 * Die Kursklausuren, die im Manager vorhanden sind
	 */
	private readonly _raeume : List<GostKlausurraum> = new ArrayList();

	/**
	 * Eine Map id -> GostKursklausur
	 */
	private readonly _mapIdRaum : JavaMap<number, GostKlausurraum> = new HashMap();

	/**
	 * Die Klausurraumstunden, die im Manager vorhanden sind
	 */
	private readonly _stunden : List<GostKlausurraumstunde> = new ArrayList();

	/**
	 * Eine Map idRaum -> Liste von Stunden
	 */
	private readonly _mapRaumStunden : JavaMap<number, List<GostKlausurraumstunde>> = new HashMap();

	/**
	 * Ein Comparator für die GostKlausurräume.
	 */
	private static readonly _compRaumId : Comparator<GostKlausurraum> = { compare : (a: GostKlausurraum, b: GostKlausurraum) => {
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Die Schuelerklausuren, die im Manager vorhanden sind
	 */
	private readonly _schuelerklausuren : List<GostSchuelerklausur> = new ArrayList();

	/**
	 * Eine Map Kursklausur-Id -> Liste von GostSchuelerklausuren
	 */
	private readonly _mapKkidSk : JavaMap<number, List<GostSchuelerklausur>> = new HashMap();


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raeume die Liste der GostKlausurräume eines Gost-Klausurtermins
	 * @param stunden   die Liste der GostKlausurraumstunden eines Gost-Klausurtermins
	 * @param schuelerklausuren die Liste der GostSchuelerklausuren des Gost-Klausurtermins
	 */
	public constructor(raeume : List<GostKlausurraum>, stunden : List<GostKlausurraumstunde>, schuelerklausuren : List<GostSchuelerklausur>) {
		super();
		for (const r of raeume)
			this.addKlausurraum(r);
		for (const s of stunden)
			this.addKlausurraumstunde(s);
		for (const k of schuelerklausuren)
			this.addSchuelerklausur(k);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param idRaum die ID des Klausurraums
	 *
	 * @return den Klausurraum
	 */
	public getKlausurraum(idRaum : number) : GostKlausurraum {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapIdRaum, idRaum);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @return die Liste der Klausurräume
	 */
	public getKlausurraeume() : List<GostKlausurraum> {
		return this._raeume;
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param raum das Gost-Klausurraum-Objekt
	 */
	public addKlausurraum(raum : GostKlausurraum) : void {
		DeveloperNotificationException.ifListAddsDuplicate("_raeume", this._raeume, raum);
		this._raeume.sort(GostKlausurraumManager._compRaumId);
		DeveloperNotificationException.ifMapPutOverwrites(this._mapIdRaum, raum.id, raum);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param stunde das Gost-Klausurraumstunde-Objekt
	 */
	public addKlausurraumstunde(stunde : GostKlausurraumstunde) : void {
		DeveloperNotificationException.ifListAddsDuplicate("_stunden", this._stunden, stunde);
		DeveloperNotificationException.ifMapGetIsNull(this._mapIdRaum, stunde.idRaum);
		DeveloperNotificationException.ifListAddsDuplicate("_mapRaumStundenList", MapUtils.getOrCreateArrayList(this._mapRaumStunden, stunde.idRaum), stunde);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param klausur das Gost-Klausurraum-Objekt
	 */
	public addSchuelerklausur(klausur : GostSchuelerklausur) : void {
		DeveloperNotificationException.ifListAddsDuplicate("_mapKkidSkList", MapUtils.getOrCreateArrayList(this._mapKkidSk, klausur.idKursklausur), klausur);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich der Klausurraum
	 * geändert hat.
	 *
	 * @param r das GostKlausurraum-Objekt
	 */
	public patchKlausurraum(r : GostKlausurraum) : void {
		DeveloperNotificationException.ifListRemoveFailes("_raeume", this._raeume, r);
		DeveloperNotificationException.ifMapRemoveFailes(this._mapIdRaum, r.id);
		this._raeume.add(r);
		this._mapIdRaum.put(r.id, r);
	}

	/**
	 * Fügt einen neuen Klausurraum den internen Datenstrukturen hinzu.
	 *
	 * @param manager das GostKlausurraumManager-Objekt
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public getKursklausuren(manager : GostKursklausurManager) : List<GostKursklausur> {
		let kursklausuren : List<GostKursklausur> | null = new ArrayList();
		for (const kkId of this._mapKkidSk.keySet()) {
			kursklausuren.add(manager.getKursklausurById(kkId));
		}
		return kursklausuren;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplan.GostKlausurraumManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplan_GostKlausurraumManager(obj : unknown) : GostKlausurraumManager {
	return obj as GostKlausurraumManager;
}
