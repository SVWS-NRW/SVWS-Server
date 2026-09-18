import type { InjectionKey } from "vue";

import type { Beschaeftigungsart } from "@core/core/data/schule/Beschaeftigungsart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des OrteStates
 */
export interface BeschaeftigungsartenState {
	readonly beschaeftigungsarten: KatalogState<Beschaeftigungsart>;
	init(): Promise<void>;
	reset(): void;
}

export const BeschaeftigungsartenStateKey: InjectionKey<BeschaeftigungsartenState> = Symbol('BeschaeftigungsartenState');

export function useBeschaeftigungsartenState(): BeschaeftigungsartenState {
	const state = AppContext.instance.inject(BeschaeftigungsartenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des BeschaeftigungsartenState über provide in der main.ts eingebunden");
	}
	return state;
}
