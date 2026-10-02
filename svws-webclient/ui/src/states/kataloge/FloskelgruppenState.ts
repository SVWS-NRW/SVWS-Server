import type { InjectionKey } from "vue";

import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des FloskelgruppenStates
 */
export interface FloskelgruppenState {
	readonly floskelgruppen: KatalogState<Floskelgruppe>;
	init(): Promise<void>;
	reset(): void;
}

export const FloskelgruppenStateKey: InjectionKey<FloskelgruppenState> = Symbol('FloskelgruppenState');

export function useFloskelgruppenState(): FloskelgruppenState {
	const state = AppContext.instance.inject(FloskelgruppenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des FloskelgruppenState über provide in der main.ts eingebunden");
	}
	return state;
}

