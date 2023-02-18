import { ShallowRef, shallowRef } from "vue";


/**
 * Das Interface mit dem internen Zustand des API-Status
 */
interface APIStatusState {

	/// Gibt an, ob die API derzeit einen laufenden Befehl hat
	pending: boolean;

	/// Der zuletzt gemeldete Fehler, falls ein API-Aufruf fehlgeschlagen ist
	error: Error | undefined;

}


/**
 * Diese Klasse enthält den reaktiven API-Status für einen Bereich der Applikation.
 * Die Status-Informationen sind üblicherweise an die Routen der Applikation gebunden
 * und werden über die Properties übergeben.
 */
export class ApiStatus {

	/// Der aktuelle Zustand des API-Status
	private _state: ShallowRef<APIStatusState> = shallowRef({
		pending: false,
		error: undefined
	});

	/**
	 * Gibt zurück, ob derzeit ein API-Aufruf am laufen ist.
	 *
	 * @returns {boolean} gibt an, ob derzeit ein API-Aufruf am laufen ist
	 */
	public get pending(): boolean {
		return this._state.value.pending;
	}

	/**
	 * Gibt zurück, ob derzeit ein Fehler vorliegt oder nicht
	 *
	 * @returns {boolean} gibt an, ob derzeit ein Fehler vorliegt oder nicht
	 */
	public get hasError(): boolean {
		return this._state.value.error !== undefined;
	}

	/**
	 * Gibt den letzten Fehler beim API-Aufruf zurück oder undefined, wenn kein Fehler
	 * aufgetreten ist.
	 *
	 * @returns {Error | undefined} der letzte Fehler oder undefined, wenn kein Fehler
	 * 		aufgetreten ist
	 */
	public get error(): Error | undefined {
		return this._state.value.error;
	}

	/**
	 * Setzt beim API-Status, dass derzeit ein API-Aufruf läuft.
	 *
	 * @returns {boolean} true, falls der API-Status gesetzt werden konnte und false, falls
	 * 	bereits ein anderer API-Aufruf läuft.
	 */
	public start(): boolean {
		if (this.pending)
			return false;
		this._state.value = { pending: true, error: undefined };
		return true;
	}

	/**
	 * Setzt beim API-Status, dass ein API-Aufruf beendet wurde und setzt ggf. den Fehler, falls einer
	 * aufgetreten ist.
	 *
	 * @param error {Error | undefined} ggf. ein Fehler, der beim API-Aufruf aufgetreten ist
	 *
	 * @returns true, falls ein API-Aufruf am laufen war und false, falls kein API-Aufruf aktiv war.
	 */
	public stop(error?: Error): boolean {
		if (!this.pending)
			return false;
		this._state.value = { pending: false, error: error };
		return true;
	}

}
