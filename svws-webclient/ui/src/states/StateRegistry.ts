import type { InjectionKey } from "vue";

import { AppContext } from "@ui/AppContext";


/**
 * Die Schnittstelle für einen beliebigen State, der eine Reset-Methode hat
 */
interface ResetableState {
	reset(): void;
}


/**
 * Die State-Registry für den Zugriff auf die registrierten States
 */
export class StateRegistry {

	/** DIe Instanz der Registry */
	private static _instance?: StateRegistry;

	/** Die Liste der registrierten Session-States */
	private readonly sessionStates: Array<ResetableState> = [];

	/** Die Liste der registrierten globalen States */
	private readonly globalStates: Array<ResetableState> = [];


	/**
	 * Erstellt die Instanz der StateRegistry
	 */
	private constructor() {
	}

	/**
	 * Gibt die Instanz zurück. Ist noch keine erzeugt, so wird eine neue angelegt.
	 */
	public static get instance(): StateRegistry {
		this._instance ??= new StateRegistry();
		return this._instance;
	}

	/**
	 * Fügt einen neuen Session State als provide hinzu
	 *
	 * @param key     der Key für das Provide
	 * @param value   der State
	 */
	public addSessionState<T extends ResetableState>(key: InjectionKey<T> | string, value: T): void {
		const context = AppContext.instance;
		context.provide(key, value);
		this.sessionStates.push(value);
	}

	/**
	 * Führt einen reset auf allen registrierten Session-States aus.
	 */
	public resetSessionStates(): void {
		for (const state of this.sessionStates) {
			state.reset();
		}
	}

	/**
	 * Fügt einen neuen globalen State als provide hinzu
	 *
	 * @param key     der Key für das Provide
	 * @param value   der State
	 */
	public addGlobalState<T extends ResetableState>(key: InjectionKey<T> | string, value: T): void {
		const context = AppContext.instance;
		context.provide(key, value);
		this.globalStates.push(value);
	}

	/**
	 * Führt einen reset auf allen registrierten globalen States aus.
	 */
	public resetGlobalStates(): void {
		for (const state of this.globalStates) {
			state.reset();
		}
	}

}
