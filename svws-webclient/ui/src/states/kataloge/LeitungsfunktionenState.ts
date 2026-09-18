import type { InjectionKey } from "vue";

import type { Leitungsfunktion } from "@core/core/data/schule/Leitungsfunktion";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des LeistungsfunktionState
 */
export interface LeitungsfunktionenState {
	readonly leitungsfunktionen: KatalogState<Leitungsfunktion>;

	init(): Promise<void>;

	reset(): void;
}

export const LeitungsfunktionenStateKey: InjectionKey<LeitungsfunktionenState> = Symbol('LeitungsfunktionenState');

export function useLeitungsfunktionenState(): LeitungsfunktionenState {
	const state = AppContext.instance.inject(LeitungsfunktionenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des LeitungsfunktionenState über provide in der main.ts eingebunden");
	}
	return state;
}
