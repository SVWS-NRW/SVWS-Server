import type { InjectionKey } from "vue";

import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des LernplattformenStates
 */
export interface LernplattformenState {
	readonly lernplattformen: KatalogState<Lernplattform>;
	init(): Promise<void>;
	reset(): void;
}

export const LernplattformenStateKey: InjectionKey<LernplattformenState> = Symbol('LernplattformenState');

export function useLernplattformenState(): LernplattformenState {
	const state = AppContext.instance.inject(LernplattformenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des LernplattformenState über provide in der main.ts eingebunden");
	}
	return state;
}

