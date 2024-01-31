import { shallowRef } from "vue";
import { type List, ArrayList, DeveloperNotificationException, GostBlockungsdaten, GostBlockungsergebnis, GostFach, GostBlockungsergebnisComparator } from "@core";
import type { WorkerKursblockungMessageType, WorkerKursblockungReplyErgebnisse, WorkerKursblockungReplyInit, WorkerKursblockungReplyNext,
	WorkerKursblockungRequestErgebnisse, WorkerKursblockungRequestInit, WorkerKursblockungRequestNext } from "./WorkerKursblockungMessageTypes";


/**
 * Diese Klasse organisiert die Kommunikation zu einem Worker-Thread,
 * der sich um die Berechnungs von Kursblockungsergebnissen der Gymnasialen
 * Oberstufe kümmert.
 */
export class WorkerManagerKursblockung {

	/** Die maximale Anzahl der Worker-Threads zurück, die von diesem Manager genutzt werden können. */
	public static readonly MAX_WORKER = (navigator.hardwareConcurrency ?? 3) - 1; // reserviere einen Thread für die GUI

	/** Die Liste der Fächer des Abiturjahrgangs */
	protected faecherListe: List<GostFach>;

	/** Die Daten der Kursblockung */
	protected blockung: GostBlockungsdaten;

	/** Die maximale Anzahl von Ergebnissen, die in der Liste der besten Ergebnisse gespeichert werden */
	protected maxErgebnisse = shallowRef<number>(10);

	/** Die aktuell besten Ergebnisse, die der Blockunsalgorithmus soweit berechnet hat */
	protected ergebnisse = shallowRef<List<GostBlockungsergebnis>>(new ArrayList());

	/* Die Referenz auf die Worker-Threads */
	protected worker : Array<Worker> = new Array(WorkerManagerKursblockung.MAX_WORKER);

	/** Die aktuell besten Ergebnisse der einzelnen Worker-Threads */
	protected workerErgebnisse : Array<List<GostBlockungsergebnis>> = new Array(WorkerManagerKursblockung.MAX_WORKER).fill(new ArrayList<GostBlockungsergebnis>());

	/** Gibt an, ob die einzelnen Worker initialisiert sind oder nicht */
	protected workerInitialized : Array<boolean> = new Array(WorkerManagerKursblockung.MAX_WORKER).fill(false);

	/* Gibt an, ob der Berechnungsalgorithmus bereits mit Daten initialisiert wurde */
	protected initialized = shallowRef<boolean>(false);

	/** Die Anzahl der genutzten Worker-Threads */
	protected usedWorkerThreads = shallowRef<number>(0);

	/** Das Intervall in Millisekunden, bei welchem die Threads kurzzeitig unterbrochen werden und mit dem Manager kommunizieren */
	protected _interval = shallowRef<number>(100);

	/* Gibt an, ob der Worker bereits terminiert wurde und somit keine neuen Berechnungen mehr ausgeführt werden können. */
	protected terminated = shallowRef<boolean>(false);

	/* Gibt an, ob der Berechnungsalgorithmus aktuell am Laufen ist und automatisch neue Berechnungsintervalle startet */
	protected running = shallowRef<boolean>(false);


	/**
	 * Erzeugt einen neuen nicht initialisierten Worker-Manager zur Berechnung von Kursblockungsergebnissen.
	 *
	 * @param faecherListe   die Liste der Fächer des Abiturjahrgangs
	 * @param blockung       die Daten der Kursblockung
	 */
	public constructor(faecherListe: List<GostFach>, blockung: GostBlockungsdaten) {
		this.faecherListe = faecherListe;
		this.blockung = blockung;
		this.usedWorkerThreads.value = 1;
	}

	/**
	 * Gibt das Intervall in Millisekunden zurück, bei welchem die Worker-Threads kurzzeitig unterbrochen werden
	 * und mit dem Manager kommunizieren.
	 */
	public get interval() : number {
		return this._interval.value;
	}

