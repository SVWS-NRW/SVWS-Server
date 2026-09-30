import type { InjectionKey } from "vue";

import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des JahrgaengeStates
 */
export interface JahrgaengeState {
	readonly jahrgaenge: KatalogState<JahrgangsDaten>;
	init(): Promise<void>;
	reset(): void;
}

export const JahrgaengeStateKey: InjectionKey<JahrgaengeState> = Symbol('JahrgaengeState');

export function useJahrgaengeState(): JahrgaengeState {
	const state = AppContext.instance.inject(JahrgaengeStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des JahrgaengeState über provide in der main.ts eingebunden");
	}
	return state;
}

