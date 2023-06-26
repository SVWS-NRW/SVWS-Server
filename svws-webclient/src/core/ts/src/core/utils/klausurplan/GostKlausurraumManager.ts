import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { GostKlausurraum } from '../../../core/data/gost/klausuren/GostKlausurraum';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { GostKlausurraumstunde } from '../../../core/data/gost/klausuren/GostKlausurraumstunde';
import { MapUtils } from '../../../core/utils/MapUtils';
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
	 * Erstellt einen neuen Manager mit den als Liste angegebenen GostKursklausuren
	 * und Klausurterminen und erzeugt die privaten Attribute.
	 *
	 * @param raeume die Liste der GostKlausurräume eines Gost-Klausurtermins
	 * @param stunden   die Liste der GostKlausurraumstunden eines Gost-Klausurtermins
	 */
	public constructor(raeume : List<GostKlausurraum>, stunden : List<GostKlausurraumstunde>) {
		super();
		for (const r of raeume) {
			this.addKlausurraum(r);
		}
		for (const s of stunden) {
			this.addKlausurraumstunde(s);
		}
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplan.GostKlausurraumManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplan_GostKlausurraumManager(obj : unknown) : GostKlausurraumManager {
	return obj as GostKlausurraumManager;
}
