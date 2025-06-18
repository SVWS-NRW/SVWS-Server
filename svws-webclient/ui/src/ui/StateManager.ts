import { shallowRef, type ShallowRef } from "vue";


/**
 * Eine abstrakte Klasse für allgemeine Methoden beim Zugriff auf die Daten, welche einer Route
 * zugeordnet sind.
 * Dabei wird intern ein reaktiver State (ShallowRef von vue.js) genutzt, welcher bei den hier
 * definierten Methoden zum Anpassen des States jeweils einmalig getriggert wird.
 */
export abstract class StateManager<State extends Record<string, any>> {


	/** Der Default-State, welcher über den Konstruktor gesetzt wird */
	protected _defaultState : State;

	/** Der aktuelle State */
	protected _state : ShallowRef<State>;

	/**
	 * Erzeugt ein neues Route-Daten-Objekt mit dem übergebenen Default-State.
	 * Optional können noch gültige Sichten/Child Routes übergeben werden, sofern diese
	 * hier genutzt werden.
	 *
	 * @param defaultState   der Default-State
	 */
	protected constructor(defaultState : State) {
		this._defaultState = defaultState;
		this._state = shallowRef<State>(this._defaultState);
	}


	/**
	 * Setzt den aktuellen State auf den Default-State.
	 */
	protected setDefaultState() {
		this._state.value = this._defaultState;
	}

	/**
	 * Setzt den aktuellen State auf den Default-State gepatched mit dem übergebenen patch.
	 *
	 * @param patch   der Patch, welcher auf den Default-State angewendet wird.
	 */
	protected setPatchedDefaultState(patch: Partial<State>) {
		this._state.value = Object.assign({ ... this._defaultState }, patch);
	}

	/**
	 * Aktualisiert den aktuellen State mit dem angegebenen Patch.
	 *
	 * @param patch   der Patch, welcher auf den aktuellen State angewendet wird.
	 */
	protected setPatchedState(patch: Partial<State>, newobj: boolean = true) {
		if (newobj)
			this._state.value = Object.assign({ ... this._state.value }, patch);
		else
			this._state.value = Object.assign(this._state.value, patch);
	}

	/**
	 * Bestätigt den aktuellen State. Diese Methode kann genutzt werden,
	 * um das reaktive Verhalten der intern genutzten Shallow-Ref zu
	 * triggern.
	 */
	protected commit(): void {
		this._state.value = { ... this._state.value };
	}


	/**
	 * Führt einen reset der Daten durch. Dabei wird der State auf den
	 * Default-State zurückgesetzt.
	 */
	public reset(): void {
		this.setDefaultState();
	}

}
