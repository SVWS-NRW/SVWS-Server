import type { InjectionKey } from "vue";

import type { KatalogEntlassgrund } from "@core/core/data/kataloge/KatalogEntlassgrund";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des EntlassgruendeStates
 */
export interface EntlassgruendeState {
	readonly entlassgruende: KatalogState<KatalogEntlassgrund>;
	init(): Promise<void>;
	reset(): void;
}

export const EntlassgruendeStateKey: InjectionKey<EntlassgruendeState> = Symbol('EntlassgruendeState');

export function useEntlassgruendeState(): EntlassgruendeState {
	const state = AppContext.instance.inject(EntlassgruendeStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des EntlassgruendeState über provide in der main.ts eingebunden");
	}
	return state;
}
