import type { InjectionKey } from "vue";

import type { Beschaeftigungsart } from "@core/core/data/schule/Beschaeftigungsart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des OrteStates
 */
export interface BeschaeftigungsartState {
	readonly beschaeftigungsarten: KatalogState<Beschaeftigungsart>;
	init(): Promise<void>;
	reset(): void;
}

export const BeschaeftigungsartStateKey: InjectionKey<BeschaeftigungsartState> = Symbol('BeschaeftigungsartState');

export function useBeschaeftigungsartState(): BeschaeftigungsartState {
	const state = AppContext.instance.inject(BeschaeftigungsartStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des BeschaeftigungsartState über provide in der main.ts eingebunden");
	}
	return state;
}
