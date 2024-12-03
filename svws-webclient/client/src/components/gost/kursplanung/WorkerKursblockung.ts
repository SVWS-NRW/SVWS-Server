/**
 * Im Worker müssen alle Importe direkt auf die jeweilige Datei verweisen, keine Importe über die index.ts, sonst wird alles in den Worker gebundelt beim Bauen.
 */

import { JsonCoreTypeReader } from "../../../../../core/src/asd/utils/JsonCoreTypeReader";
import { GostBlockungsdaten } from "../../../../../core/src/core/data/gost/GostBlockungsdaten";
import { GostBlockungsergebnis } from "../../../../../core/src/core/data/gost/GostBlockungsergebnis";
import { GostFach } from "../../../../../core/src/core/data/gost/GostFach";
import { KursblockungAlgorithmusPermanent } from "../../../../../core/src/core/kursblockung/KursblockungAlgorithmusPermanent";
import { GostBlockungsdatenManager } from "../../../../../core/src/core/utils/gost/GostBlockungsdatenManager";
import { GostFaecherManager } from "../../../../../core/src/core/utils/gost/GostFaecherManager";
import { ArrayList } from "../../../../../core/src/java/util/ArrayList";
import type { List } from "../../../../../core/src/java/util/List";
import type { WorkerKursblockungErrorMessage, WorkerKursblockungMessageType, WorkerKursblockungReplyErgebnisse, WorkerKursblockungReplyInit, WorkerKursblockungReplyNext, WorkerKursblockungRequestErgebnisse, WorkerKursblockungRequestInit, WorkerKursblockungRequestNext } from "./WorkerKursblockungMessageTypes";

/**
 * Die Klasse zur Verwaltung des Workers für die asynchrone Berechnung
 * von Kursblockungsergebnissen.
 */
class WorkerKursblockung {

	/** Der Algorithmus zur Berechnung von Kursblockungsergebnisse, sofern er initialisiert wurde */
	protected algo: KursblockungAlgorithmusPermanent | null;
	/** Der Reader für die CoreTypeDaten */
	private reader = new JsonCoreTypeReader();

	/**
	 * Erzeugt eine neue Worker-Klasse ohne diese zu Initialisieren.
	 */
	constructor() {
		this.algo = null;
	}

	/**
	 * Gibt zurück, ob der Algorithmus initialisiert wurde oder nicht.
	 *
	 * @returns true, falls der Algorithmus initialisiert wurde, und ansonsten false
	 */
	public isInitialized() : boolean {
		return (this.algo !== null);
	}


	/**
	 * Initialisiert den Algorithmus mit den übergebenen Fächer- zund Blockungsdaten
	 *
	 * @param faecher          die Fächer des Abiturjahrgangs der gymnasialen Oberstufe
	 * @param blockungsdaten   die Blockungsdaten
	 */
	public init(faecher: List<GostFach>, blockungsdaten: GostBlockungsdaten, mapCoreTypeNameJsonData: Map<string, string>) {
		this.reader.mapCoreTypeNameJsonData = mapCoreTypeNameJsonData;
		this.reader.readAll();
		const faecherManager = new GostFaecherManager(blockungsdaten.abijahrgang - 1, faecher);
		const datenmanager = new GostBlockungsdatenManager(blockungsdaten, faecherManager)
		this.algo = new KursblockungAlgorithmusPermanent(datenmanager);
	}

	/**
	 * Lässt den Algorithmus für die angegebene Zeit weiterlaufen.
	 *
	 * @param time   die Zeit, welche der Algorithmus für das Weiterrechnen zur Verfügung
	 *               gestellt bekommt, bevor eine Rückmeldung des Workers erfolgen soll
	 * @returns true, falls neue Ergebenisse vorliegen, und ansonsten false.
	 */
	public next(val: number) : boolean {
		if (this.algo === null)
			return false;
		return this.algo.next(val);
	}

	/**
	 * Gibt die aktuell besten Blockungsergebnisse zurück, die vom Blockungsalgorithmus
	 * berechnet wurden.
	 *
	 * @returns die aktuell besten Blockungsergebnisse
	 */
	public getBlockungsergebnisse() : List<GostBlockungsergebnis> {
		const result = new ArrayList<GostBlockungsergebnis>();
		if (this.algo === null)
			return result;
		for (const manager of this.algo.getBlockungsergebnisse())
			result.add(manager.getErgebnis());
		return result;
	}


	/**
	 * Bearbeitet die Initialisierungs-Nachricht
	 *
	 * @param message   die Nachricht
	 */
	protected handleInit(message : WorkerKursblockungRequestInit) {
		if (!this.isInitialized()) {
			const faecherListe = new ArrayList<GostFach>();
			for (const fach of message.faecher)
				faecherListe.add(GostFach.transpilerFromJSON(fach));
			const blockungsdaten = GostBlockungsdaten.transpilerFromJSON(message.blockungsdaten);
			const mapCoreTypeNameJsonData = message.mapCoreTypeNameJsonData
			this.init(faecherListe, blockungsdaten, mapCoreTypeNameJsonData);
		}
		postMessage(<WorkerKursblockungReplyInit>{ cmd: 'init', initialized: this.isInitialized() });
	}

	/**
	 * Bearbeitet die Nachricht dafür, dass der Algorithmus in diesem Thread
	 * Zeit für die Berechnung weiterer Blockungsergebnisse zur Verfügung gestellt
	 * bekommt.
	 *
	 * @param message   die Nachricht
	 */
	protected handleNext(message: WorkerKursblockungRequestNext) {
		const result = this.next(message.interval);
		postMessage(<WorkerKursblockungReplyNext>{cmd: 'next', hasUpdate: result });
	}

	/**
	 * Bearbeitet die Anfrage nach der Liste der aktuell besten Blockungsergebnisse, die durch diesen
	 * Worker berechnet wurden.
	 *
	 * @param message   die Nachricht
	 */
	protected handleGetErgebnisse(message: WorkerKursblockungRequestErgebnisse) {
		const ergebnisse = this.getBlockungsergebnisse();
		const result = new Array<string>();
		for (const ergebnis of ergebnisse)
			result.push(GostBlockungsergebnis.transpilerToJSON(ergebnis));
		postMessage(<WorkerKursblockungReplyErgebnisse>{cmd: 'getErgebnisse', ergebnisse: result });
	}


	/**
	 * Der Handler für das Abarbeiten von eingehenden Nachrichten von dem Manager an diesen Worker
	 *
	 * @param event   das eingehende Message-Event
	 */
	public messageHandler = (event: MessageEvent) : void => {
		const cmd: WorkerKursblockungMessageType = event.data.cmd;
		try {
			switch (cmd) {
				case 'init': this.handleInit(event.data); return;
				case 'next': this.handleNext(event.data); return;
				case 'getErgebnisse': this.handleGetErgebnisse(event.data); return;
			}
		} catch (error) {
			postMessage(<WorkerKursblockungErrorMessage>{cmd: 'Error', task: cmd, error});
		}
	}

}

const worker = new WorkerKursblockung();
onmessage = worker.messageHandler;
