import type { InjectionKey } from "vue";

import type { Merkmal } from "@core/core/data/schule/Merkmal";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des MerkmaleStates
 */
export interface MerkmaleState {
	readonly merkmale: KatalogState<Merkmal>;
	init(): Promise<void>;
	reset(): void;
}

export const MerkmaleStateKey: InjectionKey<MerkmaleState> = Symbol('MerkmaleState');

export function useMerkmaleState(): MerkmaleState {
	const state = AppContext.instance.inject(MerkmaleStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des MerkmaleState über provide in der main.ts eingebunden");
	}
	return state;
}

