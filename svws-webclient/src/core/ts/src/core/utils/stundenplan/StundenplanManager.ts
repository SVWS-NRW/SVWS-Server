import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { StundenplanPausenaufsicht } from '../../../core/data/stundenplan/StundenplanPausenaufsicht';
import { StundenplanUnterrichtsverteilung } from '../../../core/data/stundenplan/StundenplanUnterrichtsverteilung';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanKurs } from '../../../core/data/stundenplan/StundenplanKurs';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../../java/lang/JavaString';
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { StundenplanPausenzeit } from '../../../core/data/stundenplan/StundenplanPausenzeit';
import { StundenplanAufsichtsbereich } from '../../../core/data/stundenplan/StundenplanAufsichtsbereich';
import { StundenplanRaum } from '../../../core/data/stundenplan/StundenplanRaum';
import { StundenplanSchiene } from '../../../core/data/stundenplan/StundenplanSchiene';
import { StundenplanUnterricht } from '../../../core/data/stundenplan/StundenplanUnterricht';
import { List } from '../../../java/util/List';
import { StundenplanKalenderwochenzuordnung } from '../../../core/data/stundenplan/StundenplanKalenderwochenzuordnung';
import { Stundenplan } from '../../../core/data/stundenplan/Stundenplan';

export class StundenplanManager extends JavaObject {

	private readonly _daten : Stundenplan;

	private readonly _datenU : List<StundenplanUnterricht>;

	private readonly _datenUV : StundenplanUnterrichtsverteilung | null;

	private readonly _map_zeitrasterID_zu_zeitraster : HashMap<number, StundenplanZeitraster> = new HashMap();

	private readonly _map_raumID_zu_raum : HashMap<number, StundenplanRaum> = new HashMap();

	private readonly _map_schieneID_zu_schiene : HashMap<number, StundenplanSchiene> = new HashMap();

	private readonly _map_pausenzeitID_zu_pausenzeit : HashMap<number, StundenplanPausenzeit> = new HashMap();

	private readonly _map_aufsichtID_zu_aufsicht : HashMap<number, StundenplanAufsichtsbereich> = new HashMap();

	private readonly _map_kwzID_zu_kwz : HashMap<number, StundenplanKalenderwochenzuordnung> = new HashMap();

	private readonly _map_jahr_kw_zu_kwz : HashMap2D<number, number, StundenplanKalenderwochenzuordnung> = new HashMap2D();

	private readonly _map_kursID_zu_kurs : HashMap<number, StundenplanKurs> = new HashMap();

	private readonly _map_kursID_zu_unterrichte : HashMap<number, List<StundenplanUnterricht>> = new HashMap();


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
		this.initMapZeitraster();
		this.initMapRaum();
		this.initMapSchiene();
		this.initMapPausenzeit();
		this.initMapAufsicht();
		this.initMapKalenderWochenZuordnung();
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

	private initMapZeitraster() : void {
		this._map_zeitrasterID_zu_zeitraster.clear();
		for (const zeit of this._daten.zeitraster) {
			DeveloperNotificationException.ifInvalidID("zeit.id", zeit.id);
			DeveloperNotificationException.ifTrue("zeit.stundenbeginn.isBlank()", JavaString.isBlank(zeit.stundenbeginn));
			DeveloperNotificationException.ifTrue("zeit.stundenende.isBlank()", JavaString.isBlank(zeit.stundenende));
			DeveloperNotificationException.ifTrue("zeit.unterrichtstunde <= 0", zeit.unterrichtstunde <= 0);
			DeveloperNotificationException.ifTrue("(zeit.wochentag < 1) || (zeit.wochentag > 7)", (zeit.wochentag < 1) || (zeit.wochentag > 7));
			DeveloperNotificationException.ifTrue("_map_zeitrasterID_zu_zeitraster.containsKey(zeit.id)", this._map_zeitrasterID_zu_zeitraster.containsKey(zeit.id));
			this._map_zeitrasterID_zu_zeitraster.put(zeit.id, zeit);
		}
	}

	private initMapRaum() : void {
		this._map_raumID_zu_raum.clear();
		for (const raum of this._daten.raeume) {
			DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
			DeveloperNotificationException.ifTrue("raum.groesse < 0", raum.groesse < 0);
			DeveloperNotificationException.ifTrue("raum.kuerzel.isBlank()", JavaString.isBlank(raum.kuerzel));
			DeveloperNotificationException.ifMapContains("_map_raumID_zu_raum", this._map_raumID_zu_raum, raum.id);
			this._map_raumID_zu_raum.put(raum.id, raum);
		}
	}

