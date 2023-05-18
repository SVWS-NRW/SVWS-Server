import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { StundenplanPausenaufsicht } from '../../../core/data/stundenplan/StundenplanPausenaufsicht';
import { StundenplanUnterrichtsverteilung } from '../../../core/data/stundenplan/StundenplanUnterrichtsverteilung';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanKurs } from '../../../core/data/stundenplan/StundenplanKurs';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { StundenplanUnterricht } from '../../../core/data/stundenplan/StundenplanUnterricht';
import { List } from '../../../java/util/List';
import { StundenplanKalenderwochenzuordnung } from '../../../core/data/stundenplan/StundenplanKalenderwochenzuordnung';
import { Stundenplan } from '../../../core/data/stundenplan/Stundenplan';

export class StundenplanManager extends JavaObject {

	private readonly _daten : Stundenplan;

	private readonly _datenU : List<StundenplanUnterricht>;

	private readonly _datenUV : StundenplanUnterrichtsverteilung | null;

	private readonly _map_kursID_zu_unterrichte : HashMap<number, List<StundenplanUnterricht>> = new HashMap();

	private readonly _map_kursID_zu_kurs : HashMap<number, StundenplanKurs> = new HashMap();

	private readonly _map_jahr_kw_zu_wochtentyp : HashMap2D<number, number, StundenplanKalenderwochenzuordnung> = new HashMap2D();


	/**
	 * Der {@link StundenplanManager} benötigt vier data-Objekte und baut damit eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param daten                 liefert die Grunddaten des Stundenplanes.
	 * @param unterrichte           liefert die Informationen zu allen {@link StundenplanUnterricht} im Stundenplan. Die Liste darf leer sein.
	 * @param pausenaufsichten      liefert die Informationen zu allen {@link StundenplanPausenaufsicht} im Stundenplan. Die Liste darf leer sein.
	 * @param unterrichtsverteilung liefert die Informationen zu der Unterrichtsverteilung eines Stundenplans. Darf NULL sein.
	 */
	public constructor(daten : Stundenplan, unterrichte : List<StundenplanUnterricht>, pausenaufsichten : List<StundenplanPausenaufsicht>, unterrichtsverteilung : StundenplanUnterrichtsverteilung | null) {
		super();
		this._daten = daten;
		this._datenU = unterrichte;
		this._datenUV = unterrichtsverteilung;
		this.checkWochentypenKonsistenz();
		this.initMapJahrUndKwZuWochentyp();
		this.initMapKursIDZuKurs();
		this.initMapKursZuUnterrichte();
	}

	private checkWochentypenKonsistenz() : void {
		const wochentyp : number = this._daten.wochenTypModell;
		DeveloperNotificationException.ifTrue("_daten.wochenTypModell < 0", wochentyp < 0);
		DeveloperNotificationException.ifTrue("_daten.wochenTypModell == 1", wochentyp === 1);
		for (const z of this._daten.kalenderwochenZuordnung) {
			DeveloperNotificationException.ifTrue("z.wochentyp <= 0", z.wochentyp <= 0);
			DeveloperNotificationException.ifTrue("z.wochentyp > wochentyp", z.wochentyp > wochentyp);
		}
		for (const u of this._datenU) {
			DeveloperNotificationException.ifTrue("u.wochentyp < 0", u.wochentyp < 0);
			DeveloperNotificationException.ifTrue("u.wochentyp > wochentyp", u.wochentyp > wochentyp);
		}
	}

	private initMapJahrUndKwZuWochentyp() : void {
		this._map_jahr_kw_zu_wochtentyp.clear();
		for (const z of this._daten.kalenderwochenZuordnung)
			this._map_jahr_kw_zu_wochtentyp.put(z.jahr, z.kw, z);
	}

	private initMapKursIDZuKurs() : void {
		if (this._datenUV === null)
			return;
		this._map_kursID_zu_kurs.clear();
		for (const k of this._datenUV.kurse) {
			DeveloperNotificationException.ifTrue("_map_kursID_zu_kurs.containsKey(k.id)", this._map_kursID_zu_kurs.containsKey(k.id));
			this._map_kursID_zu_kurs.put(k.id, k);
		}
	}

