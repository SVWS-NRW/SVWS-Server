import type { InjectionKey } from "vue";

import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { KatalogState } from "./KatalogState";

/** Erweiterter KatalogState für Herkunftschulen mit Filtermethoden nach Statistikschulnummer */
export interface HerkunftSchuleKatalogState extends KatalogState<SchulEintrag> {
	readonly bySchulnummerStatistikFilteredByEigeneSchulform: Map<string, SchulEintrag>;
}

/**
 * Die Schnittstelle für den Zustand des SchulenStates
 */
export interface HerkunftschulenState {
	readonly herkunftschulen: HerkunftSchuleKatalogState;
	init(): Promise<void>;
	reset(): void;
}

export const HerkunftschulenStateKey: InjectionKey<HerkunftschulenState> = Symbol('HerkunftschulenState');

export function useHerkunftschulenState(): HerkunftschulenState {
	const state = AppContext.instance.inject(HerkunftschulenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des HerkunftschulenState über provide in der main.ts eingebunden");
	}
	return state;
}

