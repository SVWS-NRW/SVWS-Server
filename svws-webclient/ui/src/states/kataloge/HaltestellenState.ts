import type { InjectionKey } from "vue";

import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des HaltestellenStates
 */
export interface HaltestellenState {
	readonly haltestellen: KatalogState<Haltestelle>;
	init(): Promise<void>;
	reset(): void;
}

export const HaltestellenStateKey: InjectionKey<HaltestellenState> = Symbol('HaltestellenState');

export function useHaltestellenState(): HaltestellenState {
	const state = AppContext.instance.inject(HaltestellenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des HaltestellenState über provide in der main.ts eingebunden");
	}
	return state;
}

