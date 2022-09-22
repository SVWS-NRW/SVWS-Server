import { reactive, ComputedRef, computed } from "vue";
import { MAIN_LOADING_SYMBOL } from "./LoadingSymbols";

/**
 * Interface für die Beschreibung eines Prozesses
 */
export interface LoadingProcess {
	/** Angabe über den Initiator des Request */
	caller: string,
	/** Die Nachricht, die für diesen Prozess angezeigt werden soll */
	message: string,
	/** Liste der Kategorien, in die der Prozess fällt */
	categories: Symbol[],
}

/**
 * Klasse zur Verwaltung der laufenden Ladeprozesse innerhalb der Anwendung
 */
export class ApiLoadingStatus {
	private activeCallsCache: Map<string, LoadingProcess> = reactive(new Map<string, LoadingProcess>());
	/** Speicher aller bekannten laufenden Vorgänge */
	private activeCalls: ComputedRef<any> = computed(() => this.activeCallsCache);
	/**
	 * Ließt das activeCalls-Object aus. Gibt true zurück, falls Vorgänge laufen,
	 * false, wenn keine laufenden Vorgänge bekannt sind.
	 */
	public api_pending: ComputedRef<boolean> = computed(() => this.activeCallsCache.size > 0);

	/**
	 * Zeigt an, ob ein Prozess bekannt ist, der für die Initialisierung der Anwendung verantwortlich ist.
	 */
	public initializing: ComputedRef<boolean> = computed(() => {
		let result: boolean = false;
		for (let [key, value] of this.activeCallsCache) {
			if (value.categories.includes(MAIN_LOADING_SYMBOL)) result = true;
		}
		return result;
	})

	/**
	 * Gibt eine textliche Beschreibung des laufenden Prozesses an. Als Standard Nachricht wird
	 * "Anwendung lädt" angezeigt, bei laufenden Prozessen wird die Nachricht des erste im
	 * Cache gehaltenen Prozesses ausgegeben.
	 */
	public loading_message: ComputedRef<string> = computed(() => {
		let message: string = 'Anwendung lädt'
		if (this.api_pending.value === true) {
			const keys = [...this.activeCallsCache];
			// Zugriff auf erstes Element im activeCallsCache
			const firstMessage = keys[0][1].message;
			if (firstMessage != undefined) message = firstMessage;
		}
		return message;
	});

	constructor() { }

	/**
	 * Hört auf einen asynchronen Vorgang und verwaltet einen Api-Status für diesen
	 * @param promise Promise<any> Der Vorgang, der beobachtet werden soll
	 * @param message string Die Nachricht, die für diesen Vorgang angezeigt werden soll
	 */
	public addStatusByPromise(promise: Promise<any>, process: LoadingProcess) {
		const uuid = this.genUniqueId();
		this.activeCallsCache.set(uuid, process);
		promise.finally(() => {
			this.removeStatus(uuid);
		});
	}

	/**
	 * Entfernt den Status des Calls mit der angegebenen ID
	 * @param id string Die ID des zu entfernenden Status
	 */
	private removeStatus(id: string) {
		this.activeCallsCache.delete(id);
	}

	/**
	 * Generiert eine zufällige ID als string.
	 * @returns string Unique ID aus Zeitpunkt der Erstellung und Zufallszahl.
	 */
	private genUniqueId(): string {
		const dateStr = Date
			.now()
			.toString(36);

		const randomStr = Math
			.random()
			.toString(36)
			.substring(2, 8);

		return `${dateStr}-${randomStr}`;
	}
}