	private initMapSchiene() : void {
		this._map_schieneID_zu_schiene.clear();
		for (const schiene of this._daten.schienen) {
			DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
			DeveloperNotificationException.ifInvalidID("schiene.idJahrgang", schiene.idJahrgang);
			DeveloperNotificationException.ifTrue("schiene.nummer <= 0", schiene.nummer <= 0);
			DeveloperNotificationException.ifTrue("schiene.bezeichnung.isBlank()", JavaString.isBlank(schiene.bezeichnung));
			DeveloperNotificationException.ifMapContains("_map_schieneID_zu_schiene", this._map_schieneID_zu_schiene, schiene.id);
			this._map_schieneID_zu_schiene.put(schiene.id, schiene);
		}
	}

	private initMapPausenzeit() : void {
		this._map_pausenzeitID_zu_pausenzeit.clear();
		for (const pause of this._daten.pausenzeiten) {
			DeveloperNotificationException.ifInvalidID("pause.id", pause.id);
			DeveloperNotificationException.ifTrue("pause.beginn.isBlank()", JavaString.isBlank(pause.beginn));
			DeveloperNotificationException.ifTrue("pause.ende.isBlank()", JavaString.isBlank(pause.ende));
			DeveloperNotificationException.ifTrue("(pause.wochentag < 1) || (pause.wochentag > 7)", (pause.wochentag < 1) || (pause.wochentag > 7));
			DeveloperNotificationException.ifMapContains("_map_pausenzeitID_zu_pausenzeit", this._map_pausenzeitID_zu_pausenzeit, pause.id);
			this._map_pausenzeitID_zu_pausenzeit.put(pause.id, pause);
		}
	}

	private initMapAufsicht() : void {
		this._map_aufsichtID_zu_aufsicht.clear();
		for (const aufsicht of this._daten.aufsichtsbereiche) {
			DeveloperNotificationException.ifInvalidID("aufsicht.id", aufsicht.id);
			DeveloperNotificationException.ifTrue("aufsicht.kuerzel.isBlank()", JavaString.isBlank(aufsicht.kuerzel));
			DeveloperNotificationException.ifMapContains("_map_aufsichtID_zu_aufsicht", this._map_aufsichtID_zu_aufsicht, aufsicht.id);
			this._map_aufsichtID_zu_aufsicht.put(aufsicht.id, aufsicht);
		}
	}

	private initMapKalenderWochenZuordnung() : void {
		this._map_kwzID_zu_kwz.clear();
		this._map_jahr_kw_zu_kwz.clear();
		for (const kwz of this._daten.kalenderwochenZuordnung) {
			DeveloperNotificationException.ifInvalidID("kw.id", kwz.id);
			DeveloperNotificationException.ifTrue("(kwz.jahr < 2000) || (kwz.jahr > 3000)", (kwz.jahr < 2000) || (kwz.jahr > 3000));
			DeveloperNotificationException.ifTrue("(kwz.kw < 1) || (kwz.kw > 53)", (kwz.kw < 1) || (kwz.kw > 53));
			DeveloperNotificationException.ifTrue("kwz.wochentyp > _daten.wochenTypModell", kwz.wochentyp > this._daten.wochenTypModell);
			DeveloperNotificationException.ifTrue("kwz.wochentyp == 0", kwz.wochentyp === 0);
			this._map_jahr_kw_zu_kwz.put(kwz.jahr, kwz.kw, kwz);
			this._map_kwzID_zu_kwz.put(kwz.id, kwz);
		}
	}

