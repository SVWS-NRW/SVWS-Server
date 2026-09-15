import type { InjectionKey } from "vue";

import type { Leitungsfunktion } from "@core/core/data/schule/Leitungsfunktion";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des LeistungsfunktionState
 */
export interface LeitungsfunktionState {
	readonly leitungsfunktionen: KatalogState<Leitungsfunktion>;

	init(): Promise<void>;

	reset(): void;
}

export const LeitungsfunktionStateKey: InjectionKey<LeitungsfunktionState> = Symbol('LeitungsfunktionState');

export function useLeitungsfunktionState(): LeitungsfunktionState {
	const state = AppContext.instance.inject(LeitungsfunktionStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des LeitungsfunktionState über provide in der main.ts eingebunden");
	}
	return state;
}
