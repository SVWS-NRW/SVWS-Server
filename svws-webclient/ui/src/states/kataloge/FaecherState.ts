import type { InjectionKey } from "vue";

import type { FachDaten } from "@core/core/data/fach/FachDaten";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des FaecherStates
 */
export interface FaecherState {
	readonly faecher: KatalogState<FachDaten>;
	init(): Promise<void>;
	reset(): void;
}

export const FaecherStateKey: InjectionKey<FaecherState> = Symbol('FaecherState');

export function useFaecherState(): FaecherState {
	const state = AppContext.instance.inject(FaecherStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des FaecherState über provide in der main.ts eingebunden");
	}
	return state;
}

