import type { InjectionKey } from "vue";

import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/**
 * Die Schnittstelle für den Zustand des VermerkartenStates
 */
export interface VermerkartenState {
	readonly vermerkarten: KatalogState<VermerkartEintrag>;
	init(): Promise<void>;
	reset(): void;
}

export const VermerkartenStateKey: InjectionKey<VermerkartenState> = Symbol('VermerkartenState');

export function useVermerkartenState(): VermerkartenState {
	const state = AppContext.instance.inject(VermerkartenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des VermerkartenState über provide in der main.ts eingebunden");
	}
	return state;
}