	/**
	 * Setzt das Intervall in Millisekunden, bei welchem die Worker-Threads kurzzeitig unterbrochen werden
	 * und mit dem Manager kommunizieren.
	 */
	public set interval(value : number) {
		if (value < 100)
			throw new DeveloperNotificationException("Das Intervall sollte mindestens 100 Millisekunden betragen.");
		if (value > 5000)
			throw new DeveloperNotificationException("Das Intervall sollte nicht zu groß gewählt werden (mehr als 5 Sekunden ist definitiv sehr groß und sorgt für eine kaum reagierendes UI.");
		this._interval.value = value;
	}

	/**
	 * Gibt die Anzahl der zu nutzenden Worker-Threads zurück.
	 */
	public get threads() : number {
		return this.usedWorkerThreads.value;
	}

	/**
	 * Setzt die Anzahl der zu nutzenden Worker-Threads.
	 */
	public set threads(value : number) {
		if (value < 1)
			throw new DeveloperNotificationException("Die Anzahl der genutzten Worker-Thread darf nicht kleiner als 1 sein.");
		if (value > WorkerManagerKursblockung.MAX_WORKER)
			throw new DeveloperNotificationException("Die Anzahl der genutzten Worker-Threads darf WorkerManagerKursblockung.MAX_WORKER nicht überschreiten.");
		const oldValue = this.usedWorkerThreads.value;
		if (oldValue === value)
			return;
		this.usedWorkerThreads.value = value;
		// Prüfe zunächst, ob bereits ein Worker initialisiert wurde. Ist dies der Fall, so müssen ggf. neue Worker initialisiert und ggf. gestartet werden
		if ((this.workerInitialized[0] === true) && (value > oldValue)) {
			// Initialisiere oder starte zusätzlich genutzte Worker-Threads (nicht mehr genutzte Threads laufen automatisch aus)
			for (let i = oldValue; i < value; i++) {
				if (this.workerInitialized[i])
					this.requestNext(i);
				else
					this.initWorker(i);
			}
		}
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
	 * keine weiteren Berechnungen mehr ausgeführt werden können.
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
	 * Initialisiert den Worker mit dem angegebenen Index.
	 *
	 * @param index   der Index des Workers
	 */
	protected initWorker(index : number) {
		this.worker[index] = new Worker(new URL('./WorkerKursblockung.ts', import.meta.url), { type: 'module' });
		this.worker[index].onmessage = (ev) => this.messageHandler(index, ev);
		this.requestInit(index, this.faecherListe, this.blockung);
	}

	/**
	 * Initialisiert den Kursblockungsalgorithmus mit den Blockungsdaten
	 */
	public init() {
		if (this.initialized.value === true)
			throw new DeveloperNotificationException("Der Worker-Thread für den Kursblockungsalgorithmus wurde bereits initialisiert.")
		if (this.terminated.value === true)
			throw new DeveloperNotificationException("Der Worker-Thread fpr den Kursblockungsalgorithmus wurde bereits terminiert. Dieser kann nicht erneut initialisiert werden.");
		for (let i = 0; i < this.threads; i++)
			this.initWorker(i);
	}

	/**
	 * Startet die Berechnung.
	 */
	public start() {
		if (this.terminated.value === false) {
			this.running.value = true;
			for (let i = 0; i < this.threads; i++)
				this.requestNext(i);
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
		for (let i = 0; i < this.worker.length; i++) {
			if (this.worker[i] !== undefined) {
				this.worker[i].onmessage = null;
				this.worker[i].terminate();
			}
		}
	}


	/**
	 * Prüft, ob alle genutzten Worker initialisiert wurden.
	 *
	 * @returns true, falls alle genutzten Worker initialisiert wurden, und ansonsten false
	 */
	protected checkAllUsedWorkerInitialized() : boolean {
		for (let i = 0; i < this.threads; i++)
			if (!this.workerInitialized[i])
				return false;
		return true;
	}


	/**
	 * Stellt eine Anfrage an den Worker zur Initialisierung mit den angegebenen Daten.
	 *
	 * @param index          der Index des Workers
	 * @param faecherListe   die Liste der Fächer für den Abiturjahrgang der Blockung
	 * @param blockung       die Daten der Blockung
	 */
	protected requestInit(index : number, faecherListe: List<GostFach>, blockung: GostBlockungsdaten) {
		const faecher = new Array<string>();
		for (const f of faecherListe)
			faecher.push(GostFach.transpilerToJSON(f));
		const blockungsdaten = GostBlockungsdaten.transpilerToJSON(blockung);
		this.worker[index].postMessage(<WorkerKursblockungRequestInit>{ cmd: "init", faecher, blockungsdaten });
	}

	/**
	 * Reagiert auf die Antwort des Workers bezüglich der Anfrage zur Initialisierung.
	 *
	 * @param index   der Index des Workers
	 * @param data    die Nachricht vom Worker
	 */
	protected handleInitReply(index : number, data: WorkerKursblockungReplyInit) {
		this.workerInitialized[index] = data.initialized;
		this.initialized.value = this.checkAllUsedWorkerInitialized();
		// Starte ggf. den neuen Thread
		if (this.isRunning())
			this.requestNext(index);
	}

	/**
	 * Beauftragt den Worker damit, für den angebenen Zeitraum weitere Blockungsergebnisse
	 * zu berechnen.
	 *
	 * @param index      der Index des Workers
	 */
	protected requestNext(index : number) {
		this.worker[index].postMessage(<WorkerKursblockungRequestNext>{ cmd: 'next', interval: this._interval.value });
	}

	/**
	 * Reagiert auf die Nachricht des Workers, dass ein Berechnungszeitraum beendet ist
	 * und die Information, ob neue Blockungsergebnisse vorhanden sind oder nicht.
	 *
	 * @param index    der Index des Workers
	 * @param data   die Nachricht vom Worker
	 */
	protected handleNextReply(index : number, data: WorkerKursblockungReplyNext) {
		// Starte das nächste Berechnungsintervall, solange nicht abgebrochen wurde
		if ((this.workerInitialized[index] === true) && (this.running.value === true) && (index < this.threads))
			this.requestNext(index);
		// Wenn Daten vorliegen, dann frage die neuen Ergebnisse ab
		if (data.hasUpdate === true)
			this.queryErgebnisse(index);
	}

	/**
	 * Stellt an den Worker die Anfrage nach den aktuell besten Blockungsergebnissen.
	 *
	 * @param index   der Index des Workers
	 */
	protected queryErgebnisse(index : number) {
		this.worker[index].postMessage(<WorkerKursblockungRequestErgebnisse>{cmd: 'getErgebnisse'});
	}

	/**
	 * Reagiert auf die Antwort des Workers bezüglich der Anfrage nach den aktuell
	 * besten Blockungsergebnissen.
	 *
	 * @param index    der Index des Workers
	 * @param data   die Nachricht vom Worker mit der Liste der aktuell
	 *               besten Blockungsergebnisse
	 */
	protected handleErgebnisseReply(index : number, data: WorkerKursblockungReplyErgebnisse) {
		const workerErgebnisse = new ArrayList<GostBlockungsergebnis>();
		for (const result of data.ergebnisse) {
			const ergebnis = GostBlockungsergebnis.transpilerFromJSON(result);
			workerErgebnisse.add(ergebnis);
		}
		this.workerErgebnisse[index] = workerErgebnisse;
		const ergebnisse = new ArrayList<GostBlockungsergebnis>();
		for (let i = 0; i < this.worker.length; i++)
			ergebnisse.addAll(this.workerErgebnisse[i]);
		ergebnisse.sort(new GostBlockungsergebnisComparator());
		for (let i = ergebnisse.size() - 1; i >= this.maxErgebnisse.value; i--)
			ergebnisse.removeElementAt(i);
		this.ergebnisse.value = ergebnisse;
	}


	/**
	 * Der Handler für das Abarbeiten von eingehenden Nachrichten von dem Worker an diesen Manager
	 *
	 * @param index    der Index des Workers
	 * @param e        das Ereignis für die eingehende Nachricht
	 */
	protected messageHandler = (index : number, e: MessageEvent<any>) => {
		const cmd: WorkerKursblockungMessageType = e.data.cmd;
		switch (cmd) {
			case 'init': this.handleInitReply(index, e.data); break;
			case 'next': this.handleNextReply(index, e.data); break;
			case 'getErgebnisse': this.handleErgebnisseReply(index, e.data); break;
			default: throw new DeveloperNotificationException(`Mesage type ${e.data.cmd} not yet implemented`);
		}
	}

}