import { type ShallowRef, shallowRef } from "vue";

import { type RouteNode } from "~/router/RouteNode";
import { DeveloperNotificationException } from "../../../core/src/core/exceptions/DeveloperNotificationException";


/**
 * Die Definition von gemeinsamen Attributen des States von Routen.
 */
export interface RouteStateInterface {
	view?: RouteNode<any, any>;
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
	protected setPatchedState(patch: Partial<RouteState>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
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
	 * Setter für die aktuelle Ansicht/Child Route. Das Setzen der Ansicht triggert die Reaktivität
	 * des States. Ist dies nicht gewünscht, so kann alternativ die Methode setPatchedState
	 * mit gesetztem view-Attribut aufgerufen werden.
	 *
	 * @param value        die zu setzende Ansicht
	 * @param validViews   die Menge der gültigen Ansichten
	 */
	public setView(value: RouteNode<any, any>, validViews: RouteNode<unknown, any>[]) {
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

}
