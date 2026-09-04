import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";
import type { Config } from "@ui/utils/Config";
import type { InjectionKey } from "vue";

/**
 * Die Schnittstelle für den Zustand der Client-Konfiguration
 */
export interface ConfigState {

	/**
	 * Initialisiert die Konfiguration, indem die Client- und die Benutzer-Konfiguration vom Server
	 * geladen wird. Die nicht-persistente Konfiguration wird dabei zurückgesetzt.
	 */
	init(): Promise<void>;

	/**
	 * Leert die komplette Konfiguration
	 */
	clear(): void;

	/**
	 * Gibt die Konfiguration des angemeldeten Benutzers zurück, sofern diese geladen wurde
	 */
	get config(): Config;
	/**
	 * Gibt die nicht persistente Konfiguration des angemeldeten Benutzers zurück
	 */
	get nonPersistentConfig(): Config;

}


export const ConfigStateKey: InjectionKey<ConfigState> = Symbol('ConfigState');

export function useConfigState(): ConfigState {
	const state = AppContext.instance.inject(ConfigStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des SchuleState über provide in der main.ts eingebunden");
	}
	return state;
}
