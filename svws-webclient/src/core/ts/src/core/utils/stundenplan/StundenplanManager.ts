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
import { StundenplanUnterricht, cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterricht } from '../../../core/data/stundenplan/StundenplanUnterricht';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { StundenplanKalenderwochenzuordnung } from '../../../core/data/stundenplan/StundenplanKalenderwochenzuordnung';
import { Stundenplan, cast_de_svws_nrw_core_data_stundenplan_Stundenplan } from '../../../core/data/stundenplan/Stundenplan';
import { HashSet } from '../../../java/util/HashSet';
import { StundenplanPausenaufsicht } from '../../../core/data/stundenplan/StundenplanPausenaufsicht';
import { CollectionUtils } from '../../../core/utils/CollectionUtils';
import { MapUtils } from '../../../core/utils/MapUtils';
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { StundenplanPausenzeit } from '../../../core/data/stundenplan/StundenplanPausenzeit';
import { Map2DUtils } from '../../../core/utils/Map2DUtils';
import { StundenplanAufsichtsbereich } from '../../../core/data/stundenplan/StundenplanAufsichtsbereich';
import { StundenplanRaum } from '../../../core/data/stundenplan/StundenplanRaum';
import { StundenplanSchiene } from '../../../core/data/stundenplan/StundenplanSchiene';
import { StundenplanFach } from '../../../core/data/stundenplan/StundenplanFach';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Wochentag } from '../../../core/types/Wochentag';
import type { JavaMap } from '../../../java/util/JavaMap';
import { StundenplanKomplett, cast_de_svws_nrw_core_data_stundenplan_StundenplanKomplett } from '../../../core/data/stundenplan/StundenplanKomplett';

export class StundenplanManager extends JavaObject {

