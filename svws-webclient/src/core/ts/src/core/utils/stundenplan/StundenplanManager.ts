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
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { StundenplanPausenzeit } from '../../../core/data/stundenplan/StundenplanPausenzeit';
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

	private readonly _map_fachID_zu_fach : HashMap<number, StundenplanFach> = new HashMap();

	private readonly _map_klasseID_zu_klasse : HashMap<number, StundenplanKlasse> = new HashMap();

	private readonly _map_jahrgangID_zu_jahrgang : HashMap<number, StundenplanJahrgang> = new HashMap();

	private readonly _map_lehrerID_zu_lehrer : HashMap<number, StundenplanLehrer> = new HashMap();

	private readonly _map_schuelerID_zu_schueler : HashMap<number, StundenplanSchueler> = new HashMap();

	private readonly _map_kursID_zu_kurs : HashMap<number, StundenplanKurs> = new HashMap();

	private readonly _map_zeitrasterID_zu_zeitraster : HashMap<number, StundenplanZeitraster> = new HashMap();

	private readonly _map_wochentag_stunde_zu_zeitraster : HashMap2D<number, number, StundenplanZeitraster> = new HashMap2D();

	private readonly _map_pausenzeitID_zu_pausenzeit : HashMap<number, StundenplanPausenzeit> = new HashMap();

	private readonly _map_raumID_zu_raum : HashMap<number, StundenplanRaum> = new HashMap();

	private readonly _map_schieneID_zu_schiene : HashMap<number, StundenplanSchiene> = new HashMap();

	private readonly _map_aufsichtsbereichID_zu_aufsichtsbereich : HashMap<number, StundenplanAufsichtsbereich> = new HashMap();

	private readonly _map_kwzID_zu_kwz : HashMap<number, StundenplanKalenderwochenzuordnung> = new HashMap();

	private readonly _map_jahr_kw_zu_kwz : HashMap2D<number, number, StundenplanKalenderwochenzuordnung> = new HashMap2D();

	private readonly _map_kursID_zu_unterrichte : HashMap<number, List<StundenplanUnterricht>> = new HashMap();

	private readonly _map_pausenaufsichtID_zu_pausenaufsicht : HashMap<number, StundenplanPausenaufsicht> = new HashMap();

	private readonly _list_pausenaufsichten : List<StundenplanPausenaufsicht> = new ArrayList();

	private readonly _list_raeume : List<StundenplanRaum> = new ArrayList();

	private readonly _list_zeitraster : List<StundenplanZeitraster> = new ArrayList();

	private readonly _list_pausenzeiten : List<StundenplanPausenzeit> = new ArrayList();

	private readonly _list_aufsichtsbereiche : List<StundenplanAufsichtsbereich> = new ArrayList();

	private readonly _list_kalenderwochenzuordnungen : List<StundenplanKalenderwochenzuordnung> = new ArrayList();

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
		this.initKalenderwochenZuordnungen(listKWZ);
		this.initFaecher(listFach);
		this.initJahrgaenge(listJahrgang);
		this.initZeitraster(listZeitraster);
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

	private initKalenderwochenZuordnungen(listKWZ : List<StundenplanKalenderwochenzuordnung>) : void {
		this._map_kwzID_zu_kwz.clear();
		this._map_jahr_kw_zu_kwz.clear();
		this._list_kalenderwochenzuordnungen.clear();
		for (const kwz of listKWZ) {
			DeveloperNotificationException.ifInvalidID("kw.id", kwz.id);
			DeveloperNotificationException.ifTrue("(kwz.jahr < 2000) || (kwz.jahr > 3000)", (kwz.jahr < 2000) || (kwz.jahr > 3000));
			DeveloperNotificationException.ifTrue("(kwz.kw < 1) || (kwz.kw > 53)", (kwz.kw < 1) || (kwz.kw > 53));
			DeveloperNotificationException.ifTrue("kwz.wochentyp > stundenplanWochenTypModell", kwz.wochentyp > this.stundenplanWochenTypModell);
			DeveloperNotificationException.ifTrue("kwz.wochentyp <= 0", kwz.wochentyp <= 0);
			DeveloperNotificationException.ifMap2DPutOverwrites(this._map_jahr_kw_zu_kwz, kwz.jahr, kwz.kw, kwz);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_kwzID_zu_kwz, kwz.id, kwz);
			this._list_kalenderwochenzuordnungen.add(kwz);
		}
		this._list_kalenderwochenzuordnungen.sort(StundenplanManager._compKWZ);
	}

	private initFaecher(listFach : List<StundenplanFach>) : void {
		this._map_fachID_zu_fach.clear();
		const setFachKuerzel : HashSet<string> = new HashSet();
		for (const fach of listFach) {
			DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
			DeveloperNotificationException.ifStringIsBlank("fach.bezeichnung", fach.bezeichnung);
			DeveloperNotificationException.ifStringIsBlank("fach.kuerzel", fach.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setFachKuerzel", setFachKuerzel, fach.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_fachID_zu_fach, fach.id, fach);
		}
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
		for (const schueler of listSchueler) {
			DeveloperNotificationException.ifInvalidID("schueler.id", schueler.id);
			DeveloperNotificationException.ifStringIsBlank("schueler.nachname", schueler.nachname);
			DeveloperNotificationException.ifStringIsBlank("schueler.vorname", schueler.vorname);
			DeveloperNotificationException.ifMapNotContains("_map_klasseID_zu_klasse", this._map_klasseID_zu_klasse, schueler.idKlasse);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_zu_schueler, schueler.id, schueler);
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

	private initZeitraster(listZeitraster : List<StundenplanZeitraster>) : void {
		this._map_zeitrasterID_zu_zeitraster.clear();
		this._map_wochentag_stunde_zu_zeitraster.clear();
		this._list_zeitraster.clear();
		for (const zeit of listZeitraster) {
			DeveloperNotificationException.ifInvalidID("zeit.id", zeit.id);
			Wochentag.fromIDorException(zeit.wochentag);
			DeveloperNotificationException.ifTrue("(zeit.unterrichtstunde < 0) || (zeit.unterrichtstunde > 29)", (zeit.unterrichtstunde < 0) || (zeit.unterrichtstunde > 29));
			if ((zeit.stundenbeginn !== null) && (zeit.stundenende !== null)) {
				const beginn : number = zeit.stundenbeginn.valueOf();
				const ende : number = zeit.stundenende.valueOf();
				DeveloperNotificationException.ifTrue("beginn >= ende", beginn >= ende);
			}
			DeveloperNotificationException.ifMapPutOverwrites(this._map_zeitrasterID_zu_zeitraster, zeit.id, zeit);
			DeveloperNotificationException.ifMap2DPutOverwrites(this._map_wochentag_stunde_zu_zeitraster, zeit.wochentag, zeit.unterrichtstunde, zeit);
			this._list_zeitraster.add(zeit);
		}
		this._list_zeitraster.sort(StundenplanManager._compZeitraster);
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
		}
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
	 * Liefert die ID des Stundenplans.
	 *
	 * @return die ID des Stundenplans.
	 */
	public getID() : number {
		return this.stundenplanID;
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
			const z : StundenplanZeitraster = this.getZeitraster(u.idZeitraster);
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
	 * Liefert das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
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
	 */
	public getZeitrasterWochentagMin() : Wochentag {
		if (this._list_zeitraster.isEmpty())
			return Wochentag.MONTAG;
		let min : number = DeveloperNotificationException.ifListGetFirstFailes("_list_zeitraster", this._list_zeitraster).wochentag;
		for (const z of this._list_zeitraster)
			min = Math.min(min, z.wochentag);
		return Wochentag.fromIDorException(min);
	}

	/**
	 * Liefert den größten {@link Wochentag}, oder den Montag falls es keine Zeitraster gibt.
	 *
	 * @return den größten {@link Wochentag}, oder den Montag falls es keine Zeitraster gibt.
	 */
	public getZeitrasterWochentagMax() : Wochentag {
		if (this._list_zeitraster.isEmpty())
			return Wochentag.MONTAG;
		let max : number = DeveloperNotificationException.ifListGetFirstFailes("_list_zeitraster", this._list_zeitraster).wochentag;
		for (const z of this._list_zeitraster)
			max = Math.max(max, z.wochentag);
		return Wochentag.fromIDorException(max);
	}

	/**
	 * Liefert TRUE, falls zu (wochentag, stunde) ein zugehöriges {@link StundenplanZeitraster}-Objekt existiert.
	 *
	 * @param wochentag Der {@link Wochentag} des Zeitrasters.
	 * @param stunde    Die Unterrichtsstunde Zeitrasters.
	 *
	 * @return TRUE, falls zu (wochentag, stunde) ein zugehöriges {@link StundenplanZeitraster}-Objekt existiert.
	 */
	public testZeitrasterByWochentagStunde(wochentag : Wochentag, stunde : number) : boolean {
		return this._map_wochentag_stunde_zu_zeitraster.contains(wochentag.id, stunde);
	}

	/**
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param wochentag Der {@link Wochentag} des Zeitrasters.
	 * @param stunde    Die Unterrichtsstunde Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 */
	public getZeitrasterByWochentagStunde(wochentag : Wochentag, stunde : number) : StundenplanZeitraster {
		return this._map_wochentag_stunde_zu_zeitraster.getNonNullOrException(wochentag.id, stunde);
	}

	/**
	 * Liefert das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag.
	 *
	 * @param zeitraster Das aktuelle {@link StundenplanZeitraster}-Objekt.
	 *
	 * @return das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag.
	 */
	public getZeitrasterNext(zeitraster : StundenplanZeitraster) : StundenplanZeitraster {
		return this._map_wochentag_stunde_zu_zeitraster.getNonNullOrException(zeitraster.wochentag, zeitraster.unterrichtstunde + 1);
	}

	/**
	 * Liefert TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 * @param beginn1  Der Anfang (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param ende1    Das Ende (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iBeginn2 Der Anfang (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iEnde2   Das Ende (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 *
	 * @return TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 */
	public testIntervalleSchneidenSich(beginn1 : number, ende1 : number, iBeginn2 : number | null, iEnde2 : number | null) : boolean {
		const beginn2 : number = DeveloperNotificationException.ifNull("zeitraster.stundenbeginn ist NULL!", iBeginn2).valueOf();
		const ende2 : number = DeveloperNotificationException.ifNull("zeitraster.stundenende ist NULL!", iEnde2).valueOf();
		DeveloperNotificationException.ifTrue("beginn1 < 0", beginn1 < 0);
		DeveloperNotificationException.ifTrue("beginn2 < 0", beginn2 < 0);
		DeveloperNotificationException.ifTrue("beginn1 > ende1", beginn1 > ende1);
		DeveloperNotificationException.ifTrue("beginn2 > ende2", beginn2 > ende2);
		return !((ende1 < beginn2) || (ende2 < beginn1));
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
	 * Fügt dem Stundenplan eine neues {@link StundenplanZeitraster} hinzu.
	 *
	 * @param zeitraster Das StundenplanZeitraster, das hinzugefügt werden soll.
	 */
	public addZeitraster(zeitraster : StundenplanZeitraster) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_zeitrasterID_zu_zeitraster, zeitraster.id, zeitraster);
		DeveloperNotificationException.ifMap2DPutOverwrites(this._map_wochentag_stunde_zu_zeitraster, zeitraster.wochentag, zeitraster.unterrichtstunde, zeitraster);
		this._list_zeitraster.add(zeitraster);
		this._list_zeitraster.sort(StundenplanManager._compZeitraster);
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
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster Die ID des {@link StundenplanZeitraster}-Objekts.
	 */
	public removeZeitraster(idZeitraster : number) : void {
		const zr : StundenplanZeitraster = DeveloperNotificationException.ifNull("_map_zeitrasterID_zu_zeitraster.get(" + idZeitraster + ")", this._map_zeitrasterID_zu_zeitraster.get(idZeitraster));
		this._map_wochentag_stunde_zu_zeitraster.removeOrException(zr.wochentag, zr.unterrichtstunde);
		this._map_zeitrasterID_zu_zeitraster.remove(idZeitraster);
		this._list_zeitraster.remove(zr);
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
	 * Entfernt anhand der ID das alte {@link StundenplanZeitraster}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param zeitraster Das neue {@link StundenplanZeitraster}-Objekt, welches das alte Objekt ersetzt.
	 */
	public patchZeitraster(zeitraster : StundenplanZeitraster) : void {
		this.removeZeitraster(zeitraster.id);
		this.addZeitraster(zeitraster);
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
	 * Liefert das Fach mit der übergebenen ID.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return  das Fach mit der übergebenen ID.
	 */
	public fachGet(idFach : number) : StundenplanFach {
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
	 * Liefert die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 *
	 * @return die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public stundenplanGetWochenTypModell() : number {
		return this.stundenplanWochenTypModell;
	}

	/**
	 * Liefert die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 *
	 * @return die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetStundeMin() : number {
		if (this._list_zeitraster.isEmpty())
			return 1;
		let min : number = DeveloperNotificationException.ifListGetFirstFailes("_list_zeitraster", this._list_zeitraster).unterrichtstunde;
		for (const z of this._list_zeitraster)
			min = Math.min(min, z.unterrichtstunde);
		return min;
	}

	/**
	 * Liefert die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 *
	 * @return die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetStundeMax() : number {
		if (this._list_zeitraster.isEmpty())
			return 1;
		let max : number = DeveloperNotificationException.ifListGetFirstFailes("_list_zeitraster", this._list_zeitraster).unterrichtstunde;
		for (const z of this._list_zeitraster)
			max = Math.max(max, z.unterrichtstunde);
		return max;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplan.StundenplanManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanManager(obj : unknown) : StundenplanManager {
	return obj as StundenplanManager;
}
