import type { InjectionKey } from "vue";

import type { Floskel } from "@core/core/data/schule/Floskel";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des FloskelnStates
 */
export interface FloskelnState {
	readonly floskeln: KatalogState<Floskel>;
	init(): Promise<void>;
	reset(): void;
}

export const FloskelnStateKey: InjectionKey<FloskelnState> = Symbol('FloskelnState');

export function useFloskelnState(): FloskelnState {
	const state = AppContext.instance.inject(FloskelnStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des FloskelnState über provide in der main.ts eingebunden");
	}
	return state;
}

