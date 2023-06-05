import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { StundenplanUnterrichtsverteilung } from '../../../core/data/stundenplan/StundenplanUnterrichtsverteilung';
import { HashMap } from '../../../java/util/HashMap';
import { StundenplanKlasse } from '../../../core/data/stundenplan/StundenplanKlasse';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanKurs } from '../../../core/data/stundenplan/StundenplanKurs';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../../java/lang/JavaString';
import { StundenplanJahrgang } from '../../../core/data/stundenplan/StundenplanJahrgang';
import { Comparator } from '../../../java/util/Comparator';
import { StundenplanSchueler } from '../../../core/data/stundenplan/StundenplanSchueler';
import { StundenplanLehrer } from '../../../core/data/stundenplan/StundenplanLehrer';
import { StundenplanUnterricht } from '../../../core/data/stundenplan/StundenplanUnterricht';
import { List } from '../../../java/util/List';
import { StundenplanKalenderwochenzuordnung } from '../../../core/data/stundenplan/StundenplanKalenderwochenzuordnung';
import { Stundenplan } from '../../../core/data/stundenplan/Stundenplan';
import { HashSet } from '../../../java/util/HashSet';
import { StundenplanPausenaufsicht } from '../../../core/data/stundenplan/StundenplanPausenaufsicht';
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { StundenplanPausenzeit } from '../../../core/data/stundenplan/StundenplanPausenzeit';
import { StundenplanAufsichtsbereich } from '../../../core/data/stundenplan/StundenplanAufsichtsbereich';
import { StundenplanRaum } from '../../../core/data/stundenplan/StundenplanRaum';
import { StundenplanSchiene } from '../../../core/data/stundenplan/StundenplanSchiene';
import { StundenplanFach } from '../../../core/data/stundenplan/StundenplanFach';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Wochentag } from '../../../core/types/Wochentag';
import { JavaMap } from '../../../java/util/JavaMap';

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
	private readonly _compPausenaufsicht : Comparator<StundenplanPausenaufsicht>;

	/**
	 * Ein Comparator für die Zeitraster.
	 */
	private static readonly _compZeitraster : Comparator<StundenplanZeitraster> = { compare : (a: StundenplanZeitraster, b: StundenplanZeitraster) => {
		if (a.wochentag < b.wochentag)
			return -1;
		if (a.wochentag > b.wochentag)
			return +1;
		const beginnA : number = a.stundenbeginn === null ? -1 : a.stundenbeginn;
		const beginnB : number = b.stundenbeginn === null ? -1 : b.stundenbeginn;
		if (beginnA < beginnB)
			return -1;
		if (beginnA > beginnB)
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

	private readonly _daten : Stundenplan;

	private readonly _datenU : List<StundenplanUnterricht>;

	private readonly _datenUV : StundenplanUnterrichtsverteilung;

	private readonly _datenP : List<StundenplanPausenaufsicht>;

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
		this._datenP = pausenaufsichten;
		if (unterrichtsverteilung === null) {
			this._datenUV = new StundenplanUnterrichtsverteilung();
			this._datenUV.id = daten.id;
		} else {
			this._datenUV = unterrichtsverteilung;
		}
		DeveloperNotificationException.ifTrue("Stundenplan.id != StundenplanUnterrichtsverteilung.id", this._daten.id !== this._datenUV.id);
		DeveloperNotificationException.ifTrue("_daten.wochenTypModell < 0", this._daten.wochenTypModell < 0);
		DeveloperNotificationException.ifTrue("_daten.wochenTypModell == 1", this._daten.wochenTypModell === 1);
		this.initMapKWZuordnung();
		this.initMapFach();
		this.initMapJahrgang();
		this.initMapLehrer();
		this.initMapKlasse();
		this.initMapSchueler();
		this.initMapSchiene();
		this.initMapKurs();
		this.initMapZeitraster();
		this.initMapRaum();
		this.initMapPausenzeit();
		this.initMapAufsicht();
		this.initMapKursZuUnterrichte();
		this.initMapPausenaufsichten();
		this._compPausenaufsicht = { compare : (a: StundenplanPausenaufsicht, b: StundenplanPausenaufsicht) => {
			return JavaLong.compare(a.id, b.id);
		} };
		this.initSortierungen();
	}

	private initMapKWZuordnung() : void {
		this._map_kwzID_zu_kwz.clear();
		this._map_jahr_kw_zu_kwz.clear();
		for (const kwz of this._daten.kalenderwochenZuordnung) {
			DeveloperNotificationException.ifInvalidID("kw.id", kwz.id);
			DeveloperNotificationException.ifTrue("(kwz.jahr < 2000) || (kwz.jahr > 3000)", (kwz.jahr < 2000) || (kwz.jahr > 3000));
			DeveloperNotificationException.ifTrue("(kwz.kw < 1) || (kwz.kw > 53)", (kwz.kw < 1) || (kwz.kw > 53));
			DeveloperNotificationException.ifTrue("kwz.wochentyp > _daten.wochenTypModell", kwz.wochentyp > this._daten.wochenTypModell);
			DeveloperNotificationException.ifTrue("kwz.wochentyp <= 0", kwz.wochentyp <= 0);
			DeveloperNotificationException.ifMap2DPutOverwrites(this._map_jahr_kw_zu_kwz, kwz.jahr, kwz.kw, kwz);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_kwzID_zu_kwz, kwz.id, kwz);
		}
	}

	private initMapFach() : void {
		this._map_fachID_zu_fach.clear();
		const setFachKuerzel : HashSet<string> = new HashSet();
		for (const fach of this._datenUV.faecher) {
			DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
			DeveloperNotificationException.ifStringIsBlank("fach.bezeichnung", fach.bezeichnung);
			DeveloperNotificationException.ifStringIsBlank("fach.kuerzel", fach.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setFachKuerzel", setFachKuerzel, fach.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_fachID_zu_fach, fach.id, fach);
		}
	}

	private initMapJahrgang() : void {
		this._map_jahrgangID_zu_jahrgang.clear();
		const setJahrgangKuerzel : HashSet<string> = new HashSet();
		for (const jahrgang of this._datenUV.jahrgaenge) {
			DeveloperNotificationException.ifInvalidID("jahrgang.id", jahrgang.id);
			DeveloperNotificationException.ifStringIsBlank("jahrgang.bezeichnung", jahrgang.bezeichnung);
			DeveloperNotificationException.ifStringIsBlank("jahrgang.kuerzel", jahrgang.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setJahrgangKuerzel", setJahrgangKuerzel, jahrgang.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_jahrgangID_zu_jahrgang, jahrgang.id, jahrgang);
		}
	}

	private initMapLehrer() : void {
		this._map_lehrerID_zu_lehrer.clear();
		const setLehrerKuerzel : HashSet<string> = new HashSet();
		for (const lehrer of this._datenUV.lehrer) {
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

	private initMapKlasse() : void {
		this._map_klasseID_zu_klasse.clear();
		const setKlasseKuerzel : HashSet<string> = new HashSet();
		for (const klasse of this._datenUV.klassen) {
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

	private initMapSchueler() : void {
		this._map_schuelerID_zu_schueler.clear();
		for (const schueler of this._datenUV.schueler) {
			DeveloperNotificationException.ifInvalidID("schueler.id", schueler.id);
			DeveloperNotificationException.ifStringIsBlank("schueler.nachname", schueler.nachname);
			DeveloperNotificationException.ifStringIsBlank("schueler.vorname", schueler.vorname);
			DeveloperNotificationException.ifMapNotContains("_map_klasseID_zu_klasse", this._map_klasseID_zu_klasse, schueler.idKlasse);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_zu_schueler, schueler.id, schueler);
		}
	}

	private initMapSchiene() : void {
		this._map_schieneID_zu_schiene.clear();
		for (const schiene of this._daten.schienen) {
			DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
			DeveloperNotificationException.ifTrue("schiene.nummer <= 0", schiene.nummer <= 0);
			DeveloperNotificationException.ifStringIsBlank("schiene.bezeichnung", schiene.bezeichnung);
			DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", this._map_jahrgangID_zu_jahrgang, schiene.idJahrgang);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schieneID_zu_schiene, schiene.id, schiene);
		}
	}

	private initMapKurs() : void {
		this._map_kursID_zu_kurs.clear();
		for (const kurs of this._datenUV.kurse) {
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

	private initMapZeitraster() : void {
		this._map_zeitrasterID_zu_zeitraster.clear();
		this._map_wochentag_stunde_zu_zeitraster.clear();
		for (const zeit of this._daten.zeitraster) {
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
		}
	}

	private initMapRaum() : void {
		this._map_raumID_zu_raum.clear();
		const setRaumKuerzel : HashSet<string> = new HashSet();
		for (const raum of this._daten.raeume) {
			DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
			DeveloperNotificationException.ifStringIsBlank("raum.kuerzel", raum.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setRaumKuerzel", setRaumKuerzel, raum.kuerzel);
			DeveloperNotificationException.ifTrue("raum.groesse < 0", raum.groesse < 0);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_raumID_zu_raum, raum.id, raum);
		}
	}

	private initMapPausenzeit() : void {
		this._map_pausenzeitID_zu_pausenzeit.clear();
		for (const pausenzeit of this._daten.pausenzeiten) {
			DeveloperNotificationException.ifInvalidID("pause.id", pausenzeit.id);
			Wochentag.fromIDorException(pausenzeit.wochentag);
			if ((pausenzeit.beginn !== null) && (pausenzeit.ende !== null)) {
				const beginn : number = pausenzeit.beginn.valueOf();
				const ende : number = pausenzeit.ende.valueOf();
				DeveloperNotificationException.ifTrue("beginn >= ende", beginn >= ende);
			}
			DeveloperNotificationException.ifMapPutOverwrites(this._map_pausenzeitID_zu_pausenzeit, pausenzeit.id, pausenzeit);
		}
	}

	private initMapAufsicht() : void {
		this._map_aufsichtsbereichID_zu_aufsichtsbereich.clear();
		const setAufsichtKuerzel : HashSet<string> = new HashSet();
		for (const aufsicht of this._daten.aufsichtsbereiche) {
			DeveloperNotificationException.ifInvalidID("aufsicht.id", aufsicht.id);
			DeveloperNotificationException.ifStringIsBlank("aufsicht.kuerzel", aufsicht.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setAufsichtKuerzel", setAufsichtKuerzel, aufsicht.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_aufsichtsbereichID_zu_aufsichtsbereich, aufsicht.id, aufsicht);
		}
	}

	private initMapKursZuUnterrichte() : void {
		this._map_kursID_zu_unterrichte.clear();
		for (const idKurs of this._map_kursID_zu_kurs.keySet())
			this._map_kursID_zu_unterrichte.put(idKurs, new ArrayList());
		for (const u of this._datenU) {
			DeveloperNotificationException.ifInvalidID("u.id", u.id);
			DeveloperNotificationException.ifMapNotContains("_map_zeitrasterID_zu_zeitraster", this._map_zeitrasterID_zu_zeitraster, u.idZeitraster);
			DeveloperNotificationException.ifTrue("u.wochentyp > _daten.wochenTypModell", u.wochentyp > this._daten.wochenTypModell);
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

	private initMapPausenaufsichten() : void {
		this._map_pausenaufsichtID_zu_pausenaufsicht.clear();
		for (const pa of this._datenP) {
			DeveloperNotificationException.ifInvalidID("aufsicht.id", pa.id);
			DeveloperNotificationException.ifMapNotContains("_map_pausenzeitID_zu_pausenzeit", this._map_pausenzeitID_zu_pausenzeit, pa.idPausenzeit);
			DeveloperNotificationException.ifMapNotContains("_map_lehrerID_zu_lehrer", this._map_lehrerID_zu_lehrer, pa.idLehrer);
			DeveloperNotificationException.ifTrue("(pa.wochentyp > 0) && (pa.wochentyp > _daten.wochenTypModell)", (pa.wochentyp > 0) && (pa.wochentyp > this._daten.wochenTypModell));
			const setBereicheDieserAufsicht : HashSet<number> = new HashSet();
			for (const idAufsichtsbereich of pa.bereiche) {
				DeveloperNotificationException.ifMapNotContains("_map_aufsichtsbereichID_zu_aufsichtsbereich", this._map_aufsichtsbereichID_zu_aufsichtsbereich, idAufsichtsbereich);
				DeveloperNotificationException.ifSetAddsDuplicate("setBereicheDieserAufsicht", setBereicheDieserAufsicht, idAufsichtsbereich);
			}
			DeveloperNotificationException.ifMapPutOverwrites(this._map_pausenaufsichtID_zu_pausenaufsicht, pa.id, pa);
		}
	}

	private initSortierungen() : void {
		this._daten.raeume.sort(StundenplanManager._compRaum);
		this._daten.pausenzeiten.sort(StundenplanManager._compPausenzeit);
		this._daten.aufsichtsbereiche.sort(StundenplanManager._compAufsichtsbereich);
		this._daten.kalenderwochenZuordnung.sort(StundenplanManager._compKWZ);
		this._datenP.sort(this._compPausenaufsicht);
		this._daten.zeitraster.sort(StundenplanManager._compZeitraster);
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

	/**
	 * Filtert aus der Liste der Kurs-IDs diejenigen heraus,
	 * deren Unterricht zu (Wochentyp / Wochentag / Unterrichtsstunde) passt.
	 *
	 * @param kursIDs          Die Liste aller Kurs-IDs.
	 * @param wochentyp        Der Typ der Woche (beispielsweise bei AB-Wochen).
	 * @param wochentag        Der gewünschte {@link Wochentag}.
	 * @param unterrichtstunde Die gewünschte Unterrichtsstunde.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public getKurseGefiltert(kursIDs : List<number>, wochentyp : number, wochentag : Wochentag, unterrichtstunde : number) : List<number> {
		const result : ArrayList<number> = new ArrayList();
		for (const kursID of kursIDs)
			if (this.testKursHatUnterrichtAm(kursID!, wochentyp, wochentag, unterrichtstunde))
				result.add(kursID);
		return result;
	}

	/**
	 * Liefert TRUE, falls der übergebene Kurs am (Wochentyp / Wochentag / Unterrichtsstunde)  hat.
	 *
	 * @param kursID           Die ID des Kurses.
	 * @param wochentyp        Der Typ der Woche (beispielsweise bei AB-Wochen).
	 * @param wochentag        Der gewünschte {@link Wochentag}.
	 * @param unterrichtstunde Die gewünschte Unterrichtsstunde.
	 *
	 * @return TRUE, falls der übergebene Kurs am (wochentyp / wochentag / Unterrichtsstunde)  hat.
	 */
	public testKursHatUnterrichtAm(kursID : number, wochentyp : number, wochentag : Wochentag, unterrichtstunde : number) : boolean {
		for (const u of this.getUnterrichtDesKursesByWochentyp(kursID, wochentyp)) {
			const z : StundenplanZeitraster = this.getZeitraster(u.idZeitraster);
			if ((z.wochentag === wochentag.id) && (z.unterrichtstunde === unterrichtstunde))
				return true;
		}
		return false;
	}

	/**
	 * Liefert ein Map der Aufsichtsbereiche {@link StundenplanAufsichtsbereich} für den aktuell ausgewählten Stundenplan.
	 *
	 * @return ein Map der Aufsichtsbereiche {@link StundenplanAufsichtsbereich}
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
		return this._daten.raeume;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 */
	public getListPausenzeit() : List<StundenplanPausenzeit> {
		return this._daten.pausenzeiten;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public getListAufsichtbereich() : List<StundenplanAufsichtsbereich> {
		return this._daten.aufsichtsbereiche;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public getListKalenderwochenzuordnung() : List<StundenplanKalenderwochenzuordnung> {
		return this._daten.kalenderwochenZuordnung;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 */
	public getListPausenaufsicht() : List<StundenplanPausenaufsicht> {
		return this._datenP;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanZeitraster}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte.
	 */
	public getListZeitraster() : List<StundenplanZeitraster> {
		return this._daten.zeitraster;
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 *
	 * @param raumID Die ID des angefragten-Objektes.
	 *
	 * @return das zur raumID zugehörige {@link StundenplanRaum}-Objekt.
	 */
	public getRaum(raumID : number) : StundenplanRaum {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_raumID_zu_raum, raumID);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param pausenzeitID Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 */
	public getPausenzeit(pausenzeitID : number) : StundenplanPausenzeit {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_pausenzeitID_zu_pausenzeit, pausenzeitID);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 *
	 * @param aufsichtsbereichID Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 */
	public getAufsichtsbereich(aufsichtsbereichID : number) : StundenplanAufsichtsbereich {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_aufsichtsbereichID_zu_aufsichtsbereich, aufsichtsbereichID);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @param kwzID Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 */
	public getKalenderwochenzuordnung(kwzID : number) : StundenplanKalenderwochenzuordnung {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_kwzID_zu_kwz, kwzID);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @param pausenaufsichtID Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 */
	public getPausenaufsicht(pausenaufsichtID : number) : StundenplanPausenaufsicht {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_pausenaufsichtID_zu_pausenaufsicht, pausenaufsichtID);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param zeitrasterID Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 */
	public getZeitraster(zeitrasterID : number) : StundenplanZeitraster {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_zeitrasterID_zu_zeitraster, zeitrasterID);
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
	 * Liefert die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Zeit-Intervall berühren. <br>
	 *
	 * @param wochentag          Der {@link Wochentag} des Zeit-Intervalls.
	 * @param beginn             Der Beginn des Zeit-Intervalls.
	 * @param minutenVerstrichen Daraus ergibt sich das Ende des Zeit-Intervalls.
	 *
	 * @return die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Intervall berührt.
	 */
	public getZeitrasterByWochentagStartVerstrichen(wochentag : Wochentag, beginn : number, minutenVerstrichen : number) : List<StundenplanZeitraster> {
		const ende : number = beginn + minutenVerstrichen;
		const result : ArrayList<StundenplanZeitraster> = new ArrayList();
		for (const zeitraster of this._daten.zeitraster) {
			const beginn2 : number = DeveloperNotificationException.ifNull("zeitraster.stundenbeginn ist NULL!", zeitraster.stundenbeginn).valueOf();
			const ende2 : number = DeveloperNotificationException.ifNull("zeitraster.stundenende ist NULL!", zeitraster.stundenende).valueOf();
			if (this.testIntervalleSchneidenSich(beginn, ende, beginn2, ende2))
				result.add(zeitraster);
		}
		return result;
	}

	/**
	 * Liefert TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 * @param beginn1  Der Anfang (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param ende1    Das Ende (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param beginn2  Der Anfang (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 * @param ende2    Das Ende (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 *
	 * @return TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 */
	public testIntervalleSchneidenSich(beginn1 : number, ende1 : number, beginn2 : number, ende2 : number) : boolean {
		DeveloperNotificationException.ifTrue("beginn1 < 0", beginn1 < 0);
		DeveloperNotificationException.ifTrue("beginn2 < 0", beginn2 < 0);
		DeveloperNotificationException.ifTrue("beginn1 >= ende1", beginn1 >= ende1);
		DeveloperNotificationException.ifTrue("beginn2 >= ende2", beginn2 >= ende2);
		return !((ende1 < beginn2) || (ende2 < beginn1));
	}

	/**
	 * Fügt dem Stundenplan einen neuen {@link StundenplanRaum} hinzu.
	 *
	 * @param raum Der Raum, der hinzugefügt werden soll.
	 */
	public addRaum(raum : StundenplanRaum) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_raumID_zu_raum, raum.id, raum);
		this._daten.raeume.add(raum);
		this._daten.raeume.sort(StundenplanManager._compRaum);
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanPausenzeit} hinzu.
	 *
	 * @param pausenzeit Die Pausenzeit, die hinzugefügt werden soll.
	 */
	public addPausenzeit(pausenzeit : StundenplanPausenzeit) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_pausenzeitID_zu_pausenzeit, pausenzeit.id, pausenzeit);
		this._daten.pausenzeiten.add(pausenzeit);
		this._daten.pausenzeiten.sort(StundenplanManager._compPausenzeit);
	}

	/**
	 * Fügt dem Stundenplan einen neuen {@link StundenplanAufsichtsbereich} hinzu.
	 *
	 * @param aufsichtsbereich Der Aufsichtsbereich, der hinzugefügt werden soll.
	 */
	public addAufsichtsbereich(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_aufsichtsbereichID_zu_aufsichtsbereich, aufsichtsbereich.id, aufsichtsbereich);
		this._daten.aufsichtsbereiche.add(aufsichtsbereich);
		this._daten.aufsichtsbereiche.sort(StundenplanManager._compAufsichtsbereich);
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanKalenderwochenzuordnung} hinzu.
	 *
	 * @param kwz Die Kalenderwochenzuordnung, die hinzugefügt werden soll.
	 */
	public addKalenderwochenzuordnung(kwz : StundenplanKalenderwochenzuordnung) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_kwzID_zu_kwz, kwz.id, kwz);
		this._daten.kalenderwochenZuordnung.add(kwz);
		this._daten.kalenderwochenZuordnung.sort(StundenplanManager._compKWZ);
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanPausenaufsicht} hinzu.
	 *
	 * @param pausenaufsicht Die StundenplanPausenaufsicht, die hinzugefügt werden soll.
	 */
	public addPausenaufsicht(pausenaufsicht : StundenplanPausenaufsicht) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_pausenaufsichtID_zu_pausenaufsicht, pausenaufsicht.id, pausenaufsicht);
		this._datenP.add(pausenaufsicht);
		this._datenP.sort(this._compPausenaufsicht);
	}

	/**
	 * Fügt dem Stundenplan eine neues {@link StundenplanZeitraster} hinzu.
	 *
	 * @param zeitraster Das StundenplanZeitraster, das hinzugefügt werden soll.
	 */
	public addZeitraster(zeitraster : StundenplanZeitraster) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_zeitrasterID_zu_zeitraster, zeitraster.id, zeitraster);
		this._daten.zeitraster.add(zeitraster);
		this._daten.zeitraster.sort(StundenplanManager._compZeitraster);
	}

	/**
	 * Entfernt aus dem Stundenplan einen existierenden {@link StundenplanRaum}-Objekt.
	 *
	 * @param raumID Die ID des {@link StundenplanRaum}-Objekts.
	 */
	public removeRaum(raumID : number) : void {
		const raum : StundenplanRaum = DeveloperNotificationException.ifNull("_map_raumID_zu_raum.get(" + raumID + ")", this._map_raumID_zu_raum.get(raumID));
		this._map_raumID_zu_raum.remove(raumID);
		this._daten.raeume.remove(raum);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param pausenzeitID Die ID des {@link StundenplanPausenzeit}-Objekts.
	 */
	public removePausenzeit(pausenzeitID : number) : void {
		const pausenzeit : StundenplanPausenzeit = DeveloperNotificationException.ifNull("_map_pausenzeitID_zu_pausenzeit.get(" + pausenzeitID + ")", this._map_pausenzeitID_zu_pausenzeit.get(pausenzeitID));
		this._map_pausenzeitID_zu_pausenzeit.remove(pausenzeitID);
		this._daten.pausenzeiten.remove(pausenzeit);
	}

	/**
	 * Entfernt aus dem Stundenplan einen existierendes {@link StundenplanAufsichtsbereich}-Objekt.
	 *
	 * @param aufsichtsbereichID Die ID des {@link StundenplanAufsichtsbereich}-Objekts.
	 */
	public removeAufsichtsbereich(aufsichtsbereichID : number) : void {
		const aufsichtsbereich : StundenplanAufsichtsbereich = DeveloperNotificationException.ifNull("_map_aufsichtID_zu_aufsicht.get(" + aufsichtsbereichID + ")", this._map_aufsichtsbereichID_zu_aufsichtsbereich.get(aufsichtsbereichID));
		this._map_aufsichtsbereichID_zu_aufsichtsbereich.remove(aufsichtsbereichID);
		this._daten.aufsichtsbereiche.remove(aufsichtsbereich);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @param kwzID Die ID des {@link StundenplanKalenderwochenzuordnung}-Objekts.
	 */
	public removeKalenderwochenzuordnung(kwzID : number) : void {
		const kwz : StundenplanKalenderwochenzuordnung = DeveloperNotificationException.ifNull("_map_kwzID_zu_kwz.get(" + kwzID + ")", this._map_kwzID_zu_kwz.get(kwzID));
		this._map_kwzID_zu_kwz.remove(kwzID);
		this._daten.kalenderwochenZuordnung.remove(kwz);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @param pausenaufsichtID Die ID des {@link StundenplanPausenaufsicht}-Objekts.
	 */
	public removePausenaufsicht(pausenaufsichtID : number) : void {
		const pa : StundenplanPausenaufsicht = DeveloperNotificationException.ifNull("_map_pausenaufsichtID_zu_pausenaufsicht.get(" + pausenaufsichtID + ")", this._map_pausenaufsichtID_zu_pausenaufsicht.get(pausenaufsichtID));
		this._map_pausenaufsichtID_zu_pausenaufsicht.remove(pausenaufsichtID);
		this._datenP.remove(pa);
	}

	/**
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param zeitrasterID Die ID des {@link StundenplanZeitraster}-Objekts.
	 */
	public removeZeitraster(zeitrasterID : number) : void {
		const zr : StundenplanZeitraster = DeveloperNotificationException.ifNull("_map_zeitrasterID_zu_zeitraster.get(" + zeitrasterID + ")", this._map_zeitrasterID_zu_zeitraster.get(zeitrasterID));
		this._map_zeitrasterID_zu_zeitraster.remove(zeitrasterID);
		this._daten.zeitraster.remove(zr);
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
	 * Entfernt anhand der ID das alte {@link StundenplanPausenaufsicht}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param pausenaufsicht Das neue {@link StundenplanPausenaufsicht}-Objekt, welches das alte Objekt ersetzt.
	 */
	public patchPausenaufsicht(pausenaufsicht : StundenplanPausenaufsicht) : void {
		this.removePausenaufsicht(pausenaufsicht.id);
		this.addPausenaufsicht(pausenaufsicht);
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplan.StundenplanManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanManager(obj : unknown) : StundenplanManager {
	return obj as StundenplanManager;
}
