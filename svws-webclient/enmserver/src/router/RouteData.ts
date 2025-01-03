import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ViewType } from "@ui/ui/nav/ViewType";
import { type ShallowRef, shallowRef, ref } from "vue";

import { type RouteNode } from "~/router/RouteNode";


/**
 * Die Definition von gemeinsamen Attributen des States von Routen.
 */
export interface RouteStateInterface {
	view?: RouteNode<any, any>;
	activeViewType?: ViewType;
}


/**
 * Eine abstrakte Klasse für allgemeine Methoden beim Zugriff auf die Daten, welche einer Route
 * zugeordnet sind.
 * Dabei wird intern ein reaktiver State (ShallowRef von vue.js) genutzt, welcher bei den hier
 * definierten Methoden zum Anpassen des States jeweils einmalig getriggert wird.
 */
export abstract class RouteData<RouteState extends RouteStateInterface> {


	/** Der Default-State, welcher über den Konstruktor gesetzt wird */
	protected _defaultState : RouteState;

	/** Der aktuelle State */
	protected _state : ShallowRef<RouteState>;

	/** Parameter zum automatischen Setzen des Fokus nach der Reiterauswahl */
	protected _autofocus = ref<boolean>(false);

	/**
	 * Erzeugt ein neues Route-Daten-Objekt mit dem übergebenen Default-State.
	 * Optional können noch gültige Sichten/Child Routes übergeben werden, sofern diese
	 * hier genutzt werden.
	 *
	 * @param defaultState   der Default-State
	 */
	protected constructor(defaultState : RouteState) {
		this._defaultState = defaultState;
		this._state = shallowRef<RouteState>(this._defaultState);
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
	protected setPatchedDefaultState(patch: Partial<RouteState>) {
		this._state.value = Object.assign({ ... this._defaultState }, patch);
	}

	/**
	 * Setzt den aktuellen State auf den Default-State gepatched mit dem übergebenen patch.
	 * Dabei bleibt die gewählte Ansicht/Child Route jedoch erhalten - selbst wenn der
	 * Patch eine alternative Route angibt.
	 *
	 * @param patch   der Patch, welcher auf den Default-State angewendet wird.
	 */
	protected setPatchedDefaultStateKeepView(patch: Partial<RouteState>) {
		const tmp = Object.assign({ ... this._state.value }, patch);
		tmp.view = this._state.value.view;
		this._state.value = tmp;
	}

	/**
	 * Aktualisiert den aktuellen State mit dem angegebenen Patch.
	 *
	 * @param patch   der Patch, welcher auf den aktuellen State angewendet wird.
	 */
	protected setPatchedState(patch: Partial<RouteState>, newobj: boolean = true) {
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

	/**
	 * Setter für die aktuelle Ansicht/Child Route. Das Setzen der Ansicht triggert die Reaktivität
	 * des States.
	 *
	 * @param value        die zu setzende Ansicht
	 * @param validViews   die Menge der gültigen Ansichten
	 */
	public setView(value: RouteNode<any, any>, validViews: RouteNode<any, any>[]) {
		if (validViews.includes(value))
			this.setPatchedState(<RouteState>{ view: value });
		else
			throw new DeveloperNotificationException("Die gewählte Ansicht wird nicht unterstützt.");
	}

	/**
	 * Setter für die aktuelle Ansicht/Child Route. Das Setzen der Ansicht ist nicht reaktiv.
	 * Ist eine Reaktivität gewünscht, so muss die Methode setView aufgerufen werden.
	 *
	 * @param value        die zu setzende Ansicht
	 * @param validViews   die Menge der gültigen Ansichten
	 */
	public setViewNonReactive(value: RouteNode<any, any>, validViews: RouteNode<any, any>[]) {
		if (validViews.includes(value))
			this.setPatchedState(<RouteState>{ view: value });
		else
			throw new DeveloperNotificationException("Die gewählte Ansicht wird nicht unterstützt.");
	}

	/**
	 * Getter für die aktuelle Ansicht/Child Route.
	 */
	public get view(): RouteNode<any,any> {
		if (this._state.value.view === undefined)
			throw new DeveloperNotificationException("Bei dieser Route wurde keine Ansicht im Default-State definiert.");
		return this._state.value.view;
	}

	/**
	 * Getter für die Default-View
	 */
	public get defaultView(): RouteNode<any,any> {
		if (this._defaultState.view === undefined)
			throw new DeveloperNotificationException("Bei dieser Route wurde keine Ansicht im Default-State definiert.");
		return this._defaultState.view;
	}

	/**
	 * Gibt die aktuelle Art der View zurück (Default, Hinzufügen oder Gruppenprozess).
	 * Ist keiner spezifiziert, so wird DEFAULT zurückgegeben.
	 */
	get activeViewType() : ViewType {
		if (this._state.value.activeViewType === undefined)
			return ViewType.DEFAULT;
		return this._state.value.activeViewType;
	}

	/**
	 * Setzt die aktuelle Art der View zurück (Default, Hinzufügen oder Gruppenprozess).
	 *
	 * @param value   die Art der View
	 */
	set activeViewType(value : ViewType) {
		this._state.value.activeViewType = value;
	}

	/**
	 * Getter für die Information, ob die aktuelle Ansicht einen Autofokus unterstützen soll.
	 */
	public get autofocus(): boolean {
		return this._autofocus.value;
	}

	/**
	 * Setter für die Information, ob die aktuelle Ansicht einen Autofokus unterstützen soll.
	 */
	public set autofocus(value: boolean) {
		this._autofocus.value = value;
	}

}
