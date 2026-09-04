import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { ServerMode } from "@core/core/types/ServerMode";
import { AppContext } from "@ui/AppContext";
import type { InjectionKey } from "vue";

/**
 * Die Schnittstelle für den Zustand des Servers
 */
export interface ServerState {

	/**
	 * Gibt den ServerMode des Servers zurück, sofern bereits ein Login stattgefunden hat.
	 *
	 * @returns der ServerMode
	 */
	get mode(): ServerMode;

	/**
	 * Prüft, ob der ServerMode DEV zulässt
	 *
	 * @returns true, wenn DEV erlaubt ist
	 */
	get hasDev(): boolean

	/**
	 * Prüft, ob der ServerMode DEV zulässt
	 *
	 * @returns true, wenn DEV erlaubt ist
	 */
	get hasAlpha(): boolean

	/**
	 * Prüft, ob der ServerMode DEV zulässt
	 *
	 * @returns true, wenn DEV erlaubt ist
	 */
	get hasBeta(): boolean
}

export const ServerStateKey: InjectionKey<ServerState> = Symbol('ServerState');

export function useServerState(): ServerState {
	const state = AppContext.instance.inject(ServerStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des ServerState über provide in der main.ts eingebunden");
	}
	return state;
}
