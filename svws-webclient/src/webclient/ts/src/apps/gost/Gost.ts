import { ZulaessigesFach, GostStatistikFachwahl, List } from "@svws-nrw/svws-core-ts";

import { App } from "../BaseApp";

import { DataGostJahrgang } from "./DataGostJahrgang";
import { DataGostSchuelerFachwahlen } from "./DataGostSchuelerFachwahlen";
import { DataGostFaecher } from "./DataGostFaecher";
import { DataGostKursblockung } from "./DataGostKursblockung";
import { ListGost } from "./ListGost";
import { ListKursblockungen } from "./ListKursblockungen";
import { DataGostKursblockungsergebnis } from "./DataGostKursblockungsergebnis";
import { ListKursblockungsergebnisse } from "./ListKursblockungsergebnisse";
import { ListAbiturjahrgangSchueler } from "./ListAbiturjahrgangSchueler";
import { DataSchuelerLaufbahndaten } from "./DataSchuelerLaufbahnplanung";
import { FeedbackValues, ApiStatus } from "./userfeedback";
import { reactive } from "vue";

/**
 * Diese Klasse enthält den Abiturjahrgangsspezifischen Teil der gymnasialen
 * Oberstufe des SVWS-Client-Applikation. Beim Erstellen der
 * SVWS-Client-Applikation wird ein Objekt dieser Klasse erzeugt und steht unter
 * "this.$app.apps.gost" allen Vue-Komponenten zur Verfügung. Hierdurch ist eine
 * Kommunikation mit der Open-API-Schnittstelle möglich, ohne das die Daten über
 * mehrere Komponenten hinweg aktualisiert werden müssen.
 */
export class Gost extends App {
	/** Informationen zum allgemeinen Status dieser Teilapplikation */
	public auswahl!: ListGost;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der grundlegenden
	 * Abiturjahrgangsdaten mit dem SVWS-Server
	 */
	public dataJahrgang!: DataGostJahrgang;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der Fächer der
	 * gymnasialen Oberstufe mit dem SVWS-Server
	 */
	public dataFaecher!: DataGostFaecher;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der Fächer der
	 * gymnasialen Oberstufe mit dem SVWS-Server
	 */
	public dataFachwahlen!: DataGostSchuelerFachwahlen;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der Fächer der
	 * gymnasialen Oberstufe mit dem SVWS-Server
	 */
	public dataKursblockung!: DataGostKursblockung;

	/** Kursblockungen */
	public blockungsauswahl!: ListKursblockungen;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der Zwischenergebnisse
	 * von Kursblockungen der gymnasialen Oberstufe.
	 */
	public dataKursblockungsergebnis!: DataGostKursblockungsergebnis;

	/**
	 * Die Daten für die Auswahlliste von Zwischenergebnissen
	 * einer Kurblockung der gymnasialen Oberstufe.
	 */
	public blockungsergebnisauswahl!: ListKursblockungsergebnisse;

	/** Die Liste aller Schüler des ausgewählten Jahgangs */
	public listAbiturjahrgangSchueler!: ListAbiturjahrgangSchueler;

	/** * Schülerlaufbahndaten des in der Blockung ausgewählten Schülers */
	public dataSchuelerLaufbahndaten!: DataSchuelerLaufbahndaten;

	/**
	 * Enthält eine Liste der Halbjahre für die entweder ein Api-Call läuft oder für
	 * die im letzten Api-Call ein Fehler aufgetreten ist.
	 */
	public apiStatus: ApiStatus = reactive({});

	/**
	 * Initialisiert die Klasse und holt die relevanten Daten vom Server
	 *
	 * @returns {Promise<void>}
	 */
	public async init(): Promise<void> {
		// die Liste der Schüler, die bei den Blockungsergebnissen angezeigt wird
		// Bei Auswahl eines Schülers werden die Abiturdaten geladen, um bei
		// Bedarf noch Umwahlen zu ermöglichen oder auch Abwahlen
		this.listAbiturjahrgangSchueler = new ListAbiturjahrgangSchueler();
		this.dataSchuelerLaufbahndaten = new DataSchuelerLaufbahndaten();
		this.listAbiturjahrgangSchueler.add_data(
			this.dataSchuelerLaufbahndaten
		);

		this.blockungsergebnisauswahl = new ListKursblockungsergebnisse();
		this.dataKursblockungsergebnis = new DataGostKursblockungsergebnis();
		this.blockungsergebnisauswahl.add_data([
			this.dataKursblockungsergebnis
		]);
		this.blockungsauswahl = new ListKursblockungen();
		this.dataKursblockung = new DataGostKursblockung(this.blockungsergebnisauswahl);
		this.blockungsauswahl.add_data([this.dataKursblockung]);

		this.auswahl = new ListGost(this.listAbiturjahrgangSchueler, this.blockungsauswahl);
		this.dataFaecher = new DataGostFaecher();
		this.dataJahrgang = new DataGostJahrgang();
		this.dataFachwahlen = new DataGostSchuelerFachwahlen();
		this.auswahl.add_data([
			this.dataJahrgang,
			this.dataFaecher,
			this.dataFachwahlen
		]);
	}

	public getBgColor(row: GostStatistikFachwahl): string {
		return ZulaessigesFach.getByKuerzelASD(row.kuerzelStatistik).getHMTLFarbeRGBA(1.0).valueOf();
	}

	async create_blockung(id: number, hjId: number): Promise<List<Number>|void> {
		this.addIdToApiStatus(hjId);
		this.setApiStatusIdle(hjId);
		try {
			const res = await App.api.rechneGostBlockung(App.schema, id, 5000)
			this.blockungsauswahl.ausgewaehlt = this.blockungsauswahl.ausgewaehlt;
			this.removeApiStatusId(hjId)
			return res
		} catch (e) {
			this.setApiStatusError(hjId);
		}
	}

	/**
	 * Fügt einen Eintrag zur Apistatusliste hinzu.
	 * @param id Der Hashcode, unter dem ein neuer Eintrag hinzugefügt werden soll.
	 */
	public addIdToApiStatus(id: number) {
		this.apiStatus[id] = { error: false, idle: false };
	}

	/**
	 * Gibt den Eintrag für das Halbjahr mit der entsprechenden Id zurück.
	 * @param id Der Hashcode des gesuchten Halbjahres
	 * @returns FeedbackValues des ApiCalls
	 */
	public getApiStatus(id: number): FeedbackValues {
		return this.apiStatus[id];
	}

	/**
	 * Setzt die FeedbakValues des Halbjahres id für einen laufenden Api-Call.
	 * @param id Hashcode des Halbjahres, für das die FeedbackValues geändert werden sollen.
	 */
	public setApiStatusIdle(id: number) {
		Object.assign(this.apiStatus[id], { error: false, idle: true });
	}

	/**
	 * Setzt die FeedbakValues des Halbjahres id für einen fehlgeschlagenen Api-Call
	 * @param id
	 */
	public setApiStatusError(id: number) {
		Object.assign(this.apiStatus[id], { idle: false, error: true });
	}

	/**
	 * Entfernt den Eintrag des Halbjahres mit dem zur Id gehörigen Hashcode aus dem apiStatus.
	 * @param id Der Hashcode des Halbjahres
	 */
	public removeApiStatusId(id: number) {
		delete this.apiStatus[id];
	}
}
