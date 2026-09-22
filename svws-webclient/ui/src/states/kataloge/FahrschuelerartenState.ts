import type { InjectionKey } from "vue";

import type { Fahrschuelerart } from "@core/core/data/schule/Fahrschuelerart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des FahrschuelerartenState
 */
export interface FahrschuelerartenState {
	readonly fahrschuelerarten: KatalogState<Fahrschuelerart>;
	init(): Promise<void>;
	reset(): void;
}

export const FahrschuelerartenStateKey: InjectionKey<FahrschuelerartenState> = Symbol('FahrschuelerartenState');

export function useFahrschuelerartenState(): FahrschuelerartenState {
	const state = AppContext.instance.inject(FahrschuelerartenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des FahrschuelerartenState über provide in der main.ts eingebunden");
	}
	return state;
}
