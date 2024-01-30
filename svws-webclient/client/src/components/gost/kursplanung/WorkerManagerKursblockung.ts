import type { List } from "@core";
import { ArrayList, DeveloperNotificationException, GostBlockungsdaten, GostBlockungsergebnis, GostFach } from "@core";
import { shallowRef } from "vue";
import type { WorkerKursblockungMessageType, WorkerKursblockungReplyErgebnisse, WorkerKursblockungReplyInit, WorkerKursblockungReplyNext, WorkerKursblockungRequestErgebnisse, WorkerKursblockungRequestInit, WorkerKursblockungRequestNext } from "./WorkerKursblockungMessageTypes";


/**
 * Diese Klasse organisiert die Kommunikation zu einem Worker-Thread,
 * der sich um die Berechnungs von Kursblockungsergebnissen der Gymnasialen
 * Oberstufe kümmert.
 */
export class WorkerManagerKursblockung {

	/** Die aktuell besten Ergebnisse, die der Blockunsalgorithmus soweit berechnet hat */
	protected ergebnisse = shallowRef<List<GostBlockungsergebnis>>(new ArrayList());

	/* Die Refernz auf den Worker-Thread */
	protected worker : Worker;

	/* Gibt an, ob der Berechnungsalgorithmus bereits mit Daten initialisiert wurde */
	protected initialized = shallowRef<boolean>(false);

	/* Gibt an, ob der Worker bereits terminiert wurde und somit keine neuen Berechnungen mehr ausgeführt werden können. */
	protected terminated = shallowRef<boolean>(false);

	/* Gibt an, ob der Berechnungsalgorithmus aktuell am Laufen ist und automatisch neue Berechnungsintervalle startet */
	protected running = shallowRef<boolean>(false);


	/**
	 * Erzeugt einen neuen nicht initialisierten Worker-Thread zur Berechnung von Kursblockungsalgorithmen.
	 */
	public constructor() {
		this.worker = new Worker(new URL('./WorkerKursblockung.ts', import.meta.url), { type: 'module' });
		this.worker.addEventListener("message", this.messageHandler);
	}

	/**
	 * Gibt zurück, ob der Kursblockungsalgorithmus in dem Worker-Thread bereits mit blockungsdaten
	 * initialisiert wurde.
	 *
	 * @returns true, sofern der Algorithmus initialisiert wurde und ansonsten false
	 */
	public isInitialized() : boolean {
		return this.initialized.value;
	}

	/**
	 * Gibt zurück, ob der Worker-Thread mit dem Kursblockungsalgorithmus terminiert wurde und
	 * keine weiteren Berehcnungen mehr ausgeführt werden können.
	 *
	 * @returns true, falls er terminiert wurde und ansonsten false
	 */
	public isTerminated() : boolean {
		return this.terminated.value;
	}

	/**
	 * Gibt an, ob der Worker derzeit am Laufen ist oder nicht.
	 *
	 * @returns true, falls der Worker aktuell am Laufen ist oder nicht.
	 */
	public isRunning() : boolean {
		return this.running.value;
	}

	/**
	 * initialisiert der Kursblockungsalgorithmus mit den übergebenen Blockungsdaten
	 *
	 * @param faecherListe   die Lister der Fächer des Abiturjahrgangs
	 * @param blockung       die Daten zur Berechnung der Kursblockung
	 */
	public init(faecherListe: List<GostFach>, blockung: GostBlockungsdaten) {
		if (this.initialized.value === true)
			throw new DeveloperNotificationException("Der Worker-Thread für den Kursblockungsalgorithmus wurde bereits initialisiert.")
		if (this.terminated.value === true)
			throw new DeveloperNotificationException("Der Worker-Thread fpr den Kursblockungsalgorithmus wurde bereits terminiert. Dieser kann nicht erneut initialisiert werden.");
		this.requestInit(faecherListe, blockung);
	}

	/**
	 * Startet die Berechnung mit dem angegebenen interval
	 *
	 * @param interval   die Zeit in Millisekunden für ein Berechnungsintervall
	 */
	public start(interval : number = 100) {
		if (this.terminated.value === false) {
			this.running.value = true;
			this.requestNext(interval);
		}
	}

	/**
	 * Gibt die Liste der aktuell besten Blockungsergebnisse zurück.
	 *
	 * @returns die Liste der aktuell besten Ergebnisse
	 */
	public getErgebnisse() : List<GostBlockungsergebnis> {
		return this.ergebnisse.value;
	}

