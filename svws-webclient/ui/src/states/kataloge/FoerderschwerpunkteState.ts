import type { InjectionKey } from "vue";

import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des FoerderschwerpunkteStates
 */
export interface FoerderschwerpunkteState {
	readonly foerderschwerpunkte: KatalogState<FoerderschwerpunktEintrag>;
	init(): Promise<void>;
	reset(): void;
}

export const FoerderschwerpunkteStateKey: InjectionKey<FoerderschwerpunkteState> = Symbol('FoerderschwerpunkteState');

export function useFoerderschwerpunkteState(): FoerderschwerpunkteState {
	const state = AppContext.instance.inject(FoerderschwerpunkteStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des FoerderschwerpunkteState über provide in der main.ts eingebunden");
	}
	return state;
}

