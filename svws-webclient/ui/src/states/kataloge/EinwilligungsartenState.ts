import type { InjectionKey } from "vue";

import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des EinwilligungsartenStates
 */
export interface EinwilligungsartenState {
	readonly einwilligungsarten: KatalogState<Einwilligungsart>;
	init(): Promise<void>;
	reset(): void;
}

export const EinwilligungsartenStateKey: InjectionKey<EinwilligungsartenState> = Symbol('EinwilligungsartenState');

export function useEinwilligungsartenState(): EinwilligungsartenState {
	const state = AppContext.instance.inject(EinwilligungsartenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des EinwilligungsartenState über provide in der main.ts eingebunden");
	}
	return state;
}

