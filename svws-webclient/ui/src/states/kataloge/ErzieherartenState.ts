import type { InjectionKey } from "vue";

import type { Erzieherart } from "@core/core/data/erzieher/Erzieherart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des ErzieherartenStates
 */
export interface ErzieherartenState {
	readonly erzieherarten: KatalogState<Erzieherart>;
	init(): Promise<void>;
	reset(): void;
}

export const ErzieherartenStateKey: InjectionKey<ErzieherartenState> = Symbol('ErzieherartenState');

export function useErzieherartenState(): ErzieherartenState {
	const state = AppContext.instance.inject(ErzieherartenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des ErzieherartenState über provide in der main.ts eingebunden");
	}
	return state;
}

