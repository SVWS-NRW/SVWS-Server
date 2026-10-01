import type { InjectionKey } from "vue";

import type { Abteilung } from "@core/core/data/schule/Abteilung";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des AbteilungenStates
 */
export interface AbteilungenState {
	readonly abteilungen: KatalogState<Abteilung>;
	init(): Promise<void>;
	reset(): void;
}

export const AbteilungenStateKey: InjectionKey<AbteilungenState> = Symbol('AbteilungenState');

export function useAbteilungenState(): AbteilungenState {
	const state = AppContext.instance.inject(AbteilungenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des AbteilungenState über provide in der main.ts eingebunden");
	}
	return state;
}

