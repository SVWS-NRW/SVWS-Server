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
import { StundenplanSchueler } from '../../../core/data/stundenplan/StundenplanSchueler';
import { StundenplanLehrer } from '../../../core/data/stundenplan/StundenplanLehrer';
import { StundenplanUnterricht } from '../../../core/data/stundenplan/StundenplanUnterricht';
import { List } from '../../../java/util/List';
import { StundenplanKalenderwochenzuordnung } from '../../../core/data/stundenplan/StundenplanKalenderwochenzuordnung';
import { Stundenplan } from '../../../core/data/stundenplan/Stundenplan';
import { StundenplanPausenaufsicht } from '../../../core/data/stundenplan/StundenplanPausenaufsicht';
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { StundenplanPausenzeit } from '../../../core/data/stundenplan/StundenplanPausenzeit';
import { StundenplanAufsichtsbereich } from '../../../core/data/stundenplan/StundenplanAufsichtsbereich';
import { StundenplanRaum } from '../../../core/data/stundenplan/StundenplanRaum';
import { StundenplanSchiene } from '../../../core/data/stundenplan/StundenplanSchiene';
import { StundenplanFach } from '../../../core/data/stundenplan/StundenplanFach';
import { Wochentag } from '../../../core/types/Wochentag';
import { JavaMap } from '../../../java/util/JavaMap';

export class StundenplanManager extends JavaObject {

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
		this.checkWochentypenKonsistenz();
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
		this.initMapKWZuordnung();
		this.initMapKursZuUnterrichte();
		this.initMapPausenaufsichten();
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

	private initMapFach() : void {
		this._map_fachID_zu_fach.clear();
		for (const fach of this._datenUV.faecher) {
			DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
			DeveloperNotificationException.ifStringIsBlank("fach.bezeichnung", fach.bezeichnung);
			DeveloperNotificationException.ifStringIsBlank("fach.kuerzel", fach.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_fachID_zu_fach, fach.id, fach);
		}
	}

	private initMapJahrgang() : void {
		this._map_jahrgangID_zu_jahrgang.clear();
		for (const jahrgang of this._datenUV.jahrgaenge) {
			DeveloperNotificationException.ifInvalidID("jahrgang.id", jahrgang.id);
			DeveloperNotificationException.ifStringIsBlank("jahrgang.bezeichnung", jahrgang.bezeichnung);
			DeveloperNotificationException.ifStringIsBlank("jahrgang.kuerzel", jahrgang.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_jahrgangID_zu_jahrgang, jahrgang.id, jahrgang);
		}
	}

