import type { InjectionKey } from "vue";

import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des ReligionenStates
 */
export interface ReligionenState {
	readonly religionen: KatalogState<ReligionEintrag>;
	init(): Promise<void>;
	reset(): void;
}

export const ReligionenStateKey: InjectionKey<ReligionenState> = Symbol('ReligionenState');

export function useReligionenState(): ReligionenState {
	const state = AppContext.instance.inject(ReligionenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des ReligionenState über provide in der main.ts eingebunden");
	}
	return state;
}