	/**
	 * Pausiert die Berechnung von Kursblockungsergebnissen. Diese kann
	 * anschließend mit start fortgesetzt werden.
	 */
	public pause() {
		this.running.value = false;
	}

	/**
	 * Terminiert den Worker-Thread. Damit sind anschließend keinerlei
	 * Berechnungen mehr möglich. Für Folgeberechnungen muss ein neuer
	 * Thread erzeugt werden.
	 */
	public terminate() {
		this.terminated.value = true;
		this.running.value = false;
		this.initialized.value = false;
		this.worker.removeEventListener('message', this.messageHandler);
		this.worker.terminate();
	}


	/**
	 * Stellt eine Anfrage an den Worker zur Initialisierung mit den angegebenen Daten.
	 *
	 * @param faecherListe   die Liste der Fächer für den Abiturjahrgang der Blockung
	 * @param blockung       die Daten der Blockung
	 */
	protected requestInit(faecherListe: List<GostFach>, blockung: GostBlockungsdaten) {
		const faecher = new Array<string>();
		for (const f of faecherListe)
			faecher.push(GostFach.transpilerToJSON(f));
		const blockungsdaten = GostBlockungsdaten.transpilerToJSON(blockung);
		this.worker.postMessage(<WorkerKursblockungRequestInit>{ cmd: "init", faecher, blockungsdaten });
	}

	/**
	 * Reagiert auf die Antwort des Workers bezüglich der Anfrage zur Initialisierung.
	 *
	 * @param data   die Nachricht vom Worker
	 */
	protected handleInitReply(data: WorkerKursblockungReplyInit) {
		this.initialized.value = data.initialized;
	}

	/**
	 * Beauftragt den Worker damit, für den angebenen Zeitraum weitere Blockungsergebnisse
	 * zu berechnen.
	 *
	 * @param interval   der Zeitraum, der dem Worker zur Verfügung gestellt wird.
	 */
	protected requestNext(interval : number = 100) {
		this.worker.postMessage(<WorkerKursblockungRequestNext>{ cmd: 'next', interval });
	}

	/**
	 * Reagiert auf die Nachricht des Workers, dass ein Berechnungszeitraum beendet ist
	 * und die Information, ob neue Blockungsergebnisse vorhanden sind oder nicht.
	 *
	 * @param data   die Nachricht vom Worker
	 */
	protected handleNextReply(data: WorkerKursblockungReplyNext) {
		// Starte das nächste Berechnungsintervall, solange nicht abgebrochen wurde
		if ((this.initialized.value === true) && (this.running.value === true))
			this.requestNext();
		// Wenn Daten vorliegen, dann frage die neuen Ergebnisse ab
		if (data.hasUpdate === true)
			this.queryErgebnisse();
	}

	/**
	 * Stellt an den Worker die Anfrage nach den aktuell besten Blockungsergebnissen.
	 */
	protected queryErgebnisse() {
		this.worker.postMessage(<WorkerKursblockungRequestErgebnisse>{cmd: 'getErgebnisse'});
	}

	/**
	 * Reagiert auf die Antwort des Workers bezüglich der Anfrage nach den aktuell
	 * besten Blockungsergebnissen.
	 *
	 * @param data   die Nachricht vom Worker mit der Liste der aktuell
	 *               besten Blockungsergebnisse
	 */
	protected handleErgebnisseReply(data: WorkerKursblockungReplyErgebnisse) {
		const ergebnisse = new ArrayList<GostBlockungsergebnis>();
		for (const result of data.ergebnisse) {
			const ergebnis = GostBlockungsergebnis.transpilerFromJSON(result);
			ergebnisse.add(ergebnis);
		}
		this.ergebnisse.value = ergebnisse;
	}


	/**
	 * Der Handler für das Abarbeiten von eingehenden Nachrichten von dem Worker an diesen Manager
	 *
	 * @param e   das eingehende Message-Event
	 */
	protected messageHandler = (e: MessageEvent<any>) => {
		const cmd: WorkerKursblockungMessageType = e.data.cmd;
		switch (cmd) {
			case 'init': this.handleInitReply(e.data); break;
			case 'next': this.handleNextReply(e.data); break;
			case 'getErgebnisse': this.handleErgebnisseReply(e.data); break;
			default: throw new DeveloperNotificationException(`Mesage type ${e.data.cmd} not yet implemented`);
		}
	}

}