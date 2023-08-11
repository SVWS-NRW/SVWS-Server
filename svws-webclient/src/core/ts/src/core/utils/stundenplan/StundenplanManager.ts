import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { StundenplanUnterrichtsverteilung, cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterrichtsverteilung } from '../../../core/data/stundenplan/StundenplanUnterrichtsverteilung';
import { HashMap } from '../../../java/util/HashMap';
import { StundenplanKlasse } from '../../../core/data/stundenplan/StundenplanKlasse';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanKurs } from '../../../core/data/stundenplan/StundenplanKurs';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { StundenplanJahrgang } from '../../../core/data/stundenplan/StundenplanJahrgang';
import { DateUtils } from '../../../core/utils/DateUtils';
import type { Comparator } from '../../../java/util/Comparator';
import { StundenplanSchueler } from '../../../core/data/stundenplan/StundenplanSchueler';
import { StundenplanLehrer } from '../../../core/data/stundenplan/StundenplanLehrer';
import { StringUtils } from '../../../core/utils/StringUtils';
import { StundenplanUnterricht, cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterricht } from '../../../core/data/stundenplan/StundenplanUnterricht';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { StundenplanKalenderwochenzuordnung } from '../../../core/data/stundenplan/StundenplanKalenderwochenzuordnung';
import { Stundenplan, cast_de_svws_nrw_core_data_stundenplan_Stundenplan } from '../../../core/data/stundenplan/Stundenplan';
import { HashSet } from '../../../java/util/HashSet';
import { AVLSet } from '../../../core/adt/set/AVLSet';
import { StundenplanPausenaufsicht } from '../../../core/data/stundenplan/StundenplanPausenaufsicht';
import { CollectionUtils } from '../../../core/utils/CollectionUtils';
import { MapUtils } from '../../../core/utils/MapUtils';
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { StundenplanPausenzeit } from '../../../core/data/stundenplan/StundenplanPausenzeit';
import { Map2DUtils } from '../../../core/utils/Map2DUtils';
import { StundenplanAufsichtsbereich } from '../../../core/data/stundenplan/StundenplanAufsichtsbereich';
import { StundenplanRaum } from '../../../core/data/stundenplan/StundenplanRaum';
import { BlockungsUtils } from '../../../core/utils/BlockungsUtils';
import { StundenplanSchiene } from '../../../core/data/stundenplan/StundenplanSchiene';
import { StundenplanFach } from '../../../core/data/stundenplan/StundenplanFach';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Wochentag } from '../../../core/types/Wochentag';
import { StundenplanKomplett, cast_de_svws_nrw_core_data_stundenplan_StundenplanKomplett } from '../../../core/data/stundenplan/StundenplanKomplett';

export class StundenplanManager extends JavaObject {

	private static readonly MINUTEN_INF_POS : number = 24 * 60 + 1;

	private static readonly MINUTEN_INF_NEG : number = -1;

	private static readonly WOCHENTAG_INF_POS : number = Wochentag.SONNTAG.id + 1;

	private static readonly WOCHENTAG_INF_NEG : number = Wochentag.MONTAG.id - 1;

	private static readonly STUNDE_INF_POS : number = -1;

	private static readonly STUNDE_INF_NEG : number = -1;

	private static readonly _compAufsichtsbereich : Comparator<StundenplanAufsichtsbereich> = { compare : (a: StundenplanAufsichtsbereich, b: StundenplanAufsichtsbereich) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _list_aufsichtsbereiche : List<StundenplanAufsichtsbereich> = new ArrayList();

	private readonly _map_idAufsichtsbereich_zu_aufsichtsbereich : HashMap<number, StundenplanAufsichtsbereich> = new HashMap();

	private static readonly _compFach : Comparator<StundenplanFach> = { compare : (a: StundenplanFach, b: StundenplanFach) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _list_faecher : List<StundenplanFach> = new ArrayList();

	private readonly _map_idFach_zu_fach : HashMap<number, StundenplanFach> = new HashMap();

	private readonly _list_jahrgaenge : List<StundenplanJahrgang> = new ArrayList();

	private readonly _map_idJahrgang_zu_jahrgang : HashMap<number, StundenplanJahrgang> = new HashMap();

	private static readonly _compKWZ : Comparator<StundenplanKalenderwochenzuordnung> = { compare : (a: StundenplanKalenderwochenzuordnung, b: StundenplanKalenderwochenzuordnung) => {
		if (a.jahr < b.jahr)
			return -1;
		if (a.jahr > b.jahr)
			return +1;
		if (a.kw < b.kw)
			return -1;
		if (a.kw > b.kw)
			return +1;
		if (a.wochentyp < b.wochentyp)
			return -1;
		if (a.wochentyp > b.wochentyp)
			return +1;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _list_kwz : List<StundenplanKalenderwochenzuordnung> = new ArrayList();

	private readonly _map_idKWZ_zu_kwz : HashMap<number, StundenplanKalenderwochenzuordnung> = new HashMap();

	private readonly _map2d_jahr_kw_zu_kwz : HashMap2D<number, number, StundenplanKalenderwochenzuordnung> = new HashMap2D();

	private readonly _list_klassen : List<StundenplanKlasse> = new ArrayList();

	private readonly _map_idKlasse_zu_klasse : HashMap<number, StundenplanKlasse> = new HashMap();

	private readonly _list_kurse : List<StundenplanKurs> = new ArrayList();

	private readonly _map_idKurs_zu_kurs : HashMap<number, StundenplanKurs> = new HashMap();

	private readonly _list_lehrer : List<StundenplanLehrer> = new ArrayList();

	private readonly _map_idLehrer_zu_lehrer : HashMap<number, StundenplanLehrer> = new HashMap();

	private readonly _list_pausenaufsichten : List<StundenplanPausenaufsicht> = new ArrayList();

	private readonly _map_pausenaufsichtID_zu_pausenaufsicht : HashMap<number, StundenplanPausenaufsicht> = new HashMap();

	private readonly _compPausenaufsicht : Comparator<StundenplanPausenaufsicht> = { compare : (a: StundenplanPausenaufsicht, b: StundenplanPausenaufsicht) => JavaLong.compare(a.id, b.id) };

	private static readonly _compPausenzeit : Comparator<StundenplanPausenzeit> = { compare : (a: StundenplanPausenzeit, b: StundenplanPausenzeit) => {
		if (a.wochentag < b.wochentag)
			return -1;
		if (a.wochentag > b.wochentag)
			return +1;
		const beginnA : number = a.beginn === null ? -1 : a.beginn;
		const beginnB : number = b.beginn === null ? -1 : b.beginn;
		if (beginnA < beginnB)
			return -1;
		if (beginnA > beginnB)
			return +1;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _list_pausenzeiten : List<StundenplanPausenzeit> = new ArrayList();

	private readonly _map_idPausenzeit_zu_pausenzeit : HashMap<number, StundenplanPausenzeit> = new HashMap();

	private _pausenzeitMinutenMin : number = 480;

	private _pausenzeitMinutenMax : number = 480;

	private _pausenzeitUndZeitrasterMinutenMin : number = 480;

	private _pausenzeitUndZeitrasterMinutenMax : number = 480;

	private readonly _pausenzeitMapByWochentag : HashMap<number, List<StundenplanPausenzeit>> = new HashMap();

	private static readonly _compRaum : Comparator<StundenplanRaum> = { compare : (a: StundenplanRaum, b: StundenplanRaum) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _list_raeume : List<StundenplanRaum> = new ArrayList();

	private readonly _map_idRaum_zu_raum : HashMap<number, StundenplanRaum> = new HashMap();

	private readonly _list_schienen : List<StundenplanSchiene> = new ArrayList();

	private readonly _map_idSchiene_zu_schiene : HashMap<number, StundenplanSchiene> = new HashMap();

	private readonly _list_schueler : List<StundenplanSchueler> = new ArrayList();

	private readonly _map_schuelerID_zu_schueler : HashMap<number, StundenplanSchueler> = new HashMap();

	private readonly _list_unterricht : List<StundenplanUnterricht> = new ArrayList();

	private readonly _map_idUnterricht_zu_unterricht : HashMap<number, StundenplanUnterricht> = new HashMap();

	private readonly _map_idKurs_zu_unterrichtmenge : HashMap<number, List<StundenplanUnterricht>> = new HashMap();

	private readonly _map2d_idZeitraster_wochentyp_zu_unterrichtmenge : HashMap2D<number, number, List<StundenplanUnterricht>> = new HashMap2D();

	private readonly _map_idZeitraster_zu_unterrichtmenge : HashMap<number, List<StundenplanUnterricht>> = new HashMap();

	private readonly _map_idUnterricht_zu_lehrermenge : HashMap<number, List<StundenplanLehrer>> = new HashMap();

	private _unterrichtHatMultiWochen : boolean = false;

	private static readonly _compZeitraster : Comparator<StundenplanZeitraster> = { compare : (a: StundenplanZeitraster, b: StundenplanZeitraster) => {
		if (a.wochentag < b.wochentag)
			return -1;
		if (a.wochentag > b.wochentag)
			return +1;
		if (a.unterrichtstunde < b.unterrichtstunde)
			return -1;
		if (a.unterrichtstunde > b.unterrichtstunde)
			return +1;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _list_zeitraster : List<StundenplanZeitraster> = new ArrayList();

	private readonly _map_idZeitraster_zu_zeitraster : HashMap<number, StundenplanZeitraster> = new HashMap();

	private readonly _map2d_wochentag_stunde_zu_zeitraster : HashMap2D<number, number, StundenplanZeitraster> = new HashMap2D();

	private readonly _map_wochentag_zu_zeitrastermenge : HashMap<number, List<StundenplanZeitraster>> = new HashMap();

	private readonly _map_stunde_zu_zeitrastermenge : HashMap<number, List<StundenplanZeitraster>> = new HashMap();

	private _zeitrasterStundenRange : Array<number> = [1];

	private readonly _zeitrasterMinutenMinByStunde : HashMap<number, number | null> = new HashMap();

	private readonly _zeitrasterMinutenMaxByStunde : HashMap<number, number | null> = new HashMap();

	private _zeitrasterWochentageAlsEnumRange : Array<Wochentag> = [Wochentag.MONTAG];

	private _zeitrasterWochentagMin : number = Wochentag.MONTAG.id;

	private _zeitrasterWochentagMax : number = Wochentag.MONTAG.id;

	private _zeitrasterStundeMin : number = 1;

	private _zeitrasterStundeMax : number = 1;

	private _zeitrasterMinutenMin : number = 480;

	private _zeitrasterMinutenMax : number = 480;

	private readonly stundenplanID : number;

	private readonly stundenplanWochenTypModell : number;

	private readonly stundenplanSchuljahresAbschnittID : number;

	private readonly stundenplanGueltigAb : string;

	private readonly stundenplanGueltigBis : string;

	private readonly stundenplanBezeichnung : string;


	/**
	 * Der {@link StundenplanManager} benötigt vier data-Objekte und baut damit eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param daten                 liefert die Grund-Daten des Stundenplanes.
	 * @param unterrichte           liefert die Informationen zu allen {@link StundenplanUnterricht} im Stundenplan. Die Liste darf leer sein.
	 * @param pausenaufsichten      liefert die Informationen zu allen {@link StundenplanPausenaufsicht} im Stundenplan. Die Liste darf leer sein.
	 * @param unterrichtsverteilung liefert die Informationen zu der Unterrichtsverteilung eines Stundenplans. Darf NULL sein.
	 */
	public constructor(daten : Stundenplan, unterrichte : List<StundenplanUnterricht>, pausenaufsichten : List<StundenplanPausenaufsicht>, unterrichtsverteilung : StundenplanUnterrichtsverteilung | null);

	/**
	 * Dieser Manager baut mit Hilfe des {@link StundenplanKomplett}-Objektes eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param stundenplanKomplett  Beinhaltet alle relevanten Daten für einen Stundenplan.
	 */
	public constructor(stundenplanKomplett : StundenplanKomplett);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : Stundenplan | StundenplanKomplett, __param1? : List<StundenplanUnterricht>, __param2? : List<StundenplanPausenaufsicht>, __param3? : StundenplanUnterrichtsverteilung | null) {
		super();
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.Stundenplan')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param1 === null)) && ((typeof __param2 !== "undefined") && ((__param2 instanceof JavaObject) && ((__param2 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param2 === null)) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && ((__param3 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung'))) || (__param3 === null))) {
			const daten : Stundenplan = cast_de_svws_nrw_core_data_stundenplan_Stundenplan(__param0);
			const unterrichte : List<StundenplanUnterricht> = cast_java_util_List(__param1);
			const pausenaufsichten : List<StundenplanPausenaufsicht> = cast_java_util_List(__param2);
			const unterrichtsverteilung : StundenplanUnterrichtsverteilung | null = cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterrichtsverteilung(__param3);
			this.stundenplanID = daten.id;
			this.stundenplanWochenTypModell = daten.wochenTypModell;
			this.stundenplanSchuljahresAbschnittID = daten.idSchuljahresabschnitt;
			this.stundenplanGueltigAb = daten.gueltigAb;
			this.stundenplanGueltigBis = daten.gueltigBis;
			this.stundenplanBezeichnung = daten.bezeichnungStundenplan;
			let uv : StundenplanUnterrichtsverteilung | null = unterrichtsverteilung;
			if (uv === null) {
				uv = new StundenplanUnterrichtsverteilung();
				uv.id = this.stundenplanID;
			}
			DeveloperNotificationException.ifTrue("Stundenplan.id != StundenplanUnterrichtsverteilung.id", daten.id !== uv.id);
			this.initAll(daten.kalenderwochenZuordnung, uv.faecher, daten.jahrgaenge, daten.zeitraster, daten.raeume, daten.pausenzeiten, daten.aufsichtsbereiche, uv.lehrer, uv.schueler, daten.schienen, uv.klassen, pausenaufsichten, uv.kurse, unterrichte);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanKomplett')))) && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined")) {
			const stundenplanKomplett : StundenplanKomplett = cast_de_svws_nrw_core_data_stundenplan_StundenplanKomplett(__param0);
			this.stundenplanID = stundenplanKomplett.daten.id;
			this.stundenplanWochenTypModell = stundenplanKomplett.daten.wochenTypModell;
			this.stundenplanSchuljahresAbschnittID = stundenplanKomplett.daten.idSchuljahresabschnitt;
			this.stundenplanGueltigAb = stundenplanKomplett.daten.gueltigAb;
			this.stundenplanGueltigBis = stundenplanKomplett.daten.gueltigBis;
			this.stundenplanBezeichnung = stundenplanKomplett.daten.bezeichnungStundenplan;
			DeveloperNotificationException.ifTrue("Stundenplan.id != StundenplanUnterrichtsverteilung.id", stundenplanKomplett.daten.id !== stundenplanKomplett.unterrichtsverteilung.id);
			this.initAll(stundenplanKomplett.daten.kalenderwochenZuordnung, stundenplanKomplett.unterrichtsverteilung.faecher, stundenplanKomplett.daten.jahrgaenge, stundenplanKomplett.daten.zeitraster, stundenplanKomplett.daten.raeume, stundenplanKomplett.daten.pausenzeiten, stundenplanKomplett.daten.aufsichtsbereiche, stundenplanKomplett.unterrichtsverteilung.lehrer, stundenplanKomplett.unterrichtsverteilung.schueler, stundenplanKomplett.daten.schienen, stundenplanKomplett.unterrichtsverteilung.klassen, stundenplanKomplett.pausenaufsichten, stundenplanKomplett.unterrichtsverteilung.kurse, stundenplanKomplett.unterrichte);
		} else throw new Error('invalid method overload');
	}

	private initAll(listKWZ : List<StundenplanKalenderwochenzuordnung>, listFach : List<StundenplanFach>, listJahrgang : List<StundenplanJahrgang>, listZeitraster : List<StundenplanZeitraster>, listRaum : List<StundenplanRaum>, listPausenzeit : List<StundenplanPausenzeit>, listAufsichtsbereich : List<StundenplanAufsichtsbereich>, listLehrer : List<StundenplanLehrer>, listSchueler : List<StundenplanSchueler>, listSchiene : List<StundenplanSchiene>, listKlasse : List<StundenplanKlasse>, listPausenaufsicht : List<StundenplanPausenaufsicht>, listKurs : List<StundenplanKurs>, listUnterricht : List<StundenplanUnterricht>) : void {
		DeveloperNotificationException.ifTrue("stundenplanWochenTypModell < 0", this.stundenplanWochenTypModell < 0);
		DeveloperNotificationException.ifTrue("stundenplanWochenTypModell == 1", this.stundenplanWochenTypModell === 1);
		this.kalenderwochenzuordnungAddAll(listKWZ);
		this.kalenderwochenzuordnungErzeugePseudoMenge();
		this.fachAddAll(listFach);
		this.jahrgangAddAll(listJahrgang);
		this.zeitrasterAddAll(listZeitraster);
		this.raumAddAll(listRaum);
		this.pausenzeitAddAll(listPausenzeit);
		this.aufsichtsbereichAddAll(listAufsichtsbereich);
		this.klasseAddAll(listKlasse);
		this.lehrerAddAll(listLehrer);
		this.schuelerAddAll(listSchueler);
		this.schieneAddAll(listSchiene);
		this.pausenaufsichtAddAll(listPausenaufsicht);
		this.kursAddAll(listKurs);
		this.unterrichtAddAll(listUnterricht);
	}

	private aufsichtsbereichAddOhneUpdate(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		DeveloperNotificationException.ifInvalidID("aufsicht.id", aufsichtsbereich.id);
		DeveloperNotificationException.ifStringIsBlank("aufsicht.kuerzel", aufsichtsbereich.kuerzel);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idAufsichtsbereich_zu_aufsichtsbereich, aufsichtsbereich.id, aufsichtsbereich);
		DeveloperNotificationException.ifListAddsDuplicate("_list_aufsichtsbereiche", this._list_aufsichtsbereiche, aufsichtsbereich);
	}

	/**
	 * Fügt ein {@link StundenplanAufsichtsbereich}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanAufsichtsbereich|), da aufsichtsbereichUpdate() aufgerufen wird.
	 *
	 * @param aufsichtsbereich  Das {@link StundenplanAufsichtsbereich}-Objekt, welches hinzugefügt werden soll.
	 */
	public aufsichtsbereichAdd(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		this.aufsichtsbereichAddOhneUpdate(aufsichtsbereich);
		this.aufsichtsbereichUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanAufsichtsbereich}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanAufsichtsbereich|), da aufsichtsbereichUpdate() aufgerufen wird.
	 *
	 * @param listAufsichtsbereich  Die Menge der {@link StundenplanAufsichtsbereich}-Objekte, welche hinzugefügt werden soll.
	 */
	public aufsichtsbereichAddAll(listAufsichtsbereich : List<StundenplanAufsichtsbereich>) : void {
		for (const aufsichtsbereich of listAufsichtsbereich)
			this.aufsichtsbereichAddOhneUpdate(aufsichtsbereich);
		this.aufsichtsbereichUpdate();
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 *
	 * @param idAufsichtsbereich  Die Datenbank-ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 */
	public aufsichtsbereichGetByIdOrException(idAufsichtsbereich : number) : StundenplanAufsichtsbereich {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idAufsichtsbereich_zu_aufsichtsbereich, idAufsichtsbereich);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public aufsichtsbereichGetMengeAsList() : List<StundenplanAufsichtsbereich> {
		return this._list_aufsichtsbereiche;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanAufsichtsbereich}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param aufsichtsbereich  Das neue {@link StundenplanAufsichtsbereich}-Objekt, welches das alte Objekt ersetzt.
	 */
	public aufsichtsbereichPatch(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		this.aufsichtsbereichRemoveOhneUpdateById(aufsichtsbereich.id);
		this.aufsichtsbereichAddOhneUpdate(aufsichtsbereich);
		this.aufsichtsbereichUpdate();
	}

	private aufsichtsbereichRemoveOhneUpdateById(idAufsichtsbereich : number) : void {
		const a : StundenplanAufsichtsbereich = DeveloperNotificationException.ifMapGetIsNull(this._map_idAufsichtsbereich_zu_aufsichtsbereich, idAufsichtsbereich);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idAufsichtsbereich_zu_aufsichtsbereich, a.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_aufsichtsbereiche", this._list_aufsichtsbereiche, a);
	}

	/**
	 * Entfernt ein {@link StundenplanAufsichtsbereich}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanAufsichtsbereich|), da aufsichtsbereichUpdate() aufgerufen wird.
	 *
	 * @param idAufsichtsbereich  Die Datenbank-ID des {@link StundenplanAufsichtsbereich}-Objekts, welches entfernt werden soll.
	 */
	public aufsichtsbereichRemoveById(idAufsichtsbereich : number) : void {
		this.aufsichtsbereichRemoveOhneUpdateById(idAufsichtsbereich);
		this.aufsichtsbereichUpdate();
	}

	private aufsichtsbereichUpdate() : void {
		const setAufsichtKuerzel : HashSet<string> = new HashSet();
		for (const aufsicht of this._list_aufsichtsbereiche)
			DeveloperNotificationException.ifSetAddsDuplicate("setAufsichtKuerzel", setAufsichtKuerzel, aufsicht.kuerzel);
		this._list_aufsichtsbereiche.sort(StundenplanManager._compAufsichtsbereich);
	}

	private fachAddOhneUpdate(fach : StundenplanFach) : void {
		DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
		DeveloperNotificationException.ifStringIsBlank("fach.bezeichnung", fach.bezeichnung);
		DeveloperNotificationException.ifStringIsBlank("fach.kuerzel", fach.kuerzel);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idFach_zu_fach, fach.id, fach);
		DeveloperNotificationException.ifListAddsDuplicate("_list_faecher", this._list_faecher, fach);
	}

	/**
	 * Fügt ein {@link StundenplanFach}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanFach|), da fachUpdate() aufgerufen wird.
	 *
	 * @param fach  Das {@link StundenplanFach}-Objekt, welches hinzugefügt werden soll.
	 */
	public fachAdd(fach : StundenplanFach) : void {
		this.fachAddOhneUpdate(fach);
		this.fachUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanFach}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanFach|), da fachUpdate() aufgerufen wird.
	 *
	 * @param listFach  Die Menge der {@link StundenplanFach}-Objekte, welche hinzugefügt werden soll.
	 */
	public fachAddAll(listFach : List<StundenplanFach>) : void {
		for (const fach of listFach)
			this.fachAddOhneUpdate(fach);
		this.fachUpdate();
	}

