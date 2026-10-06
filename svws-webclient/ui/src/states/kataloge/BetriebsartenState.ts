import type { InjectionKey } from "vue";

import type { Betriebsart } from "@core/core/data/schule/Betriebsart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des BetriebsartenStates
 */
export interface BetriebsartenState {
	readonly betriebsarten: KatalogState<Betriebsart>;
	init(): Promise<void>;
	reset(): void;
}

export const BetriebsartenStateKey: InjectionKey<BetriebsartenState> = Symbol('BetriebsartenState');

export function useBetriebsartenState(): BetriebsartenState {
	const state = AppContext.instance.inject(BetriebsartenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des BetriebsartenState über provide in der main.ts eingebunden");
	}
	return state;
}
