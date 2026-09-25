import type { InjectionKey } from "vue";

import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des KindergaertenStates
 */
export interface KindergaertenState {
	readonly kindergaerten: KatalogState<Kindergarten>;
	init(): Promise<void>;
	reset(): void;
}

export const KindergaertenStateKey: InjectionKey<KindergaertenState> = Symbol('KindergaertenState');

export function useKindergaertenState(): KindergaertenState {
	const state = AppContext.instance.inject(KindergaertenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des KindergaertenState über provide in der main.ts eingebunden");
	}
	return state;
}

