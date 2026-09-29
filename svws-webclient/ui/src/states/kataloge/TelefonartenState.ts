import type { InjectionKey } from "vue";

import type { Telefonart } from "@core/core/data/schule/Telefonart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des TelefonartenStates
 */
export interface TelefonartenState {
	readonly telefonarten: KatalogState<Telefonart>;
	init(): Promise<void>;
	reset(): void;
}

export const TelefonartenStateKey: InjectionKey<TelefonartenState> = Symbol('TelefonartenState');

export function useTelefonartenState(): TelefonartenState {
	const state = AppContext.instance.inject(TelefonartenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des TelefonartenState über provide in der main.ts eingebunden");
	}
	return state;
}