	/**
	 * Liefert das Fach mit der übergebenen ID.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return  das Fach mit der übergebenen ID.
	 */
	public fachGetByIdOrException(idFach : number) : StundenplanFach {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idFach_zu_fach, idFach);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanFach}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanFach}-Objekte.
	 */
	public fachGetMengeAsList() : List<StundenplanFach> {
		return this._list_faecher;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanFach}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param fach  Das neue {@link StundenplanFach}-Objekt, welches das alte Objekt ersetzt.
	 */
	public fachPatch(fach : StundenplanFach) : void {
		this.fachRemoveOhneUpdateById(fach.id);
		this.fachAddOhneUpdate(fach);
		this.fachUpdate();
	}

	private fachRemoveOhneUpdateById(idFach : number) : void {
		const f : StundenplanFach = DeveloperNotificationException.ifMapGetIsNull(this._map_idFach_zu_fach, idFach);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idFach_zu_fach, f.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_faecher", this._list_faecher, f);
	}

	/**
	 * Entfernt ein {@link StundenplanFach}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanFach|), da fachUpdate() aufgerufen wird.
	 *
	 * @param idFach  Die Datenbank-ID des {@link StundenplanFach}-Objekts, welches entfernt werden soll.
	 */
	public fachRemoveById(idFach : number) : void {
		this.fachRemoveOhneUpdateById(idFach);
		this.fachUpdate();
	}

	private fachUpdate() : void {
		const setFachKuerzel : HashSet<string> = new HashSet();
		for (const fach of this._list_faecher)
			DeveloperNotificationException.ifSetAddsDuplicate("setFachKuerzel", setFachKuerzel, fach.kuerzel);
		this._list_faecher.sort(StundenplanManager._compFach);
	}

	private jahrgangAddOhneUpdate(jahrgang : StundenplanJahrgang) : void {
		DeveloperNotificationException.ifInvalidID("jahrgang.id", jahrgang.id);
		DeveloperNotificationException.ifStringIsBlank("jahrgang.bezeichnung", jahrgang.bezeichnung);
		DeveloperNotificationException.ifStringIsBlank("jahrgang.kuerzel", jahrgang.kuerzel);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idJahrgang_zu_jahrgang, jahrgang.id, jahrgang);
		DeveloperNotificationException.ifListAddsDuplicate("_list_jahrgaenge", this._list_jahrgaenge, jahrgang);
	}

	/**
	 * Fügt ein {@link StundenplanJahrgang}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanJahrgang|), da jahrgangUpdate() aufgerufen wird.
	 *
	 * @param jahrgang  Das {@link StundenplanJahrgang}-Objekt, welches hinzugefügt werden soll.
	 */
	public jahrgangAdd(jahrgang : StundenplanJahrgang) : void {
		this.jahrgangAddOhneUpdate(jahrgang);
		this.jahrgangUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanJahrgang}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanJahrgang|), da jahrgangUpdate() aufgerufen wird.
	 *
	 * @param listJahrgang  Die Menge der {@link StundenplanJahrgang}-Objekte, welche hinzugefügt werden soll.
	 */
	public jahrgangAddAll(listJahrgang : List<StundenplanJahrgang>) : void {
		for (const jahrgang of listJahrgang)
			this.jahrgangAddOhneUpdate(jahrgang);
		this.jahrgangUpdate();
	}

	/**
	 * Liefert das {@link StundenplanJahrgang}-Objekt mit der übergebenen ID.
	 *
	 * @param idJahrgang  Die Datenbank-ID des {@link StundenplanJahrgang}-Objekts.
	 *
	 * @return das {@link StundenplanJahrgang}-Objekt mit der übergebenen ID.
	 */
	public jahrgangGetByIdOrException(idJahrgang : number) : StundenplanJahrgang {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idJahrgang_zu_jahrgang, idJahrgang);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanJahrgang}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanJahrgang}-Objekte.
	 */
	public jahrgangGetMengeAsList() : List<StundenplanJahrgang> {
		return this._list_jahrgaenge;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanJahrgang}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param jahrgang  Das neue {@link StundenplanJahrgang}-Objekt, welches das alte Objekt ersetzt.
	 */
	public jahrgangPatch(jahrgang : StundenplanJahrgang) : void {
		this.jahrgangRemoveOhneUpdateById(jahrgang.id);
		this.jahrgangAddOhneUpdate(jahrgang);
		this.jahrgangUpdate();
	}

	private jahrgangRemoveOhneUpdateById(idJahrgang : number) : void {
		const j : StundenplanJahrgang = DeveloperNotificationException.ifMapGetIsNull(this._map_idJahrgang_zu_jahrgang, idJahrgang);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idJahrgang_zu_jahrgang, j.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_jahrgaenge", this._list_jahrgaenge, j);
	}

	/**
	 * Entfernt ein {@link StundenplanJahrgang}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanJahrgang|), da jahrgangUpdate() aufgerufen wird.
	 *
	 * @param idJahrgang  Die Datenbank-ID des {@link StundenplanJahrgang}-Objekts, welches entfernt werden soll.
	 */
	public jahrgangRemoveById(idJahrgang : number) : void {
		this.jahrgangRemoveOhneUpdateById(idJahrgang);
		this.jahrgangUpdate();
	}

	private jahrgangUpdate() : void {
		const setJahrgangKuerzel : HashSet<string> = new HashSet();
		for (const jahrgang of this._list_jahrgaenge)
			DeveloperNotificationException.ifSetAddsDuplicate("setJahrgangKuerzel", setJahrgangKuerzel, jahrgang.kuerzel);
	}

	private kalenderwochenzuordnungErzeugePseudoMenge() : void {
		const infoVon : Array<number> = DateUtils.extractFromDateISO8601(this.stundenplanGueltigAb);
		const infoBis : Array<number> = DateUtils.extractFromDateISO8601(this.stundenplanGueltigBis);
		const jahrVon : number = infoVon[6];
		const jahrBis : number = infoBis[6];
		const kwVon : number = infoVon[5];
		const kwBis : number = infoBis[5];
		DeveloperNotificationException.ifTrue("jahrVon > jahrBis", jahrVon > jahrBis);
		DeveloperNotificationException.ifTrue("(jahrVon == jahrBis) && (kwVon > kwBis)", (jahrVon === jahrBis) && (kwVon > kwBis));
		const listNeueKWZ : List<StundenplanKalenderwochenzuordnung> = new ArrayList();
		for (let jahr : number = jahrVon; jahr <= jahrBis; jahr++) {
			const von : number = (jahr === jahrVon) ? kwVon : 1;
			const bis : number = (jahr === jahrBis) ? kwBis : DateUtils.gibKalenderwochenOfJahr(jahr);
			for (let kw : number = von; kw <= bis; kw++)
				if (!this._map2d_jahr_kw_zu_kwz.contains(jahr, kw)) {
					const kwz : StundenplanKalenderwochenzuordnung = new StundenplanKalenderwochenzuordnung();
					kwz.id = -1;
					kwz.jahr = jahr;
					kwz.kw = kw;
					kwz.wochentyp = this.kalenderwochenzuordnungGetWochentypOrDefault(jahr, kw);
					listNeueKWZ.add(kwz);
				}
		}
		this.kalenderwochenzuordnungAddAll(listNeueKWZ);
	}

	private kalenderwochenzuordnungAddOhneUpdate(kwz : StundenplanKalenderwochenzuordnung) : void {
		DeveloperNotificationException.ifTrue("kwz.id < -1", kwz.id < -1);
		DeveloperNotificationException.ifTrue("(kwz.jahr < DateUtils.MIN_GUELTIGES_JAHR) || (kwz.jahr > DateUtils.MAX_GUELTIGES_JAHR)", (kwz.jahr < DateUtils.MIN_GUELTIGES_JAHR) || (kwz.jahr > DateUtils.MAX_GUELTIGES_JAHR));
		DeveloperNotificationException.ifTrue("(kwz.kw < 1) || (kwz.kw > DateUtils.gibKalenderwochenOfJahr(kwz.jahr))", (kwz.kw < 1) || (kwz.kw > DateUtils.gibKalenderwochenOfJahr(kwz.jahr)));
		DeveloperNotificationException.ifTrue("kwz.wochentyp > stundenplanWochenTypModell", kwz.wochentyp > this.stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("kwz.wochentyp < 0", kwz.wochentyp < 0);
		if (kwz.id !== -1)
			DeveloperNotificationException.ifMapPutOverwrites(this._map_idKWZ_zu_kwz, kwz.id, kwz);
		DeveloperNotificationException.ifMap2DPutOverwrites(this._map2d_jahr_kw_zu_kwz, kwz.jahr, kwz.kw, kwz);
		this._list_kwz.add(kwz);
	}

	/**
	 * Fügt ein {@link StundenplanKalenderwochenzuordnung}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanKalenderwochenzuordnung| * log ), da kalenderwochenzuordnungUpdate() aufgerufen wird.
	 *
	 * @param kwz  Das {@link StundenplanKalenderwochenzuordnung}-Objekt, welches hinzugefügt werden soll.
	 */
	public kalenderwochenzuordnungAdd(kwz : StundenplanKalenderwochenzuordnung) : void {
		this.kalenderwochenzuordnungAddOhneUpdate(kwz);
		this.kalenderwochenzuordnungUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanKalenderwochenzuordnung}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanKalenderwochenzuordnung| * log ), da kalenderwochenzuordnungUpdate() aufgerufen wird.
	 *
	 * @param listKWZ  Die Menge der {@link StundenplanKalenderwochenzuordnung}-Objekte, welche hinzugefügt werden soll.
	 */
	public kalenderwochenzuordnungAddAll(listKWZ : List<StundenplanKalenderwochenzuordnung>) : void {
		for (const kwz of listKWZ)
			this.kalenderwochenzuordnungAddOhneUpdate(kwz);
		this.kalenderwochenzuordnungUpdate();
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @param idKWZ Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 */
	public kalenderwochenzuordnungGetByIdOrException(idKWZ : number) : StundenplanKalenderwochenzuordnung {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idKWZ_zu_kwz, idKWZ);
	}

	/**
	 * Liefert das dem Jahr und der Kalenderwoche zugeordnete {@link StundenplanKalenderwochenzuordnung}-Objekt der Auswahl-Menge.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 *
	 * @param jahr           Das Jahr der Kalenderwoche.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche.
	 *
	 * @return das dem Jahr und der Kalenderwoche zugeordnete {@link StundenplanKalenderwochenzuordnung}-Objekt der Auswahl-Menge.
	 */
	public kalenderwochenzuordnungGetByJahrAndKWOrException(jahr : number, kalenderwoche : number) : StundenplanKalenderwochenzuordnung {
		return DeveloperNotificationException.ifMap2DGetIsNull(this._map2d_jahr_kw_zu_kwz, jahr, kalenderwoche);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 *
	 * @return eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public kalenderwochenzuordnungGetMengeAsList() : List<StundenplanKalenderwochenzuordnung> {
		return this._list_kwz;
	}

	/**
	 * Liefert eine String-Darstellung der Kalenderwoche des {@link StundenplanKalenderwochenzuordnung}-Objekts.
	 * <br>Beispiel: Jahr 2023, KW  5 --> "30.01.2023 - 05.02.2023 (KW 2023.5)"
	 * <br>Beispiel: Jahr 2025, KW  1 --> "30.12.2024 - 05.01.2025 (KW 2025.1)"
	 * <br>Beispiel: Jahr 2026, KW 53 --> "28.12.2026 - 03.01.2027 (KW 2026.53)"
	 * <br>Laufzeit: O(1)
	 *
	 * @param kwz  Das {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @return eine String-Darstellung der Kalenderwoche des {@link StundenplanKalenderwochenzuordnung}-Objekts.
	 */
	public kalenderwochenzuordnungGetWocheAsString(kwz : StundenplanKalenderwochenzuordnung) : string {
		const sMo : string = DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(kwz.jahr, kwz.kw);
		const sSo : string = DateUtils.gibDatumDesSonntagsOfJahrAndKalenderwoche(kwz.jahr, kwz.kw);
		const sMoGer : string = DateUtils.gibDatumGermanFormat(sMo);
		const sSoGer : string = DateUtils.gibDatumGermanFormat(sSo);
		const sJahrKW : string = "(KW " + kwz.jahr + "." + kwz.kw + ")";
		return sMoGer! + " - " + sSoGer! + " " + sJahrKW!;
	}

	/**
	 * Liefert den zugeordneten Wochentyp, oder den Default-Wochentyp, welcher sich aus der Kalenderwoche berechnet.
	 *
	 * @param jahr           Das Jahr der Kalenderwoche. Es muss zwischen {@link DateUtils#MIN_GUELTIGES_JAHR} und {@link DateUtils#MAX_GUELTIGES_JAHR} liegen.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche. Es muss zwischen 1 und {@link DateUtils#gibKalenderwochenOfJahr(int)} liegen.
	 *
	 * @return den zugeordneten Wochentyp, oder den Default-Wochentyp, welcher sich aus der Kalenderwoche berechnet.
	 */
	public kalenderwochenzuordnungGetWochentypOrDefault(jahr : number, kalenderwoche : number) : number {
		DeveloperNotificationException.ifSmaller("jahr", jahr, DateUtils.MIN_GUELTIGES_JAHR);
		DeveloperNotificationException.ifGreater("jahr", jahr, DateUtils.MAX_GUELTIGES_JAHR);
		DeveloperNotificationException.ifSmaller("kalenderwoche", kalenderwoche, 1);
		DeveloperNotificationException.ifGreater("kalenderwoche", kalenderwoche, DateUtils.gibKalenderwochenOfJahr(jahr));
		if (this.stundenplanWochenTypModell === 0)
			return 0;
		const z : StundenplanKalenderwochenzuordnung | null = this._map2d_jahr_kw_zu_kwz.getOrNull(jahr, kalenderwoche);
		if (z !== null)
			return z.wochentyp;
		const wochentyp : number = kalenderwoche % this.stundenplanWochenTypModell;
		return wochentyp === 0 ? this.stundenplanWochenTypModell : wochentyp;
	}

	/**
	 * Liefert TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" den Wochentyp verwendet wird.
	 * <br>Hinweis: Das Mapping muss existieren UND {@link #stundenplanWochenTypModell} muss mindestens 2 sein.
	 *
	 * @param jahr           Das Jahr der Kalenderwoche. Es muss zwischen {@link DateUtils#MIN_GUELTIGES_JAHR} und {@link DateUtils#MAX_GUELTIGES_JAHR} liegen.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche. Es muss zwischen 1 und {@link DateUtils#gibKalenderwochenOfJahr(int)} liegen.
	 *
	 * @return TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" den Wochentyp verwendet wird.
	 */
	public kalenderwochenzuordnungGetWochentypUsesMapping(jahr : number, kalenderwoche : number) : boolean {
		DeveloperNotificationException.ifSmaller("jahr", jahr, DateUtils.MIN_GUELTIGES_JAHR);
		DeveloperNotificationException.ifGreater("jahr", jahr, DateUtils.MAX_GUELTIGES_JAHR);
		DeveloperNotificationException.ifSmaller("kalenderwoche", kalenderwoche, 1);
		DeveloperNotificationException.ifGreater("kalenderwoche", kalenderwoche, DateUtils.gibKalenderwochenOfJahr(jahr));
		const z : StundenplanKalenderwochenzuordnung | null = this._map2d_jahr_kw_zu_kwz.getOrNull(jahr, kalenderwoche);
		return (this.stundenplanWochenTypModell >= 2) && (z !== null);
	}

	/**
	 * Entfernt anhand das alte {@link StundenplanKalenderwochenzuordnung}-Objekt anhand der ID und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Ein patchen der ID ist nicht erlaubt. Hierfür muss die
	 * {@link #kalenderwochenzuordnungReplace(StundenplanKalenderwochenzuordnung, StundenplanKalenderwochenzuordnung)}-Methode verwendet werden.
	 *
	 * @param kwz Das neue {@link StundenplanKalenderwochenzuordnung}-Objekt, welches das alte Objekt ersetzt.
	 */
	public kalenderwochenzuordnungPatch(kwz : StundenplanKalenderwochenzuordnung) : void {
		this.kalenderwochenzuordnungRemoveOhneUpdateById(kwz.id);
		this.kalenderwochenzuordnungAddOhneUpdate(kwz);
		this.kalenderwochenzuordnungUpdate();
	}

	private kalenderwochenzuordnungRemoveOhneUpdateById(idKWZ : number) : void {
		const k : StundenplanKalenderwochenzuordnung = DeveloperNotificationException.ifMapGetIsNull(this._map_idKWZ_zu_kwz, idKWZ);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idKWZ_zu_kwz, k.id);
		DeveloperNotificationException.ifMap2DRemoveFailes(this._map2d_jahr_kw_zu_kwz, k.jahr, k.kw);
		DeveloperNotificationException.ifListRemoveFailes("_list_kwz", this._list_kwz, k);
	}

	private kalenderwochenzuordnungRemoveOhneUpdateByJahrAndKW(jahr : number, kalenderwoche : number) : void {
		const k : StundenplanKalenderwochenzuordnung = DeveloperNotificationException.ifMap2DGetIsNull(this._map2d_jahr_kw_zu_kwz, jahr, kalenderwoche);
		if (k.id !== -1)
			DeveloperNotificationException.ifMapRemoveFailes(this._map_idKWZ_zu_kwz, k.id);
		DeveloperNotificationException.ifMap2DRemoveFailes(this._map2d_jahr_kw_zu_kwz, k.jahr, k.kw);
		DeveloperNotificationException.ifListRemoveFailes("_list_kwz", this._list_kwz, k);
	}

	/**
	 * Entfernt ein {@link StundenplanKalenderwochenzuordnung}-Objekt anhand seiner Datenbank-ID.
	 * <br>Laufzeit: O(|StundenplanKalenderwochenzuordnung|), da kalenderwochenzuordnungUpdate() aufgerufen wird.
	 *
	 * @param idKWZ  Die Datenbank-ID des {@link StundenplanKalenderwochenzuordnung}-Objekts, welches entfernt werden soll.
	 */
	public kalenderwochenzuordnungRemoveById(idKWZ : number) : void {
		this.kalenderwochenzuordnungRemoveOhneUpdateById(idKWZ);
		this.kalenderwochenzuordnungUpdate();
	}

	/**
	 * Entfernt ein {@link StundenplanKalenderwochenzuordnung}-Objekt anhand der Parameter (jahr, kalenderwoche).
	 * <br>Laufzeit: O(|StundenplanKalenderwochenzuordnung|), da kalenderwochenzuordnungUpdate() aufgerufen wird.
	 *
	 * @param jahr           Das Jahr der Kalenderwoche.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche.
	 */
	public kalenderwochenzuordnungRemoveByJahrAndKW(jahr : number, kalenderwoche : number) : void {
		this.kalenderwochenzuordnungRemoveOhneUpdateByJahrAndKW(jahr, kalenderwoche);
		this.kalenderwochenzuordnungUpdate();
	}

	/**
	 * Ersetzt das alte {@link StundenplanKalenderwochenzuordnung}-Objekt durch das neue Objekt.
	 *
	 * @param kwzAlt  Das alte {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 * @param kwzNeu  Das neue {@link StundenplanKalenderwochenzuordnung}-Objekt, welches das alte Objekt ersetzt.
	 */
	public kalenderwochenzuordnungReplace(kwzAlt : StundenplanKalenderwochenzuordnung, kwzNeu : StundenplanKalenderwochenzuordnung) : void {
		this.kalenderwochenzuordnungRemoveOhneUpdateByJahrAndKW(kwzAlt.jahr, kwzAlt.kw);
		this.kalenderwochenzuordnungAddOhneUpdate(kwzNeu);
		this.kalenderwochenzuordnungUpdate();
	}

	/**
	 * Aktualisiert verschiedene Werte nachdem sich die Menge der {@link StundenplanKalenderwochenzuordnung} verändert hat.
	 * <br>Laufzeit: O(|StundenplanKalenderwochenzuordnung| * log)
	 */
	private kalenderwochenzuordnungUpdate() : void {
		this._list_kwz.sort(StundenplanManager._compKWZ);
	}

	private klasseAddOhneUpdate(klasse : StundenplanKlasse) : void {
		DeveloperNotificationException.ifInvalidID("klasse.id", klasse.id);
		DeveloperNotificationException.ifStringIsBlank("klasse.kuerzel", klasse.kuerzel);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idKlasse_zu_klasse, klasse.id, klasse);
		DeveloperNotificationException.ifListAddsDuplicate("_list_klassen", this._list_klassen, klasse);
	}

	/**
	 * Fügt ein {@link StundenplanKlasse}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanKlasse|), da klasseUpdate() aufgerufen wird.
	 *
	 * @param klasse  Das {@link StundenplanKlasse}-Objekt, welches hinzugefügt werden soll.
	 */
	public klasseAdd(klasse : StundenplanKlasse) : void {
		this.klasseAddOhneUpdate(klasse);
		this.klasseUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanKlasse}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanKlasse|), da klasseUpdate() aufgerufen wird.
	 *
	 * @param listKlasse  Die Menge der {@link StundenplanKlasse}-Objekte, welche hinzugefügt werden soll.
	 */
	public klasseAddAll(listKlasse : List<StundenplanKlasse>) : void {
		for (const klasse of listKlasse)
			this.klasseAddOhneUpdate(klasse);
		this.klasseUpdate();
	}

	/**
	 * Liefert das {@link StundenplanKlasse}-Objekt mit der übergebenen ID.
	 *
	 * @param idKlasse  Die Datenbank-ID des {@link StundenplanKlasse}-Objekts.
	 *
	 * @return das {@link StundenplanKlasse}-Objekt mit der übergebenen ID.
	 */
	public klasseGetByIdOrException(idKlasse : number) : StundenplanKlasse {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idKlasse_zu_klasse, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlasse}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanKlasse}-Objekte.
	 */
	public klasseGetMengeAsList() : List<StundenplanKlasse> {
		return this._list_klassen;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanKlasse}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param klasse  Das neue {@link StundenplanKlasse}-Objekt, welches das alte Objekt ersetzt.
	 */
	public klassePatch(klasse : StundenplanKlasse) : void {
		this.klasseRemoveOhneUpdateById(klasse.id);
		this.klasseAddOhneUpdate(klasse);
		this.klasseUpdate();
	}

	private klasseRemoveOhneUpdateById(idKlasse : number) : void {
		const k : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._map_idKlasse_zu_klasse, idKlasse);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idKlasse_zu_klasse, k.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_klasse", this._list_klassen, k);
	}

	/**
	 * Entfernt ein {@link StundenplanKlasse}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanKlasse|), da klasseUpdate() aufgerufen wird.
	 *
	 * @param idKlasse  Die Datenbank-ID des {@link StundenplanKlasse}-Objekts, welches entfernt werden soll.
	 */
	public klasseRemoveById(idKlasse : number) : void {
		this.klasseRemoveOhneUpdateById(idKlasse);
		this.klasseUpdate();
	}

	private klasseUpdate() : void {
		const setKlasseKuerzel : HashSet<string> = new HashSet();
		for (const klasse of this._list_klassen) {
			DeveloperNotificationException.ifSetAddsDuplicate("setKlasseKuerzel", setKlasseKuerzel, klasse.kuerzel);
			const setJahrgaengeDerKlasse : HashSet<number> = new HashSet();
			for (const idJahrgangDerKlasse of klasse.jahrgaenge) {
				DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", this._map_idJahrgang_zu_jahrgang, idJahrgangDerKlasse);
				DeveloperNotificationException.ifSetAddsDuplicate("setJahrgaengeDerKlasse", setJahrgaengeDerKlasse, idJahrgangDerKlasse);
			}
		}
	}

	private kursAddOhneUpdate(kurs : StundenplanKurs) : void {
		DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);
		DeveloperNotificationException.ifStringIsBlank("kurs.bezeichnung", kurs.bezeichnung);
		for (const idSchieneDesKurses of kurs.schienen)
			DeveloperNotificationException.ifMapNotContains("_map_schieneID_zu_schiene", this._map_idSchiene_zu_schiene, idSchieneDesKurses);
		for (const idJahrgangDesKurses of kurs.jahrgaenge)
			DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", this._map_idJahrgang_zu_jahrgang, idJahrgangDesKurses);
		for (const idSchuelerDesKurses of kurs.schueler)
			DeveloperNotificationException.ifMapNotContains("_map_schuelerID_zu_schueler", this._map_schuelerID_zu_schueler, idSchuelerDesKurses);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idKurs_zu_kurs, kurs.id, kurs);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idKurs_zu_unterrichtmenge, kurs.id, new ArrayList());
		DeveloperNotificationException.ifListAddsDuplicate("_list_kurse", this._list_kurse, kurs);
	}

	/**
	 * Fügt ein {@link StundenplanKurs}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanKurs|), da kursUpdate() aufgerufen wird.
	 *
	 * @param kurs  Das {@link StundenplanKurs}-Objekt, welches hinzugefügt werden soll.
	 */
	public kursAdd(kurs : StundenplanKurs) : void {
		this.kursAddOhneUpdate(kurs);
		this.kursUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanKurs}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanKurs|), da kursUpdate() aufgerufen wird.
	 *
	 * @param listKurs  Die Menge der {@link StundenplanKurs}-Objekte, welche hinzugefügt werden soll.
	 */
	public kursAddAll(listKurs : List<StundenplanKurs>) : void {
		for (const kurs of listKurs)
			this.kursAddOhneUpdate(kurs);
		this.kursUpdate();
	}

	/**
	 * Liefert das {@link StundenplanKurs}-Objekt mit der übergebenen ID.
	 *
	 * @param idKurs  Die Datenbank-ID des {@link StundenplanKurs}-Objekts.
	 *
	 * @return das {@link StundenplanKurs}-Objekt mit der übergebenen ID.
	 */
	public kursGetByIdOrException(idKurs : number) : StundenplanKurs {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_kurs, idKurs);
	}

	/**
	 * Liefert TRUE, falls der übergebene Kurs am (Wochentyp / Wochentag / Unterrichtsstunde) hat.
	 *
	 * @param idKurs            Die Datenbank-ID des Kurses.
	 * @param wochentyp         Der Typ der Woche (beispielsweise bei AB-Wochen).
	 * @param wochentag         Der gewünschte {@link Wochentag}.
	 * @param unterrichtstunde  Die gewünschte Unterrichtsstunde.
	 *
	 * @return TRUE, falls der übergebene Kurs am (wochentyp / wochentag / Unterrichtsstunde) hat.
	 */
	public kursGetHatUnterrichtAm(idKurs : number, wochentyp : number, wochentag : Wochentag, unterrichtstunde : number) : boolean {
		for (const u of this.unterrichtGetMengeByKursIdAndWochentyp(idKurs, wochentyp)) {
			const z : StundenplanZeitraster = this.zeitrasterGetByIdOrException(u.idZeitraster);
			if ((z.wochentag === wochentag.id) && (z.unterrichtstunde === unterrichtstunde))
				return true;
		}
		return false;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte.
	 */
	public kursGetMengeAsList() : List<StundenplanKurs> {
		return this._list_kurse;
	}

	/**
	 * Liefert gefilterte Kurs-IDs, deren Unterricht zu (Wochentyp / Wochentag / Unterrichtsstunde) passt.
	 *
	 * @param idsKurs          Die Liste aller Kurs-IDs.
	 * @param wochentyp        Der Typ der Woche (beispielsweise bei AB-Wochen).
	 * @param wochentag        Der gewünschte {@link Wochentag}.
	 * @param unterrichtstunde Die gewünschte Unterrichtsstunde.
	 *
	 * @return gefilterte Kurs-IDs, deren Unterricht zu (Wochentyp / Wochentag / Unterrichtsstunde) passt.
	 */
	public kursGetMengeGefiltertByWochentypAndWochentagAndStunde(idsKurs : List<number>, wochentyp : number, wochentag : Wochentag, unterrichtstunde : number) : List<number> {
		return CollectionUtils.toFilteredArrayList(idsKurs, { test : (idKurs: number) => this.kursGetHatUnterrichtAm(idKurs!, wochentyp, wochentag, unterrichtstunde) });
	}

	/**
	 * Liefert gefilterte Kurs-IDs, deren Unterricht zu (Datum / Unterrichtsstunde) passt.
	 *
	 * @param idsKurs          Die Liste aller Kurs-IDs.
	 * @param datumISO8601     Das Datum. Daraus ergibt sich (Wochentyp / Wochentag).
	 * @param unterrichtstunde Die gewünschte Unterrichtsstunde.
	 *
	 * @return gefilterte Kurs-IDs, deren Unterricht zu (Datum / Unterrichtsstunde) passt.
	 */
	public kursGetMengeGefiltertByDatumAndStunde(idsKurs : List<number>, datumISO8601 : string, unterrichtstunde : number) : List<number> {
		const e : Array<number> | null = DateUtils.extractFromDateISO8601(datumISO8601);
		const wochentyp : number = this.kalenderwochenzuordnungGetWochentypOrDefault(e[6], e[5]);
		const wochentag : Wochentag = Wochentag.fromIDorException(e[3]);
		return this.kursGetMengeGefiltertByWochentypAndWochentagAndStunde(idsKurs, wochentyp, wochentag, unterrichtstunde);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanKurs}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param kurs  Das neue {@link StundenplanKurs}-Objekt, welches das alte Objekt ersetzt.
	 */
	public kursPatch(kurs : StundenplanKurs) : void {
		this.kursRemoveOhneUpdateById(kurs.id);
		this.kursAddOhneUpdate(kurs);
		this.kursUpdate();
	}

	private kursRemoveOhneUpdateById(idKurs : number) : void {
		const kurs : StundenplanKurs = DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_kurs, idKurs);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idKurs_zu_kurs, kurs.id);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idKurs_zu_unterrichtmenge, kurs.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_kurse", this._list_kurse, kurs);
	}

	/**
	 * Entfernt ein {@link StundenplanKurs}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanKurs|), da kursUpdate() aufgerufen wird.
	 *
	 * @param idKurs  Die Datenbank-ID des {@link StundenplanKurs}-Objekts, welches entfernt werden soll.
	 */
	public kursRemoveById(idKurs : number) : void {
		this.kursRemoveOhneUpdateById(idKurs);
		this.kursUpdate();
	}

	private kursUpdate() : void {
		// empty block
	}

	private lehrerAddOhneUpdate(lehrer : StundenplanLehrer) : void {
		DeveloperNotificationException.ifInvalidID("lehrer.id", lehrer.id);
		DeveloperNotificationException.ifStringIsBlank("lehrer.kuerzel", lehrer.kuerzel);
		DeveloperNotificationException.ifStringIsBlank("lehrer.nachname", lehrer.nachname);
		DeveloperNotificationException.ifStringIsBlank("lehrer.vorname", lehrer.vorname);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idLehrer_zu_lehrer, lehrer.id, lehrer);
		DeveloperNotificationException.ifListAddsDuplicate("_list_lehrer", this._list_lehrer, lehrer);
	}

	/**
	 * Fügt ein {@link StundenplanLehrer}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanLehrer|), da lehrerUpdate() aufgerufen wird.
	 *
	 * @param lehrer  Das {@link StundenplanLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public lehrerAdd(lehrer : StundenplanLehrer) : void {
		this.lehrerAddOhneUpdate(lehrer);
		this.lehrerUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanLehrer}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanLehrer|), da lehrerUpdate() aufgerufen wird.
	 *
	 * @param listLehrer  Die Menge der {@link StundenplanLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public lehrerAddAll(listLehrer : List<StundenplanLehrer>) : void {
		for (const lehrer of listLehrer)
			this.lehrerAddOhneUpdate(lehrer);
		this.lehrerUpdate();
	}

	/**
	 * Liefert das {@link StundenplanLehrer}-Objekt mit der übergebenen ID.
	 *
	 * @param idLehrer  Die Datenbank-ID des {@link StundenplanLehrer}-Objekts.
	 *
	 * @return das {@link StundenplanLehrer}-Objekt mit der übergebenen ID.
	 */
	public lehrerGetByIdOrException(idLehrer : number) : StundenplanLehrer {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idLehrer_zu_lehrer, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanLehrer}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanLehrer}-Objekte.
	 */
	public lehrerGetMengeAsList() : List<StundenplanLehrer> {
		return this._list_lehrer;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanLehrer}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param lehrer  Das neue {@link StundenplanLehrer}-Objekt, welches das alte Objekt ersetzt.
	 */
	public lehrerPatch(lehrer : StundenplanLehrer) : void {
		this.lehrerRemoveOhneUpdateById(lehrer.id);
		this.lehrerAddOhneUpdate(lehrer);
		this.lehrerUpdate();
	}

	private lehrerRemoveOhneUpdateById(idLehrer : number) : void {
		const lehrer : StundenplanLehrer = DeveloperNotificationException.ifMapGetIsNull(this._map_idLehrer_zu_lehrer, idLehrer);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idLehrer_zu_lehrer, lehrer.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_lehrer", this._list_lehrer, lehrer);
	}

	/**
	 * Entfernt ein {@link StundenplanLehrer}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanLehrer|), da lehrerUpdate() aufgerufen wird.
	 *
	 * @param idLehrer  Die Datenbank-ID des {@link StundenplanLehrer}-Objekts, welches entfernt werden soll.
	 */
	public lehrerRemoveById(idLehrer : number) : void {
		this.lehrerRemoveOhneUpdateById(idLehrer);
		this.lehrerUpdate();
	}

	private lehrerUpdate() : void {
		const setLehrerKuerzel : HashSet<string> = new HashSet();
		for (const lehrer of this._list_lehrer) {
			DeveloperNotificationException.ifSetAddsDuplicate("setLehrerKuerzel", setLehrerKuerzel, lehrer.kuerzel);
			const setFaecherDerLehrkraft : HashSet<number> = new HashSet();
			for (const idFachDerLehrkraft of lehrer.faecher) {
				DeveloperNotificationException.ifMapNotContains("_map_fachID_zu_fach", this._map_idFach_zu_fach, idFachDerLehrkraft);
				DeveloperNotificationException.ifSetAddsDuplicate("setFaecherDerLehrkraft", setFaecherDerLehrkraft, idFachDerLehrkraft);
			}
		}
	}

	private pausenaufsichtAddOhneUpdate(pausenaufsicht : StundenplanPausenaufsicht) : void {
		DeveloperNotificationException.ifInvalidID("aufsicht.id", pausenaufsicht.id);
		DeveloperNotificationException.ifMapNotContains("_map_idPausenzeit_zu_pausenzeit", this._map_idPausenzeit_zu_pausenzeit, pausenaufsicht.idPausenzeit);
		DeveloperNotificationException.ifMapNotContains("_map_idLehrer_zu_lehrer", this._map_idLehrer_zu_lehrer, pausenaufsicht.idLehrer);
		DeveloperNotificationException.ifTrue("(pa.wochentyp > 0) && (pa.wochentyp > stundenplanWochenTypModell)", (pausenaufsicht.wochentyp > 0) && (pausenaufsicht.wochentyp > this.stundenplanWochenTypModell));
		DeveloperNotificationException.ifMapPutOverwrites(this._map_pausenaufsichtID_zu_pausenaufsicht, pausenaufsicht.id, pausenaufsicht);
		DeveloperNotificationException.ifListAddsDuplicate("_list_pausenaufsichten", this._list_pausenaufsichten, pausenaufsicht);
	}

	/**
	 * Fügt ein {@link StundenplanPausenaufsicht}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanPausenaufsicht|), da pausenaufsichtUpdate() aufgerufen wird.
	 *
	 * @param pausenaufsicht  Das {@link StundenplanPausenaufsicht}-Objekt, welches hinzugefügt werden soll.
	 */
	public pausenaufsichtAdd(pausenaufsicht : StundenplanPausenaufsicht) : void {
		this.pausenaufsichtAddOhneUpdate(pausenaufsicht);
		this.pausenaufsichtUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanPausenaufsicht}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanPausenaufsicht|), da pausenaufsichtUpdate() aufgerufen wird.
	 *
	 * @param listPausenaufsicht  Die Menge der {@link StundenplanPausenaufsicht}-Objekte, welche hinzugefügt werden soll.
	 */
	private pausenaufsichtAddAll(listPausenaufsicht : List<StundenplanPausenaufsicht>) : void {
		for (const pausenaufsicht of listPausenaufsicht)
			this.pausenaufsichtAddOhneUpdate(pausenaufsicht);
		this.pausenaufsichtUpdate();
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @param idPausenaufsicht Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 */
	public pausenaufsichtGetByIdOrException(idPausenaufsicht : number) : StundenplanPausenaufsicht {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_pausenaufsichtID_zu_pausenaufsicht, idPausenaufsicht);
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 *
	 * @return eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 */
	public pausenaufsichtGetMengeAsList() : List<StundenplanPausenaufsicht> {
		return this._list_pausenaufsichten;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanPausenaufsicht}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param pausenaufsicht Das neue {@link StundenplanPausenaufsicht}-Objekt, welches das alte Objekt ersetzt.
	 */
	public pausenaufsichtPatch(pausenaufsicht : StundenplanPausenaufsicht) : void {
		this.pausenaufsichtRemoveOhneUpdateById(pausenaufsicht.id);
		this.pausenaufsichtAddOhneUpdate(pausenaufsicht);
		this.pausenaufsichtUpdate();
	}

	private pausenaufsichtRemoveOhneUpdateById(idPausenaufsicht : number) : void {
		const pausenaufsicht : StundenplanPausenaufsicht = DeveloperNotificationException.ifMapGetIsNull(this._map_pausenaufsichtID_zu_pausenaufsicht, idPausenaufsicht);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_pausenaufsichtID_zu_pausenaufsicht, pausenaufsicht.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_pausenaufsichten", this._list_pausenaufsichten, pausenaufsicht);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenaufsicht}-Objekt.
	 * <br>Laufzeit: O(|StundenplanPausenaufsicht|), da pausenaufsichtUpdate() aufgerufen wird.
	 *
	 * @param idPausenaufsicht  Die ID des {@link StundenplanPausenaufsicht}-Objekts.
	 */
	public pausenaufsichtRemoveById(idPausenaufsicht : number) : void {
		this.pausenaufsichtRemoveOhneUpdateById(idPausenaufsicht);
		this.pausenaufsichtUpdate();
	}

	private pausenaufsichtUpdate() : void {
		for (const pausenaufsicht of this._list_pausenaufsichten) {
			const setBereicheDieserAufsicht : HashSet<number> = new HashSet();
			for (const idAufsichtsbereich of pausenaufsicht.bereiche) {
				DeveloperNotificationException.ifMapNotContains("_map_aufsichtsbereichID_zu_aufsichtsbereich", this._map_idAufsichtsbereich_zu_aufsichtsbereich, idAufsichtsbereich);
				DeveloperNotificationException.ifSetAddsDuplicate("setBereicheDieserAufsicht", setBereicheDieserAufsicht, idAufsichtsbereich);
			}
		}
		this._list_pausenaufsichten.sort(this._compPausenaufsicht);
	}

	private pausenzeitAddOhneUpdate(pausenzeit : StundenplanPausenzeit) : void {
		DeveloperNotificationException.ifInvalidID("pause.id", pausenzeit.id);
		Wochentag.fromIDorException(pausenzeit.wochentag);
		if ((pausenzeit.beginn !== null) && (pausenzeit.ende !== null))
			DeveloperNotificationException.ifTrue("pausenzeit.beginn >= pausenzeit.ende", pausenzeit.beginn >= pausenzeit.ende);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idPausenzeit_zu_pausenzeit, pausenzeit.id, pausenzeit);
		DeveloperNotificationException.ifListAddsDuplicate("_list_pausenzeiten", this._list_pausenzeiten, pausenzeit);
	}

	/**
	 * Fügt ein {@link StundenplanPausenzeit}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanPausenzeit|), da pausenzeitUpdate() aufgerufen wird.
	 *
	 * @param pausenzeit  Das {@link StundenplanPausenzeit}-Objekt, welches hinzugefügt werden soll.
	 */
	public pausenzeitAdd(pausenzeit : StundenplanPausenzeit) : void {
		this.pausenzeitAddOhneUpdate(pausenzeit);
		this.pausenzeitUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanPausenzeit}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanPausenzeit|), da pausenzeitUpdate() aufgerufen wird.
	 *
	 * @param listPausenzeit  Die Menge der {@link StundenplanPausenzeit}-Objekte, welche hinzugefügt werden soll.
	 */
	public pausenzeitAddAll(listPausenzeit : List<StundenplanPausenzeit>) : void {
		for (const pausenzeit of listPausenzeit)
			this.pausenzeitAddOhneUpdate(pausenzeit);
		this.pausenzeitUpdate();
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param idPausenzeit Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 */
	public pausenzeitGetByIdOrException(idPausenzeit : number) : StundenplanPausenzeit {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenzeit_zu_pausenzeit, idPausenzeit);
	}

	/**
	 * Liefert die Beginn-Uhrzeit der {@link StundenplanPausenzeit} oder den leeren String, falls diese NULL ist.
	 * <br>Beispiel: "09:30" oder ""
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit  Die Datenbank-ID des {@link StundenplanPausenzeit}.
	 *
	 * @return die Beginn-Uhrzeit der {@link StundenplanPausenzeit} oder den leeren String, falls diese NULL ist.
	 */
	public pausenzeitGetByIdStringOfUhrzeitBeginn(idPausenzeit : number) : string {
		const pausenzeit : StundenplanPausenzeit = DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenzeit_zu_pausenzeit, idPausenzeit);
		return (pausenzeit.beginn === null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(pausenzeit.beginn);
	}

	/**
	 * Liefert die End-Uhrzeit der {@link StundenplanPausenzeit} oder den leeren String, falls diese NULL ist.
	 * <br>Beispiel: "10:15" oder ""
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit  Die Datenbank-ID des {@link StundenplanPausenzeit}.
	 *
	 * @return die End-Uhrzeit der {@link StundenplanPausenzeit} oder den leeren String, falls diese NULL ist.
	 */
	public pausenzeitGetByIdStringOfUhrzeitEnde(idPausenzeit : number) : string {
		const pausenzeit : StundenplanPausenzeit = DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenzeit_zu_pausenzeit, idPausenzeit);
		return (pausenzeit.ende === null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(pausenzeit.ende);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 */
	public pausenzeitGetMengeAsList() : List<StundenplanPausenzeit> {
		return this._list_pausenzeiten;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Wochentages, oder eine leere Liste.
	 * <br> Laufzeit: O(1)
	 *
	 * @param wochentag  Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Wochentages, oder eine leere Liste.
	 */
	public pausenzeitGetMengeByWochentagOrEmptyList(wochentag : number) : List<StundenplanPausenzeit> {
		return MapUtils.getOrCreateArrayList(this._pausenzeitMapByWochentag, wochentag);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanPausenzeit}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param pausenzeit Das neue {@link StundenplanPausenzeit}-Objekt, welches das alte Objekt ersetzt.
	 */
	public pausenzeitPatch(pausenzeit : StundenplanPausenzeit) : void {
		this.pausenzeitRemoveOhneUpdateById(pausenzeit.id);
		this.pausenzeitAddOhneUpdate(pausenzeit);
		this.pausenzeitUpdate();
	}

	private pausenzeitRemoveOhneUpdateById(idPausenzeit : number) : void {
		const pausenzeit : StundenplanPausenzeit = DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenzeit_zu_pausenzeit, idPausenzeit);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idPausenzeit_zu_pausenzeit, pausenzeit.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_pausenzeiten", this._list_pausenzeiten, pausenzeit);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenzeit}-Objekt.
	 * <br>Laufzeit: O(|StundenplanPausenzeit|), da pausenzeitUpdate() aufgerufen wird.
	 *
	 * @param idPausenzeit  Die ID des {@link StundenplanPausenzeit}-Objekts.
	 */
	public pausenzeitRemoveById(idPausenzeit : number) : void {
		this.pausenzeitRemoveOhneUpdateById(idPausenzeit);
		this.pausenzeitUpdate();
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitGetMinutenMin() : number {
		return this._pausenzeitMinutenMin;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitGetMinutenMax() : number {
		return this._pausenzeitMinutenMax;
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitUndZeitrasterGetMinutenMin() : number {
		return this._pausenzeitUndZeitrasterMinutenMin;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitUndZeitrasterGetMinutenMax() : number {
		return this._pausenzeitUndZeitrasterMinutenMax;
	}

	private pausenzeitUpdate() : void {
		this._list_pausenzeiten.sort(StundenplanManager._compPausenzeit);
		this._pausenzeitMinutenMin = StundenplanManager.MINUTEN_INF_POS;
		this._pausenzeitMinutenMax = StundenplanManager.MINUTEN_INF_NEG;
		this._pausenzeitUndZeitrasterMinutenMin = StundenplanManager.MINUTEN_INF_POS;
		this._pausenzeitUndZeitrasterMinutenMax = StundenplanManager.MINUTEN_INF_NEG;
		this._pausenzeitMapByWochentag.clear();
		for (const p of this._list_pausenzeiten) {
			this._pausenzeitMinutenMin = BlockungsUtils.minVI(this._pausenzeitMinutenMin, p.beginn);
			this._pausenzeitMinutenMax = BlockungsUtils.maxVI(this._pausenzeitMinutenMax, p.ende);
			this._pausenzeitUndZeitrasterMinutenMin = BlockungsUtils.minVI(this._pausenzeitUndZeitrasterMinutenMin, p.beginn);
			this._pausenzeitUndZeitrasterMinutenMax = BlockungsUtils.maxVI(this._pausenzeitUndZeitrasterMinutenMax, p.ende);
			MapUtils.getOrCreateArrayList(this._pausenzeitMapByWochentag, p.wochentag).add(p);
		}
		for (const z of this._list_zeitraster) {
			this._pausenzeitUndZeitrasterMinutenMin = BlockungsUtils.minVI(this._pausenzeitUndZeitrasterMinutenMin, z.stundenbeginn);
			this._pausenzeitUndZeitrasterMinutenMax = BlockungsUtils.maxVI(this._pausenzeitUndZeitrasterMinutenMax, z.stundenende);
		}
		this._pausenzeitMinutenMin = (this._pausenzeitMinutenMin === StundenplanManager.MINUTEN_INF_POS) ? 480 : this._pausenzeitMinutenMin;
		this._pausenzeitMinutenMax = (this._pausenzeitMinutenMax === StundenplanManager.MINUTEN_INF_NEG) ? 480 : this._pausenzeitMinutenMax;
		this._pausenzeitUndZeitrasterMinutenMin = (this._pausenzeitUndZeitrasterMinutenMin === StundenplanManager.MINUTEN_INF_POS) ? 480 : this._pausenzeitUndZeitrasterMinutenMin;
		this._pausenzeitUndZeitrasterMinutenMax = (this._pausenzeitUndZeitrasterMinutenMax === StundenplanManager.MINUTEN_INF_NEG) ? 480 : this._pausenzeitUndZeitrasterMinutenMax;
	}

	private raumAddOhneUpdate(raum : StundenplanRaum) : void {
		DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
		DeveloperNotificationException.ifStringIsBlank("raum.kuerzel", raum.kuerzel);
		DeveloperNotificationException.ifTrue("raum.groesse < 0", raum.groesse < 0);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idRaum_zu_raum, raum.id, raum);
		this._list_raeume.add(raum);
	}

	/**
	 * Fügt ein {@link StundenplanRaum}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanRaum|), da raumUpdate() aufgerufen wird.
	 *
	 * @param raum  Das {@link StundenplanRaum}-Objekt, welches hinzugefügt werden soll.
	 */
	public raumAdd(raum : StundenplanRaum) : void {
		this.raumAddOhneUpdate(raum);
		this.raumUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanRaum}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanRaum|), da raumUpdate() aufgerufen wird.
	 *
	 * @param listRaum  Die Menge der {@link StundenplanRaum}-Objekte, welche hinzugefügt werden soll.
	 */
	public raumAddAll(listRaum : List<StundenplanRaum>) : void {
		for (const raum of listRaum)
			this.raumAddOhneUpdate(raum);
		this.raumUpdate();
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 *
	 * @param idRaum Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 */
	public raumGetByIdOrException(idRaum : number) : StundenplanRaum {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idRaum_zu_raum, idRaum);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanRaum}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanRaum}-Objekte.
	 */
	public raumGetMengeAsList() : List<StundenplanRaum> {
		return this._list_raeume;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanRaum}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param raum Das neue {@link StundenplanRaum}-Objekt, welches das alte Objekt ersetzt.
	 */
	public raumPatch(raum : StundenplanRaum) : void {
		this.raumRemoveOhneUpdateById(raum.id);
		this.raumAddOhneUpdate(raum);
		this.raumUpdate();
	}

	private raumRemoveOhneUpdateById(idRaum : number) : void {
		const raum : StundenplanRaum = DeveloperNotificationException.ifMapGetIsNull(this._map_idRaum_zu_raum, idRaum);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idRaum_zu_raum, raum.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_raeume", this._list_raeume, raum);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanRaum}-Objekt.
	 * <br>Laufzeit: O(|StundenplanRaum|), da raumUpdate() aufgerufen wird.
	 *
	 * @param idRaum  Die ID des {@link StundenplanRaum}-Objekts.
	 */
	public raumRemoveById(idRaum : number) : void {
		this.raumRemoveOhneUpdateById(idRaum);
		this.raumUpdate();
	}

	private raumUpdate() : void {
		const setRaumKuerzel : HashSet<string> = new HashSet();
		for (const raum of this._list_raeume)
			DeveloperNotificationException.ifSetAddsDuplicate("setRaumKuerzel", setRaumKuerzel, raum.kuerzel);
		this._list_raeume.sort(StundenplanManager._compRaum);
	}

	private schieneAddOhneUpdate(schiene : StundenplanSchiene) : void {
		DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
		DeveloperNotificationException.ifTrue("schiene.nummer <= 0", schiene.nummer <= 0);
		DeveloperNotificationException.ifStringIsBlank("schiene.bezeichnung", schiene.bezeichnung);
		DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", this._map_idJahrgang_zu_jahrgang, schiene.idJahrgang);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idSchiene_zu_schiene, schiene.id, schiene);
		DeveloperNotificationException.ifListAddsDuplicate("_list_schienen", this._list_schienen, schiene);
	}

	/**
	 * Fügt ein {@link StundenplanSchiene}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanSchiene|), da schienenUpdate() aufgerufen wird.
	 *
	 * @param schiene  Das {@link StundenplanSchiene}-Objekt, welches hinzugefügt werden soll.
	 */
	public schieneAdd(schiene : StundenplanSchiene) : void {
		this.schieneAddOhneUpdate(schiene);
		this.schieneUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanSchiene}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanSchiene|), da schienenUpdate() aufgerufen wird.
	 *
	 * @param listSchiene  Die Menge der {@link StundenplanSchiene}-Objekte, welche hinzugefügt werden soll.
	 */
	public schieneAddAll(listSchiene : List<StundenplanSchiene>) : void {
		for (const schiene of listSchiene)
			this.schieneAddOhneUpdate(schiene);
		this.schieneUpdate();
	}

	private schieneUpdate() : void {
		// empty block
	}

	private schuelerAddOhneUpdate(schueler : StundenplanSchueler) : void {
		DeveloperNotificationException.ifInvalidID("schueler.id", schueler.id);
		DeveloperNotificationException.ifStringIsBlank("schueler.nachname", schueler.nachname);
		DeveloperNotificationException.ifStringIsBlank("schueler.vorname", schueler.vorname);
		DeveloperNotificationException.ifMapNotContains("_map_klasseID_zu_klasse", this._map_idKlasse_zu_klasse, schueler.idKlasse);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_zu_schueler, schueler.id, schueler);
		DeveloperNotificationException.ifListAddsDuplicate("_list_schueler", this._list_schueler, schueler);
	}

	/**
	 * Fügt ein {@link StundenplanSchueler}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanSchueler|), da schuelerUpdate() aufgerufen wird.
	 *
	 * @param schueler  Das {@link StundenplanSchueler}-Objekt, welches hinzugefügt werden soll.
	 */
	public schuelerAdd(schueler : StundenplanSchueler) : void {
		this.schuelerAddOhneUpdate(schueler);
		this.schuelerUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanSchueler}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanSchueler|), da schuelerUpdate() aufgerufen wird.
	 *
	 * @param listSchueler  Die Menge der {@link StundenplanSchueler}-Objekte, welche hinzugefügt werden soll.
	 */
	public schuelerAddAll(listSchueler : List<StundenplanSchueler>) : void {
		for (const schueler of listSchueler)
			this.schuelerAddOhneUpdate(schueler);
		this.schuelerUpdate();
	}

	private schuelerUpdate() : void {
		// empty block
	}

	/**
	 * Liefert die ID des Schuljahresabschnitts des Stundenplans.
	 *
	 * @return die ID des Schuljahresabschnitts des Stundenplans.
	 */
	public getIDSchuljahresabschnitt() : number {
		return this.stundenplanSchuljahresAbschnittID;
	}

	/**
	 * Liefert das Datum, ab dem der Stundenplan gültig ist.
	 *
	 * @return das Datum, ab dem der Stundenplan gültig ist.
	 */
	public getGueltigAb() : string {
		return this.stundenplanGueltigAb;
	}

	/**
	 * Liefert das Datum, bis wann der Stundenplan gültig ist.
	 *
	 * @return das Datum, bis wann der Stundenplan gültig ist.
	 */
	public getGueltigBis() : string {
		return this.stundenplanGueltigBis;
	}

	/**
	 * Liefert die textuelle Beschreibung des Stundenplans.
	 *
	 * @return die textuelle Beschreibung des Stundenplans.
	 */
	public getBezeichnungStundenplan() : string {
		return this.stundenplanBezeichnung;
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
		return this.stundenplanWochenTypModell;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanZeitraster}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte.
	 */
	public getListZeitraster() : List<StundenplanZeitraster> {
		return this._list_zeitraster;
	}

	/**
	 * Liefert eine Liste der {@link StundenplanZeitraster}-Objekte zu einem bestimmten Wochentag.
	 *
	 * @param wochentag der Wochentag der gewünschten Zeitraster-Objekte
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte zum übergeben Wochentag.
	 */
	public getListZeitrasterZuWochentag(wochentag : Wochentag) : List<StundenplanZeitraster> {
		return CollectionUtils.toFilteredArrayList(this._list_zeitraster, { test : (z: StundenplanZeitraster) => (wochentag.id === z.wochentag) });
	}

	/**
	 * Liefert die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Intervall berühren.
	 *
	 * @param zeitrasterStart    Das {@link StundenplanZeitraster} zu dem es startet.
	 * @param minutenVerstrichen Die verstrichene Zeit (in Minuten) seit der "startzeit" .
	 *
	 * @return die passende Menge an {@link StundenplanZeitraster}-Objekten.
	 */
	public getZeitrasterByStartVerstrichen(zeitrasterStart : StundenplanZeitraster, minutenVerstrichen : number) : List<StundenplanZeitraster> {
		const wochentag : Wochentag | null = Wochentag.fromIDorException(zeitrasterStart.wochentag);
		const startzeit : number = DeveloperNotificationException.ifNull("zeitrasterStart.stundenbeginn ist NULL", zeitrasterStart.stundenbeginn).valueOf();
		return this.getZeitrasterByWochentagStartVerstrichen(wochentag, startzeit, minutenVerstrichen);
	}

	/**
	 * Liefert die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Zeit-Intervall berühren.<br>
	 *
	 * @param wochentag          Der {@link Wochentag} des Zeit-Intervalls.
	 * @param beginn             Der Beginn des Zeit-Intervalls.
	 * @param minutenVerstrichen Daraus ergibt sich das Ende des Zeit-Intervalls.
	 *
	 * @return die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Intervall berührt.
	 */
	public getZeitrasterByWochentagStartVerstrichen(wochentag : Wochentag, beginn : number, minutenVerstrichen : number) : List<StundenplanZeitraster> {
		const ende : number = beginn + minutenVerstrichen;
		return CollectionUtils.toFilteredArrayList(this._list_zeitraster, { test : (z: StundenplanZeitraster) => (wochentag.id === z.wochentag) && this.zeitrasterGetSchneidenSich(beginn, ende, z.stundenbeginn, z.stundenende) });
	}

	/**
	 * Liefert den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public zeitrasterGetMinutenMin() : number {
		return this._zeitrasterMinutenMin;
	}

	/**
	 * Liefert den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public zeitrasterGetMinutenMax() : number {
		return this._zeitrasterMinutenMax;
	}

	/**
	 * Liefert das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag.
	 *
	 * @param zeitraster Das aktuelle {@link StundenplanZeitraster}-Objekt.
	 *
	 * @return das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag.
	 */
	public getZeitrasterNext(zeitraster : StundenplanZeitraster) : StundenplanZeitraster {
		return this._map2d_wochentag_stunde_zu_zeitraster.getNonNullOrException(zeitraster.wochentag, zeitraster.unterrichtstunde + 1);
	}

	/**
	 * Liefert die Datenbank-ID des Schülers.<br>
	 * Wirft eine Exception, falls in den Daten nicht genau ein Schüler geladen wurde.
	 *
	 * @return die Datenbank-ID des Schülers.
	 */
	public schuelerGetIDorException() : number {
		const size : number = this._list_schueler.size();
		DeveloperNotificationException.ifTrue("getSchuelerID() geht nicht bei " + size + " Schülern!", size !== 1);
		return this._list_schueler.get(0).id;
	}

	/**
	 * Liefert das globale WochenTyp-Modell (0: alle Wochen sehen gleich aus, 2 und größer: Die Wochen sehen verschieden aus).
	 * <br>Laufzeit: O(1)
	 *
	 * @return das globale WochenTyp-Modell.
	 */
	public stundenplanGetWochenTypModell() : number {
		return this.stundenplanWochenTypModell;
	}

	/**
	 * Liefert zum übergebenen Wochentyp einen passenden String.
	 * <br>Beispiel: 0 -> "Alle", 1 -> "A-Woche", ...
	 * <br>Laufzeit: O(1)
	 *
	 * @param wochenTyp  Der umzuwandelnde Wochentyp.
	 *
	 * @return zum übergebenen Wochentyp einen passenden String.
	 */
	public stundenplanGetWochenTypAsString(wochenTyp : number) : string {
		if (wochenTyp <= 0)
			return "Alle";
		const zahl : number = wochenTyp - 1;
		const z2 : number = Math.trunc(zahl / 26);
		const z1 : number = zahl - z2 * 26;
		return StringUtils.numberToLetterIndex1(z2)! + StringUtils.numberToLetterIndex0(z1)! + "-Woche";
	}

	/**
	 * Liefert die Datenbank-ID des Stundenplans.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die Datenbank-ID des Stundenplans.
	 */
	public stundenplanGetID() : number {
		return this.stundenplanID;
	}

	private unterrichtAddOhneUpdate(u : StundenplanUnterricht) : void {
		DeveloperNotificationException.ifInvalidID("u.id", u.id);
		DeveloperNotificationException.ifMapNotContains("_map_zeitrasterID_zu_zeitraster", this._map_idZeitraster_zu_zeitraster, u.idZeitraster);
		DeveloperNotificationException.ifTrue("u.wochentyp > stundenplanWochenTypModell", u.wochentyp > this.stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("u.wochentyp < 0", u.wochentyp < 0);
		if (u.idKurs !== null) {
			DeveloperNotificationException.ifMapNotContains("_map_kursID_zu_kurs", this._map_idKurs_zu_kurs, u.idKurs);
			const unterrichtDesKurses : List<StundenplanUnterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_unterrichtmenge, u.idKurs);
			DeveloperNotificationException.ifListAddsDuplicate("unterrichtDesKurses", unterrichtDesKurses, u);
		}
		DeveloperNotificationException.ifMapNotContains("_map_idFach_zu_fach", this._map_idFach_zu_fach, u.idFach);
		for (const idLehrkraftDesUnterrichts of u.lehrer)
			DeveloperNotificationException.ifMapNotContains("_map_idLehrer_zu_lehrer", this._map_idLehrer_zu_lehrer, idLehrkraftDesUnterrichts);
		for (const idKlasseDesUnterrichts of u.klassen)
			DeveloperNotificationException.ifMapNotContains("_map_idKlasse_zu_klasse", this._map_idKlasse_zu_klasse, idKlasseDesUnterrichts);
		for (const idRaumDesUnterrichts of u.raeume)
			DeveloperNotificationException.ifMapNotContains("_map_idRaum_zu_raum", this._map_idRaum_zu_raum, idRaumDesUnterrichts);
		for (const idSchieneDesUnterrichts of u.schienen)
			DeveloperNotificationException.ifMapNotContains("_map_idSchiene_zu_schiene", this._map_idSchiene_zu_schiene, idSchieneDesUnterrichts);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idUnterricht_zu_unterricht, u.id, u);
		MapUtils.getOrCreateArrayList(this._map_idZeitraster_zu_unterrichtmenge, u.idZeitraster).add(u);
		Map2DUtils.getOrCreateArrayList(this._map2d_idZeitraster_wochentyp_zu_unterrichtmenge, u.idZeitraster, u.wochentyp).add(u);
		for (const idLehrkraftDesUnterrichts of u.lehrer) {
			const lehrer : StundenplanLehrer = DeveloperNotificationException.ifMapGetIsNull(this._map_idLehrer_zu_lehrer, idLehrkraftDesUnterrichts);
			MapUtils.getOrCreateArrayList(this._map_idUnterricht_zu_lehrermenge, u.id).add(lehrer);
		}
		this._list_unterricht.add(u);
	}

	/**
	 * Fügt ein {@link StundenplanUnterricht}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanUnterricht|), da unterrichtUpdate() aufgerufen wird.
	 *
	 * @param unterricht  Das {@link StundenplanUnterricht}-Objekt, welches hinzugefügt werden soll.
	 */
	public unterrichtAdd(unterricht : StundenplanUnterricht) : void {
		this.unterrichtAddOhneUpdate(unterricht);
		this.unterrichtUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanUnterricht}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanUnterricht|), da unterrichtUpdate() aufgerufen wird.
	 *
	 * @param listUnterricht  Die Menge der {@link StundenplanUnterricht}-Objekte, welche hinzugefügt werden soll.
	 */
	public unterrichtAddAll(listUnterricht : List<StundenplanUnterricht>) : void {
		for (const unterricht of listUnterricht)
			this.unterrichtAddOhneUpdate(unterricht);
		this.unterrichtUpdate();
	}

	/**
	 * Liefert das {@link StundenplanUnterricht}-Objekt zur übergebenen ID.
	 * <br>Laufzeit: O(1)
	 * <br>Hinweis: Unnötige Methode, denn man bekommt die Objekte über Zeitraster-Abfragen.
	 *
	 * @param idUnterricht  Die Datenbank-ID des Unterrichts.
	 *
	 * @return das {@link StundenplanUnterricht}-Objekt zur übergebenen ID.
	 */
	public unterrichtGetByIdOrException(idUnterricht : number) : StundenplanUnterricht {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idUnterricht_zu_unterricht, idUnterricht);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses mit einem bestimmten Wochentyp.
	 *
	 * @param idkurs     Die ID des Kurses.
	 * @param wochentyp  Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public unterrichtGetMengeByKursIdAndWochentyp(idkurs : number, wochentyp : number) : List<StundenplanUnterricht> {
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > this.stundenplanWochenTypModell);
		const list : List<StundenplanUnterricht> = MapUtils.getOrCreateArrayList(this._map_idKurs_zu_unterrichtmenge, idkurs);
		return CollectionUtils.toFilteredArrayList(list, { test : (u: StundenplanUnterricht) => (u.wochentyp === 0) || (u.wochentyp === wochentyp) });
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 *
	 * @param idsKurs   Die IDs aller Kurse.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 */
	public unterrichtGetMengeByKursIdsAndWochentyp(idsKurs : Array<number>, wochentyp : number) : List<StundenplanUnterricht> {
		const result : ArrayList<StundenplanUnterricht> = new ArrayList();
		for (const idKurs of idsKurs)
			result.addAll(this.unterrichtGetMengeByKursIdAndWochentyp(idKurs, wochentyp));
		return result;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 *
	 * @param idKurs        Die ID des Kurses.
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public unterrichtGetMengeByKursIdAndJahrAndKW(idKurs : number, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht> {
		const wochentyp : number = this.kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return this.unterrichtGetMengeByKursIdAndWochentyp(idKurs, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge in einer bestimmten Kalenderwoche.
	 *
	 * @param idsKurs       Die IDs aller Kurse.
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge in einer bestimmten Kalenderwoche.
	 */
	public unterrichtGetMengeByKursIdsAndJahrAndKW(idsKurs : Array<number>, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht> {
		const wochentyp : number = this.kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return this.unterrichtGetMengeByKursIdsAndWochentyp(idsKurs, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 */
	public unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster : number, wochentyp : number) : List<StundenplanUnterricht> {
		return Map2DUtils.getOrCreateArrayList(this._map2d_idZeitraster_wochentyp_zu_unterrichtmenge, idZeitraster, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 * @param wochentyp  Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 */
	public unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag : Wochentag, stunde : number, wochentyp : number) : List<StundenplanUnterricht> {
		const zeitraster : StundenplanZeitraster | null = this._map2d_wochentag_stunde_zu_zeitraster.getOrNull(wochentag.id, stunde);
		return (zeitraster === null) ? new ArrayList() : this.unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(zeitraster.id, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekten, die im übergebenen Zeitraster und Wochentyp liegen.
	 * Falls der Parameter inklWoche0 TRUE ist, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 * @param wochentyp     Der Wochentyp
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekten, die im übergebenen Zeitraster und Wochentyp liegen.
	 */
	public unterrichtGetMengeByZeitrasterIdAndWochentypAndInklusiveOrEmptyList(idZeitraster : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		if ((wochentyp === 0) || (!inklWoche0))
			return this.unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster, wochentyp);
		const list : List<StundenplanUnterricht> = new ArrayList();
		list.addAll(this.unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster, wochentyp));
		list.addAll(this.unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster, 0));
		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekten, die in der Stundenplanzelle "wochentag, stunde" und "wochentyp" liegen.
	 * Falls der Parameter inklWoche0 TRUE ist, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @param wochentag     Der {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekten, die in der Stundenplanzelle "wochentag, stunde" und "wochentyp" liegen.
	 */
	public unterrichtGetMengeByWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(wochentag : Wochentag, stunde : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const idZeitraster : number = this.zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde).id;
		return this.unterrichtGetMengeByZeitrasterIdAndWochentypAndInklusiveOrEmptyList(idZeitraster, wochentyp, inklWoche0);
	}

	/**
	 * Liefert eine String-Repräsentation des das Fach- oder Kurs-Kürzel eines {@link StundenplanUnterricht}.
	 * <br>Beispiel: "M-LK1-Suffix" bei Kursen und "M" Fachkürzel bei Klassenunterricht.
	 * <br>Laufzeit: O(1)
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation des das Fach- oder Kurs-Kürzel eines {@link StundenplanUnterricht}.
	 */
	public unterrichtGetByIDStringOfFachOderKursKuerzel(idUnterricht : number) : string {
		const unterricht : StundenplanUnterricht = DeveloperNotificationException.ifMapGetIsNull(this._map_idUnterricht_zu_unterricht, idUnterricht);
		if (unterricht.idKurs === null) {
			const fach : StundenplanFach = DeveloperNotificationException.ifMapGetIsNull(this._map_idFach_zu_fach, unterricht.idFach);
			return fach.kuerzel;
		}
		const kurs : StundenplanKurs = DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_kurs, unterricht.idKurs);
		return kurs.bezeichnung;
	}

	/**
	 * Liefert eine String-Repräsentation der Klassenmenge des {@link StundenplanUnterricht}.
	 * <br>Beispiel: "5a" bei einer Klasse und "7a,7b" bei mehreren (z.B. Französisch...)
	 * <br>Laufzeit: O(1)
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Klassenmenge des {@link StundenplanUnterricht}.
	 */
	public unterrichtGetByIDStringOfKlassen(idUnterricht : number) : string {
		const unterricht : StundenplanUnterricht = DeveloperNotificationException.ifMapGetIsNull(this._map_idUnterricht_zu_unterricht, idUnterricht);
		const kuerzel : AVLSet<string> = new AVLSet();
		for (const idKlasse of unterricht.klassen) {
			const klasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._map_idKlasse_zu_klasse, idKlasse);
			kuerzel.add(klasse.kuerzel);
		}
		return StringUtils.collectionToCommaSeparatedString(kuerzel);
	}

	/**
	 * Liefert eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 * <br>Beispiel: "1.01" bei einem Raum und "T1, T2" bei mehreren (z.B. Sporthallen...)
	 * <br>Laufzeit: O(1)
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 */
	public unterrichtGetByIDStringOfRaeume(idUnterricht : number) : string {
		const unterricht : StundenplanUnterricht = DeveloperNotificationException.ifMapGetIsNull(this._map_idUnterricht_zu_unterricht, idUnterricht);
		const kuerzel : AVLSet<string> = new AVLSet();
		for (const idRaum of unterricht.raeume) {
			const raum : StundenplanRaum = DeveloperNotificationException.ifMapGetIsNull(this._map_idRaum_zu_raum, idRaum);
			kuerzel.add(raum.kuerzel);
		}
		return StringUtils.collectionToCommaSeparatedString(kuerzel);
	}

	/**
	 * Liefert die Menge aller {@link StundenplanLehrer} des {@link StundenplanUnterricht}.
	 * <br>Laufzeit: O(|Ergebnis|)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return die Menge aller {@link StundenplanLehrer} des {@link StundenplanUnterricht}.
	 */
	public unterrichtGetByIDLehrerMenge(idUnterricht : number) : List<StundenplanLehrer> {
		return MapUtils.getOrCreateArrayList(this._map_idUnterricht_zu_lehrermenge, idUnterricht);
	}

	/**
	 * Liefert die Menge aller {@link StundenplanLehrer} des {@link StundenplanUnterricht} als kommaseparierter String.
	 * <br>Laufzeit: O(|Ergebnis|)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return die Menge aller {@link StundenplanLehrer} des {@link StundenplanUnterricht} als kommaseparierter String.
	 */
	public unterrichtGetByIDLehrerMengeAsString(idUnterricht : number) : string {
		const lehrkraefteDesUnterrichts : List<StundenplanLehrer> = MapUtils.getOrCreateArrayList(this._map_idUnterricht_zu_lehrermenge, idUnterricht);
		const listeDerKuerzel : List<string> = new ArrayList();
		for (const lehkraft of lehrkraefteDesUnterrichts)
			listeDerKuerzel.add(lehkraft.kuerzel);
		return StringUtils.collectionToCommaSeparatedString(listeDerKuerzel);
	}

	/**
	 * Liefert die erste {@link StundenplanLehrer} des {@link StundenplanUnterricht} oder NULL falls nicht existent.
	 * <br>Laufzeit: O(|Ergebnis|)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 */
	public unterrichtGetByIDLehrerFirstOrNull(idUnterricht : number) : StundenplanLehrer | null {
		const lehrerDesUnterrichts : List<StundenplanLehrer> = MapUtils.getOrCreateArrayList(this._map_idUnterricht_zu_lehrermenge, idUnterricht);
		return lehrerDesUnterrichts.isEmpty() ? null : DeveloperNotificationException.ifListGetFirstFailes("lehrerDesUnterrichts.first", lehrerDesUnterrichts);
	}

	/**
	 * Liefert die erste {@link StundenplanLehrer} des {@link StundenplanUnterricht} oder NULL falls nicht existent.
	 * <br>Laufzeit: O(|Ergebnis|)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 */
	public unterrichtGetByIDLehrerFirstAsStringOrEmpty(idUnterricht : number) : string {
		const lehrkraft : StundenplanLehrer | null = this.unterrichtGetByIDLehrerFirstOrNull(idUnterricht);
		return lehrkraft === null ? "" : lehrkraft.kuerzel;
	}

	/**
	 * Liefert TRUE, falls es {@link StundenplanUnterricht} gibt, der einen Wochentyp > 0 hat.
	 * <br>Laufzeit: O(1)
	 *
	 * @return TRUE, falls es {@link StundenplanUnterricht} gibt, der einen Wochentyp > 0 hat.
	 */
	public unterrichtHatMultiWochen() : boolean {
		return this._unterrichtHatMultiWochen;
	}

	/**
	 * Aktualisiert verschiedene Werte nachdem sich die {@link StundenplanUnterricht}-Menge verändert hat.
	 */
	private unterrichtUpdate() : void {
		this._unterrichtHatMultiWochen = false;
		for (const u of this._list_unterricht)
			if (u.wochentyp > 0)
				this._unterrichtHatMultiWochen = true;
	}

	/**
	 * Liefert eine String-Menge aller Uhrzeiten der Zeitraster einer bestimmten Unterrichtsstunde. Dabei werden identische Uhrzeiten zusammengefasst.
	 * <br>Beispiel:  "08:00-8:45", falls sie nicht abweichen.
	 * <br>Beispiel:  "Mo.-Mi. 08:00-8:45", "Do. 07:55-8:40", "Fr. 07:40-8:25", falls sie abweichen.
	 *
	 * @param stunde  Die Nr. der Unterrichtsstunde.
	 *
	 * @return eine String-Menge aller Uhrzeiten der Zeitraster einer bestimmten Unterrichtsstunde. Dabei werden identische Uhrzeiten zusammengefasst.
	 */
	public unterrichtsstundeGetUhrzeitenAsStrings(stunde : number) : List<string> {
		const listUhrzeit : List<string> = new ArrayList();
		const listWochentagVon : List<string> = new ArrayList();
		const listWochentagBis : List<string> = new ArrayList();
		for (let wochentag : number = this._zeitrasterWochentagMin; wochentag <= this._zeitrasterWochentagMax; wochentag++) {
			const sUhrzeit : string = this.unterrichtsstundeGetUhrzeitAsString(wochentag, stunde);
			const sWochentag : string = Wochentag.fromIDorException(wochentag).kuerzel;
			if (listUhrzeit.isEmpty()) {
				listUhrzeit.add(sUhrzeit);
				listWochentagVon.add(sWochentag);
				listWochentagBis.add(sWochentag);
				continue;
			}
			const sUhrzeitDavor : string = DeveloperNotificationException.ifListGetLastFailes("listUhrzeit", listUhrzeit);
			if (JavaObject.equalsTranspiler(sUhrzeitDavor, (sUhrzeit))) {
				listWochentagBis.set(listWochentagBis.size() - 1, sWochentag);
			} else {
				listUhrzeit.add(sUhrzeit);
				listWochentagVon.add(sWochentag);
				listWochentagBis.add(sWochentag);
			}
		}
		if (listUhrzeit.size() <= 1)
			return listUhrzeit;
		for (let i : number = 0; i < listUhrzeit.size(); i++) {
			const sUhrzeit : string = listUhrzeit.get(i);
			const sWochentagVon : string = listWochentagVon.get(i);
			const sWochentagBis : string = listWochentagBis.get(i);
			if (JavaObject.equalsTranspiler(sWochentagVon, (sWochentagBis)))
				listUhrzeit.set(i, sWochentagVon! + ". " + sUhrzeit!);
			else
				listUhrzeit.set(i, sWochentagVon! + ".-" + sWochentagBis! + ". " + sUhrzeit!);
		}
		return listUhrzeit;
	}

	private unterrichtsstundeGetUhrzeitAsString(wochentag : number, stunde : number) : string {
		const zeitraster : StundenplanZeitraster | null = this._map2d_wochentag_stunde_zu_zeitraster.getOrNull(wochentag, stunde);
		if (zeitraster === null)
			return "???";
		const sBeginn : string = (zeitraster.stundenbeginn === null) ? "??:??" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenbeginn);
		const sEnde : string = (zeitraster.stundenende === null) ? "??:??" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenende);
		return sBeginn! + " - " + sEnde! + " Uhr";
	}

	private zeitrasterAddOhneUpdate(zeitraster : StundenplanZeitraster) : void {
		DeveloperNotificationException.ifInvalidID("zeit.id", zeitraster.id);
		Wochentag.fromIDorException(zeitraster.wochentag);
		DeveloperNotificationException.ifTrue("(zeit.unterrichtstunde < 0) || (zeit.unterrichtstunde > 29)", (zeitraster.unterrichtstunde < 0) || (zeitraster.unterrichtstunde > 29));
		if ((zeitraster.stundenbeginn !== null) && (zeitraster.stundenende !== null)) {
			const beginn : number = zeitraster.stundenbeginn.valueOf();
			const ende : number = zeitraster.stundenende.valueOf();
			DeveloperNotificationException.ifTrue("beginn >= ende", beginn >= ende);
		}
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idZeitraster_zu_zeitraster, zeitraster.id, zeitraster);
		DeveloperNotificationException.ifMap2DPutOverwrites(this._map2d_wochentag_stunde_zu_zeitraster, zeitraster.wochentag, zeitraster.unterrichtstunde, zeitraster);
		MapUtils.getOrCreateArrayList(this._map_wochentag_zu_zeitrastermenge, zeitraster.wochentag).add(zeitraster);
		MapUtils.getOrCreateArrayList(this._map_stunde_zu_zeitrastermenge, zeitraster.unterrichtstunde).add(zeitraster);
		this._list_zeitraster.add(zeitraster);
	}

	/**
	 * Fügt ein {@link StundenplanZeitraster}-Objekt hinzu.
	 * <br>Laufzeit: O(|Zeitraster| * log ), da zeitrasterUpdate() aufgerufen wird.
	 *
	 * @param zeitraster  Das {@link StundenplanZeitraster}-Objekt, welches hinzugefügt werden soll.
	 */
	public zeitrasterAdd(zeitraster : StundenplanZeitraster) : void {
		this.zeitrasterAddOhneUpdate(zeitraster);
		this.zeitrasterUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanZeitraster}-Objekte hinzu.
	 * <br>Laufzeit: O(|Zeitraster| * log ), da zeitrasterUpdate() aufgerufen wird.
	 *
	 * @param listZeitraster  Die Menge der {@link StundenplanZeitraster}-Objekte, welche hinzugefügt werden soll.
	 */
	public zeitrasterAddAll(listZeitraster : List<StundenplanZeitraster>) : void {
		for (const zeitraster of listZeitraster)
			this.zeitrasterAddOhneUpdate(zeitraster);
		this.zeitrasterUpdate();
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @param stunde  Die Unterrichtsstunde, deren Minimum gesucht wird.
	 *
	 * @return das Minimum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public zeitrasterGetMinutenMinDerStunde(stunde : number) : number {
		const min : number | null = this._zeitrasterMinutenMinByStunde.get(stunde);
		return (min === null) ? 480 : min;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @param stunde  Die Unterrichtsstunde, deren Maximum gesucht wird.
	 *
	 * @return das Maximum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public zeitrasterGetMinutenMaxDerStunde(stunde : number) : number {
		const max : number | null = this._zeitrasterMinutenMaxByStunde.get(stunde);
		return (max === null) ? 480 : max;
	}

	/**
	 * Liefert die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetStundeMin() : number {
		return this._zeitrasterStundeMin;
	}

	/**
	 * Liefert die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetStundeMax() : number {
		return this._zeitrasterStundeMax;
	}

	/**
	 * Liefert die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetWochentagMin() : number {
		return this._zeitrasterWochentagMin;
	}

	/**
	 * Liefert den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetWochentagMinEnum() : Wochentag {
		return Wochentag.fromIDorException(this._zeitrasterWochentagMin);
	}

	/**
	 * Liefert die ID des größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetWochentagMax() : number {
		return this._zeitrasterWochentagMax;
	}

	/**
	 * Liefert den größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetWochentagMaxEnum() : Wochentag {
		return Wochentag.fromIDorException(this._zeitrasterWochentagMax);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 */
	public zeitrasterGetByIdOrException(idZeitraster : number) : StundenplanZeitraster {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idZeitraster_zu_zeitraster, idZeitraster);
	}

	/**
	 * Liefert die Beginn-Uhrzeit des {@link StundenplanZeitraster} oder den leeren String, falls diese NULL ist.
	 * <br>Beispiel: "09:30" oder ""
	 * <br>Laufzeit: O(1)
	 *
	 * @param idZeitraster  Die Datenbank-ID des {@link StundenplanZeitraster}.
	 *
	 * @return die Beginn-Uhrzeit des {@link StundenplanZeitraster} oder den leeren String, falls diese NULL ist.
	 */
	public zeitrasterGetByIdStringOfUhrzeitBeginn(idZeitraster : number) : string {
		const zeitraster : StundenplanZeitraster = DeveloperNotificationException.ifMapGetIsNull(this._map_idZeitraster_zu_zeitraster, idZeitraster);
		return (zeitraster.stundenbeginn === null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenbeginn);
	}

	/**
	 * Liefert die End-Uhrzeit des {@link StundenplanZeitraster} oder den leeren String, falls diese NULL ist.
	 * <br>Beispiel: "10:15" oder ""
	 * <br>Laufzeit: O(1)
	 *
	 * @param idZeitraster  Die Datenbank-ID des {@link StundenplanZeitraster}.
	 *
	 * @return die End-Uhrzeit des {@link StundenplanZeitraster} oder den leeren String, falls diese NULL ist.
	 */
	public zeitrasterGetByIdStringOfUhrzeitEnde(idZeitraster : number) : string {
		const zeitraster : StundenplanZeitraster = DeveloperNotificationException.ifMapGetIsNull(this._map_idZeitraster_zu_zeitraster, idZeitraster);
		return (zeitraster.stundenende === null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenende);
	}

	/**
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt, falls es existiert, sonst NULL.
	 *
	 * @param wochentag  Die ENUM-ID des {@link Wochentag} des gesuchten Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde des gesuchten Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt, falls es existiert, sonst NULL.
	 */
	public zeitrasterGetByWochentagAndStundeOrException(wochentag : number, stunde : number) : StundenplanZeitraster {
		return this._map2d_wochentag_stunde_zu_zeitraster.getNonNullOrException(wochentag, stunde);
	}

	/**
	 * Liefert TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 *
	 * @param beginn1  Der Anfang (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param ende1    Das Ende (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iBeginn2 Der Anfang (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iEnde2   Das Ende (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 *
	 * @return TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 */
	public zeitrasterGetSchneidenSich(beginn1 : number, ende1 : number, iBeginn2 : number | null, iEnde2 : number | null) : boolean {
		const beginn2 : number = DeveloperNotificationException.ifNull("zeitraster.stundenbeginn ist NULL!", iBeginn2).valueOf();
		const ende2 : number = DeveloperNotificationException.ifNull("zeitraster.stundenende ist NULL!", iEnde2).valueOf();
		DeveloperNotificationException.ifTrue("beginn1 < 0", beginn1 < 0);
		DeveloperNotificationException.ifTrue("beginn2 < 0", beginn2 < 0);
		DeveloperNotificationException.ifTrue("beginn1 > ende1", beginn1 > ende1);
		DeveloperNotificationException.ifTrue("beginn2 > ende2", beginn2 > ende2);
		return !((ende1 < beginn2) || (ende2 < beginn1));
	}

	/**
	 * Liefert alle verwendeten sortierten Unterrichtsstunden der {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle Zahlen von {@link #zeitrasterGetStundeMin()} bis {@link #zeitrasterGetStundeMax()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten Unterrichtsstunden der {@link StundenplanZeitraster}.
	 */
	public zeitrasterGetStundenRange() : Array<number> {
		return this._zeitrasterStundenRange;
	}

	/**
	 * Liefert alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle {@link Wochentag}-Objekte von {@link #zeitrasterGetWochentagMin} bis {@link #zeitrasterGetWochentagMax()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanZeitraster}.
	 */
	public zeitrasterGetWochentageAlsEnumRange() : Array<Wochentag> {
		return this._zeitrasterWochentageAlsEnumRange;
	}

	/**
	 * Liefert TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 */
	public zeitrasterHatUnterrichtMitWochentyp0(idZeitraster : number) : boolean {
		return !Map2DUtils.getOrCreateArrayList(this._map2d_idZeitraster_wochentyp_zu_unterrichtmenge, idZeitraster, 0).isEmpty();
	}

	/**
	 * Liefert TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 *
	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 */
	public zeitrasterHatUnterrichtMitWochentyp0ByWochentagAndStunde(wochentag : Wochentag, stunde : number) : boolean {
		const zeitraster : StundenplanZeitraster | null = this._map2d_wochentag_stunde_zu_zeitraster.getOrNull(wochentag.id, stunde);
		return (zeitraster !== null) && this.zeitrasterHatUnterrichtMitWochentyp0(zeitraster.id);
	}

	/**
	 * Liefert TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 1 bis N gibt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 1 bis N gibt.
	 */
	public zeitrasterHatUnterrichtMitWochentyp1BisN(idZeitraster : number) : boolean {
		for (let wochentyp : number = 1; wochentyp <= this.stundenplanWochenTypModell; wochentyp++)
			if (!Map2DUtils.getOrCreateArrayList(this._map2d_idZeitraster_wochentyp_zu_unterrichtmenge, idZeitraster, wochentyp).isEmpty())
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 1 bis N gibt.
	 *
	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 1 bis N gibt.
	 */
	public zeitrasterHatUnterrichtMitWochentyp1BisNByWochentagAndStunde(wochentag : Wochentag, stunde : number) : boolean {
		const zeitraster : StundenplanZeitraster | null = this._map2d_wochentag_stunde_zu_zeitraster.getOrNull(wochentag.id, stunde);
		return (zeitraster !== null) && this.zeitrasterHatUnterrichtMitWochentyp1BisN(zeitraster.id);
	}

	/**
	 * Liefert TRUE, falls es in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" gibt.
	 *
	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 * @param wochentyp  Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return TRUE, falls es in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" gibt.
	 */
	public zeitrasterHatUnterrichtByWochentagAndStundeAndWochentyp(wochentag : Wochentag, stunde : number, wochentyp : number) : boolean {
		const zeitraster : StundenplanZeitraster | null = this._map2d_wochentag_stunde_zu_zeitraster.getOrNull(wochentag.id, stunde);
		if (zeitraster === null)
			return false;
		return !Map2DUtils.getOrCreateArrayList(this._map2d_idZeitraster_wochentyp_zu_unterrichtmenge, zeitraster.id, wochentyp).isEmpty();
	}

	/**
	 * Liefert TRUE, falls zu (wochentag, stunde) ein zugehöriges {@link StundenplanZeitraster}-Objekt existiert.
	 *
	 * @param wochentag  Der ENUM-ID des {@link Wochentag} des Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde des Zeitrasters.
	 *
	 * @return TRUE, falls zu (wochentag, stunde) ein zugehöriges {@link StundenplanZeitraster}-Objekt existiert.
	 */
	public zeitrasterExistsByWochentagAndStunde(wochentag : number, stunde : number) : boolean {
		return this._map2d_wochentag_stunde_zu_zeitraster.contains(wochentag, stunde);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanZeitraster}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param zeitraster  Das neue {@link StundenplanZeitraster}-Objekt, welches das alte Objekt ersetzt.
	 */
	public zeitrasterPatch(zeitraster : StundenplanZeitraster) : void {
		this.zeitrasterRemoveOhneUpdate(zeitraster.id);
		this.zeitrasterAddOhneUpdate(zeitraster);
		this.zeitrasterUpdate();
	}

	private zeitrasterRemoveOhneUpdate(idZeitraster : number) : void {
		const z : StundenplanZeitraster = DeveloperNotificationException.ifNull("_map_zeitrasterID_zu_zeitraster.get(" + idZeitraster + ")", this._map_idZeitraster_zu_zeitraster.get(idZeitraster));
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idZeitraster_zu_zeitraster, idZeitraster);
		DeveloperNotificationException.ifMap2DRemoveFailes(this._map2d_wochentag_stunde_zu_zeitraster, z.wochentag, z.unterrichtstunde);
		MapUtils.removeFromListAndTrimOrException(this._map_wochentag_zu_zeitrastermenge, z.wochentag, z);
		MapUtils.removeFromListAndTrimOrException(this._map_stunde_zu_zeitrastermenge, z.unterrichtstunde, z);
		DeveloperNotificationException.ifListRemoveFailes("_list_zeitraster", this._list_zeitraster, z);
	}

	/**
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des {@link StundenplanZeitraster}-Objekts.
	 */
	public zeitrasterRemove(idZeitraster : number) : void {
		this.zeitrasterRemoveOhneUpdate(idZeitraster);
		this.zeitrasterUpdate();
	}

	/**
	 * Aktualisiert verschiedene Werte nachdem sich die Menge der Zeitraster verändert hat.
	 * <br>Laufzeit: O(|Zeitraster| * log)
	 */
	private zeitrasterUpdate() : void {
		this._list_zeitraster.sort(StundenplanManager._compZeitraster);
		this._pausenzeitUndZeitrasterMinutenMin = StundenplanManager.MINUTEN_INF_POS;
		this._pausenzeitUndZeitrasterMinutenMax = StundenplanManager.MINUTEN_INF_NEG;
		this._zeitrasterMinutenMin = StundenplanManager.MINUTEN_INF_POS;
		this._zeitrasterMinutenMax = StundenplanManager.MINUTEN_INF_NEG;
		this._zeitrasterWochentagMin = StundenplanManager.WOCHENTAG_INF_POS;
		this._zeitrasterWochentagMax = StundenplanManager.WOCHENTAG_INF_NEG;
		this._zeitrasterStundeMin = StundenplanManager.STUNDE_INF_POS;
		this._zeitrasterStundeMax = StundenplanManager.STUNDE_INF_NEG;
		this._zeitrasterMinutenMinByStunde.clear();
		this._zeitrasterMinutenMaxByStunde.clear();
		for (const z of this._list_zeitraster) {
			this._pausenzeitUndZeitrasterMinutenMin = BlockungsUtils.minVI(this._pausenzeitUndZeitrasterMinutenMin, z.stundenbeginn);
			this._pausenzeitUndZeitrasterMinutenMax = BlockungsUtils.maxVI(this._pausenzeitUndZeitrasterMinutenMax, z.stundenende);
			this._zeitrasterMinutenMin = BlockungsUtils.minVI(this._zeitrasterMinutenMin, z.stundenbeginn);
			this._zeitrasterMinutenMax = BlockungsUtils.maxVI(this._zeitrasterMinutenMax, z.stundenende);
			this._zeitrasterWochentagMin = BlockungsUtils.minVI(this._zeitrasterWochentagMin, z.wochentag);
			this._zeitrasterWochentagMax = BlockungsUtils.maxVI(this._zeitrasterWochentagMax, z.wochentag);
			this._zeitrasterStundeMin = BlockungsUtils.minVI(this._zeitrasterStundeMin, z.unterrichtstunde);
			this._zeitrasterStundeMax = BlockungsUtils.maxVI(this._zeitrasterStundeMax, z.unterrichtstunde);
			this._zeitrasterMinutenMinByStunde.put(z.unterrichtstunde, BlockungsUtils.minII(this._zeitrasterMinutenMinByStunde.get(z.unterrichtstunde), z.stundenbeginn));
			this._zeitrasterMinutenMaxByStunde.put(z.unterrichtstunde, BlockungsUtils.maxII(this._zeitrasterMinutenMaxByStunde.get(z.unterrichtstunde), z.stundenende));
		}
		for (const p of this._list_pausenzeiten) {
			this._pausenzeitUndZeitrasterMinutenMin = BlockungsUtils.minVI(this._pausenzeitUndZeitrasterMinutenMin, p.beginn);
			this._pausenzeitUndZeitrasterMinutenMax = BlockungsUtils.maxVI(this._pausenzeitUndZeitrasterMinutenMax, p.ende);
		}
		this._pausenzeitUndZeitrasterMinutenMin = (this._pausenzeitUndZeitrasterMinutenMin === StundenplanManager.MINUTEN_INF_POS) ? 480 : this._pausenzeitUndZeitrasterMinutenMin;
		this._pausenzeitUndZeitrasterMinutenMax = (this._pausenzeitUndZeitrasterMinutenMax === StundenplanManager.MINUTEN_INF_NEG) ? 480 : this._pausenzeitUndZeitrasterMinutenMax;
		this._zeitrasterMinutenMin = (this._zeitrasterMinutenMin === StundenplanManager.MINUTEN_INF_POS) ? 480 : this._zeitrasterMinutenMin;
		this._zeitrasterMinutenMax = (this._zeitrasterMinutenMax === StundenplanManager.MINUTEN_INF_NEG) ? 480 : this._zeitrasterMinutenMax;
		this._zeitrasterWochentagMin = (this._zeitrasterWochentagMin === StundenplanManager.WOCHENTAG_INF_POS) ? Wochentag.MONTAG.id : this._zeitrasterWochentagMin;
		this._zeitrasterWochentagMax = (this._zeitrasterWochentagMax === StundenplanManager.WOCHENTAG_INF_NEG) ? Wochentag.MONTAG.id : this._zeitrasterWochentagMax;
		this._zeitrasterStundeMin = (this._zeitrasterStundeMin === StundenplanManager.STUNDE_INF_POS) ? 1 : this._zeitrasterStundeMin;
		this._zeitrasterStundeMax = (this._zeitrasterStundeMax === StundenplanManager.STUNDE_INF_NEG) ? 1 : this._zeitrasterStundeMax;
		this._zeitrasterStundenRange = Array(this._zeitrasterStundeMax - this._zeitrasterStundeMin + 1).fill(0);
		for (let i : number = 0; i < this._zeitrasterStundenRange.length; i++)
			this._zeitrasterStundenRange[i] = this._zeitrasterStundeMin + i;
		this._zeitrasterWochentageAlsEnumRange = Array(this._zeitrasterWochentagMax - this._zeitrasterWochentagMin + 1).fill(null);
		for (let i : number = 0; i < this._zeitrasterWochentageAlsEnumRange.length; i++)
			this._zeitrasterWochentageAlsEnumRange[i] = Wochentag.fromIDorException(this._zeitrasterWochentagMin + i);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplan.StundenplanManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanManager(obj : unknown) : StundenplanManager {
	return obj as StundenplanManager;
}
