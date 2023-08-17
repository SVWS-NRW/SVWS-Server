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
import { StundenplanKlassenunterricht } from '../../../core/data/stundenplan/StundenplanKlassenunterricht';
import { StundenplanLehrer } from '../../../core/data/stundenplan/StundenplanLehrer';
import { StringUtils } from '../../../core/utils/StringUtils';
import { StundenplanUnterricht, cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterricht } from '../../../core/data/stundenplan/StundenplanUnterricht';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { StundenplanKalenderwochenzuordnung } from '../../../core/data/stundenplan/StundenplanKalenderwochenzuordnung';
import { Stundenplan, cast_de_svws_nrw_core_data_stundenplan_Stundenplan } from '../../../core/data/stundenplan/Stundenplan';
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
import { ListUtils } from '../../../core/utils/ListUtils';
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

	private static readonly _compJahrgang : Comparator<StundenplanJahrgang> = { compare : (a: StundenplanJahrgang, b: StundenplanJahrgang) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

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

	private static readonly _compKlasse : Comparator<StundenplanKlasse> = { compare : (a: StundenplanKlasse, b: StundenplanKlasse) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _list_klassen : List<StundenplanKlasse> = new ArrayList();

	private readonly _map_idKlasse_zu_klasse : HashMap<number, StundenplanKlasse> = new HashMap();

	private readonly _map_idKlasse_zu_klassenunterricht : HashMap<number, List<StundenplanKlassenunterricht>> = new HashMap();

	private readonly _map_idKlasse_zu_unterrichtmenge : HashMap<number, List<StundenplanUnterricht>> = new HashMap();

	private static readonly _compKlassenunterricht : Comparator<StundenplanKlassenunterricht> = { compare : (a: StundenplanKlassenunterricht, b: StundenplanKlassenunterricht) => {
		if (a.idKlasse < b.idKlasse)
			return -1;
		if (a.idKlasse > b.idKlasse)
			return +1;
		if (a.idFach < b.idFach)
			return -1;
		if (a.idFach > b.idFach)
			return +1;
		if (a.wochenstunden < b.wochenstunden)
			return -1;
		if (a.wochenstunden > b.wochenstunden)
			return +1;
		return JavaString.compareTo(a.bezeichnung, b.bezeichnung);
	} };

	private readonly _list_klassenunterricht : List<StundenplanKlassenunterricht> = new ArrayList();

	private readonly _map2d_idKlasse_idFach_zu_klassenunterricht : HashMap2D<number, number, StundenplanKlassenunterricht> = new HashMap2D();

	private readonly _map2d_idKlasse_idFach_zu_unterrichtmenge : HashMap2D<number, number, List<StundenplanUnterricht>> = new HashMap2D();

	private static readonly _compKurs : Comparator<StundenplanKurs> = { compare : (a: StundenplanKurs, b: StundenplanKurs) => JavaLong.compare(a.id, b.id) };

	private readonly _list_kurse : List<StundenplanKurs> = new ArrayList();

	private readonly _map_idKurs_zu_kurs : HashMap<number, StundenplanKurs> = new HashMap();

	private readonly _map_idKurs_zu_unterrichtmenge : HashMap<number, List<StundenplanUnterricht>> = new HashMap();

	private static readonly _compLehrer : Comparator<StundenplanLehrer> = { compare : (a: StundenplanLehrer, b: StundenplanLehrer) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _list_lehrer : List<StundenplanLehrer> = new ArrayList();

	private readonly _map_idLehrer_zu_lehrer : HashMap<number, StundenplanLehrer> = new HashMap();

	private static readonly _compPausenaufsicht : Comparator<StundenplanPausenaufsicht> = { compare : (a: StundenplanPausenaufsicht, b: StundenplanPausenaufsicht) => JavaLong.compare(a.id, b.id) };

	private readonly _list_pausenaufsichten : List<StundenplanPausenaufsicht> = new ArrayList();

	private readonly _map_idPausenaufsicht_zu_pausenaufsicht : HashMap<number, StundenplanPausenaufsicht> = new HashMap();

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

	private readonly _map_idPausenzeit_zu_pausenaufsichtmenge : HashMap<number, List<StundenplanPausenaufsicht>> = new HashMap();

	private static readonly _compRaum : Comparator<StundenplanRaum> = { compare : (a: StundenplanRaum, b: StundenplanRaum) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _list_raeume : List<StundenplanRaum> = new ArrayList();

	private readonly _map_idRaum_zu_raum : HashMap<number, StundenplanRaum> = new HashMap();

	private static readonly _compSchiene : Comparator<StundenplanSchiene> = { compare : (a: StundenplanSchiene, b: StundenplanSchiene) => {
		if (a.idJahrgang < b.idJahrgang)
			return -1;
		if (a.idJahrgang > b.idJahrgang)
			return +1;
		if (a.nummer < b.nummer)
			return -1;
		if (a.nummer > b.nummer)
			return +1;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _list_schienen : List<StundenplanSchiene> = new ArrayList();

	private readonly _map_idSchiene_zu_schiene : HashMap<number, StundenplanSchiene> = new HashMap();

	private static readonly _compSchueler : Comparator<StundenplanSchueler> = { compare : (a: StundenplanSchueler, b: StundenplanSchueler) => {
		if (a.idKlasse < b.idKlasse)
			return -1;
		if (a.idKlasse > b.idKlasse)
			return +1;
		const cmpNachname : number = JavaString.compareTo(a.nachname, b.nachname);
		if (cmpNachname !== 0)
			return cmpNachname;
		const cmpVorname : number = JavaString.compareTo(a.vorname, b.vorname);
		if (cmpVorname !== 0)
			return cmpVorname;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _list_schueler : List<StundenplanSchueler> = new ArrayList();

	private readonly _map_schuelerID_zu_schueler : HashMap<number, StundenplanSchueler> = new HashMap();

	private static readonly _compUnterricht : Comparator<StundenplanUnterricht> = { compare : (a: StundenplanUnterricht, b: StundenplanUnterricht) => JavaLong.compare(a.id, b.id) };

	private readonly _list_unterricht : List<StundenplanUnterricht> = new ArrayList();

	private readonly _map_idUnterricht_zu_unterricht : HashMap<number, StundenplanUnterricht> = new HashMap();

	private readonly _map2d_idZeitraster_wochentyp_zu_unterrichtmenge : HashMap2D<number, number, List<StundenplanUnterricht>> = new HashMap2D();

	private readonly _map_idUnterricht_zu_lehrermenge : HashMap<number, List<StundenplanLehrer>> = new HashMap();

	private _uUnterrichtHatMultiWochen : boolean = false;

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

	private readonly _map_idZeitraster_zu_unterrichtmenge : HashMap<number, List<StundenplanUnterricht>> = new HashMap();

	private readonly _map2d_wochentag_stunde_zu_zeitraster : HashMap2D<number, number, StundenplanZeitraster> = new HashMap2D();

	private readonly _map_wochentag_zu_zeitrastermenge : HashMap<number, List<StundenplanZeitraster>> = new HashMap();

	private readonly _map_stunde_zu_zeitrastermenge : HashMap<number, List<StundenplanZeitraster>> = new HashMap();

	private readonly _stundenplanID : number;

	private readonly _stundenplanWochenTypModell : number;

	private readonly _stundenplanSchuljahresAbschnittID : number;

	private readonly _stundenplanGueltigAb : string;

	private readonly _stundenplanGueltigBis : string;

	private readonly _stundenplanBezeichnung : string;

	private _uZeitrasterStundenRange : Array<number> = [1];

	private _uZeitrasterStundenRangeOhneLeere : Array<number> = [1];

	private readonly _uZeitrasterMinutenMinByStunde : HashMap<number, number | null> = new HashMap();

	private readonly _uZeitrasterMinutenMaxByStunde : HashMap<number, number | null> = new HashMap();

	private _uZeitrasterWochentageAlsEnumRange : Array<Wochentag> = [Wochentag.MONTAG];

	private _uZeitrasterWochentagMin : number = Wochentag.MONTAG.id;

	private _uZeitrasterWochentagMax : number = Wochentag.MONTAG.id;

	private _uZeitrasterStundeMin : number = 1;

	private _uZeitrasterStundeMax : number = 1;

	private _uZeitrasterStundeMinOhneLeere : number = 1;

	private _uZeitrasterStundeMaxOhneLeere : number = 1;

	private _uZeitrasterMinutenMin : number = 480;

	private _uZeitrasterMinutenMax : number = 480;

	private _uPausenzeitMinutenMin : number = 480;

	private _uPausenzeitMinutenMax : number = 480;

	private _uPausenzeitUndZeitrasterMinutenMin : number = 480;

	private _uPausenzeitUndZeitrasterMinutenMax : number = 480;

	private _uPausenzeitUndZeitrasterMinutenMinOhneLeere : number = 480;

	private _uPausenzeitUndZeitrasterMinutenMaxOhneLeere : number = 480;

	private readonly _uPausenzeitMapByWochentag : HashMap<number, List<StundenplanPausenzeit>> = new HashMap();

	private readonly _uPausenaufsichtMapByWochentag : HashMap<number, List<StundenplanPausenaufsicht>> = new HashMap();

	private readonly _uKursMapByKlasseId : HashMap<number, List<StundenplanKurs>> = new HashMap();

	private readonly _uKursMapByLehrerId : HashMap<number, List<StundenplanKurs>> = new HashMap();

	private readonly _uPausenzeitListNichtLeere : List<StundenplanPausenzeit> = new ArrayList();


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
			this._stundenplanID = daten.id;
			this._stundenplanWochenTypModell = daten.wochenTypModell;
			this._stundenplanSchuljahresAbschnittID = daten.idSchuljahresabschnitt;
			this._stundenplanGueltigAb = daten.gueltigAb;
			this._stundenplanGueltigBis = daten.gueltigBis;
			this._stundenplanBezeichnung = daten.bezeichnungStundenplan;
			let uv : StundenplanUnterrichtsverteilung | null = unterrichtsverteilung;
			if (uv === null) {
				uv = new StundenplanUnterrichtsverteilung();
				uv.id = this._stundenplanID;
			}
			DeveloperNotificationException.ifTrue("Stundenplan.id != StundenplanUnterrichtsverteilung.id", daten.id !== uv.id);
			this.initAll(daten.kalenderwochenZuordnung, uv.faecher, daten.jahrgaenge, daten.zeitraster, daten.raeume, daten.pausenzeiten, daten.aufsichtsbereiche, uv.lehrer, uv.schueler, daten.schienen, uv.klassen, uv.klassenunterricht, pausenaufsichten, uv.kurse, unterrichte);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanKomplett')))) && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined")) {
			const stundenplanKomplett : StundenplanKomplett = cast_de_svws_nrw_core_data_stundenplan_StundenplanKomplett(__param0);
			this._stundenplanID = stundenplanKomplett.daten.id;
			this._stundenplanWochenTypModell = stundenplanKomplett.daten.wochenTypModell;
			this._stundenplanSchuljahresAbschnittID = stundenplanKomplett.daten.idSchuljahresabschnitt;
			this._stundenplanGueltigAb = stundenplanKomplett.daten.gueltigAb;
			this._stundenplanGueltigBis = stundenplanKomplett.daten.gueltigBis;
			this._stundenplanBezeichnung = stundenplanKomplett.daten.bezeichnungStundenplan;
			DeveloperNotificationException.ifTrue("Stundenplan.id != StundenplanUnterrichtsverteilung.id", stundenplanKomplett.daten.id !== stundenplanKomplett.unterrichtsverteilung.id);
			this.initAll(stundenplanKomplett.daten.kalenderwochenZuordnung, stundenplanKomplett.unterrichtsverteilung.faecher, stundenplanKomplett.daten.jahrgaenge, stundenplanKomplett.daten.zeitraster, stundenplanKomplett.daten.raeume, stundenplanKomplett.daten.pausenzeiten, stundenplanKomplett.daten.aufsichtsbereiche, stundenplanKomplett.unterrichtsverteilung.lehrer, stundenplanKomplett.unterrichtsverteilung.schueler, stundenplanKomplett.daten.schienen, stundenplanKomplett.unterrichtsverteilung.klassen, stundenplanKomplett.unterrichtsverteilung.klassenunterricht, stundenplanKomplett.pausenaufsichten, stundenplanKomplett.unterrichtsverteilung.kurse, stundenplanKomplett.unterrichte);
		} else throw new Error('invalid method overload');
	}

	private initAll(listKWZ : List<StundenplanKalenderwochenzuordnung>, listFach : List<StundenplanFach>, listJahrgang : List<StundenplanJahrgang>, listZeitraster : List<StundenplanZeitraster>, listRaum : List<StundenplanRaum>, listPausenzeit : List<StundenplanPausenzeit>, listAufsichtsbereich : List<StundenplanAufsichtsbereich>, listLehrer : List<StundenplanLehrer>, listSchueler : List<StundenplanSchueler>, listSchiene : List<StundenplanSchiene>, listKlasse : List<StundenplanKlasse>, listKlassenunterricht : List<StundenplanKlassenunterricht>, listPausenaufsicht : List<StundenplanPausenaufsicht>, listKurs : List<StundenplanKurs>, listUnterricht : List<StundenplanUnterricht>) : void {
		DeveloperNotificationException.ifTrue("stundenplanWochenTypModell < 0", this._stundenplanWochenTypModell < 0);
		DeveloperNotificationException.ifTrue("stundenplanWochenTypModell == 1", this._stundenplanWochenTypModell === 1);
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
		this.klassenunterrichtAddAll(listKlassenunterricht);
		this.pausenaufsichtAddAll(listPausenaufsicht);
		this.kursAddAll(listKurs);
		this.unterrichtAddAll(listUnterricht);
	}

	private update() : void {
		this.updateIteriereUnterricht();
		this._uPausenzeitMinutenMin = StundenplanManager.MINUTEN_INF_POS;
		this._uPausenzeitMinutenMax = StundenplanManager.MINUTEN_INF_NEG;
		this._uPausenzeitUndZeitrasterMinutenMin = StundenplanManager.MINUTEN_INF_POS;
		this._uPausenzeitUndZeitrasterMinutenMax = StundenplanManager.MINUTEN_INF_NEG;
		this._uPausenzeitUndZeitrasterMinutenMinOhneLeere = StundenplanManager.MINUTEN_INF_POS;
		this._uPausenzeitUndZeitrasterMinutenMaxOhneLeere = StundenplanManager.MINUTEN_INF_NEG;
		this._uPausenzeitMapByWochentag.clear();
		this._uPausenaufsichtMapByWochentag.clear();
		this._uPausenzeitListNichtLeere.clear();
		this._uZeitrasterMinutenMin = StundenplanManager.MINUTEN_INF_POS;
		this._uZeitrasterMinutenMax = StundenplanManager.MINUTEN_INF_NEG;
		this._uZeitrasterWochentagMin = StundenplanManager.WOCHENTAG_INF_POS;
		this._uZeitrasterWochentagMax = StundenplanManager.WOCHENTAG_INF_NEG;
		this._uZeitrasterStundeMin = StundenplanManager.STUNDE_INF_POS;
		this._uZeitrasterStundeMax = StundenplanManager.STUNDE_INF_NEG;
		this._uZeitrasterStundeMinOhneLeere = StundenplanManager.STUNDE_INF_POS;
		this._uZeitrasterStundeMaxOhneLeere = StundenplanManager.STUNDE_INF_NEG;
		this._uZeitrasterMinutenMinByStunde.clear();
		this._uZeitrasterMinutenMaxByStunde.clear();
		for (const a of this._list_pausenaufsichten) {
			const p : StundenplanPausenzeit = DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenzeit_zu_pausenzeit, a.idPausenzeit);
			MapUtils.getOrCreateArrayList(this._uPausenaufsichtMapByWochentag, p.wochentag).add(a);
		}
		for (const p of this._list_pausenzeiten) {
			MapUtils.getOrCreateArrayList(this._uPausenzeitMapByWochentag, p.wochentag).add(p);
			this._uPausenzeitMinutenMin = BlockungsUtils.minVI(this._uPausenzeitMinutenMin, p.beginn);
			this._uPausenzeitMinutenMax = BlockungsUtils.maxVI(this._uPausenzeitMinutenMax, p.ende);
			this._uPausenzeitUndZeitrasterMinutenMin = BlockungsUtils.minVI(this._uPausenzeitUndZeitrasterMinutenMin, p.beginn);
			this._uPausenzeitUndZeitrasterMinutenMax = BlockungsUtils.maxVI(this._uPausenzeitUndZeitrasterMinutenMax, p.ende);
			const listPA : List<StundenplanPausenaufsicht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenzeit_zu_pausenaufsichtmenge, p.id);
			if (!listPA.isEmpty()) {
				this._uPausenzeitUndZeitrasterMinutenMinOhneLeere = BlockungsUtils.minVI(this._uPausenzeitUndZeitrasterMinutenMinOhneLeere, p.beginn);
				this._uPausenzeitUndZeitrasterMinutenMaxOhneLeere = BlockungsUtils.maxVI(this._uPausenzeitUndZeitrasterMinutenMaxOhneLeere, p.ende);
				this._uPausenzeitListNichtLeere.add(p);
			}
		}
		for (const z of this._list_zeitraster) {
			this._uZeitrasterWochentagMin = BlockungsUtils.minVI(this._uZeitrasterWochentagMin, z.wochentag);
			this._uZeitrasterWochentagMax = BlockungsUtils.maxVI(this._uZeitrasterWochentagMax, z.wochentag);
			this._uZeitrasterStundeMin = BlockungsUtils.minVI(this._uZeitrasterStundeMin, z.unterrichtstunde);
			this._uZeitrasterStundeMax = BlockungsUtils.maxVI(this._uZeitrasterStundeMax, z.unterrichtstunde);
			this._uZeitrasterMinutenMinByStunde.put(z.unterrichtstunde, BlockungsUtils.minII(this._uZeitrasterMinutenMinByStunde.get(z.unterrichtstunde), z.stundenbeginn));
			this._uZeitrasterMinutenMaxByStunde.put(z.unterrichtstunde, BlockungsUtils.maxII(this._uZeitrasterMinutenMaxByStunde.get(z.unterrichtstunde), z.stundenende));
			this._uZeitrasterMinutenMin = BlockungsUtils.minVI(this._uZeitrasterMinutenMin, z.stundenbeginn);
			this._uZeitrasterMinutenMax = BlockungsUtils.maxVI(this._uZeitrasterMinutenMax, z.stundenende);
			this._uPausenzeitUndZeitrasterMinutenMin = BlockungsUtils.minVI(this._uPausenzeitUndZeitrasterMinutenMin, z.stundenbeginn);
			this._uPausenzeitUndZeitrasterMinutenMax = BlockungsUtils.maxVI(this._uPausenzeitUndZeitrasterMinutenMax, z.stundenende);
			const listU : List<StundenplanUnterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idZeitraster_zu_unterrichtmenge, z.id);
			if (!listU.isEmpty()) {
				this._uPausenzeitUndZeitrasterMinutenMinOhneLeere = BlockungsUtils.minVI(this._uPausenzeitUndZeitrasterMinutenMinOhneLeere, z.stundenbeginn);
				this._uPausenzeitUndZeitrasterMinutenMaxOhneLeere = BlockungsUtils.maxVI(this._uPausenzeitUndZeitrasterMinutenMaxOhneLeere, z.stundenende);
				this._uZeitrasterStundeMinOhneLeere = BlockungsUtils.minVI(this._uZeitrasterStundeMinOhneLeere, z.unterrichtstunde);
				this._uZeitrasterStundeMaxOhneLeere = BlockungsUtils.maxVI(this._uZeitrasterStundeMaxOhneLeere, z.unterrichtstunde);
			}
		}
		this._uPausenzeitMinutenMin = (this._uPausenzeitMinutenMin === StundenplanManager.MINUTEN_INF_POS) ? 480 : this._uPausenzeitMinutenMin;
		this._uPausenzeitMinutenMax = (this._uPausenzeitMinutenMax === StundenplanManager.MINUTEN_INF_NEG) ? 480 : this._uPausenzeitMinutenMax;
		this._uPausenzeitUndZeitrasterMinutenMin = (this._uPausenzeitUndZeitrasterMinutenMin === StundenplanManager.MINUTEN_INF_POS) ? 480 : this._uPausenzeitUndZeitrasterMinutenMin;
		this._uPausenzeitUndZeitrasterMinutenMax = (this._uPausenzeitUndZeitrasterMinutenMax === StundenplanManager.MINUTEN_INF_NEG) ? 480 : this._uPausenzeitUndZeitrasterMinutenMax;
		this._uPausenzeitUndZeitrasterMinutenMinOhneLeere = (this._uPausenzeitUndZeitrasterMinutenMinOhneLeere === StundenplanManager.MINUTEN_INF_POS) ? 480 : this._uPausenzeitUndZeitrasterMinutenMinOhneLeere;
		this._uPausenzeitUndZeitrasterMinutenMaxOhneLeere = (this._uPausenzeitUndZeitrasterMinutenMaxOhneLeere === StundenplanManager.MINUTEN_INF_NEG) ? 480 : this._uPausenzeitUndZeitrasterMinutenMaxOhneLeere;
		this._uZeitrasterMinutenMin = (this._uZeitrasterMinutenMin === StundenplanManager.MINUTEN_INF_POS) ? 480 : this._uZeitrasterMinutenMin;
		this._uZeitrasterMinutenMax = (this._uZeitrasterMinutenMax === StundenplanManager.MINUTEN_INF_NEG) ? 480 : this._uZeitrasterMinutenMax;
		this._uZeitrasterWochentagMin = (this._uZeitrasterWochentagMin === StundenplanManager.WOCHENTAG_INF_POS) ? Wochentag.MONTAG.id : this._uZeitrasterWochentagMin;
		this._uZeitrasterWochentagMax = (this._uZeitrasterWochentagMax === StundenplanManager.WOCHENTAG_INF_NEG) ? Wochentag.MONTAG.id : this._uZeitrasterWochentagMax;
		this._uZeitrasterStundeMin = (this._uZeitrasterStundeMin === StundenplanManager.STUNDE_INF_POS) ? 1 : this._uZeitrasterStundeMin;
		this._uZeitrasterStundeMax = (this._uZeitrasterStundeMax === StundenplanManager.STUNDE_INF_NEG) ? 1 : this._uZeitrasterStundeMax;
		this._uZeitrasterStundeMinOhneLeere = (this._uZeitrasterStundeMinOhneLeere === StundenplanManager.STUNDE_INF_POS) ? 1 : this._uZeitrasterStundeMinOhneLeere;
		this._uZeitrasterStundeMaxOhneLeere = (this._uZeitrasterStundeMaxOhneLeere === StundenplanManager.STUNDE_INF_NEG) ? 1 : this._uZeitrasterStundeMaxOhneLeere;
		this._uZeitrasterStundenRange = Array(this._uZeitrasterStundeMax - this._uZeitrasterStundeMin + 1).fill(0);
		for (let i : number = 0; i < this._uZeitrasterStundenRange.length; i++)
			this._uZeitrasterStundenRange[i] = this._uZeitrasterStundeMin + i;
		this._uZeitrasterStundenRangeOhneLeere = Array(this._uZeitrasterStundeMaxOhneLeere - this._uZeitrasterStundeMinOhneLeere + 1).fill(0);
		for (let i : number = 0; i < this._uZeitrasterStundenRangeOhneLeere.length; i++)
			this._uZeitrasterStundenRangeOhneLeere[i] = this._uZeitrasterStundeMinOhneLeere + i;
		this._uZeitrasterWochentageAlsEnumRange = Array(this._uZeitrasterWochentagMax - this._uZeitrasterWochentagMin + 1).fill(null);
		for (let i : number = 0; i < this._uZeitrasterWochentageAlsEnumRange.length; i++)
			this._uZeitrasterWochentageAlsEnumRange[i] = Wochentag.fromIDorException(this._uZeitrasterWochentagMin + i);
	}

	private updateIteriereUnterricht() : void {
		this._uUnterrichtHatMultiWochen = false;
		this._uKursMapByKlasseId.clear();
		this._uKursMapByLehrerId.clear();
		for (const u of this._list_unterricht) {
			if (u.wochentyp > 0)
				this._uUnterrichtHatMultiWochen = true;
			if (u.idKurs === null) {
				// empty block
			} else {
				const kurs : StundenplanKurs = DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_kurs, u.idKurs);
				for (const idKlasse of u.klassen)
					MapUtils.getOrCreateArrayList(this._uKursMapByKlasseId, idKlasse).add(kurs);
				for (const idLehrer of u.lehrer)
					MapUtils.getOrCreateArrayList(this._uKursMapByLehrerId, idLehrer).add(kurs);
			}
		}
	}

	private aufsichtsbereichAddOhneUpdate(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		StundenplanManager.aufsichtsbereichCheck(aufsichtsbereich);
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
		this._list_aufsichtsbereiche.sort(StundenplanManager._compAufsichtsbereich);
		this.update();
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
		this._list_aufsichtsbereiche.sort(StundenplanManager._compAufsichtsbereich);
		this.update();
	}

	private static aufsichtsbereichCheck(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		DeveloperNotificationException.ifInvalidID("aufsicht.id", aufsichtsbereich.id);
		DeveloperNotificationException.ifStringIsBlank("aufsicht.kuerzel", aufsichtsbereich.kuerzel);
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
	 * Aktualisiert das vorhandene {@link StundenplanAufsichtsbereich}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanAufsichtsbereich#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanAufsichtsbereich#beschreibung}
	 * <br>{@link StundenplanAufsichtsbereich#kuerzel}
	 *
	 * @param aufsichtsbereich  Das neue {@link StundenplanAufsichtsbereich}-Objekt, dessen Attribute kopiert werden.
	 */
	public aufsichtsbereichPatchAttributes(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		StundenplanManager.aufsichtsbereichCheck(aufsichtsbereich);
		const old : StundenplanAufsichtsbereich = DeveloperNotificationException.ifMapGetIsNull(this._map_idAufsichtsbereich_zu_aufsichtsbereich, aufsichtsbereich.id);
		old.beschreibung = aufsichtsbereich.beschreibung;
		old.kuerzel = aufsichtsbereich.kuerzel;
		this._list_aufsichtsbereiche.sort(StundenplanManager._compAufsichtsbereich);
		this.update();
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
		this.update();
	}

	/**
	 * Entfernt alle {@link StundenplanAufsichtsbereich}-Objekte.
	 *
	 * @param listAufsichtsbereich  Die Liste der zu entfernenden {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public aufsichtsbereichRemoveAll(listAufsichtsbereich : List<StundenplanAufsichtsbereich>) : void {
		for (const aufsichtsbereich of listAufsichtsbereich)
			this.aufsichtsbereichRemoveOhneUpdateById(aufsichtsbereich.id);
		this.update();
	}

	private fachAddOhneUpdate(fach : StundenplanFach) : void {
		StundenplanManager.fachCheck(fach);
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
		this._list_faecher.sort(StundenplanManager._compFach);
		this.update();
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
		this._list_faecher.sort(StundenplanManager._compFach);
		this.update();
	}

	private static fachCheck(fach : StundenplanFach) : void {
		DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
		DeveloperNotificationException.ifStringIsBlank("fach.bezeichnung", fach.bezeichnung);
		DeveloperNotificationException.ifStringIsBlank("fach.kuerzel", fach.kuerzel);
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

	private jahrgangAddOhneUpdate(jahrgang : StundenplanJahrgang) : void {
		StundenplanManager.jahrgangCheck(jahrgang);
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
		this._list_jahrgaenge.sort(StundenplanManager._compJahrgang);
		this.update();
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
		this._list_jahrgaenge.sort(StundenplanManager._compJahrgang);
		this.update();
	}

	private static jahrgangCheck(jahrgang : StundenplanJahrgang) : void {
		DeveloperNotificationException.ifInvalidID("jahrgang.id", jahrgang.id);
		DeveloperNotificationException.ifStringIsBlank("jahrgang.bezeichnung", jahrgang.bezeichnung);
		DeveloperNotificationException.ifStringIsBlank("jahrgang.kuerzel", jahrgang.kuerzel);
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
	 * Aktualisiert das vorhandene {@link StundenplanJahrgang}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanJahrgang#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanJahrgang#bezeichnung}
	 * <br>{@link StundenplanJahrgang#kuerzel}
	 *
	 * @param jahrgang  Das neue {@link StundenplanJahrgang}-Objekt, dessen Attribute kopiert werden.
	 */
	public jahrgangPatchAttributes(jahrgang : StundenplanJahrgang) : void {
		StundenplanManager.jahrgangCheck(jahrgang);
		const old : StundenplanJahrgang = DeveloperNotificationException.ifMapGetIsNull(this._map_idJahrgang_zu_jahrgang, jahrgang.id);
		old.bezeichnung = jahrgang.bezeichnung;
		old.kuerzel = jahrgang.kuerzel;
		this._list_jahrgaenge.sort(StundenplanManager._compJahrgang);
		this.update();
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
		this.update();
	}

	/**
	 * Entfernt alle {@link StundenplanJahrgang}-Objekte.
	 *
	 * @param listJahrgang  Die Liste der zu entfernenden {@link StundenplanJahrgang}-Objekte.
	 */
	public jahrgangRemoveAll(listJahrgang : List<StundenplanJahrgang>) : void {
		for (const jahrgang of listJahrgang)
			this.jahrgangRemoveOhneUpdateById(jahrgang.id);
		this.update();
	}

	private kalenderwochenzuordnungAddOhneUpdate(kwz : StundenplanKalenderwochenzuordnung) : void {
		this.kalenderwochenzuordnungCheck(kwz);
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
		this._list_kwz.sort(StundenplanManager._compKWZ);
		this.update();
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
		this._list_kwz.sort(StundenplanManager._compKWZ);
		this.update();
	}

	private kalenderwochenzuordnungCheck(kwz : StundenplanKalenderwochenzuordnung) : void {
		DeveloperNotificationException.ifTrue("kwz.id < -1", kwz.id < -1);
		DeveloperNotificationException.ifTrue("(kwz.jahr < DateUtils.MIN_GUELTIGES_JAHR) || (kwz.jahr > DateUtils.MAX_GUELTIGES_JAHR)", (kwz.jahr < DateUtils.MIN_GUELTIGES_JAHR) || (kwz.jahr > DateUtils.MAX_GUELTIGES_JAHR));
		DeveloperNotificationException.ifTrue("(kwz.kw < 1) || (kwz.kw > DateUtils.gibKalenderwochenOfJahr(kwz.jahr))", (kwz.kw < 1) || (kwz.kw > DateUtils.gibKalenderwochenOfJahr(kwz.jahr)));
		DeveloperNotificationException.ifTrue("kwz.wochentyp > stundenplanWochenTypModell", kwz.wochentyp > this._stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("kwz.wochentyp < 0", kwz.wochentyp < 0);
	}

	private kalenderwochenzuordnungErzeugePseudoMenge() : void {
		const infoVon : Array<number> = DateUtils.extractFromDateISO8601(this._stundenplanGueltigAb);
		const infoBis : Array<number> = DateUtils.extractFromDateISO8601(this._stundenplanGueltigBis);
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
		if (this._stundenplanWochenTypModell === 0)
			return 0;
		const z : StundenplanKalenderwochenzuordnung | null = this._map2d_jahr_kw_zu_kwz.getOrNull(jahr, kalenderwoche);
		if (z !== null)
			return z.wochentyp;
		const wochentyp : number = kalenderwoche % this._stundenplanWochenTypModell;
		return wochentyp === 0 ? this._stundenplanWochenTypModell : wochentyp;
	}

	/**
	 * Liefert TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" den Wochentyp verwendet wird.
	 * <br>Hinweis: Das Mapping muss existieren UND {@link #_stundenplanWochenTypModell} muss mindestens 2 sein.
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
		return (this._stundenplanWochenTypModell >= 2) && (z !== null);
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanKalenderwochenzuordnung}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanKalenderwochenzuordnung#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanKalenderwochenzuordnung#jahr}
	 * <br>{@link StundenplanKalenderwochenzuordnung#kw}
	 * <br>{@link StundenplanKalenderwochenzuordnung#wochentyp}
	 *
	 * @param kwz  Das neue {@link StundenplanKalenderwochenzuordnung}-Objekt, dessen Attribute kopiert werden.
	 */
	public kalenderwochenzuordnungPatchAttributes(kwz : StundenplanKalenderwochenzuordnung) : void {
		this.kalenderwochenzuordnungCheck(kwz);
		const old : StundenplanKalenderwochenzuordnung = DeveloperNotificationException.ifMapGetIsNull(this._map_idKWZ_zu_kwz, kwz.id);
		DeveloperNotificationException.ifMap2DRemoveFailes(this._map2d_jahr_kw_zu_kwz, old.jahr, old.kw);
		old.jahr = kwz.jahr;
		old.kw = kwz.kw;
		old.wochentyp = kwz.wochentyp;
		DeveloperNotificationException.ifMap2DPutOverwrites(this._map2d_jahr_kw_zu_kwz, kwz.jahr, kwz.kw, old);
		this._list_kwz.sort(StundenplanManager._compKWZ);
		this.update();
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
	 *
	 * @param idKWZ  Die Datenbank-ID des {@link StundenplanKalenderwochenzuordnung}-Objekts, welches entfernt werden soll.
	 */
	public kalenderwochenzuordnungRemoveById(idKWZ : number) : void {
		this.kalenderwochenzuordnungRemoveOhneUpdateById(idKWZ);
		this.update();
	}

	/**
	 * Entfernt ein {@link StundenplanKalenderwochenzuordnung}-Objekt anhand der Parameter (jahr, kalenderwoche).
	 *
	 * @param jahr           Das Jahr der Kalenderwoche.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche.
	 */
	public kalenderwochenzuordnungRemoveByJahrAndKW(jahr : number, kalenderwoche : number) : void {
		this.kalenderwochenzuordnungRemoveOhneUpdateByJahrAndKW(jahr, kalenderwoche);
		this.update();
	}

	/**
	 * Entfernt alle {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 *
	 * @param listKWZ  Die Liste der zu entfernenden {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public kalenderwochenzuordnungRemoveAll(listKWZ : List<StundenplanKalenderwochenzuordnung>) : void {
		for (const kwz of listKWZ)
			this.kalenderwochenzuordnungRemoveOhneUpdateById(kwz.id);
		this.update();
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
		this.update();
	}

	private klasseAddOhneUpdate(klasse : StundenplanKlasse) : void {
		StundenplanManager.klasseCheck(klasse);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idKlasse_zu_klasse, klasse.id, klasse);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idKlasse_zu_klassenunterricht, klasse.id, new ArrayList());
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idKlasse_zu_unterrichtmenge, klasse.id, new ArrayList());
		DeveloperNotificationException.ifListAddsDuplicate("_list_klassen", this._list_klassen, klasse);
	}

	/**
	 * Fügt ein {@link StundenplanKlasse}-Objekt hinzu.
	 *
	 * @param klasse  Das {@link StundenplanKlasse}-Objekt, welches hinzugefügt werden soll.
	 */
	public klasseAdd(klasse : StundenplanKlasse) : void {
		this.klasseAddOhneUpdate(klasse);
		this._list_klassen.sort(StundenplanManager._compKlasse);
		this.update();
	}

	/**
	 * Fügt alle {@link StundenplanKlasse}-Objekte hinzu.
	 *
	 * @param listKlasse  Die Menge der {@link StundenplanKlasse}-Objekte, welche hinzugefügt werden soll.
	 */
	public klasseAddAll(listKlasse : List<StundenplanKlasse>) : void {
		for (const klasse of listKlasse)
			this.klasseAddOhneUpdate(klasse);
		this._list_klassen.sort(StundenplanManager._compKlasse);
		this.update();
	}

	private static klasseCheck(klasse : StundenplanKlasse) : void {
		DeveloperNotificationException.ifInvalidID("klasse.id", klasse.id);
		DeveloperNotificationException.ifStringIsBlank("klasse.kuerzel", klasse.kuerzel);
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
	 * Aktualisiert das vorhandene {@link StundenplanKlasse}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanKlasse#id}
	 * <br>{@link StundenplanKlasse#jahrgaenge}
	 * <br>{@link StundenplanKlasse#schueler}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanKlasse#bezeichnung}
	 * <br>{@link StundenplanKlasse#kuerzel}
	 *
	 * @param klasse  Das neue {@link StundenplanKlasse}-Objekt, dessen Attribute kopiert werden.
	 */
	public klassePatchAttributes(klasse : StundenplanKlasse) : void {
		StundenplanManager.klasseCheck(klasse);
		const old : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._map_idKlasse_zu_klasse, klasse.id);
		old.bezeichnung = klasse.bezeichnung;
		old.kuerzel = klasse.kuerzel;
		this._list_klassen.sort(StundenplanManager._compKlasse);
		this.update();
	}

	private klasseRemoveOhneUpdateById(idKlasse : number) : void {
		const listKU : List<StundenplanKlassenunterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idKlasse_zu_klassenunterricht, idKlasse);
		const listKU2 : List<StundenplanKlassenunterricht> = new ArrayList(listKU);
		for (const u of listKU2)
			this.klassenunterrichtRemoveOhneUpdateById(u.idKlasse, u.idFach);
		const k : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._map_idKlasse_zu_klasse, idKlasse);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idKlasse_zu_klasse, idKlasse);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idKlasse_zu_klassenunterricht, idKlasse);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idKlasse_zu_unterrichtmenge, idKlasse);
		DeveloperNotificationException.ifListRemoveFailes("_list_klasse", this._list_klassen, k);
	}

	/**
	 * Entfernt ein {@link StundenplanKlasse}-Objekt anhand seiner ID.
	 *
	 * @param idKlasse  Die Datenbank-ID des {@link StundenplanKlasse}-Objekts, welches entfernt werden soll.
	 */
	public klasseRemoveById(idKlasse : number) : void {
		this.klasseRemoveOhneUpdateById(idKlasse);
		this.update();
	}

	/**
	 * Entfernt alle {@link StundenplanKlasse}-Objekte.
	 *
	 * @param listKlasse  Die Liste der zu entfernenden {@link StundenplanKlasse}-Objekte.
	 */
	public klasseRemoveAll(listKlasse : List<StundenplanKlasse>) : void {
		for (const klasse of listKlasse)
			this.klasseRemoveOhneUpdateById(klasse.id);
		this.update();
	}

	private klassenunterrichtAddOhneUpdate(klassenunterricht : StundenplanKlassenunterricht) : void {
		this.klassenunterrichtCheck(klassenunterricht);
		DeveloperNotificationException.ifMapGetIsNull(this._map_idKlasse_zu_klassenunterricht, klassenunterricht.idKlasse).add(klassenunterricht);
		DeveloperNotificationException.ifMap2DPutOverwrites(this._map2d_idKlasse_idFach_zu_klassenunterricht, klassenunterricht.idKlasse, klassenunterricht.idFach, klassenunterricht);
		DeveloperNotificationException.ifMap2DPutOverwrites(this._map2d_idKlasse_idFach_zu_unterrichtmenge, klassenunterricht.idKlasse, klassenunterricht.idFach, new ArrayList());
		DeveloperNotificationException.ifListAddsDuplicate("_list_klassenunterricht", this._list_klassenunterricht, klassenunterricht);
	}

	/**
	 * Fügt ein {@link StundenplanKlassenunterricht}-Objekt hinzu.
	 *
	 * @param klassenunterricht  Das {@link StundenplanKlassenunterricht}-Objekt, welches hinzugefügt werden soll.
	 */
	public klassenunterrichtAdd(klassenunterricht : StundenplanKlassenunterricht) : void {
		this.klassenunterrichtAddOhneUpdate(klassenunterricht);
		this._list_klassenunterricht.sort(StundenplanManager._compKlassenunterricht);
		this.update();
	}

	/**
	 * Fügt alle {@link StundenplanKlassenunterricht}-Objekte hinzu.
	 *
	 * @param listKlassenunterricht  Die Menge der {@link StundenplanKlassenunterricht}-Objekte, welche hinzugefügt werden soll.
	 */
	private klassenunterrichtAddAll(listKlassenunterricht : List<StundenplanKlassenunterricht>) : void {
		for (const klassenunterricht of listKlassenunterricht)
			this.klassenunterrichtAddOhneUpdate(klassenunterricht);
		this._list_klassenunterricht.sort(StundenplanManager._compKlassenunterricht);
		this.update();
	}

	private klassenunterrichtCheck(klassenunterricht : StundenplanKlassenunterricht) : void {
		DeveloperNotificationException.ifMapNotContains("_map_idKlasse_zu_klasse", this._map_idKlasse_zu_klasse, klassenunterricht.idKlasse);
		DeveloperNotificationException.ifMapNotContains("_map_idFach_zu_fach", this._map_idFach_zu_fach, klassenunterricht.idFach);
		for (const idSchiene of klassenunterricht.schienen)
			DeveloperNotificationException.ifMapNotContains("_map_idSchiene_zu_schiene", this._map_idSchiene_zu_schiene, idSchiene);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte.
	 */
	public klassenunterrichtGetMengeAsList() : List<StundenplanKlassenunterricht> {
		return this._list_klassenunterricht;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte der Klasse.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte der Klasse.
	 */
	public klassenunterrichtGetMengeByKlasseId(idKlasse : number) : List<StundenplanKlassenunterricht> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idKlasse_zu_klassenunterricht, idKlasse);
	}

	/**
	 * Liefert die IST-Wochenstunden des {@link StundenplanKlassenunterricht}.
	 * <br>Hinweis: Durch AB-Wochen, ist der Rückgabewert eine Kommazahl, da nur Stundenanteile gesetzt sein können.
	 * <br>Laufzeit: O(|Unterrichte des Klassenunterricht|)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 *
	 * @return die IST-Wochenstunden des {@link StundenplanKlassenunterricht}.
	 */
	public klassenunterrichtGetWochenstundenIst(idKlasse : number, idFach : number) : number {
		let summe : number = 0;
		const faktor : number = (this._stundenplanWochenTypModell === 0) ? 1 : this._stundenplanWochenTypModell;
		const listU : List<StundenplanUnterricht> = DeveloperNotificationException.ifMap2DGetIsNull(this._map2d_idKlasse_idFach_zu_unterrichtmenge, idKlasse, idFach);
		for (const u of listU)
			summe += (u.wochentyp === 0) ? faktor : 1;
		return summe / faktor;
	}

	/**
	 * Liefert die SOLL-Wochenstunden des {@link StundenplanKlassenunterricht}.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 *
	 * @return die SOLL-Wochenstunden des {@link StundenplanKlassenunterricht}.
	 */
	public klassenunterrichtGetWochenstundenSoll(idKlasse : number, idFach : number) : number {
		return DeveloperNotificationException.ifMap2DGetIsNull(this._map2d_idKlasse_idFach_zu_klassenunterricht, idKlasse, idFach).wochenstunden;
	}

	private klassenunterrichtRemoveOhneUpdateById(idKlasse : number, idFach : number) : void {
		const listU : List<StundenplanUnterricht> = DeveloperNotificationException.ifMap2DGetIsNull(this._map2d_idKlasse_idFach_zu_unterrichtmenge, idKlasse, idFach);
		const listU2 : List<StundenplanUnterricht> = new ArrayList(listU);
		for (const u of listU2)
			this.unterrichtRemoveByIdOhneUpdate(u.id);
		const klassenunterricht : StundenplanKlassenunterricht = DeveloperNotificationException.ifMap2DGetIsNull(this._map2d_idKlasse_idFach_zu_klassenunterricht, idKlasse, idFach);
		DeveloperNotificationException.ifMapGetIsNull(this._map_idKlasse_zu_klassenunterricht, idKlasse).remove(klassenunterricht);
		DeveloperNotificationException.ifMap2DRemoveFailes(this._map2d_idKlasse_idFach_zu_klassenunterricht, idKlasse, idFach);
		DeveloperNotificationException.ifMap2DRemoveFailes(this._map2d_idKlasse_idFach_zu_unterrichtmenge, idKlasse, idFach);
		DeveloperNotificationException.ifListRemoveFailes("_list_klassenunterricht", this._list_klassenunterricht, klassenunterricht);
	}

	/**
	 * Entfernt ein {@link StundenplanKlassenunterricht}-Objekt anhand seiner ID.
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 */
	public klassenunterrichtRemoveById(idKlasse : number, idFach : number) : void {
		this.klassenunterrichtRemoveOhneUpdateById(idKlasse, idFach);
		this.update();
	}

	/**
	 * Entfernt alle {@link StundenplanKlassenunterricht}-Objekte.
	 *
	 * @param listKlassenunterricht  Die Liste der zu entfernenden {@link StundenplanKlassenunterricht}-Objekte.
	 */
	public klassenunterrichtRemoveAll(listKlassenunterricht : List<StundenplanKlassenunterricht>) : void {
		for (const klassenunterricht of listKlassenunterricht)
			this.klassenunterrichtRemoveOhneUpdateById(klassenunterricht.idKlasse, klassenunterricht.idFach);
		this.update();
	}

	private kursAddOhneUpdate(kurs : StundenplanKurs) : void {
		this.kursCheck(kurs);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idKurs_zu_kurs, kurs.id, kurs);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idKurs_zu_unterrichtmenge, kurs.id, new ArrayList());
		DeveloperNotificationException.ifListAddsDuplicate("_list_kurse", this._list_kurse, kurs);
	}

	/**
	 * Fügt ein {@link StundenplanKurs}-Objekt hinzu.
	 *
	 * @param kurs  Das {@link StundenplanKurs}-Objekt, welches hinzugefügt werden soll.
	 */
	public kursAdd(kurs : StundenplanKurs) : void {
		this.kursAddOhneUpdate(kurs);
		this._list_kurse.sort(StundenplanManager._compKurs);
		this.update();
	}

	/**
	 * Fügt alle {@link StundenplanKurs}-Objekte hinzu.
	 *
	 * @param listKurs  Die Menge der {@link StundenplanKurs}-Objekte, welche hinzugefügt werden soll.
	 */
	public kursAddAll(listKurs : List<StundenplanKurs>) : void {
		for (const kurs of listKurs)
			this.kursAddOhneUpdate(kurs);
		this._list_kurse.sort(StundenplanManager._compKurs);
		this.update();
	}

	private kursCheck(kurs : StundenplanKurs) : void {
		DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);
		DeveloperNotificationException.ifStringIsBlank("kurs.bezeichnung", kurs.bezeichnung);
		for (const idSchieneDesKurses of kurs.schienen)
			DeveloperNotificationException.ifMapNotContains("_map_schieneID_zu_schiene", this._map_idSchiene_zu_schiene, idSchieneDesKurses);
		for (const idJahrgangDesKurses of kurs.jahrgaenge)
			DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", this._map_idJahrgang_zu_jahrgang, idJahrgangDesKurses);
		for (const idSchuelerDesKurses of kurs.schueler)
			DeveloperNotificationException.ifMapNotContains("_map_schuelerID_zu_schueler", this._map_schuelerID_zu_schueler, idSchuelerDesKurses);
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
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte der Klasse.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte der Klasse.
	 */
	public kursGetMengeByKlasseIdAsList(idKlasse : number) : List<StundenplanKurs> {
		return MapUtils.getOrCreateArrayList(this._uKursMapByKlasseId, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte des Lehrers.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @param idLehrer  Die Datenbank-ID des Lehrers.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte des Lehrers.
	 */
	public kursGetMengeByLehrerIdAsList(idLehrer : number) : List<StundenplanKurs> {
		return MapUtils.getOrCreateArrayList(this._uKursMapByLehrerId, idLehrer);
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
	 * Liefert die IST-Wochenstunden des {@link StundenplanKurs}.
	 * <br>Hinweis: Durch AB-Wochen, ist der Rückgabewert eine Kommazahl, da nur Stundenanteile gesetzt sein können.
	 * <br>Laufzeit: O(|Unterrichte des Kurses|)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die IST-Wochenstunden des {@link StundenplanKlassenunterricht}.
	 */
	public kursGetWochenstundenIst(idKurs : number) : number {
		let summe : number = 0;
		const faktor : number = (this._stundenplanWochenTypModell === 0) ? 1 : this._stundenplanWochenTypModell;
		const listU : List<StundenplanUnterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_unterrichtmenge, idKurs);
		for (const u of listU)
			summe += (u.wochentyp === 0) ? faktor : 1;
		return summe / faktor;
	}

	/**
	 * Liefert die Wochenstunden des Kurses.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Wochenstunden des Kurses.
	 */
	public kursGetWochenstundenSoll(idKurs : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_kurs, idKurs).wochenstunden;
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanKurs}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanKurs#id}
	 * <br>{@link StundenplanKurs#jahrgaenge}
	 * <br>{@link StundenplanKurs#schienen}
	 * <br>{@link StundenplanKurs#schueler}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanKurs#bezeichnung}
	 * <br>{@link StundenplanKurs#wochenstunden}
	 *
	 * @param kurs  Das neue {@link StundenplanKurs}-Objekt, dessen Attribute kopiert werden.
	 */
	public kursPatchAttributtes(kurs : StundenplanKurs) : void {
		this.kursCheck(kurs);
		const old : StundenplanKurs = DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_kurs, kurs.id);
		old.bezeichnung = kurs.bezeichnung;
		old.wochenstunden = kurs.wochenstunden;
		this._list_kurse.sort(StundenplanManager._compKurs);
		this.update();
	}

	private kursRemoveOhneUpdateById(idKurs : number) : void {
		const listU : List<StundenplanUnterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_unterrichtmenge, idKurs);
		const listU2 : List<StundenplanUnterricht> = new ArrayList(listU);
		for (const u of listU2)
			this.unterrichtRemoveByIdOhneUpdate(u.id);
		const kurs : StundenplanKurs = DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_kurs, idKurs);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idKurs_zu_kurs, kurs.id);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idKurs_zu_unterrichtmenge, kurs.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_kurse", this._list_kurse, kurs);
	}

	/**
	 * Entfernt ein {@link StundenplanKurs}-Objekt anhand seiner ID.
	 *
	 * @param idKurs  Die Datenbank-ID des {@link StundenplanKurs}-Objekts, welches entfernt werden soll.
	 */
	public kursRemoveById(idKurs : number) : void {
		this.kursRemoveOhneUpdateById(idKurs);
		this.update();
	}

	/**
	 * Entfernt alle {@link StundenplanKurs}-Objekte.
	 *
	 * @param listKurs  Die Liste der zu entfernenden {@link StundenplanKurs}-Objekte.
	 */
	public kursRemoveAll(listKurs : List<StundenplanKurs>) : void {
		for (const kurs of listKurs)
			this.kursRemoveOhneUpdateById(kurs.id);
		this.update();
	}

	private lehrerAddOhneUpdate(lehrer : StundenplanLehrer) : void {
		StundenplanManager.lehrerCheck(lehrer);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idLehrer_zu_lehrer, lehrer.id, lehrer);
		DeveloperNotificationException.ifListAddsDuplicate("_list_lehrer", this._list_lehrer, lehrer);
	}

	/**
	 * Fügt ein {@link StundenplanLehrer}-Objekt hinzu.
	 *
	 * @param lehrer  Das {@link StundenplanLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public lehrerAdd(lehrer : StundenplanLehrer) : void {
		this.lehrerAddOhneUpdate(lehrer);
		this._list_lehrer.sort(StundenplanManager._compLehrer);
		this.update();
	}

	/**
	 * Fügt alle {@link StundenplanLehrer}-Objekte hinzu.
	 *
	 * @param listLehrer  Die Menge der {@link StundenplanLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public lehrerAddAll(listLehrer : List<StundenplanLehrer>) : void {
		for (const lehrer of listLehrer)
			this.lehrerAddOhneUpdate(lehrer);
		this._list_lehrer.sort(StundenplanManager._compLehrer);
		this.update();
	}

	private static lehrerCheck(lehrer : StundenplanLehrer) : void {
		DeveloperNotificationException.ifInvalidID("lehrer.id", lehrer.id);
		DeveloperNotificationException.ifStringIsBlank("lehrer.kuerzel", lehrer.kuerzel);
		DeveloperNotificationException.ifStringIsBlank("lehrer.nachname", lehrer.nachname);
		DeveloperNotificationException.ifStringIsBlank("lehrer.vorname", lehrer.vorname);
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
	 * Aktualisiert das vorhandene {@link StundenplanLehrer}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanLehrer#id}
	 * <br>{@link StundenplanLehrer#faecher}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanLehrer#kuerzel}
	 * <br>{@link StundenplanLehrer#nachname}
	 * <br>{@link StundenplanLehrer#vorname}
	 *
	 * @param lehrer  Das neue {@link StundenplanLehrer}-Objekt, dessen Attribute kopiert werden.
	 */
	public lehrerPatchAttributes(lehrer : StundenplanLehrer) : void {
		StundenplanManager.lehrerCheck(lehrer);
		const old : StundenplanLehrer = DeveloperNotificationException.ifMapGetIsNull(this._map_idLehrer_zu_lehrer, lehrer.id);
		old.kuerzel = lehrer.kuerzel;
		old.nachname = lehrer.nachname;
		old.vorname = lehrer.vorname;
		this._list_lehrer.sort(StundenplanManager._compLehrer);
		this.update();
	}

	private lehrerRemoveOhneUpdateById(idLehrer : number) : void {
		const lehrer : StundenplanLehrer = DeveloperNotificationException.ifMapGetIsNull(this._map_idLehrer_zu_lehrer, idLehrer);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idLehrer_zu_lehrer, lehrer.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_lehrer", this._list_lehrer, lehrer);
	}

	/**
	 * Entfernt ein {@link StundenplanLehrer}-Objekt anhand seiner ID.
	 *
	 * @param idLehrer  Die Datenbank-ID des {@link StundenplanLehrer}-Objekts, welches entfernt werden soll.
	 */
	public lehrerRemoveById(idLehrer : number) : void {
		this.lehrerRemoveOhneUpdateById(idLehrer);
		this.update();
	}

	/**
	 * Entfernt alle {@link StundenplanLehrer}-Objekte.
	 *
	 * @param listLehrer  Die Liste der zu entfernenden {@link StundenplanLehrer}-Objekte.
	 */
	public lehrerRemoveAll(listLehrer : List<StundenplanLehrer>) : void {
		for (const lehrer of listLehrer)
			this.lehrerRemoveOhneUpdateById(lehrer.id);
		this.update();
	}

	private pausenaufsichtAddOhneUpdate(pausenaufsicht : StundenplanPausenaufsicht) : void {
		this.pausenaufsichtCheck(pausenaufsicht);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idPausenaufsicht_zu_pausenaufsicht, pausenaufsicht.id, pausenaufsicht);
		DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenzeit_zu_pausenaufsichtmenge, pausenaufsicht.idPausenzeit).add(pausenaufsicht);
		DeveloperNotificationException.ifListAddsDuplicate("_list_pausenaufsichten", this._list_pausenaufsichten, pausenaufsicht);
	}

	/**
	 * Fügt ein {@link StundenplanPausenaufsicht}-Objekt hinzu.
	 *
	 * @param pausenaufsicht  Das {@link StundenplanPausenaufsicht}-Objekt, welches hinzugefügt werden soll.
	 */
	public pausenaufsichtAdd(pausenaufsicht : StundenplanPausenaufsicht) : void {
		this.pausenaufsichtAddOhneUpdate(pausenaufsicht);
		this._list_pausenaufsichten.sort(StundenplanManager._compPausenaufsicht);
		this.update();
	}

	/**
	 * Fügt alle {@link StundenplanPausenaufsicht}-Objekte hinzu.
	 *
	 * @param listPausenaufsicht  Die Menge der {@link StundenplanPausenaufsicht}-Objekte, welche hinzugefügt werden soll.
	 */
	private pausenaufsichtAddAll(listPausenaufsicht : List<StundenplanPausenaufsicht>) : void {
		for (const pausenaufsicht of listPausenaufsicht)
			this.pausenaufsichtAddOhneUpdate(pausenaufsicht);
		this._list_pausenaufsichten.sort(StundenplanManager._compPausenaufsicht);
		this.update();
	}

	private pausenaufsichtCheck(pausenaufsicht : StundenplanPausenaufsicht) : void {
		DeveloperNotificationException.ifInvalidID("pausenaufsicht.id", pausenaufsicht.id);
		DeveloperNotificationException.ifMapNotContains("_map_idLehrer_zu_lehrer", this._map_idLehrer_zu_lehrer, pausenaufsicht.idLehrer);
		DeveloperNotificationException.ifMapNotContains("_map_idPausenzeit_zu_pausenzeit", this._map_idPausenzeit_zu_pausenzeit, pausenaufsicht.idPausenzeit);
		DeveloperNotificationException.ifTrue("(pa.wochentyp > 0) && (pa.wochentyp > stundenplanWochenTypModell)", (pausenaufsicht.wochentyp > 0) && (pausenaufsicht.wochentyp > this._stundenplanWochenTypModell));
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @param idPausenaufsicht Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 */
	public pausenaufsichtGetByIdOrException(idPausenaufsicht : number) : StundenplanPausenaufsicht {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenaufsicht_zu_pausenaufsicht, idPausenaufsicht);
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @return eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 */
	public pausenaufsichtGetMengeAsList() : List<StundenplanPausenaufsicht> {
		return this._list_pausenaufsichten;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Wochentages.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @param wochentag  Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Wochentages.
	 */
	public pausenaufsichtGetMengeByWochentagOrEmptyList(wochentag : number) : List<StundenplanPausenaufsicht> {
		return MapUtils.getOrCreateArrayList(this._uPausenaufsichtMapByWochentag, wochentag);
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanPausenaufsicht}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanPausenaufsicht#id}
	 * <br>{@link StundenplanPausenaufsicht#bereiche}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanPausenaufsicht#idLehrer}
	 * <br>{@link StundenplanPausenaufsicht#idPausenzeit}
	 * <br>{@link StundenplanPausenaufsicht#wochentyp}
	 *
	 * @param pausenaufsicht  Das neue {@link StundenplanPausenaufsicht}-Objekt, dessen Attribute kopiert werden.
	 */
	public pausenaufsichtPatchAttributes(pausenaufsicht : StundenplanPausenaufsicht) : void {
		this.pausenaufsichtCheck(pausenaufsicht);
		const old : StundenplanPausenaufsicht = DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenaufsicht_zu_pausenaufsicht, pausenaufsicht.id);
		old.idLehrer = pausenaufsicht.idLehrer;
		old.idPausenzeit = pausenaufsicht.idPausenzeit;
		old.wochentyp = pausenaufsicht.wochentyp;
		this._list_pausenaufsichten.sort(StundenplanManager._compPausenaufsicht);
		this.update();
	}

	private pausenaufsichtRemoveOhneUpdateById(idPausenaufsicht : number) : void {
		const pausenaufsicht : StundenplanPausenaufsicht = DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenaufsicht_zu_pausenaufsicht, idPausenaufsicht);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idPausenaufsicht_zu_pausenaufsicht, pausenaufsicht.id);
		DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenzeit_zu_pausenaufsichtmenge, pausenaufsicht.idPausenzeit).remove(pausenaufsicht);
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
		this.update();
	}

	private pausenzeitAddOhneUpdate(pausenzeit : StundenplanPausenzeit) : void {
		StundenplanManager.pausenzeitCheck(pausenzeit);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idPausenzeit_zu_pausenzeit, pausenzeit.id, pausenzeit);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idPausenzeit_zu_pausenaufsichtmenge, pausenzeit.id, new ArrayList());
		DeveloperNotificationException.ifListAddsDuplicate("_list_pausenzeiten", this._list_pausenzeiten, pausenzeit);
	}

	/**
	 * Fügt ein {@link StundenplanPausenzeit}-Objekt hinzu.
	 *
	 * @param pausenzeit  Das {@link StundenplanPausenzeit}-Objekt, welches hinzugefügt werden soll.
	 */
	public pausenzeitAdd(pausenzeit : StundenplanPausenzeit) : void {
		this.pausenzeitAddOhneUpdate(pausenzeit);
		this._list_pausenzeiten.sort(StundenplanManager._compPausenzeit);
		this.update();
	}

	/**
	 * Fügt alle {@link StundenplanPausenzeit}-Objekte hinzu.
	 *
	 * @param listPausenzeit  Die Menge der {@link StundenplanPausenzeit}-Objekte, welche hinzugefügt werden soll.
	 */
	public pausenzeitAddAll(listPausenzeit : List<StundenplanPausenzeit>) : void {
		for (const pausenzeit of listPausenzeit)
			this.pausenzeitAddOhneUpdate(pausenzeit);
		this._list_pausenzeiten.sort(StundenplanManager._compPausenzeit);
		this.update();
	}

	private static pausenzeitCheck(pausenzeit : StundenplanPausenzeit) : void {
		DeveloperNotificationException.ifInvalidID("pause.id", pausenzeit.id);
		Wochentag.fromIDorException(pausenzeit.wochentag);
		if ((pausenzeit.beginn !== null) && (pausenzeit.ende !== null))
			DeveloperNotificationException.ifTrue("pausenzeit.beginn >= pausenzeit.ende", pausenzeit.beginn >= pausenzeit.ende);
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
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @param wochentag  Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Wochentages, oder eine leere Liste.
	 */
	public pausenzeitGetMengeByWochentagOrEmptyList(wochentag : number) : List<StundenplanPausenzeit> {
		return MapUtils.getOrCreateArrayList(this._uPausenzeitMapByWochentag, wochentag);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte, die mindestens eine {@link StundenplanPausenaufsicht} beinhalten.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte, die mindestens eine {@link StundenplanPausenaufsicht} beinhalten.
	 */
	public pausenzeitGetMengeNichtLeereAsList() : List<StundenplanPausenzeit> {
		return this._uPausenzeitListNichtLeere;
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanPausenzeit}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanPausenzeit#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanPausenzeit#beginn}
	 * <br>{@link StundenplanPausenzeit#bezeichnung}
	 * <br>{@link StundenplanPausenzeit#ende}
	 * <br>{@link StundenplanPausenzeit#wochentag}
	 *
	 * @param pausenzeit  Das neue {@link StundenplanPausenzeit}-Objekt, dessen Attribute kopiert werden.
	 */
	public pausenzeitPatchAttributes(pausenzeit : StundenplanPausenzeit) : void {
		StundenplanManager.pausenzeitCheck(pausenzeit);
		const old : StundenplanPausenzeit = DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenzeit_zu_pausenzeit, pausenzeit.id);
		old.beginn = pausenzeit.beginn;
		old.bezeichnung = pausenzeit.bezeichnung;
		old.ende = pausenzeit.ende;
		old.wochentag = pausenzeit.wochentag;
		this._list_pausenzeiten.sort(StundenplanManager._compPausenzeit);
		this.update();
	}

	private pausenzeitRemoveOhneUpdateById(idPausenzeit : number) : void {
		const pausenzeit : StundenplanPausenzeit = DeveloperNotificationException.ifMapGetIsNull(this._map_idPausenzeit_zu_pausenzeit, idPausenzeit);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idPausenzeit_zu_pausenzeit, pausenzeit.id);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idPausenzeit_zu_pausenaufsichtmenge, pausenzeit.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_pausenzeiten", this._list_pausenzeiten, pausenzeit);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param idPausenzeit  Die ID des {@link StundenplanPausenzeit}-Objekts.
	 */
	public pausenzeitRemoveById(idPausenzeit : number) : void {
		this.pausenzeitRemoveOhneUpdateById(idPausenzeit);
		this.update();
	}

	/**
	 * Entfernt alle {@link StundenplanPausenzeit}-Objekte.
	 *
	 * @param listPausenzeit  Die Liste der zu entfernenden {@link StundenplanPausenzeit}-Objekte.
	 */
	public pausenzeitRemoveAll(listPausenzeit : List<StundenplanPausenzeit>) : void {
		for (const pausenzeit of listPausenzeit)
			this.pausenzeitRemoveOhneUpdateById(pausenzeit.id);
		this.update();
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitGetMinutenMin() : number {
		return this._uPausenzeitMinutenMin;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitGetMinutenMax() : number {
		return this._uPausenzeitMinutenMax;
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitUndZeitrasterGetMinutenMin() : number {
		return this._uPausenzeitUndZeitrasterMinutenMin;
	}

	/**
	 * Liefert das Minimum aller nicht leeren {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller nicht leeren {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitUndZeitrasterGetMinutenMinOhneLeere() : number {
		return this._uPausenzeitUndZeitrasterMinutenMinOhneLeere;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitUndZeitrasterGetMinutenMax() : number {
		return this._uPausenzeitUndZeitrasterMinutenMax;
	}

	/**
	 * Liefert das Maximum aller nicht leeren {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller nicht leeren {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitUndZeitrasterGetMinutenMaxOhneLeere() : number {
		return this._uPausenzeitUndZeitrasterMinutenMaxOhneLeere;
	}

	private raumAddOhneUpdate(raum : StundenplanRaum) : void {
		StundenplanManager.raumCheck(raum);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idRaum_zu_raum, raum.id, raum);
		DeveloperNotificationException.ifListAddsDuplicate("_list_raeume", this._list_raeume, raum);
	}

	/**
	 * Fügt ein {@link StundenplanRaum}-Objekt hinzu.
	 *
	 * @param raum  Das {@link StundenplanRaum}-Objekt, welches hinzugefügt werden soll.
	 */
	public raumAdd(raum : StundenplanRaum) : void {
		this.raumAddOhneUpdate(raum);
		this._list_raeume.sort(StundenplanManager._compRaum);
		this.update();
	}

	/**
	 * Fügt alle {@link StundenplanRaum}-Objekte hinzu.
	 *
	 * @param listRaum  Die Menge der {@link StundenplanRaum}-Objekte, welche hinzugefügt werden soll.
	 */
	public raumAddAll(listRaum : List<StundenplanRaum>) : void {
		for (const raum of listRaum)
			this.raumAddOhneUpdate(raum);
		this._list_raeume.sort(StundenplanManager._compRaum);
		this.update();
	}

	private static raumCheck(raum : StundenplanRaum) : void {
		DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
		DeveloperNotificationException.ifStringIsBlank("raum.kuerzel", raum.kuerzel);
		DeveloperNotificationException.ifTrue("raum.groesse < 0", raum.groesse < 0);
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
	 * Aktualisiert das vorhandene {@link StundenplanRaum}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanRaum#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanRaum#beschreibung}
	 * <br>{@link StundenplanRaum#groesse}
	 * <br>{@link StundenplanRaum#kuerzel}
	 *
	 * @param raum  Das neue {@link StundenplanRaum}-Objekt, dessen Attribute kopiert werden.
	 */
	public raumPatchAttributes(raum : StundenplanRaum) : void {
		StundenplanManager.raumCheck(raum);
		const old : StundenplanRaum = DeveloperNotificationException.ifMapGetIsNull(this._map_idRaum_zu_raum, raum.id);
		old.beschreibung = raum.beschreibung;
		old.groesse = raum.groesse;
		old.kuerzel = raum.kuerzel;
		this._list_raeume.sort(StundenplanManager._compRaum);
		this.update();
	}

	private raumRemoveOhneUpdateById(idRaum : number) : void {
		const raum : StundenplanRaum = DeveloperNotificationException.ifMapGetIsNull(this._map_idRaum_zu_raum, idRaum);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idRaum_zu_raum, raum.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_raeume", this._list_raeume, raum);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanRaum}-Objekt.
	 *
	 * @param idRaum  Die ID des {@link StundenplanRaum}-Objekts.
	 */
	public raumRemoveById(idRaum : number) : void {
		this.raumRemoveOhneUpdateById(idRaum);
		this.update();
	}

	/**
	 * Entfernt alle {@link StundenplanRaum}-Objekte.
	 *
	 * @param listRaum  Die Liste der zu entfernenden {@link StundenplanRaum}-Objekte.
	 */
	public raumRemoveAll(listRaum : List<StundenplanRaum>) : void {
		for (const raum of listRaum)
			this.raumRemoveOhneUpdateById(raum.id);
		this.update();
	}

	private schieneAddOhneUpdate(schiene : StundenplanSchiene) : void {
		this.schieneCheck(schiene);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idSchiene_zu_schiene, schiene.id, schiene);
		DeveloperNotificationException.ifListAddsDuplicate("_list_schienen", this._list_schienen, schiene);
	}

	/**
	 * Fügt ein {@link StundenplanSchiene}-Objekt hinzu.
	 *
	 * @param schiene  Das {@link StundenplanSchiene}-Objekt, welches hinzugefügt werden soll.
	 */
	public schieneAdd(schiene : StundenplanSchiene) : void {
		this.schieneAddOhneUpdate(schiene);
		this._list_schienen.sort(StundenplanManager._compSchiene);
		this.update();
	}

	/**
	 * Fügt alle {@link StundenplanSchiene}-Objekte hinzu.
	 *
	 * @param listSchiene  Die Menge der {@link StundenplanSchiene}-Objekte, welche hinzugefügt werden soll.
	 */
	public schieneAddAll(listSchiene : List<StundenplanSchiene>) : void {
		for (const schiene of listSchiene)
			this.schieneAddOhneUpdate(schiene);
		this._list_schienen.sort(StundenplanManager._compSchiene);
		this.update();
	}

	private schieneCheck(schiene : StundenplanSchiene) : void {
		DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
		DeveloperNotificationException.ifTrue("schiene.nummer <= 0", schiene.nummer <= 0);
		DeveloperNotificationException.ifStringIsBlank("schiene.bezeichnung", schiene.bezeichnung);
		DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", this._map_idJahrgang_zu_jahrgang, schiene.idJahrgang);
	}

	private schuelerAddOhneUpdate(schueler : StundenplanSchueler) : void {
		this.schuelerCheck(schueler);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_zu_schueler, schueler.id, schueler);
		DeveloperNotificationException.ifListAddsDuplicate("_list_schueler", this._list_schueler, schueler);
	}

	/**
	 * Fügt ein {@link StundenplanSchueler}-Objekt hinzu.
	 *
	 * @param schueler  Das {@link StundenplanSchueler}-Objekt, welches hinzugefügt werden soll.
	 */
	public schuelerAdd(schueler : StundenplanSchueler) : void {
		this.schuelerAddOhneUpdate(schueler);
		this._list_schueler.sort(StundenplanManager._compSchueler);
		this.update();
	}

	/**
	 * Fügt alle {@link StundenplanSchueler}-Objekte hinzu.
	 *
	 * @param listSchueler  Die Menge der {@link StundenplanSchueler}-Objekte, welche hinzugefügt werden soll.
	 */
	public schuelerAddAll(listSchueler : List<StundenplanSchueler>) : void {
		for (const schueler of listSchueler)
			this.schuelerAddOhneUpdate(schueler);
		this._list_schueler.sort(StundenplanManager._compSchueler);
		this.update();
	}

	private schuelerCheck(schueler : StundenplanSchueler) : void {
		DeveloperNotificationException.ifInvalidID("schueler.id", schueler.id);
		DeveloperNotificationException.ifStringIsBlank("schueler.nachname", schueler.nachname);
		DeveloperNotificationException.ifStringIsBlank("schueler.vorname", schueler.vorname);
		DeveloperNotificationException.ifMapNotContains("_map_klasseID_zu_klasse", this._map_idKlasse_zu_klasse, schueler.idKlasse);
	}

	/**
	 * Liefert die ID des Schuljahresabschnitts des Stundenplans.
	 *
	 * @return die ID des Schuljahresabschnitts des Stundenplans.
	 */
	public getIDSchuljahresabschnitt() : number {
		return this._stundenplanSchuljahresAbschnittID;
	}

	/**
	 * Liefert das Datum, ab dem der Stundenplan gültig ist.
	 *
	 * @return das Datum, ab dem der Stundenplan gültig ist.
	 */
	public getGueltigAb() : string {
		return this._stundenplanGueltigAb;
	}

	/**
	 * Liefert das Datum, bis wann der Stundenplan gültig ist.
	 *
	 * @return das Datum, bis wann der Stundenplan gültig ist.
	 */
	public getGueltigBis() : string {
		return this._stundenplanGueltigBis;
	}

	/**
	 * Liefert die textuelle Beschreibung des Stundenplans.
	 *
	 * @return die textuelle Beschreibung des Stundenplans.
	 */
	public getBezeichnungStundenplan() : string {
		return this._stundenplanBezeichnung;
	}

	/**
	 * Liefert das (globale) Wochentyp-Modell für die Wochen des Stundenplans. <br>
	 * 0: Stundenplan gilt jede Woche. <br>
	 * 1: Ungültiger Wert. <br>
	 * N: Stundenplan wiederholt sich alle N Wochen. <br>
	 * <br>Laufzeit: O(1)
	 *
	 * @return das (globale) Wochentyp-Modell für die Wochen des Stundenplans.
	 */
	public getWochenTypModell() : number {
		return this._stundenplanWochenTypModell;
	}

	/**
	 * Liefert die Datenbank-ID des Schülers.<br>
	 * Wirft eine Exception, falls in den Daten nicht genau ein Schüler geladen wurde.
	 *
	 * @return  Die Datenbank-ID des Schülers.
	 */
	public schuelerGetIDorException() : number {
		const size : number = this._list_schueler.size();
		DeveloperNotificationException.ifTrue("getSchuelerID() geht nicht bei " + size + " Schülern!", size !== 1);
		return this._list_schueler.get(0).id;
	}

	/**
	 * Liefert das (globale) Wochentyp-Modell für die Wochen des Stundenplans. <br>
	 * 0: Stundenplan gilt jede Woche. <br>
	 * 1: Ungültiger Wert. <br>
	 * N: Stundenplan wiederholt sich alle N Wochen. <br>
	 * <br>Laufzeit: O(1)
	 *
	 * @return das (globale) Wochentyp-Modell für die Wochen des Stundenplans.
	 */
	public stundenplanGetWochenTypModell() : number {
		return this._stundenplanWochenTypModell;
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
		return this._stundenplanID;
	}

	private unterrichtAddOhneUpdate(u : StundenplanUnterricht) : void {
		this.unterrichtCheck(u);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idUnterricht_zu_unterricht, u.id, u);
		DeveloperNotificationException.ifMapGetIsNull(this._map_idZeitraster_zu_unterrichtmenge, u.idZeitraster).add(u);
		Map2DUtils.getOrCreateArrayList(this._map2d_idZeitraster_wochentyp_zu_unterrichtmenge, u.idZeitraster, u.wochentyp).add(u);
		for (const idLehrkraftDesUnterrichts of u.lehrer) {
			const lehrer : StundenplanLehrer = DeveloperNotificationException.ifMapGetIsNull(this._map_idLehrer_zu_lehrer, idLehrkraftDesUnterrichts);
			MapUtils.getOrCreateArrayList(this._map_idUnterricht_zu_lehrermenge, u.id).add(lehrer);
		}
		if (u.idKurs !== null) {
			const unterrichtKurs : List<StundenplanUnterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_unterrichtmenge, u.idKurs);
			DeveloperNotificationException.ifListAddsDuplicate("unterrichtKurs", unterrichtKurs, u);
		} else {
			for (const idKlasse of u.klassen) {
				const unterrichtKlasseFach : List<StundenplanUnterricht> = Map2DUtils.getOrCreateArrayList(this._map2d_idKlasse_idFach_zu_unterrichtmenge, idKlasse, u.idFach);
				DeveloperNotificationException.ifListAddsDuplicate("unterrichtKlasseFach", unterrichtKlasseFach, u);
				const unterrichtKlasse : List<StundenplanUnterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idKlasse_zu_unterrichtmenge, idKlasse);
				DeveloperNotificationException.ifListAddsDuplicate("unterrichtKL", unterrichtKlasse, u);
			}
		}
		this._list_unterricht.add(u);
	}

	/**
	 * Fügt ein {@link StundenplanUnterricht}-Objekt hinzu.
	 *
	 * @param unterricht  Das {@link StundenplanUnterricht}-Objekt, welches hinzugefügt werden soll.
	 */
	public unterrichtAdd(unterricht : StundenplanUnterricht) : void {
		this.unterrichtAddOhneUpdate(unterricht);
		this._list_unterricht.sort(StundenplanManager._compUnterricht);
		this.update();
	}

	/**
	 * Fügt alle {@link StundenplanUnterricht}-Objekte hinzu.
	 *
	 * @param listUnterricht  Die Menge der {@link StundenplanUnterricht}-Objekte, welche hinzugefügt werden soll.
	 */
	public unterrichtAddAll(listUnterricht : List<StundenplanUnterricht>) : void {
		for (const unterricht of listUnterricht)
			this.unterrichtAddOhneUpdate(unterricht);
		this._list_unterricht.sort(StundenplanManager._compUnterricht);
		this.update();
	}

	private unterrichtCheck(u : StundenplanUnterricht) : void {
		DeveloperNotificationException.ifInvalidID("u.id", u.id);
		DeveloperNotificationException.ifMapNotContains("_map_zeitrasterID_zu_zeitraster", this._map_idZeitraster_zu_zeitraster, u.idZeitraster);
		DeveloperNotificationException.ifTrue("u.wochentyp > stundenplanWochenTypModell", u.wochentyp > this._stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("u.wochentyp < 0", u.wochentyp < 0);
		DeveloperNotificationException.ifMapNotContains("_map_idFach_zu_fach", this._map_idFach_zu_fach, u.idFach);
		for (const idLehrkraftDesUnterrichts of u.lehrer)
			DeveloperNotificationException.ifMapNotContains("_map_idLehrer_zu_lehrer", this._map_idLehrer_zu_lehrer, idLehrkraftDesUnterrichts);
		for (const idKlasseDesUnterrichts of u.klassen)
			DeveloperNotificationException.ifMapNotContains("_map_idKlasse_zu_klasse", this._map_idKlasse_zu_klasse, idKlasseDesUnterrichts);
		for (const idRaumDesUnterrichts of u.raeume)
			DeveloperNotificationException.ifMapNotContains("_map_idRaum_zu_raum", this._map_idRaum_zu_raum, idRaumDesUnterrichts);
		for (const idSchieneDesUnterrichts of u.schienen)
			DeveloperNotificationException.ifMapNotContains("_map_idSchiene_zu_schiene", this._map_idSchiene_zu_schiene, idSchieneDesUnterrichts);
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
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse mit einem bestimmten Wochentyp.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param wochentyp  Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse mit einem bestimmten Wochentyp.
	 */
	public unterrichtGetMengeByKlasseIdAndWochentyp(idKlasse : number, wochentyp : number) : List<StundenplanUnterricht> {
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > this._stundenplanWochenTypModell);
		const listU : List<StundenplanUnterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idKlasse_zu_unterrichtmenge, idKlasse);
		return CollectionUtils.toFilteredArrayList(listU, { test : (u: StundenplanUnterricht) => (u.wochentyp === 0) || (u.wochentyp === wochentyp) });
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse in einer bestimmten Kalenderwoche.
	 *
	 * @param idKlasse       Die Datenbank-ID der Klasse.
	 * @param jahr           Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche  Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse in einer bestimmten Kalenderwoche.
	 */
	public unterrichtGetMengeByKlasseIdAndJahrAndKW(idKlasse : number, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht> {
		const wochentyp : number = this.kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return this.unterrichtGetMengeByKlasseIdAndWochentyp(idKlasse, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach) mit einem bestimmten Wochentyp.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param wochentyp  Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach) mit einem bestimmten Wochentyp.
	 */
	public unterrichtGetMengeByKlasseIdAndFachIdAndWochentyp(idKlasse : number, idFach : number, wochentyp : number) : List<StundenplanUnterricht> {
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > this._stundenplanWochenTypModell);
		const listU : List<StundenplanUnterricht> = DeveloperNotificationException.ifMap2DGetIsNull(this._map2d_idKlasse_idFach_zu_unterrichtmenge, idKlasse, idFach);
		return CollectionUtils.toFilteredArrayList(listU, { test : (u: StundenplanUnterricht) => (u.wochentyp === 0) || (u.wochentyp === wochentyp) });
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach) in einer bestimmten Kalenderwoche.
	 *
	 * @param idKlasse       Die Datenbank-ID der Klasse.
	 * @param idFach         Die Datenbank-ID des Faches.
	 * @param jahr           Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche  Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach) in einer bestimmten Kalenderwoche.
	 */
	public unterrichtGetMengeByKlasseIdAndFachIdAndJahrAndKW(idKlasse : number, idFach : number, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht> {
		const wochentyp : number = this.kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return this.unterrichtGetMengeByKlasseIdAndFachIdAndWochentyp(idKlasse, idFach, wochentyp);
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
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > this._stundenplanWochenTypModell);
		const listU : List<StundenplanUnterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_unterrichtmenge, idkurs);
		return CollectionUtils.toFilteredArrayList(listU, { test : (u: StundenplanUnterricht) => (u.wochentyp === 0) || (u.wochentyp === wochentyp) });
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
		return this._uUnterrichtHatMultiWochen;
	}

	private unterrichtRemoveByIdOhneUpdate(idUnterricht : number) : void {
		const u : StundenplanUnterricht = DeveloperNotificationException.ifNull("_map_idUnterricht_zu_unterricht.get(" + idUnterricht + ")", this._map_idUnterricht_zu_unterricht.get(idUnterricht));
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idUnterricht_zu_unterricht, u.id);
		DeveloperNotificationException.ifMapGetIsNull(this._map_idZeitraster_zu_unterrichtmenge, u.idZeitraster).remove(u);
		Map2DUtils.getOrCreateArrayList(this._map2d_idZeitraster_wochentyp_zu_unterrichtmenge, u.idZeitraster, u.wochentyp).remove(u);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idUnterricht_zu_lehrermenge, u.id);
		if (u.idKurs !== null) {
			const unterrichtKurs : List<StundenplanUnterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_zu_unterrichtmenge, u.idKurs);
			DeveloperNotificationException.ifListRemoveFailes("unterrichtKurs", unterrichtKurs, u);
		} else {
			for (const idKlasse of u.klassen) {
				const unterrichtKlasseFach : List<StundenplanUnterricht> = DeveloperNotificationException.ifMap2DGetIsNull(this._map2d_idKlasse_idFach_zu_unterrichtmenge, idKlasse, u.idFach);
				DeveloperNotificationException.ifListRemoveFailes("unterrichtKlasseFach", unterrichtKlasseFach, u);
				const unterrichtKlasse : List<StundenplanUnterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idKlasse_zu_unterrichtmenge, idKlasse);
				DeveloperNotificationException.ifListRemoveFailes("unterrichtKL", unterrichtKlasse, u);
			}
		}
		this._list_unterricht.remove(u);
	}

	/**
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanUnterricht}-Objekt.
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}-Objekts.
	 */
	public unterrichtRemoveById(idUnterricht : number) : void {
		this.unterrichtRemoveByIdOhneUpdate(idUnterricht);
		this.update();
	}

	/**
	 * Entfernt alle {@link StundenplanUnterricht}-Objekte.
	 *
	 * @param listUnterricht  Die Liste der zu entfernenden {@link StundenplanUnterricht}-Objekte.
	 */
	public unterrichtRemoveAll(listUnterricht : List<StundenplanUnterricht>) : void {
		for (const unterricht of listUnterricht)
			this.unterrichtRemoveByIdOhneUpdate(unterricht.id);
		this.update();
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
		for (let wochentag : number = this._uZeitrasterWochentagMin; wochentag <= this._uZeitrasterWochentagMax; wochentag++) {
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
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte zum übergebenen Wochentag.
	 */
	public getListZeitrasterZuWochentag(wochentag : Wochentag) : List<StundenplanZeitraster> {
		return CollectionUtils.toFilteredArrayList(this._list_zeitraster, { test : (z: StundenplanZeitraster) => (wochentag.id === z.wochentag) });
	}

	/**
	 * Liefert eine Liste der {@link StundenplanZeitraster}-Objekte zu einer bestimmten Unterrichtsstunde.
	 *
	 * @param unterrichtstunde   die Unterrichtsstunde der gewünschten Zeitraster-Objekte
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte zur übergebenen Unterrichtsstunde.
	 */
	public getListZeitrasterZuStunde(unterrichtstunde : number) : List<StundenplanZeitraster> {
		return CollectionUtils.toFilteredArrayList(this._list_zeitraster, { test : (z: StundenplanZeitraster) => (unterrichtstunde === z.unterrichtstunde) });
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
	 * Liefert das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag.
	 *
	 * @param zeitraster Das aktuelle {@link StundenplanZeitraster}-Objekt.
	 *
	 * @return das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag.
	 */
	public getZeitrasterNext(zeitraster : StundenplanZeitraster) : StundenplanZeitraster {
		return this._map2d_wochentag_stunde_zu_zeitraster.getNonNullOrException(zeitraster.wochentag, zeitraster.unterrichtstunde + 1);
	}

	private zeitrasterAddOhneUpdate(zeitraster : StundenplanZeitraster) : void {
		StundenplanManager.zeitrasterCheck(zeitraster);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idZeitraster_zu_zeitraster, zeitraster.id, zeitraster);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idZeitraster_zu_unterrichtmenge, zeitraster.id, new ArrayList());
		DeveloperNotificationException.ifMap2DPutOverwrites(this._map2d_wochentag_stunde_zu_zeitraster, zeitraster.wochentag, zeitraster.unterrichtstunde, zeitraster);
		MapUtils.getOrCreateArrayList(this._map_wochentag_zu_zeitrastermenge, zeitraster.wochentag).add(zeitraster);
		MapUtils.getOrCreateArrayList(this._map_stunde_zu_zeitrastermenge, zeitraster.unterrichtstunde).add(zeitraster);
		this._list_zeitraster.add(zeitraster);
	}

	/**
	 * Fügt ein {@link StundenplanZeitraster}-Objekt hinzu.
	 *
	 * @param zeitraster  Das {@link StundenplanZeitraster}-Objekt, welches hinzugefügt werden soll.
	 */
	public zeitrasterAdd(zeitraster : StundenplanZeitraster) : void {
		this.zeitrasterAddOhneUpdate(zeitraster);
		this._list_zeitraster.sort(StundenplanManager._compZeitraster);
		this.update();
	}

	/**
	 * Fügt alle {@link StundenplanZeitraster}-Objekte hinzu.
	 *
	 * @param listZeitraster  Die Menge der {@link StundenplanZeitraster}-Objekte, welche hinzugefügt werden soll.
	 */
	public zeitrasterAddAll(listZeitraster : List<StundenplanZeitraster>) : void {
		for (const zeitraster of listZeitraster)
			this.zeitrasterAddOhneUpdate(zeitraster);
		this._list_zeitraster.sort(StundenplanManager._compZeitraster);
		this.update();
	}

	private static zeitrasterCheck(zeitraster : StundenplanZeitraster) : void {
		DeveloperNotificationException.ifInvalidID("zeit.id", zeitraster.id);
		Wochentag.fromIDorException(zeitraster.wochentag);
		DeveloperNotificationException.ifTrue("(zeit.unterrichtstunde < 0) || (zeit.unterrichtstunde > 29)", (zeitraster.unterrichtstunde < 0) || (zeitraster.unterrichtstunde > 29));
		if ((zeitraster.stundenbeginn !== null) && (zeitraster.stundenende !== null)) {
			const beginn : number = zeitraster.stundenbeginn.valueOf();
			const ende : number = zeitraster.stundenende.valueOf();
			DeveloperNotificationException.ifTrue("beginn >= ende", beginn >= ende);
		}
	}

	/**
	 * Liefert den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public zeitrasterGetMinutenMin() : number {
		return this._uZeitrasterMinutenMin;
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
		const min : number | null = this._uZeitrasterMinutenMinByStunde.get(stunde);
		return (min === null) ? 480 : min;
	}

	/**
	 * Liefert den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public zeitrasterGetMinutenMax() : number {
		return this._uZeitrasterMinutenMax;
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
		const max : number | null = this._uZeitrasterMinutenMaxByStunde.get(stunde);
		return (max === null) ? 480 : max;
	}

	/**
	 * Liefert die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetStundeMin() : number {
		return this._uZeitrasterStundeMin;
	}

	/**
	 * Liefert die kleinste nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die kleinste nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetStundeMinOhneLeere() : number {
		return this._uZeitrasterStundeMinOhneLeere;
	}

	/**
	 * Liefert die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetStundeMax() : number {
		return this._uZeitrasterStundeMax;
	}

	/**
	 * Liefert die größte nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die größte nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetStundeMaxOhneLeere() : number {
		return this._uZeitrasterStundeMaxOhneLeere;
	}

	/**
	 * Liefert die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetWochentagMin() : number {
		return this._uZeitrasterWochentagMin;
	}

	/**
	 * Liefert den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetWochentagMinEnum() : Wochentag {
		return Wochentag.fromIDorException(this._uZeitrasterWochentagMin);
	}

	/**
	 * Liefert die ID des größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetWochentagMax() : number {
		return this._uZeitrasterWochentagMax;
	}

	/**
	 * Liefert den größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetWochentagMaxEnum() : Wochentag {
		return Wochentag.fromIDorException(this._uZeitrasterWochentagMax);
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
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param wochentag  Die ENUM-ID des {@link Wochentag} des gesuchten Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde des gesuchten Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 * @throws DeveloperNotificationException falls kein Zeitraster-Eintrag existiert
	 */
	public zeitrasterGetByWochentagAndStundeOrException(wochentag : number, stunde : number) : StundenplanZeitraster {
		return this._map2d_wochentag_stunde_zu_zeitraster.getNonNullOrException(wochentag, stunde);
	}

	/**
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt, falls es existiert, sonst NULL.
	 *
	 * @param wochentag  Die ENUM-ID des {@link Wochentag} des gesuchten Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde des gesuchten Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt, falls es existiert, sonst NULL.
	 */
	public zeitrasterGetByWochentagAndStundeOrNull(wochentag : number, stunde : number) : StundenplanZeitraster | null {
		return this._map2d_wochentag_stunde_zu_zeitraster.getOrNull(wochentag, stunde);
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
		return this._uZeitrasterStundenRange;
	}

	/**
	 * Liefert alle verwendeten sortierten Unterrichtsstunden der nicht leeren {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle Zahlen von {@link #zeitrasterGetStundeMinOhneLeere()} bis {@link #zeitrasterGetStundeMaxOhneLeere()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten Unterrichtsstunden der nicht leeren {@link StundenplanZeitraster}.
	 */
	public zeitrasterGetStundenRangeOhneLeere() : Array<number> {
		return this._uZeitrasterStundenRangeOhneLeere;
	}

	/**
	 * Liefert alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle {@link Wochentag}-Objekte von {@link #zeitrasterGetWochentagMin} bis {@link #zeitrasterGetWochentagMax()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanZeitraster}.
	 */
	public zeitrasterGetWochentageAlsEnumRange() : Array<Wochentag> {
		return this._uZeitrasterWochentageAlsEnumRange;
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
		for (let wochentyp : number = 1; wochentyp <= this._stundenplanWochenTypModell; wochentyp++)
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
	 * Liefert TRUE, falls ein {@link StundenplanZeitraster}-Objekt mit dem Wochentag existiert.
	 *
	 * @param wochentag  Der Wochentag, deren Zeitrastermenge überprüft wird.
	 *
	 * @return TRUE, falls ein {@link StundenplanZeitraster}-Objekt mit dem Wochentag existiert.
	 */
	public zeitrasterExistsByWochentag(wochentag : number) : boolean {
		return !MapUtils.getOrCreateArrayList(this._map_wochentag_zu_zeitrastermenge, wochentag).isEmpty();
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanZeitraster}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanZeitraster#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanZeitraster#stundenbeginn}
	 * <br>{@link StundenplanZeitraster#stundenende}
	 * <br>{@link StundenplanZeitraster#unterrichtstunde}
	 * <br>{@link StundenplanZeitraster#wochentag}
	 *
	 * @param zeitraster  Das neue {@link StundenplanZeitraster}-Objekt, dessen Attribute kopiert werden.
	 */
	public zeitrasterPatchAttributes(zeitraster : StundenplanZeitraster) : void {
		StundenplanManager.zeitrasterCheck(zeitraster);
		const old : StundenplanZeitraster = DeveloperNotificationException.ifMapGetIsNull(this._map_idZeitraster_zu_zeitraster, zeitraster.id);
		DeveloperNotificationException.ifMap2DRemoveFailes(this._map2d_wochentag_stunde_zu_zeitraster, old.wochentag, old.unterrichtstunde);
		MapUtils.removeFromListAndTrimOrException(this._map_wochentag_zu_zeitrastermenge, old.wochentag, old);
		MapUtils.removeFromListAndTrimOrException(this._map_stunde_zu_zeitrastermenge, old.unterrichtstunde, old);
		old.stundenbeginn = zeitraster.stundenbeginn;
		old.stundenende = zeitraster.stundenende;
		old.unterrichtstunde = zeitraster.unterrichtstunde;
		old.wochentag = zeitraster.wochentag;
		DeveloperNotificationException.ifMap2DPutOverwrites(this._map2d_wochentag_stunde_zu_zeitraster, old.wochentag, old.unterrichtstunde, old);
		MapUtils.getOrCreateArrayList(this._map_wochentag_zu_zeitrastermenge, old.wochentag).add(old);
		MapUtils.getOrCreateArrayList(this._map_stunde_zu_zeitrastermenge, old.unterrichtstunde).add(old);
		this._list_zeitraster.sort(StundenplanManager._compZeitraster);
		this.update();
	}

	private zeitrasterRemoveOhneUpdate(idZeitraster : number) : void {
		const listU : List<StundenplanUnterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_idZeitraster_zu_unterrichtmenge, idZeitraster);
		const listU2 : List<StundenplanUnterricht> = new ArrayList(listU);
		for (const u of listU2)
			this.unterrichtRemoveByIdOhneUpdate(u.id);
		const z : StundenplanZeitraster = DeveloperNotificationException.ifNull("_map_zeitrasterID_zu_zeitraster.get(" + idZeitraster + ")", this._map_idZeitraster_zu_zeitraster.get(idZeitraster));
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idZeitraster_zu_zeitraster, idZeitraster);
		DeveloperNotificationException.ifMapRemoveFailes(this._map_idZeitraster_zu_unterrichtmenge, idZeitraster);
		DeveloperNotificationException.ifMap2DRemoveFailes(this._map2d_wochentag_stunde_zu_zeitraster, z.wochentag, z.unterrichtstunde);
		MapUtils.removeFromListAndTrimOrException(this._map_wochentag_zu_zeitrastermenge, z.wochentag, z);
		MapUtils.removeFromListAndTrimOrException(this._map_stunde_zu_zeitrastermenge, z.unterrichtstunde, z);
		DeveloperNotificationException.ifListRemoveFailes("_list_zeitraster", this._list_zeitraster, z);
	}

	/**
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanZeitraster}-Objekt.
	 * <br> Hinweis: Kaskadierend werden auch alle {@link StundenplanUnterricht}-Objekte gelöscht.
	 *
	 * @param idZeitraster  Die Datenbank-ID des {@link StundenplanZeitraster}-Objekts.
	 */
	public zeitrasterRemoveById(idZeitraster : number) : void {
		this.zeitrasterRemoveOhneUpdate(idZeitraster);
		this.update();
	}

	/**
	 * Entfernt alle {@link StundenplanZeitraster}-Objekte aus dem Stundenplan.
	 * <br> Hinweis: Kaskadierend werden auch alle {@link StundenplanUnterricht}-Objekte gelöscht.
	 *
	 * @param listZeitraster  Die {@link StundenplanZeitraster}-Objekte, die entfernt werden sollen.
	 */
	public zeitrasterRemoveAll(listZeitraster : List<StundenplanZeitraster>) : void {
		for (const zeitraster of listZeitraster)
			this.zeitrasterRemoveOhneUpdate(zeitraster.id);
		this.update();
	}

	/**
	 * Entfernt alle {@link StundenplanZeitraster}-Objekte, die einen bestimmten Wochentag haben.
	 *
	 * @param wochentagEnumID  Die ID des {@link Wochentag}.
	 */
	public zeitrasterRemoveByWochentag(wochentagEnumID : number) : void {
		const list : List<StundenplanZeitraster> = ListUtils.getCopyFiltered(this._list_zeitraster, { test : (z: StundenplanZeitraster) => z.wochentag === wochentagEnumID });
		this.zeitrasterRemoveAll(list);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplan.StundenplanManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanManager(obj : unknown) : StundenplanManager {
	return obj as StundenplanManager;
}