	private initMapLehrer() : void {
		this._map_lehrerID_zu_lehrer.clear();
		for (const lehrer of this._datenUV.lehrer) {
			DeveloperNotificationException.ifInvalidID("lehrer.id", lehrer.id);
			DeveloperNotificationException.ifStringIsBlank("lehrer.kuerzel", lehrer.kuerzel);
			DeveloperNotificationException.ifStringIsBlank("lehrer.nachname", lehrer.nachname);
			DeveloperNotificationException.ifStringIsBlank("lehrer.vorname", lehrer.vorname);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_lehrerID_zu_lehrer, lehrer.id, lehrer);
			const listFaecherDerLehrkraft : ArrayList<number> = new ArrayList();
			for (const idFachDerLehrkraft of lehrer.faecher) {
				DeveloperNotificationException.ifMapNotContains("_map_fachID_zu_fach", this._map_fachID_zu_fach, idFachDerLehrkraft);
				DeveloperNotificationException.ifListAddsDuplicate("listFaecherDerLehrkraft", listFaecherDerLehrkraft, idFachDerLehrkraft);
			}
		}
	}

	private initMapKlasse() : void {
		this._map_klasseID_zu_klasse.clear();
		for (const klasse of this._datenUV.klassen) {
			DeveloperNotificationException.ifInvalidID("klasse.id", klasse.id);
			DeveloperNotificationException.ifStringIsBlank("klasse.kuerzel", klasse.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_klasseID_zu_klasse, klasse.id, klasse);
			const listJahrgaengeDerKlasse : ArrayList<number> = new ArrayList();
			for (const idJahrgangDerKlasse of klasse.jahrgaenge) {
				DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", this._map_jahrgangID_zu_jahrgang, idJahrgangDerKlasse);
				DeveloperNotificationException.ifListAddsDuplicate("listJahrgaengeDerKlasse", listJahrgaengeDerKlasse, idJahrgangDerKlasse);
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
			for (const idSchuelerDesKurses of kurs.schueler)
				DeveloperNotificationException.ifMapNotContains("_map_schuelerID_zu_schueler", this._map_schuelerID_zu_schueler, idSchuelerDesKurses);
			for (const idJahrgangDesKurses of kurs.jahrgaenge)
				DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", this._map_jahrgangID_zu_jahrgang, idJahrgangDesKurses);
			for (const idSchieneDesKurses of kurs.schienen)
				DeveloperNotificationException.ifMapNotContains("_map_schieneID_zu_schiene", this._map_schieneID_zu_schiene, idSchieneDesKurses);
		}
	}

	private initMapZeitraster() : void {
		this._map_zeitrasterID_zu_zeitraster.clear();
		this._map_wochentag_stunde_zu_zeitraster.clear();
		for (const zeit of this._daten.zeitraster) {
			Wochentag.fromIDorException(zeit.wochentag);
			DeveloperNotificationException.ifInvalidID("zeit.id", zeit.id);
			DeveloperNotificationException.ifNull("zeit.stundenbeginn == null", zeit.stundenbeginn);
			DeveloperNotificationException.ifNull("zeit.stundenende == null", zeit.stundenende);
			DeveloperNotificationException.ifTrue("zeit.unterrichtstunde <= 0", zeit.unterrichtstunde <= 0);
			DeveloperNotificationException.ifMapContains("_map_zeitrasterID_zu_zeitraster", this._map_zeitrasterID_zu_zeitraster, zeit.id);
			this._map_zeitrasterID_zu_zeitraster.put(zeit.id, zeit);
			this._map_wochentag_stunde_zu_zeitraster.put(zeit.wochentag, zeit.unterrichtstunde, zeit);
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

	private initMapPausenzeit() : void {
		this._map_pausenzeitID_zu_pausenzeit.clear();
		for (const pause of this._daten.pausenzeiten) {
			Wochentag.fromIDorException(pause.wochentag);
			DeveloperNotificationException.ifInvalidID("pause.id", pause.id);
			DeveloperNotificationException.ifNull("pause.beginn == null", pause.beginn);
			DeveloperNotificationException.ifNull("pause.ende == null", pause.ende);
			DeveloperNotificationException.ifMapContains("_map_pausenzeitID_zu_pausenzeit", this._map_pausenzeitID_zu_pausenzeit, pause.id);
			this._map_pausenzeitID_zu_pausenzeit.put(pause.id, pause);
		}
	}

	private initMapAufsicht() : void {
		this._map_aufsichtsbereichID_zu_aufsichtsbereich.clear();
		for (const aufsicht of this._daten.aufsichtsbereiche) {
			DeveloperNotificationException.ifInvalidID("aufsicht.id", aufsicht.id);
			DeveloperNotificationException.ifTrue("aufsicht.kuerzel.isBlank()", JavaString.isBlank(aufsicht.kuerzel));
			DeveloperNotificationException.ifMapContains("_map_aufsichtID_zu_aufsicht", this._map_aufsichtsbereichID_zu_aufsichtsbereich, aufsicht.id);
			this._map_aufsichtsbereichID_zu_aufsichtsbereich.put(aufsicht.id, aufsicht);
		}
	}

	private initMapKWZuordnung() : void {
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

	private initMapPausenaufsichten() : void {
		this._map_pausenaufsichtID_zu_pausenaufsicht.clear();
		for (const pa of this._datenP) {
			DeveloperNotificationException.ifTrue("(pa.wochentyp > 0) && (pa.wochentyp > _daten.wochenTypModell)", (pa.wochentyp > 0) && (pa.wochentyp > this._daten.wochenTypModell));
			DeveloperNotificationException.ifInvalidID("aufsicht.id", pa.id);
			DeveloperNotificationException.ifInvalidID("aufsicht.id", pa.idLehrer);
			DeveloperNotificationException.ifInvalidID("aufsicht.id", pa.idPausenzeit);
			DeveloperNotificationException.ifMapNotContains("_map_lehrerID_zu_lehrer", this._map_lehrerID_zu_lehrer, pa.idLehrer);
			DeveloperNotificationException.ifMapNotContains("_map_pausenzeitID_zu_pausenzeit", this._map_pausenzeitID_zu_pausenzeit, pa.idPausenzeit);
			DeveloperNotificationException.ifMapContains("_map_pausenaufsichtID_zu_pausenaufsicht", this._map_pausenaufsichtID_zu_pausenaufsicht, pa.id);
			const listAB : ArrayList<number> = new ArrayList();
			for (const aufsichtsbereichID of pa.bereiche) {
				DeveloperNotificationException.ifMapNotContains("_map_aufsichtsbereichID_zu_aufsichtsbereich", this._map_aufsichtsbereichID_zu_aufsichtsbereich, aufsichtsbereichID);
				DeveloperNotificationException.ifTrue("listAB.contains(" + aufsichtsbereichID! + ")", listAB.contains(aufsichtsbereichID));
				listAB.add(aufsichtsbereichID);
			}
			this._map_pausenaufsichtID_zu_pausenaufsicht.put(pa.id, pa);
		}
	}

	private initMapKursZuUnterrichte() : void {
		this._map_kursID_zu_unterrichte.clear();
		for (const u of this._datenU) {
			if (u.idKurs === null)
				continue;
			DeveloperNotificationException.ifMapNotContains("_map_kursID_zu_kurs", this._map_kursID_zu_kurs, u.idKurs);
			DeveloperNotificationException.ifMapNotContains("_map_fachID_zu_fach", this._map_fachID_zu_fach, u.idFach);
			DeveloperNotificationException.ifMapNotContains("_map_zeitrasterID_zu_zeitraster", this._map_zeitrasterID_zu_zeitraster, u.idZeitraster);
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
	 * Liefert die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Intervall berührt.
	 *
	 * @param zeitrasterStart    Das {@link StundenplanZeitraster} zu dem es startet.
	 * @param minutenVerstrichen Die verstrichene Zeit seit dem Start des Zeitrasters.
	 *
	 * @return die passende Menge an {@link StundenplanZeitraster}-Objekten.
	 */
	public getZeitrasterByStartVerstrichen(zeitrasterStart : StundenplanZeitraster, minutenVerstrichen : number) : List<StundenplanZeitraster> {
		const result : List<StundenplanZeitraster> = new ArrayList();
		return result;
	}

	/**
	 * Liefert die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Intervall berührt.
	 *
	 * @param wochentag DUMMY
	 * @param stundenbeginn DUMMY
	 * @param minutenVerstrichen DUMMY
	 *
	 * @return die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Intervall berührt.
	 */
	public getZeitrasterByWochentagStartVerstrichen(wochentag : Wochentag, stundenbeginn : string, minutenVerstrichen : number) : List<StundenplanZeitraster> {
		const result : List<StundenplanZeitraster> = new ArrayList();
		return result;
	}

	/**
	 * Fügt dem Stundenplan einen neuen {@link StundenplanRaum} hinzu.
	 *
	 * @param raum Der Raum, der hinzugefügt werden soll.
	 */
	public addRaum(raum : StundenplanRaum) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_raumID_zu_raum, raum.id, raum);
		this._daten.raeume.add(raum);
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanPausenzeit} hinzu.
	 *
	 * @param pausenzeit Die Pausenzeit, die hinzugefügt werden soll.
	 */
	public addPausenzeit(pausenzeit : StundenplanPausenzeit) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_pausenzeitID_zu_pausenzeit, pausenzeit.id, pausenzeit);
		this._daten.pausenzeiten.add(pausenzeit);
	}

	/**
	 * Fügt dem Stundenplan einen neuen {@link StundenplanAufsichtsbereich} hinzu.
	 *
	 * @param aufsichtsbereich Der Aufsichtsbereich, der hinzugefügt werden soll.
	 */
	public addAufsichtsbereich(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_aufsichtsbereichID_zu_aufsichtsbereich, aufsichtsbereich.id, aufsichtsbereich);
		this._daten.aufsichtsbereiche.add(aufsichtsbereich);
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanKalenderwochenzuordnung} hinzu.
	 *
	 * @param kwz Die Kalenderwochenzuordnung, die hinzugefügt werden soll.
	 */
	public addKalenderwochenzuordnung(kwz : StundenplanKalenderwochenzuordnung) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_kwzID_zu_kwz, kwz.id, kwz);
		this._daten.kalenderwochenZuordnung.add(kwz);
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanPausenaufsicht} hinzu.
	 *
	 * @param pausenaufsicht Die StundenplanPausenaufsicht, die hinzugefügt werden soll.
	 */
	public addPausenaufsicht(pausenaufsicht : StundenplanPausenaufsicht) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_pausenaufsichtID_zu_pausenaufsicht, pausenaufsicht.id, pausenaufsicht);
		this._datenP.add(pausenaufsicht);
	}

	/**
	 * Fügt dem Stundenplan eine neues {@link StundenplanZeitraster} hinzu.
	 *
	 * @param zeitraster Das StundenplanZeitraster, das hinzugefügt werden soll.
	 */
	public addZeitraster(zeitraster : StundenplanZeitraster) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_zeitrasterID_zu_zeitraster, zeitraster.id, zeitraster);
		this._daten.zeitraster.add(zeitraster);
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
