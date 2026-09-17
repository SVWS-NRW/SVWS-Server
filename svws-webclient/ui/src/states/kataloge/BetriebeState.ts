import type { InjectionKey } from "vue";

import type { Betrieb } from "@core/core/data/schule/Betrieb";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des BetriebeStates
 */
export interface BetriebeState {
	readonly betriebe: KatalogState<Betrieb>;
	init(): Promise<void>;
	reset(): void;
}

export const BetriebeStateKey: InjectionKey<BetriebeState> = Symbol('BetriebeState');

export function useBetriebeState(): BetriebeState {
	const state = AppContext.instance.inject(BetriebeStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des BetriebeState über provide in der main.ts eingebunden");
	}
	return state;
}
