import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostBlockungsergebnisListeneintrag } from '../../../core/data/gost/GostBlockungsergebnisListeneintrag';
import { GostFaecherManager, cast_de_svws_nrw_core_utils_gost_GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { LongArrayKey } from '../../../core/adt/LongArrayKey';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../../java/lang/JavaString';
import { GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import type { Comparator } from '../../../java/util/Comparator';
import { GostKursblockungRegelTyp } from '../../../core/types/kursblockung/GostKursblockungRegelTyp';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import type { List } from '../../../java/util/List';
import { GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';
import { HashSet } from '../../../java/util/HashSet';
import { GostFach } from '../../../core/data/gost/GostFach';
import { GostBlockungKursLehrer } from '../../../core/data/gost/GostBlockungKursLehrer';
import { GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { ArrayMap } from '../../../core/adt/map/ArrayMap';
import { MapUtils } from '../../../core/utils/MapUtils';
import { Map2DUtils } from '../../../core/utils/Map2DUtils';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { GostBlockungsergebnis } from '../../../core/data/gost/GostBlockungsergebnis';
import { GostBlockungsdaten, cast_de_svws_nrw_core_data_gost_GostBlockungsdaten } from '../../../core/data/gost/GostBlockungsdaten';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { GostBlockungsergebnisListeneintragComparator } from '../../../core/utils/gost/GostBlockungsergebnisListeneintragComparator';
import { GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { JavaLong } from '../../../java/lang/JavaLong';
import { ListUtils } from '../../../core/utils/ListUtils';
import type { JavaMap } from '../../../java/util/JavaMap';
import { UserNotificationException } from '../../../core/exceptions/UserNotificationException';

export class GostBlockungsdatenManager extends JavaObject {

	/**
	 * Die Blockungsdaten, die im Manager vorhanden sind.
	 */
	private readonly _daten : GostBlockungsdaten;

	/**
	 * Der Fächermanager mit den Fächern der gymnasialen Oberstufe.
	 */
	private readonly _faecherManager : GostFaecherManager;

	/**
	 * Ein Comparator für Kurse der Blockung. Dieser vergleicht nur die Kursnummern!
	 */
	private static readonly _compKursnummer : Comparator<GostBlockungKurs> = { compare : (a: GostBlockungKurs, b: GostBlockungKurs) => JavaInteger.compare(a.nummer, b.nummer) };

	/**
	 * Ein Comparator für Schienen der Blockung
	 */
	private static readonly _compSchiene : Comparator<GostBlockungSchiene> = { compare : (a: GostBlockungSchiene, b: GostBlockungSchiene) => JavaInteger.compare(a.nummer, b.nummer) };

	/**
	 * Ein Comparator für Regeln der Blockung
	 */
	private static readonly _compRegel : Comparator<GostBlockungRegel> = { compare : (a: GostBlockungRegel, b: GostBlockungRegel) => {
		const result : number = JavaInteger.compare(a.typ, b.typ);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Ein Comparator für die Lehrkräfte eines Kurses
	 */
	private static readonly _compLehrkraefte : Comparator<GostBlockungKursLehrer> = { compare : (a: GostBlockungKursLehrer, b: GostBlockungKursLehrer) => {
		const result : number = JavaInteger.compare(a.reihenfolge, b.reihenfolge);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Ein Comparator für die Schüler.
	 */
	private static readonly _compSchueler : Comparator<Schueler> = { compare : (a: Schueler, b: Schueler) => {
		const cNachname : number = JavaString.compareTo(a.nachname, b.nachname);
		if (cNachname !== 0)
			return cNachname;
		const cVorname : number = JavaString.compareTo(a.vorname, b.vorname);
		if (cVorname !== 0)
			return cVorname;
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Ein Comparator für die Fachwahlen (SCHÜLERID, FACH, KURSART)
	 */
	private readonly _compFachwahlen : Comparator<GostFachwahl>;

	/**
	 * Ein Comparator für die {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung.
	 */
	private readonly _compErgebnisse : Comparator<GostBlockungsergebnisListeneintrag> = new GostBlockungsergebnisListeneintragComparator();

	/**
	 * Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER)
	 */
	private readonly _compKurs_kursart_fach_kursnummer : Comparator<GostBlockungKurs>;

	/**
	 * Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER).
	 */
	private readonly _compKurs_fach_kursart_kursnummer : Comparator<GostBlockungKurs>;

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Kurse anhand ihrer Datenbank-ID.
	 */
	private readonly _map_idKurs_kurs : HashMap<number, GostBlockungKurs> = new HashMap();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Listen der Kurse, welche Fach und Kursart gemeinsam haben, anhand der beiden IDs.
	 */
	private readonly _map2d_idFach_idKursart_kurse : HashMap2D<number, number, List<GostBlockungKurs>> = new HashMap2D();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Listen der Fachwahlen, welche Fach und Kursart gemeinsam haben, anhand der beiden IDs.
	 */
	private readonly _map2d_idFach_idKursart_fachwahlen : HashMap2D<number, number, List<GostFachwahl>> = new HashMap2D();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Schienen anhand ihrer Datenbank-ID.
	 */
	private readonly _map_idSchiene_schiene : HashMap<number, GostBlockungSchiene> = new HashMap();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Regeln anhand ihrer Datenbank-ID.
	 */
	private readonly _map_idRegel_regel : HashMap<number, GostBlockungRegel> = new HashMap();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 */
	private readonly _map_regeltyp_regeln : JavaMap<GostKursblockungRegelTyp, List<GostBlockungRegel>> = new ArrayMap(GostKursblockungRegelTyp.values());

	/**
	 * Eine interne Hashmap zum Multi-Key-Zugriff auf die Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 */
	private readonly _map_multikey_regeln : HashMap<LongArrayKey, GostBlockungRegel> = new HashMap();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Schueler anhand ihrer Datenbank-ID.
	 */
	private readonly _map_idSchueler_schueler : HashMap<number, Schueler> = new HashMap();

	/**
	 * Schüler-ID --> List<Fachwahl> = Die Fachwahlen des Schülers der jeweiligen Fachart.
	 */
	private readonly _map_idSchueler_fachwahlen : HashMap<number, List<GostFachwahl>> = new HashMap();

	/**
	 * (Schüler-ID, Fach-ID) --> Kursart = Die Fachwahl des Schülers die dem Fach die Kursart zuordnet.
	 */
	private readonly _map2d_idSchueler_idFach_fachwahl : HashMap2D<number, number, GostFachwahl> = new HashMap2D();

	/**
	 * Fachart-ID --> List<Fachwahl> = Die Fachwahlen einer Fachart.
	 */
	private readonly _map_idFachart_fachwahlen : HashMap<number, List<GostFachwahl>> = new HashMap();

	/**
	 * Ergebnis-ID --> {@link GostBlockungsergebnisListeneintrag}
	 */
	private readonly _map_idErgebnis_Ergebnis : HashMap<number, GostBlockungsergebnisListeneintrag> = new HashMap();

	/**
	 * Eine sortierte, gecachte Menge der Kurse nach: (FACH, KURSART, KURSNUMMER).
	 */
	private readonly _list_kurse_sortiert_fach_kursart_kursnummer : List<GostBlockungKurs> = new ArrayList();

	/**
	 * Eine sortierte, gecachte Menge der Kurse nach: (KURSART, FACH, KURSNUMMER)
	 */
	private readonly _list_kurse_sortiert_kursart_fach_kursnummer : List<GostBlockungKurs> = new ArrayList();

	/**
	 * Die maximale Zeit in Millisekunden die der Blockungsalgorithmus verwenden darf.
	 */
	private _maxTimeMillis : number = 1000;


	/**
	 * Erstellt einen neuen Manager mit leeren Blockungsdaten und einem leeren Fächer-Manager.
	 */
	public constructor();

	/**
	 *Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und dem Fächer-Manager.
	 *
	 * @param daten           die Blockungsdaten
	 * @param faecherManager  der Fächer-Manager
	 */
	public constructor(daten : GostBlockungsdaten, faecherManager : GostFaecherManager);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : GostBlockungsdaten, __param1? : GostFaecherManager) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined")) {
			this._faecherManager = new GostFaecherManager();
			this._daten = new GostBlockungsdaten();
			this._daten.gostHalbjahr = GostHalbjahr.EF1.id;
			this._compKurs_fach_kursart_kursnummer = this.createComparatorKursFachKursartNummer();
			this._compKurs_kursart_fach_kursnummer = this.createComparatorKursKursartFachNummer();
			this._compFachwahlen = this.createComparatorFachwahlen();
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostBlockungsdaten')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.utils.gost.GostFaecherManager'))))) {
			const daten : GostBlockungsdaten = cast_de_svws_nrw_core_data_gost_GostBlockungsdaten(__param0);
			const faecherManager : GostFaecherManager = cast_de_svws_nrw_core_utils_gost_GostFaecherManager(__param1);
			this._faecherManager = faecherManager;
			this._compKurs_fach_kursart_kursnummer = this.createComparatorKursFachKursartNummer();
			this._compKurs_kursart_fach_kursnummer = this.createComparatorKursKursartFachNummer();
			this._compFachwahlen = this.createComparatorFachwahlen();
			this._daten = new GostBlockungsdaten();
			this._daten.id = daten.id;
			this._daten.name = daten.name;
			this._daten.abijahrgang = daten.abijahrgang;
			this._daten.gostHalbjahr = daten.gostHalbjahr;
			this._daten.istAktiv = daten.istAktiv;
			this.schieneAddListe(daten.schienen);
			this.regelAddListe(daten.regeln);
			this.kursAddListe(daten.kurse);
			this.schuelerAddListe(daten.schueler);
			this.fachwahlAddListe(daten.fachwahlen);
			this.ergebnisAddListe(daten.ergebnisse);
		} else throw new Error('invalid method overload');
	}

	private createComparatorKursFachKursartNummer() : Comparator<GostBlockungKurs> {
		const comp : Comparator<GostBlockungKurs> = { compare : (a: GostBlockungKurs, b: GostBlockungKurs) => {
			const aFach : GostFach = this._faecherManager.getOrException(a.fach_id);
			const bFach : GostFach = this._faecherManager.getOrException(b.fach_id);
			const cmpFach : number = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach !== 0)
				return cmpFach;
			if (a.kursart < b.kursart)
				return -1;
			if (a.kursart > b.kursart)
				return +1;
			return JavaInteger.compare(a.nummer, b.nummer);
		} };
		return comp;
	}

	private createComparatorKursKursartFachNummer() : Comparator<GostBlockungKurs> {
		const comp : Comparator<GostBlockungKurs> = { compare : (a: GostBlockungKurs, b: GostBlockungKurs) => {
			if (a.kursart < b.kursart)
				return -1;
			if (a.kursart > b.kursart)
				return +1;
			const aFach : GostFach = this._faecherManager.getOrException(a.fach_id);
			const bFach : GostFach = this._faecherManager.getOrException(b.fach_id);
			const cmpFach : number = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach !== 0)
				return cmpFach;
			return JavaInteger.compare(a.nummer, b.nummer);
		} };
		return comp;
	}

	private createComparatorFachwahlen() : Comparator<GostFachwahl> {
		const comp : Comparator<GostFachwahl> = { compare : (a: GostFachwahl, b: GostFachwahl) => {
			if (a.schuelerID < b.schuelerID)
				return -1;
			if (a.schuelerID > b.schuelerID)
				return +1;
			if (a.kursartID < b.kursartID)
				return -1;
			if (a.kursartID > b.kursartID)
				return +1;
			const aFach : GostFach = this._faecherManager.getOrException(a.fachID);
			const bFach : GostFach = this._faecherManager.getOrException(b.fachID);
			return GostFaecherManager.comp.compare(aFach, bFach);
		} };
		return comp;
	}

	private ergebnisAddOhneSortierung(ergebnis : GostBlockungsergebnisListeneintrag) : void {
		DeveloperNotificationException.ifInvalidID("pErgebnis.id", ergebnis.id);
		DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", ergebnis.blockungID);
		DeveloperNotificationException.ifNull("GostHalbjahr.fromID(" + ergebnis.gostHalbjahr + ")", GostHalbjahr.fromID(ergebnis.gostHalbjahr));
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idErgebnis_Ergebnis, ergebnis.id, ergebnis);
		this._daten.ergebnisse.add(ergebnis);
	}

	/**
	 * Fügt das übergebenen Ergebnis der Blockung hinzu.
	 *
	 * @param ergebnis Das {@link GostBlockungsergebnisListeneintrag}-Objekt, welches hinzugefügt wird.
	 * @throws DeveloperNotificationException Falls in den Daten des Listeneintrags Inkonsistenzen sind.
	 */
	public ergebnisAdd(ergebnis : GostBlockungsergebnisListeneintrag) : void {
		this.ergebnisAddOhneSortierung(ergebnis);
		this._daten.ergebnisse.sort(this._compErgebnisse);
	}

	/**
	 * Fügt die Menge an Ergebnissen {@link GostBlockungsergebnisListeneintrag} hinzu.
	 *
	 * @param ergebnismenge Die Menge an Ergebnissen.
	 * @throws DeveloperNotificationException Falls in den Daten der Listeneinträge Inkonsistenzen sind.
	 */
	public ergebnisAddListe(ergebnismenge : List<GostBlockungsergebnisListeneintrag>) : void {
		for (const ergebnis of ergebnismenge)
			this.ergebnisAddOhneSortierung(ergebnis);
		this._daten.ergebnisse.sort(this._compErgebnisse);
	}

	/**
	 * Liefert einen {@link GostBlockungsergebnisListeneintrag} aus der Liste der Ergebnisse.
	 * Wirft eine Exception, falls es keinen Listeneintrag mit dieser ID gibt.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return einen {@link GostBlockungsergebnisListeneintrag} aus der Liste der Ergebnisse.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGet(idErgebnis : number) : GostBlockungsergebnisListeneintrag {
		const e : GostBlockungsergebnisListeneintrag | null = this._map_idErgebnis_Ergebnis.get(idErgebnis);
		return DeveloperNotificationException.ifNull("Es wurde kein Listeneintrag mit ID(" + idErgebnis + ") gefunden!", e);
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung.
	 *
	 * @return Eine sortierte Menge der {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung.
	 */
	public ergebnisGetListeSortiertNachBewertung() : List<GostBlockungsergebnisListeneintrag> {
		const result : List<GostBlockungsergebnisListeneintrag> = new ArrayList(this._daten.ergebnisse);
		return result;
	}

	/**
	 * Entfernt das Ergebnis mit der übergebenen ID aus der Blockung.
	 *
	 * @param idErgebnis  Die Datenbank-ID des zu entfernenden Ergebnisses.
	 *
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisRemoveByID(idErgebnis : number) : void {
		const e : GostBlockungsergebnisListeneintrag = this.ergebnisGet(idErgebnis);
		this._daten.ergebnisse.remove(e);
		this._map_idErgebnis_Ergebnis.remove(idErgebnis);
	}

	/**
	 * Entfernt das übergebenen Ergebnis aus der Blockung.
	 *
	 * @param ergebnis  Das zu entfernende Ergebnis.
	 *
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisRemove(ergebnis : GostBlockungsergebnisListeneintrag) : void {
		this.ergebnisRemoveByID(ergebnis.id);
	}

	/**
	 * Aktualisiert die Bewertung im {@link GostBlockungsdatenManager} mit der aus dem {@link GostBlockungsergebnis}. <br>
	 * Wirft eine Exception, falls kein  {@link GostBlockungsergebnisListeneintrag} mit der ID gefunden wurde.
	 *
	 * @param ergebnis  Das Ergebnis mit der neuen Bewertung.
	 *
	 * @throws DeveloperNotificationException Falls kein  {@link GostBlockungsergebnisListeneintrag} mit der ID gefunden wurde.
	 */
	public ergebnisUpdateBewertung(ergebnis : GostBlockungsergebnis) : void {
		DeveloperNotificationException.ifInvalidID("pErgebnis.id", ergebnis.id);
		DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", ergebnis.blockungID);
		for (const eintrag of this._daten.ergebnisse)
			if (eintrag.id === ergebnis.id)
				eintrag.bewertung = ergebnis.bewertung;
		this._daten.ergebnisse.sort(this._compErgebnisse);
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 *
	 * @param idErgebnis Die Datenbank-ID des Listeneintrages.
	 *
	 * @return Den Wert des 1. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGetBewertung1Wert(idErgebnis : number) : number {
		const e : GostBlockungsergebnisListeneintrag = this.ergebnisGet(idErgebnis);
		let summe : number = 0;
		summe += e.bewertung.anzahlKurseNichtZugeordnet;
		summe += e.bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @param idErgebnis  Die Datenbank-ID des Listeneintrages.
	 *
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGetBewertung1Intervall(idErgebnis : number) : number {
		const summe : number = this.ergebnisGetBewertung1Wert(idErgebnis);
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param idErgebnis  Die Datenbank-ID des Listeneintrages.
	 *
	 * @return Den Wert des 2. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGetBewertung2Wert(idErgebnis : number) : number {
		const e : GostBlockungsergebnisListeneintrag = this.ergebnisGet(idErgebnis);
		let summe : number = 0;
		summe += e.bewertung.anzahlSchuelerNichtZugeordnet;
		summe += e.bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param idErgebnis  Die Datenbank-ID des Listeneintrages.
	 *
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGetBewertung2Intervall(idErgebnis : number) : number {
		const summe : number = this.ergebnisGetBewertung2Wert(idErgebnis);
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Listeneintrages.
	 *
	 * @return Den Wert des 3. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGetBewertung3Wert(idErgebnis : number) : number {
		const e : GostBlockungsergebnisListeneintrag = this.ergebnisGet(idErgebnis);
		return e.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Listeneintrages.
	 *
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGetBewertung3Intervall(idErgebnis : number) : number {
		let wert : number = this.ergebnisGetBewertung3Wert(idErgebnis);
		if (wert > 0)
			wert--;
		return 1 - 1 / (0.25 * wert + 1);
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param idErgebnis  Die Datenbank-ID des Listeneintrages.
	 *
	 * @return Den Wert des 4. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGetBewertung4Wert(idErgebnis : number) : number {
		const e : GostBlockungsergebnisListeneintrag = this.ergebnisGet(idErgebnis);
		return e.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param idErgebnis  Die Datenbank-ID des Listeneintrages.
	 *
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGetBewertung4Intervall(idErgebnis : number) : number {
		const wert : number = this.ergebnisGetBewertung4Wert(idErgebnis);
		return 1 - 1 / (0.25 * wert + 1);
	}

	private kursAddKursOhneSortierung(kurs : GostBlockungKurs) : void {
		const nSchienen : number = this.schieneGetAnzahl();
		DeveloperNotificationException.ifInvalidID("pKurs.id", kurs.id);
		DeveloperNotificationException.ifNull("_faecherManager.get(pKurs.fach_id)", this._faecherManager.get(kurs.fach_id));
		DeveloperNotificationException.ifNull("GostKursart.fromIDorNull(pKurs.kursart)", GostKursart.fromIDorNull(kurs.kursart));
		DeveloperNotificationException.ifSmaller("pKurs.wochenstunden", kurs.wochenstunden, 0);
		DeveloperNotificationException.ifSmaller("pKurs.anzahlSchienen", kurs.anzahlSchienen, 1);
		DeveloperNotificationException.ifGreater("pKurs.anzahlSchienen", kurs.anzahlSchienen, nSchienen);
		DeveloperNotificationException.ifSmaller("pKurs.nummer", kurs.nummer, 1);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idKurs_kurs, kurs.id, kurs);
		DeveloperNotificationException.ifListAddsDuplicate("_kurse_sortiert_fach_kursart_kursnummer", this._list_kurse_sortiert_fach_kursart_kursnummer, kurs);
		DeveloperNotificationException.ifListAddsDuplicate("_kurse_sortiert_kursart_fach_kursnummer", this._list_kurse_sortiert_kursart_fach_kursnummer, kurs);
		const liste : List<GostBlockungKurs> | null = Map2DUtils.getOrCreateArrayList(this._map2d_idFach_idKursart_kurse, kurs.fach_id, kurs.kursart);
		liste.add(kurs);
		liste.sort(GostBlockungsdatenManager._compKursnummer);
		this._daten.kurse.add(kurs);
	}

	/**
	 * Fügt den übergebenen Kurs zu der Blockung hinzu.
	 *
	 * @param kurs  Das {@link GostBlockungKurs}-Objekt, welches hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException falls die Daten des Kurses inkonsistent sind.
	 */
	public kursAdd(kurs : GostBlockungKurs) : void {
		this.kursAddKursOhneSortierung(kurs);
		this._list_kurse_sortiert_fach_kursart_kursnummer.sort(this._compKurs_fach_kursart_kursnummer);
		this._list_kurse_sortiert_kursart_fach_kursnummer.sort(this._compKurs_kursart_fach_kursnummer);
	}

	/**
	 * Fügt die Menge an Kursen hinzu.
	 *
	 * @param kursmenge Die Menge an Kursen.
	 * @throws DeveloperNotificationException Falls die Daten der Kurse inkonsistent sind.
	 */
	public kursAddListe(kursmenge : List<GostBlockungKurs>) : void {
		for (const gKurs of kursmenge)
			this.kursAddKursOhneSortierung(gKurs);
		this._list_kurse_sortiert_fach_kursart_kursnummer.sort(this._compKurs_fach_kursart_kursnummer);
		this._list_kurse_sortiert_kursart_fach_kursnummer.sort(this._compKurs_kursart_fach_kursnummer);
	}

	/**
	 * Liefert TRUE, falls der Kurs mit der übergebenen ID existiert.
	 *
	 * @param idKurs Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs mit der übergebenen ID existiert.
	 */
	public kursGetExistiert(idKurs : number) : boolean {
		return this._map_idKurs_kurs.get(idKurs) !== null;
	}

	/**
	 * Liefert die Anzahl an Kursen.
	 *
	 * @return Die Anzahl an Kursen.
	 */
	public kursGetAnzahl() : number {
		return this._map_idKurs_kurs.size();
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][Suffix], beispielsweise D-GK1.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return Den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][Suffix], beispielsweise D-GK1.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetName(idKurs : number) : string {
		const kurs : GostBlockungKurs = this.kursGet(idKurs);
		const gFach : GostFach = this._faecherManager.getOrException(kurs.fach_id);
		const sSuffix : string = JavaObject.equalsTranspiler(kurs.suffix, ("")) ? "" : ("-" + kurs.suffix);
		return gFach.kuerzelAnzeige + "-" + GostKursart.fromID(kurs.kursart).kuerzel + kurs.nummer + sSuffix!;
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer] ohne den potentiellen Suffix, beispielsweise D-GK1.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer] ohne den potentiellen Suffix, beispielsweise D-GK1.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetNameOhneSuffix(idKurs : number) : string {
		const kurs : GostBlockungKurs = this.kursGet(idKurs);
		const gFach : GostFach = this._faecherManager.getOrException(kurs.fach_id);
		return gFach.kuerzelAnzeige + "-" + GostKursart.fromID(kurs.kursart).kuerzel + kurs.nummer;
	}

	/**
	 * Liefert das {@link GostBlockungKurs}-Objekt mit der übergebenen ID.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses
	 *
	 * @return das {@link GostBlockungKurs}-Objekt mit der übergebenen ID.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGet(idKurs : number) : GostBlockungKurs {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_kurs, idKurs);
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene Nummer hat. <br>
	 * Wirft eine Exceptions, falls es eine solche Lehrkraft nicht gibt.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param reihenfolgeNr  Die Lehrkraft mit der Nummer, die gesucht wird.
	 *
	 * @return die Lehrkraft des Kurses, welche die angegebene Nummer hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public kursGetLehrkraftMitNummer(idKurs : number, reihenfolgeNr : number) : GostBlockungKursLehrer | null {
		for (const lehrkraft of this.kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.reihenfolge === reihenfolgeNr)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + idKurs + " keine Lehrkraft mit ReihenfolgeNr. " + reihenfolgeNr + "!")
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene ID hat. <br>
	 * Wirft eine Exceptions, falls es eine solche Lehrkraft nicht gibt.
	 *
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 * @param idLehrkraft  Die Datenbank-ID der gesuchten Lehrkraft.
	 *
	 * @return Die Lehrkraft des Kurses, welche die angegebene ID hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public kursGetLehrkraftMitID(idKurs : number, idLehrkraft : number) : GostBlockungKursLehrer | null {
		for (const lehrkraft of this.kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.id === idLehrkraft)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + idKurs + " keine Lehrkraft mit ID " + idLehrkraft + "!")
	}

	/**
	 * Liefert eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public kursGetListeSortiertNachFachKursartNummer() : List<GostBlockungKurs> {
		return this._list_kurse_sortiert_fach_kursart_kursnummer;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public kursGetListeSortiertNachKursartFachNummer() : List<GostBlockungKurs> {
		return this._list_kurse_sortiert_kursart_fach_kursnummer;
	}

	/**
	 * Liefert eine nach Kursnummer sortiere Liste der Kurse für das angegebenen Fach und die angegebene Kursart.
	 *
	 * @param idFach      die ID des Fachs
	 * @param idKursart   die ID der Kursart
	 *
	 * @return die sortiere Liste der Kurse für das Fach und die Kursart
	 */
	public kursGetListeByFachUndKursart(idFach : number, idKursart : number) : List<GostBlockungKurs> {
		const liste : List<GostBlockungKurs> | null = this._map2d_idFach_idKursart_kurse.getOrNull(idFach, idKursart);
		if (liste === null)
			return new ArrayList();
		liste.sort(GostBlockungsdatenManager._compKursnummer);
		return liste;
	}

	/**
	 * Liefert alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetLehrkraefteSortiert(idKurs : number) : List<GostBlockungKursLehrer> {
		return this.kursGet(idKurs).lehrer;
	}

	/**
	 * Liefert TRUE, falls ein Löschen des Kurses erlaubt ist. <br>
	 * Kriterium: Der Kurs muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls ein Löschen des Kurses erlaubt ist.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetIsRemoveAllowed(idKurs : number) : boolean {
		return (this._map_idKurs_kurs.get(idKurs) !== null) && this.getIstBlockungsVorlage();
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund von Regeln in der angegebenen Schiene verboten ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund von Regeln in der angegebenen Schiene verboten ist.
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public kursGetIstVerbotenInSchiene(idKurs : number, idSchiene : number) : boolean {
		if (this.kursGetHatSperrungInSchiene(idKurs, idSchiene))
			return true;
		const nummer : number = this.schieneGet(idSchiene).nummer;
		const kursart : number = this.kursGet(idKurs).kursart;
		for (const regel of this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS))
			if (nummer >= regel.parameter.get(1) && nummer <= regel.parameter.get(2)) {
				if (regel.parameter.get(0) !== kursart)
					return true;
			} else {
				if (regel.parameter.get(0) === kursart)
					return true;
			}
		for (const regel of this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS))
			if ((nummer >= regel.parameter.get(1) && nummer <= regel.parameter.get(2)) && (regel.parameter.get(0) === kursart))
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist.
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public kursGetHatSperrungInSchiene(idKurs : number, idSchiene : number) : boolean {
		const nrSchiene : number = this.schieneGet(idSchiene).nummer;
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene]);
		return this._map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene gesperrt hat.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene gesperrt hat.
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public kursGetRegelGesperrtInSchiene(idKurs : number, idSchiene : number) : GostBlockungRegel {
		const nrSchiene : number = this.schieneGet(idSchiene).nummer;
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene]);
		return DeveloperNotificationException.ifNull("Kurs " + idKurs + " ist nicht gesperrt in Schiene " + idSchiene + "!", this._map_multikey_regeln.get(key));
	}

	/**
	 * Liefert die Regel, welche die Anzahl der DummySuS eines Kurses definiert oder NULL.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche die Anzahl der DummySuS eines Kurses definiert oder NULL.
	 */
	public kursGetRegelDummySchuelerOrNull(idKurs : number) : GostBlockungRegel | null {
		for (const regel of this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN))
			if (regel.parameter.get(0) === idKurs)
				return regel;
		return null;
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE} in der angegebenen Schiene fixiert ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE} in der angegebenen Schiene fixiert ist.
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public kursGetHatFixierungInSchiene(idKurs : number, idSchiene : number) : boolean {
		const nrSchiene : number = this.schieneGet(idSchiene).nummer;
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene]);
		return this._map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene fixiert hat.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene fixiert hat.
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public kursGetRegelFixierungInSchiene(idKurs : number, idSchiene : number) : GostBlockungRegel {
		const nrSchiene : number = this.schieneGet(idSchiene).nummer;
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene]);
		return DeveloperNotificationException.ifNull("Kurs " + idKurs + " ist nicht fixiert in Schiene " + idSchiene + "!", this._map_multikey_regeln.get(key));
	}

	/**
	 * Entfernt den Kurs mit der übergebenen ID aus der Blockung.
	 *
	 * @param idKurs  Die Datenbank-ID des zu entfernenden Kurses.
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursRemoveByID(idKurs : number) : void {
		this.kurseRemoveByID(ListUtils.create1(idKurs));
	}

	/**
	 * Entfernt den übergebenen Kurs aus der Blockung.<br>
	 * Wirft eine DeveloperNotificationException, falls der Kurs nicht existiert.
	 *
	 * @param kurs  Der zu entfernende Kurs.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public kursRemove(kurs : GostBlockungKurs) : void {
		this.kursRemoveByID(kurs.id);
	}

	/**
	 * Entfernt alleKurse mit den übergebenen IDs aus der Blockung.
	 *
	 * @param idKurse  Die Datenbank-IDs der zu entfernenden Kurse.
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public kurseRemoveByID(idKurse : List<number>) : void {
		DeveloperNotificationException.ifTrue("Ein Löschen des Kurses ist nur bei einer Blockungsvorlage erlaubt!", !this.getIstBlockungsVorlage());
		for (const idKurs of idKurse)
			DeveloperNotificationException.ifMapNotContains("_map_idKurs_kurs", this._map_idKurs_kurs, idKurs);
		for (const idKurs of idKurse) {
			const kurs : GostBlockungKurs = this.kursGet(idKurs);
			this._list_kurse_sortiert_fach_kursart_kursnummer.remove(kurs);
			this._list_kurse_sortiert_kursart_fach_kursnummer.remove(kurs);
			Map2DUtils.removeFromListAndTrimOrException(this._map2d_idFach_idKursart_kurse, kurs.fach_id, kurs.kursart, kurs);
			DeveloperNotificationException.ifMapRemoveFailes(this._map_idKurs_kurs, idKurs);
			this._daten.kurse.remove(kurs);
		}
	}

	/**
	 * Entfernt alleKurse mit den übergebenen IDs aus der Blockung.
	 *
	 * @param kurse Die zu entfernenden {{@link GostBlockungKurs} GostBlockungKurs-Objekte.
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public kurseRemove(kurse : List<GostBlockungKurs>) : void {
		const idKurse : List<number> = new ArrayList();
		for (const kursExtern of kurse)
			idKurse.add(kursExtern.id);
		this.kurseRemoveByID(idKurse);
	}

	/**
	 * Fügt die übergebene Lehrkraft zum Kurs hinzu. <br>
	 * Wirft eine DeveloperNotificationException, falls der Kurs nicht existiert oder die Lehrkraft oder die ReihenfolgeNr bereits im Kurs existiert.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param neueLehrkraft  Das {@link GostBlockungKursLehrer}-Objekt.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert oder die Lehrkraft oder die ReihenfolgeNr bereits im Kurs existiert.
	 */
	public kursAddLehrkraft(idKurs : number, neueLehrkraft : GostBlockungKursLehrer) : void {
		const kurs : GostBlockungKurs = this.kursGet(idKurs);
		const listOfLehrer : List<GostBlockungKursLehrer> = kurs.lehrer;
		for (const lehrkraft of listOfLehrer) {
			DeveloperNotificationException.ifTrue("kursAddLehrkraft: Der Kurs hat bereits eine Lehrkraft mit ID " + lehrkraft.id, lehrkraft.id === neueLehrkraft.id);
			DeveloperNotificationException.ifTrue("kursAddLehrkraft: Der Kurs hat bereits eine Lehrkraft mit Reihenfolge " + lehrkraft.reihenfolge, lehrkraft.reihenfolge === neueLehrkraft.reihenfolge);
		}
		listOfLehrer.add(neueLehrkraft);
		listOfLehrer.sort(GostBlockungsdatenManager._compLehrkraefte);
	}

	/**
	 * Löscht aus dem übergebenen Kurs die angegebene Lehrkraft. <br>
	 * Wirft eine DeveloperNotificationException, falls der Kurs nicht existiert oder es eine solche Lehrkraft im Kurs nicht gibt.
	 *
	 * @param idKurs           Die Datenbank-ID des Kurses.
	 * @param idAlteLehrkraft  Die Datenbank-ID des {@link GostBlockungKursLehrer}-Objekt.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert oder es eine solche Lehrkraft im Kurs nicht gibt.
	 */
	public kursRemoveLehrkraft(idKurs : number, idAlteLehrkraft : number) : void {
		const kurs : GostBlockungKurs = this.kursGet(idKurs);
		const listOfLehrer : List<GostBlockungKursLehrer> = kurs.lehrer;
		for (let i : number = 0; i < listOfLehrer.size(); i++)
			if (listOfLehrer.get(i).id === idAlteLehrkraft) {
				listOfLehrer.remove(listOfLehrer.get(i));
				return;
			}
		throw new DeveloperNotificationException("kursRemoveLehrkraft: Kurs (" + idKurs + ") hat keine Lehrkraft (" + idAlteLehrkraft + ")!")
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param reihenfolgeNr  Die Lehrkraft mit der Nummer, die gesucht wird.
	 *
	 * @return TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 */
	public kursGetLehrkraftMitNummerExists(idKurs : number, reihenfolgeNr : number) : boolean {
		for (const lehrkraft of this.kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.reihenfolge === reihenfolgeNr)
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 *
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 * @param idLehrkraft  Die Datenbank-ID der gesuchten Lehrkraft.
	 *
	 * @return TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 */
	public kursGetLehrkraftMitIDExists(idKurs : number, idLehrkraft : number) : boolean {
		for (const lehrkraft of this.kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.id === idLehrkraft)
				return true;
		return false;
	}

	/**
	 * Setzt den Suffix des Kurses.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 * @param  suffix  Der neue Suffix des Kurses.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht in der Blockung existiert.
	 */
	public kursSetSuffix(idKurs : number, suffix : string) : void {
		this.kursGet(idKurs).suffix = suffix;
	}

	private schieneAddOhneSortierung(schiene : GostBlockungSchiene) : void {
		DeveloperNotificationException.ifInvalidID("GostBlockungSchiene.id", schiene.id);
		DeveloperNotificationException.ifTrue("GostBlockungSchiene.bezeichnung darf nicht leer sein!", JavaObject.equalsTranspiler("", (schiene.bezeichnung)));
		DeveloperNotificationException.ifSmaller("GostBlockungSchiene.nummer", schiene.nummer, 1);
		DeveloperNotificationException.ifSmaller("GostBlockungSchiene.wochenstunden", schiene.wochenstunden, 1);
		DeveloperNotificationException.ifMapContains("mapSchienen", this._map_idSchiene_schiene, schiene.id);
		this._map_idSchiene_schiene.put(schiene.id, schiene);
		this._daten.schienen.add(schiene);
	}

	/**
	 * Fügt die übergebene Schiene zu der Blockung hinzu.
	 *
	 * @param schiene  Die hinzuzufügende Schiene.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public schieneAdd(schiene : GostBlockungSchiene) : void {
		this.schieneAddListe(ListUtils.create1(schiene));
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
	 *
	 * @param schienenmenge  Die Menge an Schienen.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public schieneAddListe(schienenmenge : List<GostBlockungSchiene>) : void {
		for (const schiene of schienenmenge)
			this.schieneAddOhneSortierung(schiene);
		this._daten.schienen.sort(GostBlockungsdatenManager._compSchiene);
	}

	/**
	 * Gibt die Schiene der Blockung anhand von deren ID zurück.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return Das zugehörige {@link GostBlockungSchiene} Objekt.
	 * @throws DeveloperNotificationException Falls die Schiene nicht in der Blockung existiert.
	 */
	public schieneGet(idSchiene : number) : GostBlockungSchiene {
		return DeveloperNotificationException.ifNull("_mapSchienen.get(" + idSchiene + ")", this._map_idSchiene_schiene.get(idSchiene));
	}

	/**
	 * Liefert TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 */
	public schieneGetExistiert(idSchiene : number) : boolean {
		return this._map_idSchiene_schiene.get(idSchiene) !== null;
	}

	/**
	 * Liefert die aktuelle Menge aller Schienen.
	 * Das ist die interne Referenz zur Liste der Schienen im {@link GostBlockungsdaten}-Objekt.
	 * Diese Liste ist stets sortiert nach der Schienen-Nummer.
	 *
	 * @return Die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 */
	public schieneGetListe() : List<GostBlockungSchiene> {
		return new ArrayList(this._daten.schienen);
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist. <br>
	 * Kriterium: Die Schiene muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert.
	 */
	public schieneGetIsRemoveAllowed(idSchiene : number) : boolean {
		return (this.schieneGet(idSchiene) !== null) && this.getIstBlockungsVorlage();
	}

	/**
	 * Ändert das Attribut {@link GostBlockungSchiene#bezeichnung} der Schiene mit der jeweiligen ID.
	 *
	 * @param idSchiene    Die Datenbank-ID der Schiene.
	 * @param bezeichnung  Die neue Bezeichnung.
	 */
	public schienePatchBezeichnung(idSchiene : number, bezeichnung : string) : void {
		this.schieneGet(idSchiene).bezeichnung = bezeichnung;
	}

	/**
	 * Ändert das Attribut {@link GostBlockungSchiene#wochenstunden} der Schiene mit der jeweiligen ID.
	 *
	 * @param idSchiene      Die Datenbank-ID der Schiene.
	 * @param wochenstunden  Die neuen Wochenstunden.
	 */
	public schienePatchWochenstunden(idSchiene : number, wochenstunden : number) : void {
		this.schieneGet(idSchiene).wochenstunden = wochenstunden;
	}

	/**
	 * Entfernt die Schiene mit der übergebenen ID aus der Blockung.
	 * Konsequenz: <br>
	 * (1) Das Löschen der Schiene muss erlaubt sein, sonst Exception.
	 * (2) Die Schienen werden neu nummeriert. <br>
	 * (3) Die Konsistenz der sortierten Schienen muss überprüft werden. <br>
	 * (4) Die Regeln müssen bei Schienen-Nummern angepasst werden. <br>
	 *
	 * @param idSchiene  Die Datenbank-ID der zu entfernenden Schiene.
	 *
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public schieneRemoveByID(idSchiene : number) : void {
		DeveloperNotificationException.ifTrue("Ein Löschen einer Schiene ist nur bei einer Blockungsvorlage erlaubt!", !this.getIstBlockungsVorlage());
		const schieneR : GostBlockungSchiene = this.schieneGet(idSchiene);
		this._map_idSchiene_schiene.remove(idSchiene);
		this._daten.schienen.remove(schieneR);
		for (const schiene of this._daten.schienen)
			if (schiene.nummer > schieneR.nummer)
				schiene.nummer--;
		for (let index : number = 0; index < this._daten.schienen.size(); index++)
			DeveloperNotificationException.ifTrue("Schiene am Index " + index + " hat nicht Nr. " + (index + 1) + "!", this._daten.schienen.get(index).nummer !== index + 1);
		const iRegel : JavaIterator<GostBlockungRegel> | null = this._daten.regeln.iterator();
		if (iRegel === null)
			return;
		while (iRegel.hasNext()) {
			const r : GostBlockungRegel = iRegel.next();
			const a : Array<number> | null = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			if (a === null)
				iRegel.remove();
			else
				for (let i : number = 0; i < a.length; i++)
					r.parameter.set(i, a[i]);
		}
	}

	/**
	 * Entfernt die übergebene Schiene aus der Blockung.
	 *
	 * @param schiene  Die zu entfernende Schiene.
	 *
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public schieneRemove(schiene : GostBlockungSchiene) : void {
		this.schieneRemoveByID(schiene.id);
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 *
	 * @return Die Anzahl an Schienen.
	 */
	public schieneGetAnzahl() : number {
		return this._map_idSchiene_schiene.size();
	}

	/**
	 * Liefert die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 *
	 * @param  halbjahr  Das Halbjahr, für welches die Blockung angelegt werden soll.
	 *
	 * @return Die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 */
	public static schieneGetDefaultAnzahl(halbjahr : GostHalbjahr) : number {
		return (halbjahr.id < 2) ? 13 : 11;
	}

	private regelAddOhneSortierung(regel : GostBlockungRegel) : void {
		DeveloperNotificationException.ifInvalidID("Regel.id", regel.id);
		const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
		DeveloperNotificationException.ifTrue("Der Typ(" + regel.typ + ") der Regel(" + regel.id + ") ist unbekannt!", typ as unknown === GostKursblockungRegelTyp.UNDEFINIERT as unknown);
		const multikey : LongArrayKey = GostBlockungsdatenManager.regelToMultikey(regel);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idRegel_regel, regel.id, regel);
		MapUtils.getOrCreateArrayList(this._map_regeltyp_regeln, typ).add(regel);
		this._map_multikey_regeln.put(multikey, regel);
		this._daten.regeln.add(regel);
	}

	/**
	 * Fügt die übergebene Regel zu der Blockung hinzu.
	 *
	 * @param regel  Die hinzuzufügende Regel
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regel inkonsistent sind.
	 */
	public regelAdd(regel : GostBlockungRegel) : void {
		this.regelAddOhneSortierung(regel);
		this._daten.regeln.sort(GostBlockungsdatenManager._compRegel);
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 *
	 * @param regelmenge  Die Menge an Regeln.
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public regelAddListe(regelmenge : List<GostBlockungRegel>) : void {
		for (const regel of regelmenge)
			this.regelAddOhneSortierung(regel);
		this._daten.regeln.sort(GostBlockungsdatenManager._compRegel);
	}

	/**
	 * Liefert die Anzahl an Regeln.
	 *
	 * @return Die Anzahl an Regeln.
	 */
	public regelGetAnzahl() : number {
		return this._map_idRegel_regel.size();
	}

	/**
	 * Gibt die Regel der Blockung anhand von deren ID zurück.
	 *
	 * @param idRegel  Die Datenbank-ID der Regel.
	 *
	 * @return Das {@link GostBlockungRegel} Objekt.
	 * @throws DeveloperNotificationException Falls die Regel nicht in der Blockung existiert.
	 */
	public regelGet(idRegel : number) : GostBlockungRegel {
		return DeveloperNotificationException.ifNull("_mapRegeln.get(" + idRegel + ")", this._map_idRegel_regel.get(idRegel));
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln.
	 * Das ist die interne Referenz zur Liste der Regeln im {@link GostBlockungsdaten}-Objekt.
	 * Diese Liste ist stets sortiert nach (TYP, ID).
	 *
	 * @return Die aktuelle Menge aller Regeln sortiert nach (TYP, id).
	 */
	public regelGetListe() : List<GostBlockungRegel> {
		return this._daten.regeln;
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 *
	 * @param typ Der {@link GostKursblockungRegelTyp}.
	 *
	 * @return die aktuelle Menge aller  Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 */
	public regelGetListeOfTyp(typ : GostKursblockungRegelTyp) : List<GostBlockungRegel> {
		return MapUtils.getOrCreateArrayList(this._map_regeltyp_regeln, typ);
	}

	/**
	 * Liefert eine Liste von Regeln, welche den Status der Kurs-Schienen-Sperrung in einem Auswahl-Rechteck ändern soll.
	 * <br>Hinweis: Die Regeln sind vom Typ {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE}. Eine negative ID steht
	 * symbolisch für eine Regel, die noch nicht existiert, andernfalls erhält man eine existierende Regel. Die GUI kann selbst
	 * entscheiden, wie sie mit den Regeln umgeht (toggle, create, delete).
	 *
	 * @deprecated      Die Methode ist in den Ergebnismanager gewandert.
	 *
	 * @param list      Die aktuelle sortierte Liste der GUI.
	 * @param kursA     Der erste oder der letzte Kurs der Auswahl.
	 * @param kursB     Der erste oder der letzte Kurs der Auswahl.
	 * @param schieneA  Die erste oder letzte Schiene der Auswahl.
	 * @param schieneB  Die erste oder letzte Schiene der Auswahl.
	 * @return eine Liste von Regeln, welche den Status der Kurs-Schienen-Sperrung in einem Auswahl-Rechteck ändern soll.
	 */
	public regelGetListeToggleSperrung(list : List<GostBlockungKurs>, kursA : GostBlockungKurs, kursB : GostBlockungKurs, schieneA : GostBlockungSchiene, schieneB : GostBlockungSchiene) : List<GostBlockungRegel> {
		const regeln : List<GostBlockungRegel> = new ArrayList();
		let aktiv : boolean = false;
		const min : number = Math.min(schieneA.nummer, schieneB.nummer);
		const max : number = Math.max(schieneA.nummer, schieneB.nummer);
		for (const kurs of list) {
			if ((kurs as unknown === kursA as unknown) || (kurs as unknown === kursB as unknown))
				aktiv = !aktiv;
			if (!aktiv)
				continue;
			for (let nr : number = min; nr <= max; nr++)
				regeln.add(this.regelGetRegelOrDummyKursGesperrtInSchiene(kurs.id, nr));
		}
		return regeln;
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene sperrt, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param nrSchiene  Die Nummer der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene sperrt, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 */
	public regelGetRegelOrDummyKursGesperrtInSchiene(idKurs : number, nrSchiene : number) : GostBlockungRegel {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene]);
		const regel : GostBlockungRegel | null = this._map_multikey_regeln.get(key);
		if (regel !== null)
			return regel;
		const regelDummy : GostBlockungRegel = new GostBlockungRegel();
		regelDummy.id = -1;
		regelDummy.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		regelDummy.parameter.add(idKurs);
		regelDummy.parameter.add(nrSchiene as number);
		return regelDummy;
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param nrSchiene  Die Nummer der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 */
	public regelGetRegelOrDummyKursFixierungInSchiene(idKurs : number, nrSchiene : number) : GostBlockungRegel {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene]);
		const regel : GostBlockungRegel | null = this._map_multikey_regeln.get(key);
		if (regel !== null)
			return regel;
		const regelDummy : GostBlockungRegel = new GostBlockungRegel();
		regelDummy.id = -1;
		regelDummy.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regelDummy.parameter.add(idKurs);
		regelDummy.parameter.add(nrSchiene as number);
		return regelDummy;
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche den Schüler in einem Kurs fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 */
	public regelGetRegelOrDummySchuelerInKursFixierung(idSchueler : number, idKurs : number) : GostBlockungRegel {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
		const regel : GostBlockungRegel | null = this._map_multikey_regeln.get(key);
		if (regel !== null)
			return regel;
		const regelDummy : GostBlockungRegel = new GostBlockungRegel();
		regelDummy.id = -1;
		regelDummy.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regelDummy.parameter.add(idSchueler);
		regelDummy.parameter.add(idKurs);
		return regelDummy;
	}

	/**
	 * Liefert TRUE, falls die Regel mit der übergebenen ID existiert.
	 *
	 * @param idRegel  Die Datenbank-ID der Regel.
	 *
	 * @return TRUE, falls die Regel mit der übergebenen ID existiert.
	 */
	public regelGetExistiert(idRegel : number) : boolean {
		return this._map_idRegel_regel.get(idRegel) !== null;
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Regel erlaubt ist. <br>
	 * Kriterium: Die Regel muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param  idRegel Die Datenbank-ID der Regel.
	 *
	 * @return TRUE, falls ein Löschen der Regel erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public regelGetIsRemoveAllowed(idRegel : number) : boolean {
		return this._map_idRegel_regel.containsKey(idRegel) && this.getIstBlockungsVorlage();
	}

	/**
	 * Entfernt die Regel mit der übergebenen ID aus der Blockung.
	 * Wirft eine Exception, falls es sich nicht um eine Blockungsvorlage handelt.
	 *
	 * @param idRegel Die Datenbank-ID der zu entfernenden Regel.
	 *
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 * @throws UserNotificationException Falls es sich nicht um eine Blockungsvorlage handelt.
	 */
	public regelRemoveByID(idRegel : number) : void {
		UserNotificationException.ifTrue("Ein Löschen einer Regel ist nur bei einer Blockungsvorlage erlaubt!", !this.getIstBlockungsVorlage());
		const regel : GostBlockungRegel = this.regelGet(idRegel);
		const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
		const multikey : LongArrayKey = GostBlockungsdatenManager.regelToMultikey(regel);
		this._map_idRegel_regel.remove(idRegel);
		MapUtils.getOrCreateArrayList(this._map_regeltyp_regeln, typ).remove(regel);
		this._map_multikey_regeln.remove(multikey);
		this._daten.regeln.remove(regel);
	}

	/**
	 * Entfernt eine Menge von Regeln.
	 *
	 * @param regelmenge  Die Menge an Regeln, die entfernt werden soll.
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public regelRemoveListe(regelmenge : List<GostBlockungRegel>) : void {
		for (const regel of regelmenge)
			this.regelRemoveByID(regel.id);
	}

	private static regelToMultikey(regel : GostBlockungRegel) : LongArrayKey {
		const size : number = regel.parameter.size();
		const keys : Array<number> | null = Array(size + 1).fill(0);
		keys[0] = regel.typ;
		for (let i : number = 1; i <= size; i++)
			keys[i] = regel.parameter.get(i - 1).valueOf();
		return new LongArrayKey(keys);
	}

	/**
	 * Entfernt die übergebene Regel aus der Blockung.
	 *
	 * @param regel  Die zu entfernende Regel
	 *
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 * @throws UserNotificationException Falls es sich nicht um eine Blockungsvorlage handelt.
	 */
	public regelRemove(regel : GostBlockungRegel) : void {
		this.regelRemoveByID(regel.id);
	}

	/**
	 * Liefert die Menge aller Kursarten des Faches, welche in Kursen oder Fachwahlen vorkommen.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Menge aller Kursarten des Faches, welche in Kursen oder Fachwahlen vorkommen.
	 */
	public fachGetMengeKursarten(idFach : number) : List<GostKursart> {
		const idKursarten : HashSet<number> = new HashSet();
		if (this._map2d_idFach_idKursart_kurse.containsKey1(idFach))
			idKursarten.addAll(this._map2d_idFach_idKursart_kurse.getKeySetOf(idFach));
		if (this._map2d_idFach_idKursart_fachwahlen.containsKey1(idFach))
			idKursarten.addAll(this._map2d_idFach_idKursart_fachwahlen.getKeySetOf(idFach));
		const list : List<GostKursart> = new ArrayList();
		for (const kursart of GostKursart.values())
			if (idKursarten.contains(kursart.id))
				list.add(kursart);
		return list;
	}

	/**
	 * Fügt eine Fachwahl hinzu.<br>
	 * Wirft eine Exception, falls die Fachwahl-Daten inkonsistent sind.
	 *
	 * @param fachwahl  Die Fachwahl, die hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public fachwahlAdd(fachwahl : GostFachwahl) : void {
		DeveloperNotificationException.ifMap2DPutOverwrites(this._map2d_idSchueler_idFach_fachwahl, fachwahl.schuelerID, fachwahl.fachID, fachwahl);
		const fachwahlenDesSchuelers : List<GostFachwahl> = MapUtils.getOrCreateArrayList(this._map_idSchueler_fachwahlen, fachwahl.schuelerID);
		fachwahlenDesSchuelers.add(fachwahl);
		fachwahlenDesSchuelers.sort(this._compFachwahlen);
		const fachartID : number = GostKursart.getFachartIDByFachwahl(fachwahl);
		this.fachwahlGetListeOfFachart(fachartID).add(fachwahl);
		Map2DUtils.getOrCreateArrayList(this._map2d_idFach_idKursart_fachwahlen, fachwahl.fachID, fachwahl.kursartID).add(fachwahl);
		this._daten.fachwahlen.add(fachwahl);
	}

	/**
	 * Fügt alle Fachwahlen hinzu.
	 *
	 * @param fachwahlmenge  Die Menge an Fachwahlen.
	 *
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public fachwahlAddListe(fachwahlmenge : List<GostFachwahl>) : void {
		for (const gFachwahl of fachwahlmenge)
			this.fachwahlAdd(gFachwahl);
	}

	/**
	 * Liefert die Anzahl an Fachwahlen.
	 *
	 * @return Die Anzahl an Fachwahlen.
	 */
	public fachwahlGetAnzahl() : number {
		return this._daten.fachwahlen.size();
	}

	/**
	 * Liefert den Namen (Fach-Kursart) der Fachwahl, beispielsweise 'M-GK'.
	 *
	 * @param fachwahl  Das Fachwahl-Objekt.
	 *
	 * @return den Namen (Fach-Kursart) der Fachwahl, beispielsweise 'M-GK'.
	 * @throws DeveloperNotificationException Falls ein Fach mit der ID nicht bekannt ist.
	 */
	public fachwahlGetName(fachwahl : GostFachwahl) : string {
		const gFach : GostFach = this._faecherManager.getOrException(fachwahl.fachID);
		const gKursart : GostKursart = GostKursart.fromID(fachwahl.kursartID);
		return gFach.kuerzelAnzeige + "-" + gKursart.kuerzel;
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID. <br>
	 * Die Fachart-ID lässt sich mit {@link GostKursart#getFachartID} berechnen.
	 *
	 * @param idFachart Die Fachart-ID berechnet aus Fach-ID und Kursart-ID.
	 *
	 * @return Die Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID.
	 */
	public fachwahlGetListeOfFachart(idFachart : number) : List<GostFachwahl> {
		return MapUtils.getOrCreateArrayList(this._map_idFachart_fachwahlen, idFachart);
	}

	/**
	 * Liefert die Anzahl verschiedenen Kursarten. Dies passiert indem über alle Fachwahlen summiert wird.
	 *
	 * @return Die Anzahl verschiedenen Kursarten.
	 */
	public fachwahlGetAnzahlVerwendeterKursarten() : number {
		const setKursartenIDs : HashSet<number> = new HashSet();
		for (const fachwahl of this._daten.fachwahlen)
			setKursartenIDs.add(fachwahl.kursartID);
		return setKursartenIDs.size();
	}

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls die Schüler Daten inkonsistent sind.
	 *
	 * @param schueler  Der Schüler, der hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	private schuelerAddOhneSortierung(schueler : Schueler) : void {
		DeveloperNotificationException.ifInvalidID("pSchueler.id", schueler.id);
		DeveloperNotificationException.ifSmaller("pSchueler.geschlecht", schueler.geschlecht, 0);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idSchueler_schueler, schueler.id, schueler);
		if (!this._map_idSchueler_fachwahlen.containsKey(schueler.id))
			this._map_idSchueler_fachwahlen.put(schueler.id, new ArrayList());
		this._daten.schueler.add(schueler);
	}

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls die Schüler Daten inkonsistent sind.
	 *
	 * @param schueler  Der Schüler, der hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	public schuelerAdd(schueler : Schueler) : void {
		this.schuelerAddOhneSortierung(schueler);
		this._daten.schueler.sort(GostBlockungsdatenManager._compSchueler);
	}

	/**
	 * Fügt alle Schüler hinzu.
	 *
	 * @param schuelermenge  Die Menge an Schülern.
	 *
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	public schuelerAddListe(schuelermenge : List<Schueler>) : void {
		for (const schueler of schuelermenge)
			this.schuelerAddOhneSortierung(schueler);
		this._daten.schueler.sort(GostBlockungsdatenManager._compSchueler);
	}

	/**
	 * Liefert die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 *
	 * @return die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 */
	public schuelerGetAnzahlMitMindestensEinerFachwahl() : number {
		const setSchuelerIDs : HashSet<number> | null = new HashSet();
		for (const fachwahl of this._daten.fachwahlen)
			setSchuelerIDs.add(fachwahl.schuelerID);
		return setSchuelerIDs.size();
	}

	/**
	 * Liefert die Anzahl an Schülern.
	 *
	 * @return Die Anzahl an Schülern.
	 */
	public schuelerGetAnzahl() : number {
		return this._daten.schueler.size();
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. <br>
	 * Wirft eine DeveloperNotificationException, falls die Schüler-ID unbekannt ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return Das zugehörige {@link Schueler}-Objekt.
	 * @throws DeveloperNotificationException  Falls die Schüler-ID unbekannt ist.
	 */
	public schuelerGet(idSchueler : number) : Schueler {
		return DeveloperNotificationException.ifNull("_map_id_schueler.get(" + idSchueler + ")", this._map_idSchueler_schueler.get(idSchueler));
	}

	/**
	 * Liefert die aktuelle Menge aller Schüler.
	 * Das ist die interne Referenz zur Liste der Schüler im {@link GostBlockungsdaten}-Objekt.
	 *
	 * @return Die aktuelle Menge aller Schüler.
	 */
	public schuelerGetListe() : List<Schueler> {
		return this._daten.schueler;
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Kursart. <br>
	 * Wirft eine Exception, falls der Schüler das Fach nicht gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches.
	 *
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostKursart}.
	 * @throws DeveloperNotificationException Falls der Schüler das Fach nicht gewählt hat.
	 */
	public schuelerGetOfFachKursart(idSchueler : number, idFach : number) : GostKursart {
		const fachwahl : GostFachwahl = this.schuelerGetOfFachFachwahl(idSchueler, idFach);
		return GostKursart.fromID(fachwahl.kursartID);
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Fachwahl. <br>
	 * Wirft eine Exception, falls der Schüler das Fach nicht gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches.
	 *
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostFachwahl}.
	 * @throws DeveloperNotificationException Falls der Schüler das Fach nicht gewählt hat.
	 */
	public schuelerGetOfFachFachwahl(idSchueler : number, idFach : number) : GostFachwahl {
		return this._map2d_idSchueler_idFach_fachwahl.getNonNullOrException(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler das entsprechende Fach gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank.ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler das entsprechende Fach gewählt hat.
	 * @throws DeveloperNotificationException Falls die Schüler-ID unbekannt ist.
	 */
	public schuelerGetHatFach(idSchueler : number, idFach : number) : boolean {
		return this._map2d_idSchueler_idFach_fachwahl.contains(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls beide Schüler bezogen auf das Fach die selbe Kursart haben oder eine Exception, falls zum Fach keine Fachwahl existiert.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 * @param idFach       Die Datenbank-ID des Faches
	 *
	 * @return TRUE, falls beide Schüler bezogen auf das Fach die selbe Kursart haben oder eine Exception, falls zum Fach keine Fachwahl existiert.
	 */
	public schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1 : number, idSchueler2 : number, idFach : number) : boolean {
		const fachwahl1 : GostFachwahl = this._map2d_idSchueler_idFach_fachwahl.getNonNullOrException(idSchueler1, idFach);
		const fachwahl2 : GostFachwahl = this._map2d_idSchueler_idFach_fachwahl.getNonNullOrException(idSchueler2, idFach);
		return fachwahl1.kursartID === fachwahl2.kursartID;
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 *
	 * @param idSchueler  Die Datenbank.ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param idKursart   Die Datenbank-ID der Kursart der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 * @throws DeveloperNotificationException Falls die Schüler-ID unbekannt ist.
	 */
	public schuelerGetHatFachart(idSchueler : number, idFach : number, idKursart : number) : boolean {
		if (!this._map2d_idSchueler_idFach_fachwahl.contains(idSchueler, idFach))
			return false;
		return this._map2d_idSchueler_idFach_fachwahl.getNonNullOrException(idSchueler, idFach).kursartID === idKursart;
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} des Schülers.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller {@link GostFachwahl} des Schülers.
	 * @throws DeveloperNotificationException Falls die Schüler-ID unbekannt ist.
	 */
	public schuelerGetListeOfFachwahlen(pSchuelerID : number) : List<GostFachwahl> {
		return DeveloperNotificationException.ifNull("_map_schuelerID_fachwahlen.get(" + pSchuelerID + ")", this._map_idSchueler_fachwahlen.get(pSchuelerID));
	}

	/**
	 * Liefert TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS} im angegebenen Kurs verboten ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS} im angegebenen Kurs verboten ist.
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public schuelerGetIstVerbotenInKurs(idSchueler : number, idKurs : number) : boolean {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs]);
		return this._map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs verbietet.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche den Schüler in einem Kurs verbietet.
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public schuelerGetRegelVerbotenInKurs(idSchueler : number, idKurs : number) : GostBlockungRegel {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs]);
		return DeveloperNotificationException.ifNull("Schüler " + idSchueler + " ist nicht verboten in Kurs " + idKurs + "!", this._map_multikey_regeln.get(key));
	}

	/**
	 * Liefert TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} im angegebenen Kurs fixiert ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} im angegebenen Kurs fixiert ist.
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public schuelerGetIstFixiertInKurs(idSchueler : number, idKurs : number) : boolean {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
		return this._map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs fixiert.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche den Schüler in einem Kurs fixiert.
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public schuelerGetRegelFixiertInKurs(idSchueler : number, idKurs : number) : GostBlockungRegel {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
		return DeveloperNotificationException.ifNull("Schüler " + idSchueler + " ist nicht fixiert in Kurs " + idKurs + "!", this._map_multikey_regeln.get(key));
	}

	/**
	 * Gibt die ID der Blockung zurück.
	 *
	 * @return die ID der Blockung
	 */
	public getID() : number {
		return this._daten.id;
	}

	/**
	 * Setzt die ID der Blockung.
	 *
	 * @param pBlockungsID die ID, welche der Blockung zugewiesen wird.
	 * @throws DeveloperNotificationException Falls die übergebene ID ungültig bzw. negativ ist.
	 */
	public setID(pBlockungsID : number) : void {
		DeveloperNotificationException.ifInvalidID("pBlockungsID", pBlockungsID);
		this._daten.id = pBlockungsID;
	}

	/**
	 * Liefert die maximale Blockungszeit in Millisekunden.
	 *
	 * @return Die maximale Blockungszeit in Millisekunden.
	 */
	public getMaxTimeMillis() : number {
		return this._maxTimeMillis;
	}

	/**
	 * Setzt die maximale Blockungszeit in Millisekunden.
	 *
	 * @param pZeit die maximale Blockungszeit in Millisekunden.
	 */
	public setMaxTimeMillis(pZeit : number) : void {
		this._maxTimeMillis = pZeit;
	}

	/**
	 * Gibt den Namen der Blockung zurück.
	 *
	 * @return der Name der Blockung
	 */
	public getName() : string {
		return this._daten.name;
	}

	/**
	 * Setzt den Namen der Blockung
	 *
	 * @param pName der Name, welcher der Blockung zugewiesen wird.
	 * @throws UserNotificationException Falls der übergebene String leer ist.
	 */
	public setName(pName : string) : void {
		UserNotificationException.ifTrue("Ein leerer Name ist für die Blockung nicht zulässig.", JavaObject.equalsTranspiler("", (pName)));
		this._daten.name = pName;
	}

	/**
	 * Gibt das Halbjahr der gymnasialen Oberstufe zurück, für welches die Blockung angelegt wurde.
	 *
	 * @return das Halbjahr der gymnasialen Oberstufe
	 */
	public getHalbjahr() : GostHalbjahr {
		return GostHalbjahr.fromIDorException(this._daten.gostHalbjahr);
	}

	/**
	 * Setzt das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 *
	 * @param pHalbjahr das Halbjahr der gymnasialen Oberstufe
	 */
	public setHalbjahr(pHalbjahr : GostHalbjahr) : void {
		this._daten.gostHalbjahr = pHalbjahr.id;
	}

	/**
	 * Liefert TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 *
	 * @return TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 */
	public getIstBlockungsVorlage() : boolean {
		return this._daten.ergebnisse.size() === 1;
	}

	/**
	 * Liefert die Anzahl an Fächern.
	 *
	 * @return Die Anzahl an Fächern.
	 */
	public getFaecherAnzahl() : number {
		return this._faecherManager.faecher().size();
	}

	/**
	 * Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 *
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager})
	 */
	public faecherManager() : GostFaecherManager {
		return this._faecherManager;
	}

	/**
	 * Gibt die Blockungsdaten zurück.
	 *
	 * @return die Blockungsdaten (siehe {@link GostBlockungsdaten})
	 */
	public daten() : GostBlockungsdaten {
		return this._daten;
	}

	/**
	 * Liefert den Kurs-Comparator der nach (KURSART, FACH, KURSNUMMER) sortiert.
	 *
	 * @return den Kurs-Comparator der nach (KURSART, FACH, KURSNUMMER) sortiert.
	 */
	public getComparatorKurs_kursart_fach_kursnummer() : Comparator<GostBlockungKurs> {
		return this._compKurs_kursart_fach_kursnummer;
	}

	/**
	 * Liefert den Kurs-Comparator der nach (FACH, KURSART, KURSNUMMER) sortiert.
	 *
	 * @return den Kurs-Comparator der nach (FACH, KURSART, KURSNUMMER) sortiert.
	 */
	public getComparatorKurs_fach_kursart_kursnummer() : Comparator<GostBlockungKurs> {
		return this._compKurs_fach_kursart_kursnummer;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostBlockungsdatenManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostBlockungsdatenManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostBlockungsdatenManager(obj : unknown) : GostBlockungsdatenManager {
	return obj as GostBlockungsdatenManager;
}
