import { shallowRef, triggerRef, type ShallowRef } from "vue";

/**
 * Abstrakte Basisklasse zur reaktiven Zustandsverwaltung.
 *
 * Der `StateManager` kapselt ein State-Objekt als reaktiven State und stellt Methoden
 * bereit, um diesen State reaktiv zu initialisieren, zu patchen oder zurückzusetzen.
 * Die Reaktivität basiert auf `ShallowRef`, d.h. nur Änderungen der gesamten Statereferenz
 * (nicht tief verschachtelter Eigenschaften) lösen ein reaktives Update aus.
 *
 * D.h. reaktive Statemutation erfolgt nur, wenn man die Objektreferenz ändert. Zur
 * Mutation des States muss daher die Methode `setPatchedState` oder `commit` genutzt werden.
 *
 * Konkrete Subklassen (wie StateImpl, Manager oder RouteData Klassen) definieren den State-Typ
 * und liefern den initialen Default-State als Argument.
 *
 * @abstract
 * @typeParam State - Der State-Typ; muss ein flaches Objekt (`Record<string, any>`) sein.
 * @example
 * ```ts
 * interface SchuelerState {
 *   id: number | null;
 *   name: string;
 *   isLoading: boolean;
 * }
 *
 * class SchuelerStateManager extends StateManager<SchuelerState> {
 *   public constructor() {
 *     super({ id: null, name: "", isLoading: false });
 *   }
 *
 *   public async loadSchueler(id: number): Promise<void> {
 *     this.setPatchedState({ isLoading: true });
 *     const schueler = await api.getSchuelerById(id);
 *     this.setPatchedState({ id: schueler.id, name: schueler.name, isLoading: false });
 *   }
 * }
 * ```
 */
export abstract class StateManager<State extends Record<string, any>> {

	/** Der Default-State, welcher über den Konstruktor gesetzt wird */
	protected _defaultState: State;

	/** Der aktuelle reaktive State als ShallowRef – enthält den Zustand der Subklasse */
	protected _state: ShallowRef<State>;

	/**
	 * Setzt den übergebenen Default-State als reaktiven State (durch ShallowRef)
	 *
	 * @param defaultState   der Default-State
	 */
	protected constructor(defaultState: State) {
		this._defaultState = defaultState;
		this._state = shallowRef<State>(this._defaultState);
	}

	/**
	 * Setzt den aktuellen State reaktiv auf den Default-State zurück.
	 */
	protected setDefaultState() {
		this._state.value = this._defaultState;
	}

	/**
	 * Setzt den aktuellen State reaktiv auf den Default-State zurück gepatched mit dem übergebenen patch.
	 *
	 * @param patch   der Patch, welcher auf den Default-State angewendet wird.
	 */
	protected setPatchedDefaultState(patch: Partial<State>) {
		this._state.value = { ...this._defaultState, ...patch };
	}

	/**
	 * Aktualisiert den aktuellen State mit dem angegebenen Patch.
	 *
	 * Nur mit newobj = true (default) wird der State reaktiv aktualisiert,
	 * da dabei eine neue Objekt-Referenz erzeugt und so die Reaktivität des
	 * intern genutzten shallowRefs getriggert wird.
	 *
	 * Mit newobj = false wird NUR der State mutiert, ohne eine neue Referenz zu erzeugen.
	 * Wichtig: Daher wird KEIN reaktives Update ausgelöst – dafür muss anschließend explizit
	 * `commit()` aufgerufen werden.
	 *
	 * @param patch   der Patch, welcher auf den aktuellen State angewendet wird.
	 * @param newobj  Boolean, der definiert, ob eine neue Objekt-Referenz erzeugt werden soll
	 */
	protected setPatchedState(patch: Partial<State>, newobj: boolean = true) {
		if (newobj) {
			this._state.value = { ...this._state.value, ...patch };
		} else {
			this._state.value = Object.assign(this._state.value, patch);
		}
	}

	/**
	 * Bestätigt den aktuellen State und triggert das reaktive Update des States.
	 *
	 * Wenn man einen State direkt ändert (z.B. in Subklassen den State tief zu ändern),
	 * wird durch das verwendete `shallowRef` keine Reaktivität getriggert.
	 * Diese Funktion kann daher aufgerufen werden, um den geänderten State final zu bestätigen und
	 * durch `triggerRef` die Reaktivität auszulösen.
	 *
	 * Für das Update wird die Vue Methode `triggerRef` genutzt, die bei einem `shallowRef`
	 * ein reaktives Update erzwingt - ohne dass eine neue Referenz erzeugt werden muss.
	 */
	protected commit(): void {
		triggerRef(this._state);
	}


	/**
	 * Führt einen reset der Daten durch. Dabei wird der State reaktiv auf den
	 * Default-State zurückgesetzt.
	 */
	public reset(): void {
		this.setDefaultState();
	}

	/**
	 * Gibt den internen State zurück
	 *
	 * @returns der interne State
	 */
	protected get state(): State {
		return this._state.value;
	}

}