	private initMapKursIDZuKurs() : void {
		if (this._datenUV === null)
			return;
		this._map_kursID_zu_kurs.clear();
		for (const k of this._datenUV.kurse) {
			DeveloperNotificationException.ifMapContains("_map_kursID_zu_kurs", this._map_kursID_zu_kurs, k.id);
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
	 * Liefert die ID des Stundenplans.
	 *
	 * @return die ID des Stundenplans.
	 */
	public getID() : number {
		return this._daten.id;
	}

	/**
	 * Liefert die ID des Schuljahresabschnitts des Stundenplans.
	 *
	 * @return die ID des Schuljahresabschnitts des Stundenplans.
	 */
	public getIDSchuljahresabschnitt() : number {
		return this._daten.idSchuljahresabschnitt;
	}

	/**
	 * Liefert das Datum, ab dem der Stundenplan gültig ist.
	 *
	 * @return das Datum, ab dem der Stundenplan gültig ist.
	 */
	public getGueltigAb() : string {
		return this._daten.gueltigAb;
	}

	/**
	 * Liefert das Datum, bis wann der Stundenplan gültig ist.
	 *
	 * @return das Datum, bis wann der Stundenplan gültig ist.
	 */
	public getGueltigBis() : string {
		return this._daten.gueltigBis;
	}

	/**
	 * Liefert die textuelle Beschreibung des Stundenplans.
	 *
	 * @return die textuelle Beschreibung des Stundenplans.
	 */
	public getBezeichnungStundenplan() : string {
		return this._daten.bezeichnungStundenplan;
	}

	/**
	 * Liefert das Modell für die Wochen des Stundenplans. <br>
	 * 0: Stundenplan gilt jede Woche. <br>
	 * 1: Kein gültiger Wert. <br>
	 * N: Stundenplan wiederholt sich alle N Wochen. <br>
	 *
	 * @return das Modell für die Wochen des Stundenplans.
	 */
	public getWochenTypModell() : number {
		return this._daten.wochenTypModell;
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
		const z : StundenplanKalenderwochenzuordnung | null = this._map_jahr_kw_zu_kwz.getOrNull(jahr, kalenderwoche);
		if (z !== null)
			return z.wochentyp;
		const wochentyp : number = kalenderwoche % this._daten.wochenTypModell;
		return wochentyp === 0 ? this._daten.wochenTypModell : wochentyp;
	}

	/**
	 * Liefert TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" auf "Kalenderwoche" verwendet wird.
	 *
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" auf "Kalenderwoche" verwendet wird.
	 */
	public getWochentypUsesMapping(jahr : number, kalenderwoche : number) : boolean {
		DeveloperNotificationException.ifTrue("(jahr < 2000) || (jahr > 3000)", (jahr < 2000) || (jahr > 3000));
		DeveloperNotificationException.ifTrue("(kalenderwoche < 1) || (kalenderwoche > 53)", (kalenderwoche < 1) || (kalenderwoche > 53));
		const z : StundenplanKalenderwochenzuordnung | null = this._map_jahr_kw_zu_kwz.getOrNull(jahr, kalenderwoche);
		return (this._daten.wochenTypModell >= 2) && (z !== null);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses mit einem bestimmten Wochentyp.
	 *
	 * @param kursID    Die ID des Kurses.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public getUnterrichtDesKursesByWochentyp(kursID : number, wochentyp : number) : List<StundenplanUnterricht> {
		DeveloperNotificationException.ifTrue("wochentyp > _daten.wochenTypModell", wochentyp > this._daten.wochenTypModell);
		const list : List<StundenplanUnterricht> = DeveloperNotificationException.ifNull("_map_kursID_zu_unterrichte.get(kursID)==NULL", this._map_kursID_zu_unterrichte.get(kursID));
		const result : ArrayList<StundenplanUnterricht> = new ArrayList();
		for (const u of list)
			if ((u.wochentyp === 0) || (u.wochentyp === wochentyp))
				result.add(u);
		return result;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 *
	 * @param kursID        Die ID des Kurses.
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public getUnterrichtDesKursesByKW(kursID : number, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht> {
		const wochentyp : number = this.getWochentypOrDefault(jahr, kalenderwoche);
		return this.getUnterrichtDesKursesByWochentyp(kursID, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 *
	 * @param kursIDs   Die IDs aller Kurse.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 */
	public getUnterrichtDerKurseByWochentyp(kursIDs : Array<number>, wochentyp : number) : List<StundenplanUnterricht> {
		const result : ArrayList<StundenplanUnterricht> = new ArrayList();
		for (const kursID of kursIDs)
			result.addAll(this.getUnterrichtDesKursesByWochentyp(kursID, wochentyp));
		return result;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge in einer bestimmten Kalenderwoche.
	 *
	 * @param kursIDs       Die IDs aller Kurse.
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge in einer bestimmten Kalenderwoche.
	 */
	public getUnterrichtDerKurseByKW(kursIDs : Array<number>, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht> {
		const wochentyp : number = this.getWochentypOrDefault(jahr, kalenderwoche);
		return this.getUnterrichtDerKurseByWochentyp(kursIDs, wochentyp);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplan.StundenplanManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanManager(obj : unknown) : StundenplanManager {
	return obj as StundenplanManager;
}
