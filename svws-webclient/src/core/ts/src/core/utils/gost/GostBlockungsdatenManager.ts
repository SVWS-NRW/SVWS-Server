import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisListeneintrag } from '../../../core/data/gost/GostBlockungsergebnisListeneintrag';
import { GostFaecherManager, cast_de_svws_nrw_core_utils_gost_GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import { Comparator } from '../../../java/util/Comparator';
import { GostKursblockungRegelTyp } from '../../../core/types/kursblockung/GostKursblockungRegelTyp';
import { JavaFunction } from '../../../java/util/function/JavaFunction';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { JavaIterator } from '../../../java/util/JavaIterator';
import { List } from '../../../java/util/List';
import { GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';
import { HashSet } from '../../../java/util/HashSet';
import { GostFach } from '../../../core/data/gost/GostFach';
import { GostBlockungKursLehrer } from '../../../core/data/gost/GostBlockungKursLehrer';
import { GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { GostBlockungsergebnis } from '../../../core/data/gost/GostBlockungsergebnis';
import { GostBlockungsdaten, cast_de_svws_nrw_core_data_gost_GostBlockungsdaten } from '../../../core/data/gost/GostBlockungsdaten';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { JavaLong } from '../../../java/lang/JavaLong';
import { GostBlockungsergebnisComparator } from '../../../core/utils/gost/GostBlockungsergebnisComparator';
import { UserNotificationException } from '../../../core/exceptions/UserNotificationException';

export class GostBlockungsdatenManager extends JavaObject {

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
	 * Die Blockungsdaten, die im Manager vorhanden sind.
	 */
	private readonly _daten : GostBlockungsdaten;

	/**
	 * Der Fächermanager mit den Fächern der gymnasialen Oberstufe.
	 */
	private readonly _faecherManager : GostFaecherManager;

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Kurse anhand ihrer Datenbank-ID.
	 */
	private readonly _mapKurse : HashMap<number, GostBlockungKurs> = new HashMap();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Schienen anhand ihrer Datenbank-ID.
	 */
	private readonly _mapSchienen : HashMap<number, GostBlockungSchiene> = new HashMap();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Regeln anhand ihrer Datenbank-ID.
	 */
	private readonly _mapRegeln : HashMap<number, GostBlockungRegel> = new HashMap();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Schueler anhand ihrer Datenbank-ID.
	 */
	private readonly _map_id_schueler : HashMap<number, Schueler> = new HashMap();

	/**
	 * Schüler-ID --> List<Fachwahl> = Die Fachwahlen des Schülers der jeweiligen Fachart.
	 */
	private readonly _map_schuelerID_fachwahlen : HashMap<number, List<GostFachwahl>> = new HashMap();

	/**
	 * Fachart-ID --> List<Fachwahl> = Die Fachwahlen einer Fachart.
	 */
	private readonly _map_fachartID_fachwahlen : HashMap<number, List<GostFachwahl>> = new HashMap();

	/**
	 * Schüler-ID --> Fach-ID --> Kursart = Die Fachwahl des Schülers die dem Fach die Kursart zuordnet.
	 */
	private readonly _map_schulerID_fachID_fachwahl : HashMap<number, HashMap<number, GostFachwahl>> = new HashMap();

	/**
	 * Ergebnis-ID --> {@link GostBlockungsergebnisListeneintrag}
	 */
	private readonly _mapErgebnis : HashMap<number, GostBlockungsergebnisListeneintrag> = new HashMap();

	/**
	 * Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER).
	 */
	private readonly _compKurs_fach_kursart_kursnummer : Comparator<GostBlockungKurs>;

	/**
	 * Eine sortierte, gecachte Menge der Kurse nach: (FACH, KURSART, KURSNUMMER).
	 */
	private readonly _kurse_sortiert_fach_kursart_kursnummer : List<GostBlockungKurs> = new ArrayList();

	/**
	 * Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER)
	 */
	private readonly _compKurs_kursart_fach_kursnummer : Comparator<GostBlockungKurs>;

	/**
	 * Eine sortierte, gecachte Menge der Kurse nach: (KURSART, FACH, KURSNUMMER)
	 */
	private readonly _kurse_sortiert_kursart_fach_kursnummer : List<GostBlockungKurs> = new ArrayList();

	/**
	 * Ein Comparator für die Fachwahlen (SCHÜLERID, FACH, KURSART)
	 */
	private readonly _compFachwahlen : Comparator<GostFachwahl>;

	/**
	 * Ein Comparator für die {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung.
	 */
	private readonly _compErgebnisse : Comparator<GostBlockungsergebnisListeneintrag> = new GostBlockungsergebnisComparator();

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
	 * @param pDaten          die Blockungsdaten
	 * @param pFaecherManager der Fächer-Manager
	 */
	public constructor(pDaten : GostBlockungsdaten, pFaecherManager : GostFaecherManager);

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
			const pDaten : GostBlockungsdaten = cast_de_svws_nrw_core_data_gost_GostBlockungsdaten(__param0);
			const pFaecherManager : GostFaecherManager = cast_de_svws_nrw_core_utils_gost_GostFaecherManager(__param1);
			this._faecherManager = pFaecherManager;
			this._compKurs_fach_kursart_kursnummer = this.createComparatorKursFachKursartNummer();
			this._compKurs_kursart_fach_kursnummer = this.createComparatorKursKursartFachNummer();
			this._compFachwahlen = this.createComparatorFachwahlen();
			this._daten = new GostBlockungsdaten();
			this._daten.id = pDaten.id;
			this._daten.name = pDaten.name;
			this._daten.abijahrgang = pDaten.abijahrgang;
			this._daten.gostHalbjahr = pDaten.gostHalbjahr;
			this._daten.istAktiv = pDaten.istAktiv;
			this.addSchienListe(pDaten.schienen);
			this.addRegelListe(pDaten.regeln);
			this.addKursListe(pDaten.kurse);
			this.addSchuelerListe(pDaten.schueler);
			this.addFachwahlListe(pDaten.fachwahlen);
			this.addErgebnisListe(pDaten.ergebnisse);
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

	/**
	 * Liefert TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 *
	 * @return TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 */
	public getIstBlockungsVorlage() : boolean {
		return this._daten.ergebnisse.size() === 1;
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Den Wert des 1. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public getOfBewertung1Wert(pErgebnisID : number) : number {
		const e : GostBlockungsergebnisListeneintrag = this.getErgebnis(pErgebnisID);
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
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public getOfBewertung1Intervall(pErgebnisID : number) : number {
		const summe : number = this.getOfBewertung1Wert(pErgebnisID);
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Den Wert des 2. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public getOfBewertung2Wert(pErgebnisID : number) : number {
		const e : GostBlockungsergebnisListeneintrag = this.getErgebnis(pErgebnisID);
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
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public getOfBewertung2Intervall(pErgebnisID : number) : number {
		const summe : number = this.getOfBewertung2Wert(pErgebnisID);
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Den Wert des 3. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public getOfBewertung3Wert(pErgebnisID : number) : number {
		const e : GostBlockungsergebnisListeneintrag = this.getErgebnis(pErgebnisID);
		return e.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public getOfBewertung3Intervall(pErgebnisID : number) : number {
		let wert : number = this.getOfBewertung3Wert(pErgebnisID);
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
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Den Wert des 4. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public getOfBewertung4Wert(pErgebnisID : number) : number {
		const e : GostBlockungsergebnisListeneintrag = this.getErgebnis(pErgebnisID);
		return e.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public getOfBewertung4Intervall(pErgebnisID : number) : number {
		const wert : number = this.getOfBewertung4Wert(pErgebnisID);
		return 1 - 1 / (0.25 * wert + 1);
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
	 * Liefert die Anzahl an Fächern.
	 *
	 * @return Die Anzahl an Fächern.
	 */
	public getFaecherAnzahl() : number {
		return this._faecherManager.faecher().size();
	}

	/**
	 * Liefert die Anzahl verschiedenen Kursarten. Dies passiert indem über alle Fachwahlen summiert wird.
	 *
	 * @return Die Anzahl verschiedenen Kursarten.
	 */
	public getKursartenAnzahl() : number {
		const setKursartenIDs : HashSet<number> = new HashSet();
		for (const fachwahl of this._daten.fachwahlen)
			setKursartenIDs.add(fachwahl.kursartID);
		return setKursartenIDs.size();
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

	private addErgebnisOhneSortierung(pErgebnis : GostBlockungsergebnisListeneintrag) : void {
		DeveloperNotificationException.ifInvalidID("pErgebnis.id", pErgebnis.id);
		DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", pErgebnis.blockungID);
		DeveloperNotificationException.ifNull("GostHalbjahr.fromID(" + pErgebnis.gostHalbjahr + ")", GostHalbjahr.fromID(pErgebnis.gostHalbjahr));
		DeveloperNotificationException.ifMapContains("_mapErgebnis", this._mapErgebnis, pErgebnis.id);
		this._daten.ergebnisse.add(pErgebnis);
		this._mapErgebnis.put(pErgebnis.id, pErgebnis);
	}

	/**
	 * Fügt das übergebenen Ergebnis der Blockung hinzu.
	 *
	 * @param pErgebnis Das {@link GostBlockungsergebnisListeneintrag}-Objekt, welches hinzugefügt wird.
	 * @throws DeveloperNotificationException Falls in den Daten des Listeneintrags Inkonsistenzen sind.
	 */
	public addErgebnis(pErgebnis : GostBlockungsergebnisListeneintrag) : void {
		this.addErgebnisOhneSortierung(pErgebnis);
		this._daten.ergebnisse.sort(this._compErgebnisse);
	}

	/**
	 * Fügt die Menge an Ergebnissen {@link GostBlockungsergebnisListeneintrag} hinzu.
	 *
	 * @param pErgebnisse Die Menge an Ergebnissen.
	 * @throws DeveloperNotificationException Falls in den Daten der Listeneinträge Inkonsistenzen sind.
	 */
	public addErgebnisListe(pErgebnisse : List<GostBlockungsergebnisListeneintrag>) : void {
		for (const ergebnis of pErgebnisse)
			this.addErgebnisOhneSortierung(ergebnis);
		this._daten.ergebnisse.sort(this._compErgebnisse);
	}

	/**
	 * Liefert einen {@link GostBlockungsergebnisListeneintrag} aus der Liste der Ergebnisse.
	 * Wirft eine Exception, falls es keinen Listeneintrag mit dieser ID gibt.
	 *
	 * @param pErgebnisID  Die Datenbank-ID des Ergebnisses.
	 * @return einen {@link GostBlockungsergebnisListeneintrag} aus der Liste der Ergebnisse.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public getErgebnis(pErgebnisID : number) : GostBlockungsergebnisListeneintrag {
		const e : GostBlockungsergebnisListeneintrag | null = this._mapErgebnis.get(pErgebnisID);
		return DeveloperNotificationException.ifNull("Es wurde kein Listeneintrag mit ID(" + pErgebnisID + ") gefunden!", e);
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung.
	 *
	 * @return Eine sortierte Menge der {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung.
	 */
	public getErgebnisseSortiertNachBewertung() : List<GostBlockungsergebnisListeneintrag> {
		return this._daten.ergebnisse;
	}

	/**
	 * Entfernt das Ergebnis mit der übergebenen ID aus der Blockung.
	 *
	 * @param pErgebnisID Die Datenbank-ID des zu entfernenden Ergebnisses.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public removeErgebnisByID(pErgebnisID : number) : void {
		const e : GostBlockungsergebnisListeneintrag = this.getErgebnis(pErgebnisID);
		this._daten.ergebnisse.remove(e);
		this._mapErgebnis.remove(pErgebnisID);
	}

	/**
	 * Entfernt das übergebenen Ergebnis aus der Blockung.
	 *
	 * @param pErgebnis Das zu entfernende Ergebnis.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public removeErgebnis(pErgebnis : GostBlockungsergebnisListeneintrag) : void {
		this.removeErgebnisByID(pErgebnis.id);
	}

	/**
	 * Aktualisiert die Bewertung im {@link GostBlockungsdatenManager} mit der aus dem {@link GostBlockungsergebnis}. <br>
	 * Wirft eine Exception, falls kein  {@link GostBlockungsergebnisListeneintrag} mit der ID gefunden wurde.
	 *
	 * @param pErgebnis Das Ergebnis mit der neuen Bewertung.
	 * @throws DeveloperNotificationException Falls kein  {@link GostBlockungsergebnisListeneintrag} mit der ID gefunden wurde.
	 */
	public updateErgebnisBewertung(pErgebnis : GostBlockungsergebnis) : void {
		DeveloperNotificationException.ifInvalidID("pErgebnis.id", pErgebnis.id);
		DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", pErgebnis.blockungID);
		for (const eintrag of this._daten.ergebnisse)
			if (eintrag.id === pErgebnis.id)
				eintrag.bewertung = pErgebnis.bewertung;
		this._daten.ergebnisse.sort(this._compErgebnisse);
	}

	private addKursOhneSortierung(pKurs : GostBlockungKurs) : void {
		const nSchienen : number = this.getSchienenAnzahl();
		DeveloperNotificationException.ifInvalidID("pKurs.id", pKurs.id);
		DeveloperNotificationException.ifMapContains("_mapKurse", this._mapKurse, pKurs.id);
		DeveloperNotificationException.ifNull("_faecherManager.get(pKurs.fach_id)", this._faecherManager.get(pKurs.fach_id));
		DeveloperNotificationException.ifNull("GostKursart.fromIDorNull(pKurs.kursart)", GostKursart.fromIDorNull(pKurs.kursart));
		DeveloperNotificationException.ifSmaller("pKurs.wochenstunden", pKurs.wochenstunden, 0);
		DeveloperNotificationException.ifSmaller("pKurs.anzahlSchienen", pKurs.anzahlSchienen, 1);
		DeveloperNotificationException.ifGreater("pKurs.anzahlSchienen", pKurs.anzahlSchienen, nSchienen);
		DeveloperNotificationException.ifSmaller("pKurs.nummer", pKurs.nummer, 1);
		this._daten.kurse.add(pKurs);
		this._mapKurse.put(pKurs.id, pKurs);
		this._kurse_sortiert_fach_kursart_kursnummer.add(pKurs);
		this._kurse_sortiert_kursart_fach_kursnummer.add(pKurs);
	}

	/**
	 * Fügt den übergebenen Kurs zu der Blockung hinzu.
	 *
	 * @param pKurs Das {@link GostBlockungKurs}-Objekt, welches hinzugefügt wird.
	 * @throws DeveloperNotificationException Falls die Daten des Kurses inkonsistent sind.
	 */
	public addKurs(pKurs : GostBlockungKurs) : void {
		this.addKursOhneSortierung(pKurs);
		this._kurse_sortiert_fach_kursart_kursnummer.sort(this._compKurs_fach_kursart_kursnummer);
		this._kurse_sortiert_kursart_fach_kursnummer.sort(this._compKurs_kursart_fach_kursnummer);
	}

	/**
	 * Fügt die Menge an Kursen hinzu.
	 *
	 * @param pKurse Die Menge an Kursen.
	 * @throws DeveloperNotificationException Falls die Daten der Kurse inkonsistent sind.
	 */
	public addKursListe(pKurse : List<GostBlockungKurs>) : void {
		for (const gKurs of pKurse)
			this.addKursOhneSortierung(gKurs);
		this._kurse_sortiert_fach_kursart_kursnummer.sort(this._compKurs_fach_kursart_kursnummer);
		this._kurse_sortiert_kursart_fach_kursnummer.sort(this._compKurs_kursart_fach_kursnummer);
	}

	/**
	 * Gibt den Kurs der Blockung anhand von dessen ID zurück.
	 *
	 * @param  pKursID die ID des Kurses
	 * @return Das zugehörige {@link GostBlockungKurs} Objekt.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public getKurs(pKursID : number) : GostBlockungKurs {
		return DeveloperNotificationException.ifNull("_mapKurse.get(" + pKursID + ")", this._mapKurse.get(pKursID));
	}

	/**
	 * Liefert TRUE, falls der Kurs mit der übergebenen ID existiert.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @return TRUE, falls der Kurs mit der übergebenen ID existiert.
	 */
	public getKursExistiert(pKursID : number) : boolean {
		const kurs : GostBlockungKurs | null = this._mapKurse.get(pKursID);
		return kurs !== null;
	}

	/**
	 * Liefert die Anzahl an Kursen.
	 *
	 * @return Die Anzahl an Kursen.
	 */
	public getKursAnzahl() : number {
		return this._mapKurse.size();
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][Suffix], beispielsweise D-GK1.
	 *
	 * @param pKursID  Die Datenbank-ID des Kurses.
	 * @return Den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][Suffix], beispielsweise D-GK1.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public getNameOfKurs(pKursID : number) : string {
		const kurs : GostBlockungKurs = this.getKurs(pKursID);
		const gFach : GostFach = this._faecherManager.getOrException(kurs.fach_id);
		const sSuffix : string = JavaObject.equalsTranspiler(kurs.suffix, ("")) ? "" : ("-" + kurs.suffix);
		return gFach.kuerzelAnzeige + "-" + GostKursart.fromID(kurs.kursart).kuerzel + kurs.nummer + sSuffix!;
	}

	/**
	 * Liefert den Namen (Fach-Kursart) der Fachwahl.
	 *
	 * @param pFachwahl Das Fachwahl-Objekt.
	 * @return Den Namen (Fach-Kursart) der Fachwahl.
	 * @throws DeveloperNotificationException Falls ein Fach mit der ID nicht bekannt ist.
	 */
	public getNameOfFachwahl(pFachwahl : GostFachwahl) : string {
		const gFach : GostFach = this._faecherManager.getOrException(pFachwahl.fachID);
		const gKursart : GostKursart = GostKursart.fromID(pFachwahl.kursartID);
		return gFach.kuerzelAnzeige + "-" + gKursart.kuerzel;
	}

	/**
	 * Setzt den Suffix des Kurses.
	 *
	 * @param  pKursID  Die Datenbank-ID des Kurses.
	 * @param  pSuffix  Der neue Suffix des Kurses.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public setSuffixOfKurs(pKursID : number, pSuffix : string) : void {
		const kurs : GostBlockungKurs = this.getKurs(pKursID);
		kurs.suffix = pSuffix;
	}

	/**
	 * Liefert eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public getKursmengeSortiertNachFachKursartNummer() : List<GostBlockungKurs> {
		return this._kurse_sortiert_fach_kursart_kursnummer;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public getKursmengeSortiertNachKursartFachNummer() : List<GostBlockungKurs> {
		return this._kurse_sortiert_kursart_fach_kursnummer;
	}

	/**
	 * Liefert TRUE, falls ein Löschen des Kurses erlaubt ist. <br>
	 * Kriterium: Der Kurs muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param  pKursID               Die Datenbank-ID des Kurses.
	 * @return                       TRUE, falls ein Löschen des Kurses erlaubt ist.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public removeKursAllowed(pKursID : number) : boolean {
		return (this.getKurs(pKursID) !== null) && this.getIstBlockungsVorlage();
	}

	/**
	 * Entfernt den Kurs mit der übergebenen ID aus der Blockung.
	 *
	 * @param pKursID Die Datenbank-ID des zu entfernenden Kurses.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 * @throws UserNotificationException Falls es sich derzeit nicht um die Blockungsvorlage handelt.
	 */
	public removeKursByID(pKursID : number) : void {
		DeveloperNotificationException.ifTrue("Ein Löschen des Kurses ist nur bei einer Blockungsvorlage erlaubt!", !this.getIstBlockungsVorlage());
		const kurs : GostBlockungKurs = this.getKurs(pKursID);
		this._kurse_sortiert_fach_kursart_kursnummer.remove(kurs);
		this._kurse_sortiert_kursart_fach_kursnummer.remove(kurs);
		this._mapKurse.remove(pKursID);
		this._daten.kurse.remove(kurs);
	}

	/**
	 * Entfernt den übergebenen Kurs aus der Blockung.
	 *
	 * @param pKurs  Der zu entfernende Kurs.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public removeKurs(pKurs : GostBlockungKurs) : void {
		this.removeKursByID(pKurs.id);
	}

	/**
	 * Liefert alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 *
	 * @param pKursID  Die Datenbank-ID des Kurses.
	 * @return Alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public getOfKursLehrkraefteSortiert(pKursID : number) : List<GostBlockungKursLehrer> {
		const kurs : GostBlockungKurs = this.getKurs(pKursID);
		return kurs.lehrer;
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene Nummer hat. <br>
	 * Wirft eine Exceptions, falls es eine solche Lehrkraft nicht gibt.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @param pReihenfolgeNr Die Lehrkraft mit der Nummer, die gesucht wird.
	 * @return Die Lehrkraft des Kurses, welche die angegebene Nummer hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public getOfKursLehrkraftMitNummer(pKursID : number, pReihenfolgeNr : number) : GostBlockungKursLehrer | null {
		for (const lehrkraft of this.getOfKursLehrkraefteSortiert(pKursID))
			if (lehrkraft.reihenfolge === pReihenfolgeNr)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + pKursID + " keine Lehrkraft mit ReihenfolgeNr. " + pReihenfolgeNr + "!")
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @param pReihenfolgeNr Die Lehrkraft mit der Nummer, die gesucht wird.
	 * @return TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public getOfKursLehrkraftMitNummerExists(pKursID : number, pReihenfolgeNr : number) : boolean {
		for (const lehrkraft of this.getOfKursLehrkraefteSortiert(pKursID))
			if (lehrkraft.reihenfolge === pReihenfolgeNr)
				return true;
		return false;
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene ID hat. <br>
	 * Wirft eine Exceptions, falls es eine solche Lehrkraft nicht gibt.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @param pLehrkraftID Die Datenbank-ID der gesuchten Lehrkraft.
	 * @return Die Lehrkraft des Kurses, welche die angegebene ID hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public getOfKursLehrkraftMitID(pKursID : number, pLehrkraftID : number) : GostBlockungKursLehrer | null {
		for (const lehrkraft of this.getOfKursLehrkraefteSortiert(pKursID))
			if (lehrkraft.id === pLehrkraftID)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + pKursID + " keine Lehrkraft mit ID " + pLehrkraftID)
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @param pLehrkraftID Die Datenbank-ID der gesuchten Lehrkraft.
	 * @return TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public getOfKursLehrkraftMitIDExists(pKursID : number, pLehrkraftID : number) : boolean {
		for (const lehrkraft of this.getOfKursLehrkraefteSortiert(pKursID))
			if (lehrkraft.id === pLehrkraftID)
				return true;
		return false;
	}

	/**
	 * Fügt die übergebene Lehrkraft zum Kurs hinzu. <br>
	 * Wirft eine DeveloperNotificationException, falls der Kurs nicht existiert oder die Lehrkraft oder die ReihenfolgeNr bereits im Kurs gibt.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @param pKursLehrer Das {@link GostBlockungKursLehrer}-Objekt.
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es die Lehrkraft oder die ReihenfolgeNr im Kurs bereits gibt.
	 */
	public patchOfKursAddLehrkraft(pKursID : number, pKursLehrer : GostBlockungKursLehrer) : void {
		const kurs : GostBlockungKurs = this.getKurs(pKursID);
		const lehrer : List<GostBlockungKursLehrer> = kurs.lehrer;
		for (const lehrkraft of lehrer) {
			DeveloperNotificationException.ifTrue("patchOfKursAddLehrkraft: Der Kurs hat bereits eine Lehrkraft mit ID " + lehrkraft.id, lehrkraft.id === pKursLehrer.id);
			DeveloperNotificationException.ifTrue("patchOfKursAddLehrkraft: Der Kurs hat bereits eine Lehrkraft mit reihenfolge " + lehrkraft.reihenfolge, lehrkraft.reihenfolge === pKursLehrer.reihenfolge);
		}
		lehrer.add(pKursLehrer);
		lehrer.sort(GostBlockungsdatenManager._compLehrkraefte);
	}

	/**
	 * Löscht aus dem übergebenen Kurs die angegebene Lehrkraft. <br>
	 * Wirft eine DeveloperNotificationException, falls der Kurs nicht existiert oder es eine solche Lehrkraft im Kurs nicht gibt.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @param pLehrkraftID Die Datenbank-ID des {@link GostBlockungKursLehrer}-Objekt.
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es eine solche Lehrkraft im Kurs nicht gibt.
	 */
	public patchOfKursRemoveLehrkraft(pKursID : number, pLehrkraftID : number) : void {
		const kurs : GostBlockungKurs = this.getKurs(pKursID);
		const lehrer : List<GostBlockungKursLehrer> = kurs.lehrer;
		for (let i : number = 0; i < lehrer.size(); i++)
			if (lehrer.get(i).id === pLehrkraftID) {
				lehrer.remove(lehrer.get(i));
				return;
			}
		throw new DeveloperNotificationException("patchOfKursRemoveLehrkraft: Kurs (" + pKursID + ") hat keine Lehrkraft (" + pLehrkraftID + ")!")
	}

	private addSchieneOhneSortierung(pSchiene : GostBlockungSchiene) : void {
		DeveloperNotificationException.ifInvalidID("GostBlockungSchiene.id", pSchiene.id);
		DeveloperNotificationException.ifTrue("GostBlockungSchiene.bezeichnung darf nicht leer sein!", JavaObject.equalsTranspiler("", (pSchiene.bezeichnung)));
		DeveloperNotificationException.ifSmaller("GostBlockungSchiene.nummer", pSchiene.nummer, 1);
		DeveloperNotificationException.ifSmaller("GostBlockungSchiene.wochenstunden", pSchiene.wochenstunden, 1);
		DeveloperNotificationException.ifMapContains("mapSchienen", this._mapSchienen, pSchiene.id);
		this._mapSchienen.put(pSchiene.id, pSchiene);
		this._daten.schienen.add(pSchiene);
	}

	/**
	 * Fügt die übergebene Schiene zu der Blockung hinzu.
	 *
	 * @param pSchiene Die hinzuzufügende Schiene.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public addSchiene(pSchiene : GostBlockungSchiene) : void {
		this.addSchieneOhneSortierung(pSchiene);
		this._daten.schienen.sort(GostBlockungsdatenManager._compSchiene);
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
	 *
	 * @param pSchienen Die Menge an Schienen.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public addSchienListe(pSchienen : List<GostBlockungSchiene>) : void {
		for (const schiene of pSchienen)
			this.addSchieneOhneSortierung(schiene);
		this._daten.schienen.sort(GostBlockungsdatenManager._compSchiene);
	}

	/**
	 * Gibt die Schiene der Blockung anhand von deren ID zurück.
	 *
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 * @return Das zugehörige {@link GostBlockungSchiene} Objekt.
	 * @throws DeveloperNotificationException Falls die Schiene nicht in der Blockung existiert.
	 */
	public getSchiene(pSchienenID : number) : GostBlockungSchiene {
		return DeveloperNotificationException.ifNull("_mapSchienen.get(" + pSchienenID + ")", this._mapSchienen.get(pSchienenID));
	}

	/**
	 * Liefert TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 *
	 * @param pSchienenID  Die Datenbank-ID der Schiene.
	 * @return TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 */
	public getSchieneExistiert(pSchienenID : number) : boolean {
		return this._mapSchienen.get(pSchienenID) !== null;
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 *
	 * @return Die Anzahl an Schienen.
	 */
	public getSchienenAnzahl() : number {
		return this._mapSchienen.size();
	}

	/**
	 * Liefert die aktuelle Menge aller Schienen.
	 * Das ist die interne Referenz zur Liste der Schienen im {@link GostBlockungsdaten}-Objekt.
	 * Diese Liste ist stets sortiert nach der Schienen-Nummer.
	 *
	 * @return Die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 */
	public getMengeOfSchienen() : List<GostBlockungSchiene> {
		return this._daten.schienen;
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist. <br>
	 * Kriterium: Die Schiene muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 * @return TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert.
	 */
	public removeSchieneAllowed(pSchienenID : number) : boolean {
		return (this.getSchiene(pSchienenID) !== null) && this.getIstBlockungsVorlage();
	}

	/**
	 * Entfernt die Schiene mit der übergebenen ID aus der Blockung.
	 * Konsequenz: <br>
	 * (1) Das Löschen der Schiene muss erlaubt sein, sonst Exception.
	 * (2) Die Schienen werden neu nummeriert. <br>
	 * (3) Die Konsistenz der sortierten Schienen muss überprüft werden. <br>
	 * (4) Die Regeln müssen bei Schienen-Nummern angepasst werden. <br>
	 *
	 * @param pSchienenID Die Datenbank-ID der zu entfernenden Schiene.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public removeSchieneByID(pSchienenID : number) : void {
		DeveloperNotificationException.ifTrue("Ein Löschen einer Schiene ist nur bei einer Blockungsvorlage erlaubt!", !this.getIstBlockungsVorlage());
		const schieneR : GostBlockungSchiene = this.getSchiene(pSchienenID);
		this._mapSchienen.remove(pSchienenID);
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
	 * @param pSchiene Die zu entfernende Schiene.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public removeSchiene(pSchiene : GostBlockungSchiene) : void {
		this.removeSchieneByID(pSchiene.id);
	}

	/**
	 * Liefert die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 *
	 * @param  pHalbjahr  Das Halbjahr, für welches die Blockung angelegt werden soll.
	 * @return Die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 */
	public static getDefaultSchienenAnzahl(pHalbjahr : GostHalbjahr) : number {
		return (pHalbjahr.id < 2) ? 13 : 11;
	}

	private addRegelOhneSortierung(pRegel : GostBlockungRegel) : void {
		DeveloperNotificationException.ifInvalidID("Regel.id", pRegel.id);
		DeveloperNotificationException.ifMapContains("_mapRegeln", this._mapRegeln, pRegel.id);
		DeveloperNotificationException.ifTrue("Der Typ(" + pRegel.typ + ") der Regel(" + pRegel.id + ") ist unbekannt!", GostKursblockungRegelTyp.fromTyp(pRegel.typ) as unknown === GostKursblockungRegelTyp.UNDEFINIERT as unknown);
		this._daten.regeln.add(pRegel);
		this._mapRegeln.put(pRegel.id, pRegel);
	}

	/**
	 * Fügt die übergebene Regel zu der Blockung hinzu.
	 *
	 * @param pRegel die hinzuzufügende Regel
	 * @throws DeveloperNotificationException Falls die Daten der Regel inkonsistent sind.
	 */
	public addRegel(pRegel : GostBlockungRegel) : void {
		this.addRegelOhneSortierung(pRegel);
		this._daten.regeln.sort(GostBlockungsdatenManager._compRegel);
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 *
	 * @param pRegeln Die Menge an Regeln.
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public addRegelListe(pRegeln : List<GostBlockungRegel>) : void {
		for (const regel of pRegeln)
			this.addRegelOhneSortierung(regel);
		this._daten.regeln.sort(GostBlockungsdatenManager._compRegel);
	}

	/**
	 * Gibt die Regel der Blockung anhand von deren ID zurück.
	 *
	 * @param  pRegelID Die Datenbank-ID der Regel.
	 * @return Das {@link GostBlockungRegel} Objekt.
	 * @throws DeveloperNotificationException Falls die Regel nicht in der Blockung existiert.
	 */
	public getRegel(pRegelID : number) : GostBlockungRegel {
		return DeveloperNotificationException.ifNull("_mapRegeln.get(" + pRegelID + ")", this._mapRegeln.get(pRegelID));
	}

	/**
	 * Liefert TRUE, falls die Regel mit der übergebenen ID existiert.
	 *
	 * @param pRegelID Die Datenbank-ID der Regel.
	 * @return TRUE, falls die Regel mit der übergebenen ID existiert.
	 */
	public getRegelExistiert(pRegelID : number) : boolean {
		return this._mapRegeln.get(pRegelID) !== null;
	}

	/**
	 * Liefert die Anzahl an Regeln.
	 *
	 * @return Die Anzahl an Regeln.
	 */
	public getRegelAnzahl() : number {
		return this._mapRegeln.size();
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln.
	 * Das ist die interne Referenz zur Liste der Regeln im {@link GostBlockungsdaten}-Objekt.
	 * Diese Liste ist stets sortiert nach (TYP, ID).
	 *
	 * @return Die aktuelle Menge aller Regeln sortiert nach (TYP, id).
	 */
	public getMengeOfRegeln() : List<GostBlockungRegel> {
		return this._daten.regeln;
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Regel erlaubt ist. <br>
	 * Kriterium: Die Regel muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param  pRegelID Die Datenbank-ID der Regel.
	 * @return TRUE, falls ein Löschen der Regel erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public removeRegelAllowed(pRegelID : number) : boolean {
		return (this.getRegel(pRegelID) !== null) && this.getIstBlockungsVorlage();
	}

	/**
	 * Entfernt die Regel mit der übergebenen ID aus der Blockung.
	 * Wirft eine Exception, falls es sich nicht um eine Blockungsvorlage handelt.
	 *
	 * @param pRegelID Die Datenbank-ID der zu entfernenden Regel.
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 * @throws UserNotificationException Falls es sich nicht um eine Blockungsvorlage handelt.
	 */
	public removeRegelByID(pRegelID : number) : void {
		UserNotificationException.ifTrue("Ein Löschen einer Regel ist nur bei einer Blockungsvorlage erlaubt!", !this.getIstBlockungsVorlage());
		const regel : GostBlockungRegel = this.getRegel(pRegelID);
		this._mapRegeln.remove(pRegelID);
		this._daten.regeln.remove(regel);
	}

	/**
	 * Entfernt die übergebene Regel aus der Blockung.
	 *
	 * @param pRegel die zu entfernende Regel
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 * @throws UserNotificationException Falls es sich nicht um eine Blockungsvorlage handelt.
	 */
	public removeRegel(pRegel : GostBlockungRegel) : void {
		this.removeRegelByID(pRegel.id);
	}

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls die Schüler Daten inkonsistent sind.
	 *
	 * @param pSchueler Der Schüler, der hinzugefügt wird.
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	public addSchueler(pSchueler : Schueler) : void {
		DeveloperNotificationException.ifInvalidID("pSchueler.id", pSchueler.id);
		DeveloperNotificationException.ifMapContains("_map_id_schueler", this._map_id_schueler, pSchueler.id);
		DeveloperNotificationException.ifSmaller("pSchueler.geschlecht", pSchueler.geschlecht, 0);
		this._daten.schueler.add(pSchueler);
		this._map_id_schueler.put(pSchueler.id, pSchueler);
		if (!this._map_schuelerID_fachwahlen.containsKey(pSchueler.id))
			this._map_schuelerID_fachwahlen.put(pSchueler.id, new ArrayList());
		if (!this._map_schulerID_fachID_fachwahl.containsKey(pSchueler.id))
			this._map_schulerID_fachID_fachwahl.put(pSchueler.id, new HashMap());
	}

	/**
	 * Fügt alle Schüler hinzu.
	 *
	 * @param pSchueler Die Menge an Schülern.
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	public addSchuelerListe(pSchueler : List<Schueler>) : void {
		for (const schueler of pSchueler)
			this.addSchueler(schueler);
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. <br>
	 * Wirft eine DeveloperNotificationException, falls die Schüler-ID unbekannt ist.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Das zugehörige {@link Schueler}-Objekt.
	 * @throws DeveloperNotificationException  Falls die Schüler-ID unbekannt ist.
	 */
	public getSchueler(pSchuelerID : number) : Schueler {
		return DeveloperNotificationException.ifNull("_map_id_schueler.get(" + pSchuelerID + ")", this._map_id_schueler.get(pSchuelerID));
	}

	/**
	 * Liefert die aktuelle Menge aller Schüler.
	 * Das ist die interne Referenz zur Liste der Schüler im {@link GostBlockungsdaten}-Objekt.
	 *
	 * @return Die aktuelle Menge aller Schüler.
	 */
	public getMengeOfSchueler() : List<Schueler> {
		return this._daten.schueler;
	}

	/**
	 * Liefert die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 *
	 * @return die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 */
	public getSchuelerAnzahlMitFachwahlen() : number {
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
	public getSchuelerAnzahl() : number {
		return this._daten.schueler.size();
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Kursart. <br>
	 * Wirft eine Exception, falls der Schüler das Fach nicht gewählt hat.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @param pFachID Die Datenbank-ID des Faches.
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostKursart}.
	 * @throws DeveloperNotificationException Falls der Schüler das Fach nicht gewählt hat.
	 */
	public getOfSchuelerOfFachKursart(pSchuelerID : number, pFachID : number) : GostKursart {
		const fachwahl : GostFachwahl = this.getOfSchuelerOfFachFachwahl(pSchuelerID, pFachID);
		return GostKursart.fromID(fachwahl.kursartID);
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Fachwahl. <br>
	 * Wirft eine Exception, falls der Schüler das Fach nicht gewählt hat.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @param pFachID Die Datenbank-ID des Faches.
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostFachwahl}.
	 * @throws DeveloperNotificationException Falls der Schüler das Fach nicht gewählt hat.
	 */
	public getOfSchuelerOfFachFachwahl(pSchuelerID : number, pFachID : number) : GostFachwahl {
		const mapFachFachwahl : HashMap<number, GostFachwahl> = DeveloperNotificationException.ifNull("_map_schulerID_fachID_fachwahl.get(" + pSchuelerID + ")", this._map_schulerID_fachID_fachwahl.get(pSchuelerID));
		return DeveloperNotificationException.ifNull("mapFachFachwahl.get(" + pFachID + ")", mapFachFachwahl.get(pFachID));
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} des Schülers.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller {@link GostFachwahl} des Schülers.
	 * @throws DeveloperNotificationException Falls die Schüler-ID unbekannt ist.
	 */
	public getOfSchuelerFacharten(pSchuelerID : number) : List<GostFachwahl> {
		return DeveloperNotificationException.ifNull("_map_schuelerID_fachwahlen.get(" + pSchuelerID + ")", this._map_schuelerID_fachwahlen.get(pSchuelerID));
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 *
	 * @param pSchuelerID Die Datenbank.ID des Schülers.
	 * @param pFach Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param pKursart Die Datenbank-ID der Kursart der Fachwahl des Schülers.
	 * @return TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 * @throws DeveloperNotificationException Falls die Schüler-ID unbekannt ist.
	 */
	public getOfSchuelerHatFachart(pSchuelerID : number, pFach : number, pKursart : number) : boolean {
		const map : HashMap<number, GostFachwahl> = DeveloperNotificationException.ifNull("_map_schulerID_fachID_fachwahl.get(" + pSchuelerID + ")", this._map_schulerID_fachID_fachwahl.get(pSchuelerID));
		const wahl : GostFachwahl | null = map.get(pFach);
		return (wahl !== null) && (wahl.kursartID === pKursart);
	}

	/**
	 * Fügt eine Fachwahl hinzu.
	 * Wirft eine Exception, falls die Fachwahl-Daten inkonsistent sind.
	 *
	 * @param pFachwahl Die Fachwahl, die hinzugefügt wird.
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public addFachwahl(pFachwahl : GostFachwahl) : void {
		let mapSFA : HashMap<number, GostFachwahl> | null = this._map_schulerID_fachID_fachwahl.get(pFachwahl.schuelerID);
		if (mapSFA === null) {
			mapSFA = new HashMap();
			this._map_schulerID_fachID_fachwahl.put(pFachwahl.schuelerID, mapSFA);
		}
		const fachID : number = pFachwahl.fachID;
		if (mapSFA.put(fachID, pFachwahl) !== null)
			throw new DeveloperNotificationException("Schüler-ID (" + pFachwahl.schuelerID + "), (Fach-ID" + fachID + ") doppelt!")
		let fachwahlenDesSchuelers : List<GostFachwahl> | null = this._map_schuelerID_fachwahlen.get(pFachwahl.schuelerID);
		if (fachwahlenDesSchuelers === null) {
			fachwahlenDesSchuelers = new ArrayList();
			this._map_schuelerID_fachwahlen.put(pFachwahl.schuelerID, fachwahlenDesSchuelers);
		}
		fachwahlenDesSchuelers.add(pFachwahl);
		fachwahlenDesSchuelers.sort(this._compFachwahlen);
		const fachartID : number = GostKursart.getFachartIDByFachwahl(pFachwahl);
		this.getOfFachartMengeFachwahlen(fachartID).add(pFachwahl);
		this._daten.fachwahlen.add(pFachwahl);
	}

	/**
	 * Fügt alle Fachwahlen hinzu.
	 *
	 * @param pFachwahlen Die Menge an Fachwahlen.
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public addFachwahlListe(pFachwahlen : List<GostFachwahl>) : void {
		for (const gFachwahl of pFachwahlen)
			this.addFachwahl(gFachwahl);
	}

	/**
	 * Liefert die Anzahl an Fachwahlen.
	 *
	 * @return Die Anzahl an Fachwahlen.
	 */
	public getFachwahlAnzahl() : number {
		return this._daten.fachwahlen.size();
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID. <br>
	 * Die Fachart-ID lässt sich mit {@link GostKursart#getFachartID} berechnen. <br>
	 *
	 * @param pFachartID Die Fachart-ID berechnet aus Fach-ID und Kursart-ID.
	 * @return Die Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID.
	 */
	public getOfFachartMengeFachwahlen(pFachartID : number) : List<GostFachwahl> {
		const itemCreator : JavaFunction<number, List<GostFachwahl>> = { apply : (key: number) => new ArrayList() };
		const result : List<GostFachwahl> | null = this._map_fachartID_fachwahlen.computeIfAbsent(pFachartID, itemCreator);
		if (result === null)
			return new ArrayList();
		return result;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostBlockungsdatenManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostBlockungsdatenManager(obj : unknown) : GostBlockungsdatenManager {
	return obj as GostBlockungsdatenManager;
}
