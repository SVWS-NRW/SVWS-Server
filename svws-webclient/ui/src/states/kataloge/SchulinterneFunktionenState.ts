import type { InjectionKey } from "vue";

import type { SchulinterneFunktion } from "@core/core/data/schule/SchulinterneFunktion";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des SchulinterneFunktionenState
 */
export interface SchulinterneFunktionenState {
	readonly schulinterneFunktionen: KatalogState<SchulinterneFunktion>;

	init(): Promise<void>;

	reset(): void;
}

export const SchulinterneFunktionenStateKey: InjectionKey<SchulinterneFunktionenState> = Symbol('SchulinterneFunktionenState');

export function useSchulinterneFunktionenState(): SchulinterneFunktionenState {
	const state = AppContext.instance.inject(SchulinterneFunktionenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des SchulinterneFunktionenState über provide in der main.ts eingebunden");
	}
	return state;
}