	private initMapKursZuUnterrichte() : void {
		this._map_kursID_zu_unterrichte.clear();
		for (const u of this._datenU) {
			if (u.idKurs === null)
				continue;
			DeveloperNotificationException.ifTrue("!_map_kursID_zu_kurs.containsKey(u.idKurs)", !this._map_kursID_zu_kurs.containsKey(u.idKurs));
			let listU : List<StundenplanUnterricht> | null = this._map_kursID_zu_unterrichte.get(u.idKurs);
			if (listU === null) {
				listU = new ArrayList();
				this._map_kursID_zu_unterrichte.put(u.idKurs, listU);
			}
			DeveloperNotificationException.ifTrue("listU.contains(u)", listU.contains(u));
			listU.add(u);
		}
	}

	/**
	 * Liefert den zugeordneten Wochentyp, oder den Default-Wochentyp.
	 *
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return den zugeordneten Wochentyp, oder den Default-Wochentyp.
	 */
	public getWochentypOrDefault(jahr : number, kalenderwoche : number) : number {
		DeveloperNotificationException.ifTrue("(jahr < 2000) || (jahr > 3000)", (jahr < 2000) || (jahr > 3000));
		DeveloperNotificationException.ifTrue("(kalenderwoche < 1) || (kalenderwoche > 53)", (kalenderwoche < 1) || (kalenderwoche > 53));
		if (this._daten.wochenTypModell === 0)
			return 0;
		const z : StundenplanKalenderwochenzuordnung | null = this._map_jahr_kw_zu_wochtentyp.getOrNull(jahr, kalenderwoche);
		if (z !== null)
			return z.wochentyp;
		const wochentyp : number = kalenderwoche % this._daten.wochenTypModell;
		return wochentyp === 0 ? this._daten.wochenTypModell : wochentyp;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 *
	 * @param kursID        Die ID des Kurses.
	 * @param jahr          Das Jahr der Kalenderwoche.
	 * @param kalenderwoche Die gewünschten Kalenderwoche. Der Wert darf nicht 0 sein.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public getUnterrichtDesKurses(kursID : number, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht>;

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses mit einem bestimmten Wochentyp.
	 *
	 * @param kursID    Die ID des Kurses.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn es global keine AB-Wochen gibt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public getUnterrichtDesKurses(kursID : number, wochentyp : number) : List<StundenplanUnterricht>;

	/**
	 * Implementation for method overloads of 'getUnterrichtDesKurses'
	 */
	public getUnterrichtDesKurses(__param0 : number, __param1 : number, __param2? : number) : List<StundenplanUnterricht> {
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && ((typeof __param2 !== "undefined") && typeof __param2 === "number")) {
			const kursID : number = __param0 as number;
			const jahr : number = __param1 as number;
			const kalenderwoche : number = __param2 as number;
			const wochentyp : number = this.getWochentypOrDefault(jahr, kalenderwoche);
			return this.getUnterrichtDesKurses(kursID, wochentyp);
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && (typeof __param2 === "undefined")) {
			const kursID : number = __param0 as number;
			const wochentyp : number = __param1 as number;
			DeveloperNotificationException.ifTrue("wochentyp > _daten.wochenTypModell", wochentyp > this._daten.wochenTypModell);
			const list : List<StundenplanUnterricht> = DeveloperNotificationException.ifNull("_map_kursID_zu_unterrichte.get(kursID)==NULL", this._map_kursID_zu_unterrichte.get(kursID));
			const result : ArrayList<StundenplanUnterricht> | null = new ArrayList();
			for (const u of list)
				if ((u.wochentyp === 0) || (u.wochentyp === wochentyp))
					result.add(u);
			return result;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplan.StundenplanManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanManager(obj : unknown) : StundenplanManager {
	return obj as StundenplanManager;
}