	/**
	 * Ein Comparator für die Räume.
	 */
	private static readonly _compRaum : Comparator<StundenplanRaum> = { compare : (a: StundenplanRaum, b: StundenplanRaum) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Ein Comparator für die Pausenzeiten.
	 */
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

	/**
	 * Ein Comparator für die Aufsichtsbereiche.
	 */
	private static readonly _compAufsichtsbereich : Comparator<StundenplanAufsichtsbereich> = { compare : (a: StundenplanAufsichtsbereich, b: StundenplanAufsichtsbereich) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Ein Comparator für die Pausenaufsichten.
	 */
	private readonly _compPausenaufsicht : Comparator<StundenplanPausenaufsicht> = { compare : (a: StundenplanPausenaufsicht, b: StundenplanPausenaufsicht) => JavaLong.compare(a.id, b.id) };

	/**
	 * Ein Comparator für die Zeitraster.
	 */
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

	/**
	 * Ein Comparator für die StundenplanKalenderwochenzuordnung.
	 */
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

	private readonly _map_klasseID_zu_klasse : HashMap<number, StundenplanKlasse> = new HashMap();

	private readonly _map_jahrgangID_zu_jahrgang : HashMap<number, StundenplanJahrgang> = new HashMap();

	private readonly _map_lehrerID_zu_lehrer : HashMap<number, StundenplanLehrer> = new HashMap();

	private readonly _map_schuelerID_zu_schueler : HashMap<number, StundenplanSchueler> = new HashMap();

	private readonly _map_kursID_zu_kurs : HashMap<number, StundenplanKurs> = new HashMap();

	private readonly _map_pausenzeitID_zu_pausenzeit : HashMap<number, StundenplanPausenzeit> = new HashMap();

	private readonly _map_raumID_zu_raum : HashMap<number, StundenplanRaum> = new HashMap();

	private readonly _map_schieneID_zu_schiene : HashMap<number, StundenplanSchiene> = new HashMap();

	private readonly _map_aufsichtsbereichID_zu_aufsichtsbereich : HashMap<number, StundenplanAufsichtsbereich> = new HashMap();

	private readonly _map_kwzID_zu_kwz : HashMap<number, StundenplanKalenderwochenzuordnung> = new HashMap();

	private readonly _map_jahr_kw_zu_kwz : HashMap2D<number, number, StundenplanKalenderwochenzuordnung> = new HashMap2D();

	private readonly _map_pausenaufsichtID_zu_pausenaufsicht : HashMap<number, StundenplanPausenaufsicht> = new HashMap();

	private readonly _map_fachID_zu_fach : HashMap<number, StundenplanFach> = new HashMap();

	private readonly _list_faecher : List<StundenplanFach> = new ArrayList();

	private readonly _map_zeitrasterID_zu_zeitraster : HashMap<number, StundenplanZeitraster> = new HashMap();

	private readonly _map2d_wochentag_stunde_zu_zeitraster : HashMap2D<number, number, StundenplanZeitraster> = new HashMap2D();

	private readonly _map_wochentag_zu_zeitrastermenge : HashMap<number, List<StundenplanZeitraster>> = new HashMap();

	private readonly _map_stunde_zu_zeitrastermenge : HashMap<number, List<StundenplanZeitraster>> = new HashMap();

	private readonly _list_zeitraster : List<StundenplanZeitraster> = new ArrayList();

	private _zeitrasterWochentagMin : number = Wochentag.MONTAG.id;

	private _zeitrasterWochentagMax : number = Wochentag.MONTAG.id;

	private _zeitrasterStundeMin : number = 1;

	private _zeitrasterStundeMax : number = 1;

	private _unterrichtHatMultiWochen : boolean = false;

	private readonly _map_kursID_zu_unterrichte : HashMap<number, List<StundenplanUnterricht>> = new HashMap();

	private readonly _map_idUnterricht_zu_unterricht : HashMap<number, StundenplanUnterricht> = new HashMap();

	private readonly _map2d_idZeitraster_wochentyp_zu_unterrichte : HashMap2D<number, number, List<StundenplanUnterricht>> = new HashMap2D();

	private readonly _map_idZeitraster_zu_unterrichte : HashMap<number, List<StundenplanUnterricht>> = new HashMap();

	private readonly _list_unterricht : List<StundenplanUnterricht> = new ArrayList();

	private readonly _list_pausenaufsichten : List<StundenplanPausenaufsicht> = new ArrayList();

	private readonly _list_raeume : List<StundenplanRaum> = new ArrayList();

	private readonly _list_pausenzeiten : List<StundenplanPausenzeit> = new ArrayList();

	private readonly _list_aufsichtsbereiche : List<StundenplanAufsichtsbereich> = new ArrayList();

	private readonly _list_kalenderwochenzuordnungen : List<StundenplanKalenderwochenzuordnung> = new ArrayList();

	private readonly _list_schueler : List<StundenplanSchueler> = new ArrayList();

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
		this.fachAddAll(listFach);
		this.initJahrgaenge(listJahrgang);
		this.zeitrasterAddAll(listZeitraster);
		this.initRaeume(listRaum);
		this.initPausenzeiten(listPausenzeit);
		this.initAufsichtsbereiche(listAufsichtsbereich);
		this.initKlassen(listKlasse);
		this.initLehrer(listLehrer);
		this.initSchueler(listSchueler);
		this.initSchienen(listSchiene);
		this.initPausenaufsichten(listPausenaufsicht);
		this.initKurse(listKurs);
		this.initUnterrichte(listUnterricht);
	}

	private fachAddOhneUpdate(fach : StundenplanFach) : void {
		DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
		DeveloperNotificationException.ifStringIsBlank("fach.bezeichnung", fach.bezeichnung);
		DeveloperNotificationException.ifStringIsBlank("fach.kuerzel", fach.kuerzel);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_fachID_zu_fach, fach.id, fach);
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

	private fachUpdate() : void {
		const setFachKuerzel : HashSet<string> = new HashSet();
		for (const fach of this._list_faecher)
			DeveloperNotificationException.ifSetAddsDuplicate("setFachKuerzel", setFachKuerzel, fach.kuerzel);
	}

	private kalenderwochenzuordnungAddOhneUpdate(kwz : StundenplanKalenderwochenzuordnung) : void {
		DeveloperNotificationException.ifInvalidID("kw.id", kwz.id);
		DeveloperNotificationException.ifTrue("(kwz.jahr < 2000) || (kwz.jahr > 3000)", (kwz.jahr < 2000) || (kwz.jahr > 3000));
		DeveloperNotificationException.ifTrue("(kwz.kw < 1) || (kwz.kw > 53)", (kwz.kw < 1) || (kwz.kw > 53));
		DeveloperNotificationException.ifTrue("kwz.wochentyp > stundenplanWochenTypModell", kwz.wochentyp > this.stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("kwz.wochentyp <= 0", kwz.wochentyp <= 0);
		DeveloperNotificationException.ifMap2DPutOverwrites(this._map_jahr_kw_zu_kwz, kwz.jahr, kwz.kw, kwz);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_kwzID_zu_kwz, kwz.id, kwz);
		this._list_kalenderwochenzuordnungen.add(kwz);
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
	 * Aktualisiert verschiedene Werte nachdem sich die Menge der {@link StundenplanKalenderwochenzuordnung} verändert hat.
	 * <br>Laufzeit: O(|StundenplanKalenderwochenzuordnung| * log)
	 */
	private kalenderwochenzuordnungUpdate() : void {
		this._list_kalenderwochenzuordnungen.sort(StundenplanManager._compKWZ);
	}

	private initJahrgaenge(listJahrgang : List<StundenplanJahrgang>) : void {
		this._map_jahrgangID_zu_jahrgang.clear();
		const setJahrgangKuerzel : HashSet<string> = new HashSet();
		for (const jahrgang of listJahrgang) {
			DeveloperNotificationException.ifInvalidID("jahrgang.id", jahrgang.id);
			DeveloperNotificationException.ifStringIsBlank("jahrgang.bezeichnung", jahrgang.bezeichnung);
			DeveloperNotificationException.ifStringIsBlank("jahrgang.kuerzel", jahrgang.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setJahrgangKuerzel", setJahrgangKuerzel, jahrgang.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_jahrgangID_zu_jahrgang, jahrgang.id, jahrgang);
		}
	}

	private initLehrer(listLehrer : List<StundenplanLehrer>) : void {
		this._map_lehrerID_zu_lehrer.clear();
		const setLehrerKuerzel : HashSet<string> = new HashSet();
		for (const lehrer of listLehrer) {
			DeveloperNotificationException.ifInvalidID("lehrer.id", lehrer.id);
			DeveloperNotificationException.ifStringIsBlank("lehrer.kuerzel", lehrer.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setLehrerKuerzel", setLehrerKuerzel, lehrer.kuerzel);
			DeveloperNotificationException.ifStringIsBlank("lehrer.nachname", lehrer.nachname);
			DeveloperNotificationException.ifStringIsBlank("lehrer.vorname", lehrer.vorname);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_lehrerID_zu_lehrer, lehrer.id, lehrer);
			const setFaecherDerLehrkraft : HashSet<number> = new HashSet();
			for (const idFachDerLehrkraft of lehrer.faecher) {
				DeveloperNotificationException.ifMapNotContains("_map_fachID_zu_fach", this._map_fachID_zu_fach, idFachDerLehrkraft);
				DeveloperNotificationException.ifSetAddsDuplicate("setFaecherDerLehrkraft", setFaecherDerLehrkraft, idFachDerLehrkraft);
			}
		}
	}

	private initKlassen(listKlasse : List<StundenplanKlasse>) : void {
		this._map_klasseID_zu_klasse.clear();
		const setKlasseKuerzel : HashSet<string> = new HashSet();
		for (const klasse of listKlasse) {
			DeveloperNotificationException.ifInvalidID("klasse.id", klasse.id);
			DeveloperNotificationException.ifStringIsBlank("klasse.kuerzel", klasse.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setKlasseKuerzel", setKlasseKuerzel, klasse.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_klasseID_zu_klasse, klasse.id, klasse);
			const setJahrgaengeDerKlasse : HashSet<number> = new HashSet();
			for (const idJahrgangDerKlasse of klasse.jahrgaenge) {
				DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", this._map_jahrgangID_zu_jahrgang, idJahrgangDerKlasse);
				DeveloperNotificationException.ifSetAddsDuplicate("setJahrgaengeDerKlasse", setJahrgaengeDerKlasse, idJahrgangDerKlasse);
			}
		}
	}

	private initSchueler(listSchueler : List<StundenplanSchueler>) : void {
		this._map_schuelerID_zu_schueler.clear();
		this._list_schueler.clear();
		for (const schueler of listSchueler) {
			DeveloperNotificationException.ifInvalidID("schueler.id", schueler.id);
			DeveloperNotificationException.ifStringIsBlank("schueler.nachname", schueler.nachname);
			DeveloperNotificationException.ifStringIsBlank("schueler.vorname", schueler.vorname);
			DeveloperNotificationException.ifMapNotContains("_map_klasseID_zu_klasse", this._map_klasseID_zu_klasse, schueler.idKlasse);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_zu_schueler, schueler.id, schueler);
			this._list_schueler.add(schueler);
		}
	}

	private initSchienen(listSchiene : List<StundenplanSchiene>) : void {
		this._map_schieneID_zu_schiene.clear();
		for (const schiene of listSchiene) {
			DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
			DeveloperNotificationException.ifTrue("schiene.nummer <= 0", schiene.nummer <= 0);
			DeveloperNotificationException.ifStringIsBlank("schiene.bezeichnung", schiene.bezeichnung);
			DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", this._map_jahrgangID_zu_jahrgang, schiene.idJahrgang);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schieneID_zu_schiene, schiene.id, schiene);
		}
	}

	private initKurse(listKurs : List<StundenplanKurs>) : void {
		this._map_kursID_zu_kurs.clear();
		for (const kurs of listKurs) {
			DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);
			DeveloperNotificationException.ifStringIsBlank("kurs.bezeichnung", kurs.bezeichnung);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_kursID_zu_kurs, kurs.id, kurs);
			for (const idSchieneDesKurses of kurs.schienen)
				DeveloperNotificationException.ifMapNotContains("_map_schieneID_zu_schiene", this._map_schieneID_zu_schiene, idSchieneDesKurses);
			for (const idJahrgangDesKurses of kurs.jahrgaenge)
				DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", this._map_jahrgangID_zu_jahrgang, idJahrgangDesKurses);
			for (const idSchuelerDesKurses of kurs.schueler)
				DeveloperNotificationException.ifMapNotContains("_map_schuelerID_zu_schueler", this._map_schuelerID_zu_schueler, idSchuelerDesKurses);
		}
	}

	private initRaeume(listRaum : List<StundenplanRaum>) : void {
		this._map_raumID_zu_raum.clear();
		this._list_raeume.clear();
		const setRaumKuerzel : HashSet<string> = new HashSet();
		for (const raum of listRaum) {
			DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
			DeveloperNotificationException.ifStringIsBlank("raum.kuerzel", raum.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setRaumKuerzel", setRaumKuerzel, raum.kuerzel);
			DeveloperNotificationException.ifTrue("raum.groesse < 0", raum.groesse < 0);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_raumID_zu_raum, raum.id, raum);
			this._list_raeume.add(raum);
		}
		this._list_raeume.sort(StundenplanManager._compRaum);
	}

	private initPausenzeiten(listPausenzeit : List<StundenplanPausenzeit>) : void {
		this._map_pausenzeitID_zu_pausenzeit.clear();
		this._list_pausenzeiten.clear();
		for (const pausenzeit of listPausenzeit) {
			DeveloperNotificationException.ifInvalidID("pause.id", pausenzeit.id);
			Wochentag.fromIDorException(pausenzeit.wochentag);
			if ((pausenzeit.beginn !== null) && (pausenzeit.ende !== null)) {
				const beginn : number = pausenzeit.beginn.valueOf();
				const ende : number = pausenzeit.ende.valueOf();
				DeveloperNotificationException.ifTrue("beginn >= ende", beginn >= ende);
			}
			DeveloperNotificationException.ifMapPutOverwrites(this._map_pausenzeitID_zu_pausenzeit, pausenzeit.id, pausenzeit);
			this._list_pausenzeiten.add(pausenzeit);
		}
		this._list_pausenzeiten.sort(StundenplanManager._compPausenzeit);
	}

	private initAufsichtsbereiche(listAufsichtsbereich : List<StundenplanAufsichtsbereich>) : void {
		this._map_aufsichtsbereichID_zu_aufsichtsbereich.clear();
		this._list_aufsichtsbereiche.clear();
		const setAufsichtKuerzel : HashSet<string> = new HashSet();
		for (const aufsicht of listAufsichtsbereich) {
			DeveloperNotificationException.ifInvalidID("aufsicht.id", aufsicht.id);
			DeveloperNotificationException.ifStringIsBlank("aufsicht.kuerzel", aufsicht.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setAufsichtKuerzel", setAufsichtKuerzel, aufsicht.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_aufsichtsbereichID_zu_aufsichtsbereich, aufsicht.id, aufsicht);
			this._list_aufsichtsbereiche.add(aufsicht);
		}
		this._list_aufsichtsbereiche.sort(StundenplanManager._compAufsichtsbereich);
	}

	private initUnterrichte(listUnterricht : List<StundenplanUnterricht>) : void {
		this._map_idUnterricht_zu_unterricht.clear();
		this._map_idZeitraster_zu_unterrichte.clear();
		this._map2d_idZeitraster_wochentyp_zu_unterrichte.clear();
		this._map_kursID_zu_unterrichte.clear();
		for (const idKurs of this._map_kursID_zu_kurs.keySet())
			this._map_kursID_zu_unterrichte.put(idKurs, new ArrayList());
		for (const u of listUnterricht) {
			DeveloperNotificationException.ifInvalidID("u.id", u.id);
			DeveloperNotificationException.ifMapNotContains("_map_zeitrasterID_zu_zeitraster", this._map_zeitrasterID_zu_zeitraster, u.idZeitraster);
			DeveloperNotificationException.ifTrue("u.wochentyp > stundenplanWochenTypModell", u.wochentyp > this.stundenplanWochenTypModell);
			DeveloperNotificationException.ifTrue("u.wochentyp < 0", u.wochentyp < 0);
			if (u.idKurs !== null) {
				DeveloperNotificationException.ifMapNotContains("_map_kursID_zu_kurs", this._map_kursID_zu_kurs, u.idKurs);
				const listDerUnterrichte : List<StundenplanUnterricht> = DeveloperNotificationException.ifMapGetIsNull(this._map_kursID_zu_unterrichte, u.idKurs);
				DeveloperNotificationException.ifListAddsDuplicate("listDerUnterrichte", listDerUnterrichte, u);
			}
			DeveloperNotificationException.ifMapNotContains("_map_fachID_zu_fach", this._map_fachID_zu_fach, u.idFach);
			for (const idLehrkraftDesUnterrichts of u.lehrer)
				DeveloperNotificationException.ifMapNotContains("_map_lehrerID_zu_lehrer", this._map_lehrerID_zu_lehrer, idLehrkraftDesUnterrichts);
			for (const idKlasseDesUnterrichts of u.klassen)
				DeveloperNotificationException.ifMapNotContains("_map_klasseID_zu_klasse", this._map_klasseID_zu_klasse, idKlasseDesUnterrichts);
			for (const idRaumDesUnterrichts of u.raeume)
				DeveloperNotificationException.ifMapNotContains("_map_raumID_zu_raum", this._map_raumID_zu_raum, idRaumDesUnterrichts);
			for (const idSchieneDesUnterrichts of u.schienen)
				DeveloperNotificationException.ifMapNotContains("_map_schieneID_zu_schiene", this._map_schieneID_zu_schiene, idSchieneDesUnterrichts);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_idUnterricht_zu_unterricht, u.id, u);
			MapUtils.getOrCreateArrayList(this._map_idZeitraster_zu_unterrichte, u.idZeitraster).add(u);
			Map2DUtils.getOrCreateArrayList(this._map2d_idZeitraster_wochentyp_zu_unterrichte, u.idZeitraster, u.wochentyp).add(u);
			this._list_unterricht.add(u);
		}
		this.unterrichtUpdate();
	}

	private initPausenaufsichten(listPausenaufsicht : List<StundenplanPausenaufsicht>) : void {
		this._map_pausenaufsichtID_zu_pausenaufsicht.clear();
		this._list_pausenaufsichten.clear();
		for (const pa of listPausenaufsicht) {
			DeveloperNotificationException.ifInvalidID("aufsicht.id", pa.id);
			DeveloperNotificationException.ifMapNotContains("_map_pausenzeitID_zu_pausenzeit", this._map_pausenzeitID_zu_pausenzeit, pa.idPausenzeit);
			DeveloperNotificationException.ifMapNotContains("_map_lehrerID_zu_lehrer", this._map_lehrerID_zu_lehrer, pa.idLehrer);
			DeveloperNotificationException.ifTrue("(pa.wochentyp > 0) && (pa.wochentyp > stundenplanWochenTypModell)", (pa.wochentyp > 0) && (pa.wochentyp > this.stundenplanWochenTypModell));
			const setBereicheDieserAufsicht : HashSet<number> = new HashSet();
			for (const idAufsichtsbereich of pa.bereiche) {
				DeveloperNotificationException.ifMapNotContains("_map_aufsichtsbereichID_zu_aufsichtsbereich", this._map_aufsichtsbereichID_zu_aufsichtsbereich, idAufsichtsbereich);
				DeveloperNotificationException.ifSetAddsDuplicate("setBereicheDieserAufsicht", setBereicheDieserAufsicht, idAufsichtsbereich);
			}
			DeveloperNotificationException.ifMapPutOverwrites(this._map_pausenaufsichtID_zu_pausenaufsicht, pa.id, pa);
			this._list_pausenaufsichten.add(pa);
		}
		this._list_pausenaufsichten.sort(this._compPausenaufsicht);
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
		if (this.stundenplanWochenTypModell === 0)
			return 0;
		const z : StundenplanKalenderwochenzuordnung | null = this._map_jahr_kw_zu_kwz.getOrNull(jahr, kalenderwoche);
		if (z !== null)
			return z.wochentyp;
		const wochentyp : number = kalenderwoche % this.stundenplanWochenTypModell;
		return wochentyp === 0 ? this.stundenplanWochenTypModell : wochentyp;
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
		return (this.stundenplanWochenTypModell >= 2) && (z !== null);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses mit einem bestimmten Wochentyp.
	 *
	 * @param idKkurs   Die ID des Kurses.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public getUnterrichtDesKursesByWochentyp(idKkurs : number, wochentyp : number) : List<StundenplanUnterricht> {
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > this.stundenplanWochenTypModell);
		const list : List<StundenplanUnterricht> = DeveloperNotificationException.ifNull("_map_kursID_zu_unterrichte.get(kursID)==NULL", this._map_kursID_zu_unterrichte.get(idKkurs));
		return CollectionUtils.toFilteredArrayList(list, { test : (u: StundenplanUnterricht) => (u.wochentyp === 0) || (u.wochentyp === wochentyp) });
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
	public getUnterrichtDesKursesByKW(idKurs : number, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht> {
		const wochentyp : number = this.getWochentypOrDefault(jahr, kalenderwoche);
		return this.getUnterrichtDesKursesByWochentyp(idKurs, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 *
	 * @param idsKurs   Die IDs aller Kurse.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 */
	public getUnterrichtDerKurseByWochentyp(idsKurs : Array<number>, wochentyp : number) : List<StundenplanUnterricht> {
		const result : ArrayList<StundenplanUnterricht> = new ArrayList();
		for (const idKurs of idsKurs)
			result.addAll(this.getUnterrichtDesKursesByWochentyp(idKurs, wochentyp));
		return result;
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
	public getUnterrichtDerKurseByKW(idsKurs : Array<number>, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht> {
		const wochentyp : number = this.getWochentypOrDefault(jahr, kalenderwoche);
		return this.getUnterrichtDerKurseByWochentyp(idsKurs, wochentyp);
	}

	/**
	 * Liefert alle Unterrichte des Schülers bezüglich einer bestimmten Stundenplanzelle.<br>
	 * Die Liste sollte 0 oder 1 Element (in der Regel) enthalten.
	 *
	 * @param idSchueler        Die Datenbank-ID des Schülers.
	 * @param wochentyp         Der Typ der Woche (beispielsweise bei AB-Wochen).
	 * @param wochentag         Der gewünschte {@link Wochentag}.
	 * @param unterrichtstunde  Die gewünschte Unterrichtsstunde.
	 *
	 * @return alle Unterrichte des Schülers bezüglich einer bestimmten Stundenplanzelle.
	 */
	public unterrichtGetMengeBySchuelerIDWochentypWochentagUnterrichtsstunde(idSchueler : number, wochentyp : number, wochentag : Wochentag, unterrichtstunde : number) : List<StundenplanUnterricht> {
		return new ArrayList();
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
	public getKurseGefiltert(idsKurs : List<number>, wochentyp : number, wochentag : Wochentag, unterrichtstunde : number) : List<number> {
		return CollectionUtils.toFilteredArrayList(idsKurs, { test : (idKurs: number) => this.testKursHatUnterrichtAm(idKurs!, wochentyp, wochentag, unterrichtstunde) });
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
	public getKurseGefiltertByDatum(idsKurs : List<number>, datumISO8601 : string, unterrichtstunde : number) : List<number> {
		const e : Array<number> | null = DateUtils.extractFromDateISO8601(datumISO8601);
		const wochentyp : number = this.getWochentypOrDefault(e[6], e[5]);
		const wochentag : Wochentag = Wochentag.fromIDorException(e[3]);
		return this.getKurseGefiltert(idsKurs, wochentyp, wochentag, unterrichtstunde);
	}

	/**
	 * Liefert TRUE, falls der übergebene Kurs am (Wochentyp / Wochentag / Unterrichtsstunde)  hat.
	 *
	 * @param idKurs           Die ID des Kurses.
	 * @param wochentyp        Der Typ der Woche (beispielsweise bei AB-Wochen).
	 * @param wochentag        Der gewünschte {@link Wochentag}.
	 * @param unterrichtstunde Die gewünschte Unterrichtsstunde.
	 *
	 * @return TRUE, falls der übergebene Kurs am (wochentyp / wochentag / Unterrichtsstunde)  hat.
	 */
	public testKursHatUnterrichtAm(idKurs : number, wochentyp : number, wochentag : Wochentag, unterrichtstunde : number) : boolean {
		for (const u of this.getUnterrichtDesKursesByWochentyp(idKurs, wochentyp)) {
			const z : StundenplanZeitraster = this.zeitrasterGetByIdOrException(u.idZeitraster);
			if ((z.wochentag === wochentag.id) && (z.unterrichtstunde === unterrichtstunde))
				return true;
		}
		return false;
	}

	/**
	 * Liefert eine Map der Aufsichtsbereiche {@link StundenplanAufsichtsbereich} für den aktuell ausgewählten Stundenplan.
	 *
	 * @return eine Map der Aufsichtsbereiche {@link StundenplanAufsichtsbereich}
	 */
	public getMapAufsichtsbereich() : JavaMap<number, StundenplanAufsichtsbereich> {
		return this._map_aufsichtsbereichID_zu_aufsichtsbereich;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanRaum}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanRaum}-Objekte.
	 */
	public getListRaum() : List<StundenplanRaum> {
		return this._list_raeume;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 */
	public getListPausenzeit() : List<StundenplanPausenzeit> {
		return this._list_pausenzeiten;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public getListAufsichtbereich() : List<StundenplanAufsichtsbereich> {
		return this._list_aufsichtsbereiche;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public getListKalenderwochenzuordnung() : List<StundenplanKalenderwochenzuordnung> {
		return this._list_kalenderwochenzuordnungen;
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 *
	 * @param idRaum Die ID des angefragten-Objektes.
	 *
	 * @return das zur raumID zugehörige {@link StundenplanRaum}-Objekt.
	 */
	public getRaum(idRaum : number) : StundenplanRaum {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_raumID_zu_raum, idRaum);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param idPausenzeit Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 */
	public getPausenzeit(idPausenzeit : number) : StundenplanPausenzeit {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_pausenzeitID_zu_pausenzeit, idPausenzeit);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 *
	 * @param idAufsichtsbereich Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 */
	public getAufsichtsbereich(idAufsichtsbereich : number) : StundenplanAufsichtsbereich {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_aufsichtsbereichID_zu_aufsichtsbereich, idAufsichtsbereich);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @param idKWZ Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 */
	public getKalenderwochenzuordnung(idKWZ : number) : StundenplanKalenderwochenzuordnung {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_kwzID_zu_kwz, idKWZ);
	}

	/**
	 * Fügt dem Stundenplan einen neuen {@link StundenplanRaum} hinzu.
	 *
	 * @param raum Der Raum, der hinzugefügt werden soll.
	 */
	public addRaum(raum : StundenplanRaum) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_raumID_zu_raum, raum.id, raum);
		this._list_raeume.add(raum);
		this._list_raeume.sort(StundenplanManager._compRaum);
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanPausenzeit} hinzu.
	 *
	 * @param pausenzeit Die Pausenzeit, die hinzugefügt werden soll.
	 */
	public addPausenzeit(pausenzeit : StundenplanPausenzeit) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_pausenzeitID_zu_pausenzeit, pausenzeit.id, pausenzeit);
		this._list_pausenzeiten.add(pausenzeit);
		this._list_pausenzeiten.sort(StundenplanManager._compPausenzeit);
	}

	/**
	 * Fügt dem Stundenplan einen neuen {@link StundenplanAufsichtsbereich} hinzu.
	 *
	 * @param aufsichtsbereich Der Aufsichtsbereich, der hinzugefügt werden soll.
	 */
	public addAufsichtsbereich(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_aufsichtsbereichID_zu_aufsichtsbereich, aufsichtsbereich.id, aufsichtsbereich);
		this._list_aufsichtsbereiche.add(aufsichtsbereich);
		this._list_aufsichtsbereiche.sort(StundenplanManager._compAufsichtsbereich);
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanKalenderwochenzuordnung} hinzu.
	 *
	 * @param kwz Die Kalenderwochenzuordnung, die hinzugefügt werden soll.
	 */
	public addKalenderwochenzuordnung(kwz : StundenplanKalenderwochenzuordnung) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_kwzID_zu_kwz, kwz.id, kwz);
		this._list_kalenderwochenzuordnungen.add(kwz);
		this._list_kalenderwochenzuordnungen.sort(StundenplanManager._compKWZ);
	}

	/**
	 * Entfernt aus dem Stundenplan einen existierenden {@link StundenplanRaum}-Objekt.
	 *
	 * @param idRaum Die ID des {@link StundenplanRaum}-Objekts.
	 */
	public removeRaum(idRaum : number) : void {
		const raum : StundenplanRaum = DeveloperNotificationException.ifNull("_map_raumID_zu_raum.get(" + idRaum + ")", this._map_raumID_zu_raum.get(idRaum));
		this._map_raumID_zu_raum.remove(idRaum);
		this._list_raeume.remove(raum);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param idPausenzeit Die ID des {@link StundenplanPausenzeit}-Objekts.
	 */
	public removePausenzeit(idPausenzeit : number) : void {
		const pausenzeit : StundenplanPausenzeit = DeveloperNotificationException.ifNull("_map_pausenzeitID_zu_pausenzeit.get(" + idPausenzeit + ")", this._map_pausenzeitID_zu_pausenzeit.get(idPausenzeit));
		this._map_pausenzeitID_zu_pausenzeit.remove(idPausenzeit);
		this._list_pausenzeiten.remove(pausenzeit);
	}

	/**
	 * Entfernt aus dem Stundenplan einen existierendes {@link StundenplanAufsichtsbereich}-Objekt.
	 *
	 * @param idAufsichtsbereich Die ID des {@link StundenplanAufsichtsbereich}-Objekts.
	 */
	public removeAufsichtsbereich(idAufsichtsbereich : number) : void {
		const aufsichtsbereich : StundenplanAufsichtsbereich = DeveloperNotificationException.ifNull("_map_aufsichtID_zu_aufsicht.get(" + idAufsichtsbereich + ")", this._map_aufsichtsbereichID_zu_aufsichtsbereich.get(idAufsichtsbereich));
		this._map_aufsichtsbereichID_zu_aufsichtsbereich.remove(idAufsichtsbereich);
		this._list_aufsichtsbereiche.remove(aufsichtsbereich);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @param idKWZ Die ID des {@link StundenplanKalenderwochenzuordnung}-Objekts.
	 */
	public removeKalenderwochenzuordnung(idKWZ : number) : void {
		const kwz : StundenplanKalenderwochenzuordnung = DeveloperNotificationException.ifNull("_map_kwzID_zu_kwz.get(" + idKWZ + ")", this._map_kwzID_zu_kwz.get(idKWZ));
		this._map_kwzID_zu_kwz.remove(idKWZ);
		this._list_kalenderwochenzuordnungen.remove(kwz);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanRaum}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param raum Das neue {@link StundenplanRaum}-Objekt, welches das alte Objekt ersetzt.
	 */
	public patchRaum(raum : StundenplanRaum) : void {
		this.removeRaum(raum.id);
		this.addRaum(raum);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanPausenzeit}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param pausenzeit Das neue {@link StundenplanPausenzeit}-Objekt, welches das alte Objekt ersetzt.
	 */
	public patchPausenzeit(pausenzeit : StundenplanPausenzeit) : void {
		this.removePausenzeit(pausenzeit.id);
		this.addPausenzeit(pausenzeit);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanAufsichtsbereich}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param aufsichtsbereich Das neue {@link StundenplanAufsichtsbereich}-Objekt, welches das alte Objekt ersetzt.
	 */
	public patchAufsichtsbereich(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		this.removeAufsichtsbereich(aufsichtsbereich.id);
		this.addAufsichtsbereich(aufsichtsbereich);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanKalenderwochenzuordnung}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param kwz Das neue {@link StundenplanKalenderwochenzuordnung}-Objekt, welches das alte Objekt ersetzt.
	 */
	public patchKalenderwochenzuordnung(kwz : StundenplanKalenderwochenzuordnung) : void {
		this.removeKalenderwochenzuordnung(kwz.id);
		this.addKalenderwochenzuordnung(kwz);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 *
	 * @deprecated use {@link #pausenaufsichtGetMenge()}
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 */
	public getListPausenaufsicht() : List<StundenplanPausenaufsicht> {
		return this.pausenaufsichtGetMenge();
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanPausenaufsicht} hinzu.
	 *
	 * @deprecated use {@link #pausenaufsichtAdd(StundenplanPausenaufsicht)}
	 * @param pausenaufsicht Die StundenplanPausenaufsicht, die hinzugefügt werden soll.
	 */
	public addPausenaufsicht(pausenaufsicht : StundenplanPausenaufsicht) : void {
		this.pausenaufsichtAdd(pausenaufsicht);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @deprecated use {@link #pausenaufsichtRemove(long)}
	 * @param idPausenaufsicht Die ID des {@link StundenplanPausenaufsicht}-Objekts.
	 */
	public removePausenaufsicht(idPausenaufsicht : number) : void {
		this.pausenaufsichtRemove(idPausenaufsicht);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @deprecated use {@link #pausenaufsichtGet(long)}
	 * @param idPausenaufsicht Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 */
	public getPausenaufsicht(idPausenaufsicht : number) : StundenplanPausenaufsicht {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_pausenaufsichtID_zu_pausenaufsicht, idPausenaufsicht);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanPausenaufsicht}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @deprecated use {@link #pausenaufsichtPatch(StundenplanPausenaufsicht)}
	 * @param pausenaufsicht Das neue {@link StundenplanPausenaufsicht}-Objekt, welches das alte Objekt ersetzt.
	 */
	public patchPausenaufsicht(pausenaufsicht : StundenplanPausenaufsicht) : void {
		this.pausenaufsichtPatch(pausenaufsicht);
	}

	/**
	 * Liefert die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 *
	 * @deprecated use {@link #zeitrasterGetStundeMin()}
	 * @return die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public getZeitrasterStundeMin() : number {
		return this.zeitrasterGetStundeMin();
	}

	/**
	 * Liefert die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 *
	 * @deprecated use {@link #zeitrasterGetStundeMax()}
	 * @return die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public getZeitrasterStundeMax() : number {
		return this.zeitrasterGetStundeMax();
	}

	/**
	 * Liefert die ID des Stundenplans.
	 *
	 * @deprecated use {@link #stundenplanGetID()}
	 * @return die ID des Stundenplans.
	 */
	public getID() : number {
		return this.stundenplanID;
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
		return CollectionUtils.toFilteredArrayList(this._list_zeitraster, { test : (z: StundenplanZeitraster) => (wochentag.id === z.wochentag) && this.testIntervalleSchneidenSich(beginn, ende, z.stundenbeginn, z.stundenende) });
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 * @deprecated use {@link #zeitrasterGetByIdOrException(long)}
	 */
	public getZeitraster(idZeitraster : number) : StundenplanZeitraster {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_zeitrasterID_zu_zeitraster, idZeitraster);
	}

	/**
	 * Liefert den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 *
	 * @return den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public getZeitrasterMinutenMin() : number {
		for (const z of this._list_zeitraster)
			if (z.stundenbeginn !== null) {
				let min : number = z.stundenbeginn.valueOf();
				for (const z2 of this._list_zeitraster)
					if ((z2.stundenbeginn !== null) && (z2.stundenbeginn < min))
						min = z2.stundenbeginn.valueOf();
				return min;
			}
		return 480;
	}

	/**
	 * Liefert den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 *
	 * @return den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public getZeitrasterMinutenMax() : number {
		for (const z of this._list_zeitraster)
			if (z.stundenende !== null) {
				let max : number = z.stundenende.valueOf();
				for (const z2 of this._list_zeitraster)
					if ((z2.stundenende !== null) && (z2.stundenende > max))
						max = z2.stundenende.valueOf();
				return max;
			}
		return 480;
	}

	/**
	 * Liefert den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 *
	 * @return den kleinsten {@link Wochentag}, oder den Montag falls es keine Zeitraster gibt.
	 * @deprecated use {@link #zeitrasterGetWochentagMinEnum()}
	 */
	public getZeitrasterWochentagMin() : Wochentag {
		return this.zeitrasterGetWochentagMinEnum();
	}

	/**
	 * Liefert den größten {@link Wochentag}, oder den Montag falls es keine Zeitraster gibt.
	 *
	 * @return den größten {@link Wochentag}, oder den Montag falls es keine Zeitraster gibt.
	 * @deprecated use {@link #zeitrasterGetWochentagMaxEnum()}
	 */
	public getZeitrasterWochentagMax() : Wochentag {
		return this.zeitrasterGetWochentagMaxEnum();
	}

	/**
	 * Liefert TRUE, falls zu (wochentag, stunde) ein zugehöriges {@link StundenplanZeitraster}-Objekt existiert.
	 *
	 * @param wochentag Der {@link Wochentag} des Zeitrasters.
	 * @param stunde    Die Unterrichtsstunde Zeitrasters.
	 *
	 * @return TRUE, falls zu (wochentag, stunde) ein zugehöriges {@link StundenplanZeitraster}-Objekt existiert.
	 * @deprecated use {@link #zeitrasterExistsByWochentagAndStunde(int, int)}
	 */
	public testZeitrasterByWochentagStunde(wochentag : Wochentag, stunde : number) : boolean {
		return this.zeitrasterExistsByWochentagAndStunde(wochentag.id, stunde);
	}

	/**
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param wochentag Der {@link Wochentag} des Zeitrasters.
	 * @param stunde    Die Unterrichtsstunde Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 * @deprecated use {@link #zeitrasterGetByWochentagAndStundeOrException(int, int)}
	 */
	public getZeitrasterByWochentagStunde(wochentag : Wochentag, stunde : number) : StundenplanZeitraster {
		return this.zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde);
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
	 * Liefert TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 * @param beginn1  Der Anfang (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param ende1    Das Ende (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iBeginn2 Der Anfang (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iEnde2   Das Ende (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 *
	 * @return TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 * @deprecated use {@link #zeitrasterGetSchneidenSich(int, int, Integer, Integer)}
	 */
	public testIntervalleSchneidenSich(beginn1 : number, ende1 : number, iBeginn2 : number | null, iEnde2 : number | null) : boolean {
		return this.zeitrasterGetSchneidenSich(beginn1, ende1, iBeginn2, iEnde2);
	}

	/**
	 * Fügt dem Stundenplan eine neues {@link StundenplanZeitraster} hinzu.
	 *
	 * @param zeitraster Das StundenplanZeitraster, das hinzugefügt werden soll.
	 * @deprecated use {@link #zeitrasterAdd(StundenplanZeitraster)}
	 */
	public addZeitraster(zeitraster : StundenplanZeitraster) : void {
		this.zeitrasterAdd(zeitraster);
	}

	/**
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster Die ID des {@link StundenplanZeitraster}-Objekts.
	 * @deprecated use {@link #zeitrasterRemove(long)}
	 */
	public removeZeitraster(idZeitraster : number) : void {
		this.zeitrasterRemove(idZeitraster);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanZeitraster}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param zeitraster Das neue {@link StundenplanZeitraster}-Objekt, welches das alte Objekt ersetzt.
	 * @deprecated use {@link #zeitrasterPatch(StundenplanZeitraster)}
	 */
	public patchZeitraster(zeitraster : StundenplanZeitraster) : void {
		this.zeitrasterPatch(zeitraster);
	}

	/**
	 * Liefert das Fach mit der übergebenen ID.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return  das Fach mit der übergebenen ID.
	 */
	public fachGetByIdOrException(idFach : number) : StundenplanFach {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_fachID_zu_fach, idFach);
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 *
	 * @return eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 */
	public pausenaufsichtGetMenge() : List<StundenplanPausenaufsicht> {
		return this._list_pausenaufsichten;
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanPausenaufsicht} hinzu und sortiert die Liste der Pausenaufsichten.
	 *
	 * @param pausenaufsicht Die StundenplanPausenaufsicht, die hinzugefügt werden soll.
	 */
	public pausenaufsichtAdd(pausenaufsicht : StundenplanPausenaufsicht) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_pausenaufsichtID_zu_pausenaufsicht, pausenaufsicht.id, pausenaufsicht);
		this._list_pausenaufsichten.add(pausenaufsicht);
		this._list_pausenaufsichten.sort(this._compPausenaufsicht);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @param idPausenaufsicht Die ID des {@link StundenplanPausenaufsicht}-Objekts.
	 */
	public pausenaufsichtRemove(idPausenaufsicht : number) : void {
		const pa : StundenplanPausenaufsicht = DeveloperNotificationException.ifNull("_map_pausenaufsichtID_zu_pausenaufsicht.get(" + idPausenaufsicht + ")", this._map_pausenaufsichtID_zu_pausenaufsicht.get(idPausenaufsicht));
		this._map_pausenaufsichtID_zu_pausenaufsicht.remove(idPausenaufsicht);
		this._list_pausenaufsichten.remove(pa);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @param idPausenaufsicht Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 */
	public pausenaufsichtGet(idPausenaufsicht : number) : StundenplanPausenaufsicht {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_pausenaufsichtID_zu_pausenaufsicht, idPausenaufsicht);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanPausenaufsicht}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param pausenaufsicht Das neue {@link StundenplanPausenaufsicht}-Objekt, welches das alte Objekt ersetzt.
	 */
	public pausenaufsichtPatch(pausenaufsicht : StundenplanPausenaufsicht) : void {
		this.pausenaufsichtRemove(pausenaufsicht.id);
		this.pausenaufsichtAdd(pausenaufsicht);
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
	 * Liefert die Datenbank-ID des Stundenplans.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die Datenbank-ID des Stundenplans.
	 */
	public stundenplanGetID() : number {
		return this.stundenplanID;
	}

	/**
	 * Liefert TRUE, falls es {@link StundenplanUnterricht} gibt, der einen Wochentyp > 0 hat.
	 * <br>Laufzeit: O(1)
	 *
	 * @deprecated Sollte nicht benutzt werden. Stattdessen sollte man pro Zeitraster (Zelle) arbeiten.
	 * @return TRUE, falls es {@link StundenplanUnterricht} gibt, der einen Wochentyp > 0 hat.
	 */
	public unterrichtHatMultiWochen() : boolean {
		return this._unterrichtHatMultiWochen;
	}

	/**
	 * Liefert die Anzahl an Wochentypen.
	 * <br>Laufzeit: O(1)
	 *
	 * @deprecated Sollte nicht benutzt werden.  Stattdessen sollte man pro Zeitraster (Zelle) arbeiten.
	 * @return die Anzahl an Wochentypen.
	 */
	public unterrichtGetAnzahlWochentypen() : number {
		return this.unterrichtHatMultiWochen() ? this.stundenplanWochenTypModell : 1;
	}

	/**
	 * Liefert das {@link StundenplanUnterricht}-Objekt zur übergebenen ID.
	 * <br>Laufzeit: O(1)
	 * <br>Hinweis: Unnötige Methode, denn man bekommt ddie Objekte über Zeitraster abfragen.
	 *
	 * @param idUnterricht  Die Datenbank-ID des Unterrichts.
	 *
	 * @return das {@link StundenplanUnterricht}-Objekt zur übergebenen ID.
	 */
	public unterrichtGetByIdOrException(idUnterricht : number) : StundenplanUnterricht {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idUnterricht_zu_unterricht, idUnterricht);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergebenen Zeitraster liegen.
	 * <br>Hinweis: Diese Methode sollte der Client nicht benutzen, denn dann müsste er den Wochentyp parsen.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergebenen Zeitraster liegen.
	 */
	public unterrichtGetMengeByZeitrasterIdOrEmptyList(idZeitraster : number) : List<StundenplanUnterricht> {
		return MapUtils.getOrCreateArrayList(this._map_idZeitraster_zu_unterrichte, idZeitraster);
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
		return Map2DUtils.getOrCreateArrayList(this._map2d_idZeitraster_wochentyp_zu_unterrichte, idZeitraster, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekten, die im übergebenen Zeitraster und Wochentyp liegen.
	 * Falls der Parameter inklWoche0 TRUE ist, wird Unterricht des Wochentyps 0 hinzugefügt.
	 * <br>Hinweis: Diese Methode sollte der Client nicht benutzen. Der Client sollte sich nach dem Wochentyp des Zeitrasters informieren
	 *              und dann entsprechend {@link #unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(long, int)} aufrufen.
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
	 * Liefert die Wochentypen ohne Typ 0 zurück, außer es gibt nur Typ 0.
	 * <br>Laufzeit: O(1)
	 *
	 * @deprecated Sollte nicht benutzt werden.  Stattdessen sollte man pro Zeitraster (Zelle) arbeiten.
	 * @return die Wochentypen ohne Typ 0 zurück, außer es gibt nur Typ 0.
	 */
	public unterrichtGetWochentypenMenge() : List<number> {
		const list : List<number> | null = new ArrayList();
		if (this._unterrichtHatMultiWochen) {
			for (let i : number = 1; i <= this.stundenplanWochenTypModell; i++)
				list.add(i);
		} else {
			list.add(0);
		}
		return list;
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

	private zeitrasterAddOhneUpdate(zeitraster : StundenplanZeitraster) : void {
		DeveloperNotificationException.ifInvalidID("zeit.id", zeitraster.id);
		Wochentag.fromIDorException(zeitraster.wochentag);
		DeveloperNotificationException.ifTrue("(zeit.unterrichtstunde < 0) || (zeit.unterrichtstunde > 29)", (zeitraster.unterrichtstunde < 0) || (zeitraster.unterrichtstunde > 29));
		if ((zeitraster.stundenbeginn !== null) && (zeitraster.stundenende !== null)) {
			const beginn : number = zeitraster.stundenbeginn.valueOf();
			const ende : number = zeitraster.stundenende.valueOf();
			DeveloperNotificationException.ifTrue("beginn >= ende", beginn >= ende);
		}
		DeveloperNotificationException.ifMapPutOverwrites(this._map_zeitrasterID_zu_zeitraster, zeitraster.id, zeitraster);
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
		return DeveloperNotificationException.ifMapGetIsNull(this._map_zeitrasterID_zu_zeitraster, idZeitraster);
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
	 * Liefert die Liste aller {@link StundenplanZeitraster}-Objekte des Wochentags.
	 *
	 * @param wochentag  Die ENUM-ID des {@link Wochentag}.
	 *
	 * @deprecated Sollte nicht benutzt werden, stattdessen {@link #zeitrasterGetByWochentagAndStundeOrException(int, int)}
	 * @return die Liste aller {@link StundenplanZeitraster}-Objekte des Wochentags.
	 */
	public zeitrasterGetMengeByWochentag(wochentag : number) : List<StundenplanZeitraster> {
		return MapUtils.getOrCreateArrayList(this._map_wochentag_zu_zeitrastermenge, wochentag);
	}

	/**
	 * Liefert die Liste aller {@link StundenplanZeitraster}-Objekte der Unterrichtsstunde.
	 *
	 * @param stunde  Die Unterrichtsstunde.
	 *
	 * @deprecated Sollte nicht benutzt werden, stattdessen {@link #zeitrasterGetByWochentagAndStundeOrException(int, int)}
	 * @return die Liste aller {@link StundenplanZeitraster}-Objekte der Unterrichtsstunde.
	 */
	public zeitrasterGetMengeByStunde(stunde : number) : List<StundenplanZeitraster> {
		return MapUtils.getOrCreateArrayList(this._map_stunde_zu_zeitrastermenge, stunde);
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
	 * Liefert TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 */
	public zeitrasterHatUnterrichtMitWochentyp0(idZeitraster : number) : boolean {
		return !Map2DUtils.getOrCreateArrayList(this._map2d_idZeitraster_wochentyp_zu_unterrichte, idZeitraster, 0).isEmpty();
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
			if (!Map2DUtils.getOrCreateArrayList(this._map2d_idZeitraster_wochentyp_zu_unterrichte, idZeitraster, wochentyp).isEmpty())
				return true;
		return false;
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
	 *
	 * @param zeitraster  Das neue {@link StundenplanZeitraster}-Objekt, welches das alte Objekt ersetzt.
	 */
	public zeitrasterPatch(zeitraster : StundenplanZeitraster) : void {
		this.zeitrasterRemoveOhneUpdate(zeitraster.id);
		this.zeitrasterAddOhneUpdate(zeitraster);
		this.zeitrasterUpdate();
	}

	private zeitrasterRemoveOhneUpdate(idZeitraster : number) : void {
		const z : StundenplanZeitraster = DeveloperNotificationException.ifNull("_map_zeitrasterID_zu_zeitraster.get(" + idZeitraster + ")", this._map_zeitrasterID_zu_zeitraster.get(idZeitraster));
		DeveloperNotificationException.ifMapRemoveFailes(this._map_zeitrasterID_zu_zeitraster, idZeitraster);
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
		if (this._list_zeitraster.isEmpty()) {
			this._zeitrasterWochentagMin = Wochentag.MONTAG.id;
			this._zeitrasterWochentagMax = Wochentag.MONTAG.id;
			this._zeitrasterStundeMin = 1;
			this._zeitrasterStundeMax = 1;
		} else {
			this._list_zeitraster.sort(StundenplanManager._compZeitraster);
			const first : StundenplanZeitraster = DeveloperNotificationException.ifListGetFirstFailes("_list_zeitraster", this._list_zeitraster);
			let minWT : number = first.wochentag;
			let maxWT : number = first.wochentag;
			let minSt : number = first.unterrichtstunde;
			let maxSt : number = first.unterrichtstunde;
			this._unterrichtHatMultiWochen = false;
			for (const z of this._list_zeitraster) {
				minWT = Math.min(minWT, z.wochentag);
				maxWT = Math.max(maxWT, z.wochentag);
				minSt = Math.min(minSt, z.unterrichtstunde);
				maxSt = Math.max(maxSt, z.unterrichtstunde);
			}
			this._zeitrasterWochentagMin = minWT;
			this._zeitrasterWochentagMax = maxWT;
			this._zeitrasterStundeMin = minSt;
			this._zeitrasterStundeMax = maxSt;
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplan.StundenplanManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanManager(obj : unknown) : StundenplanManager {
	return obj as StundenplanManager;
}
