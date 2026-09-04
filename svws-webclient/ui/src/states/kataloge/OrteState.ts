import type { InjectionKey } from "vue";
import type { KatalogState } from "./KatalogState";
import type { OrtKatalogEintrag } from "@core/core/data/kataloge/OrtKatalogEintrag";
import type { OrtsteilKatalogEintrag } from "@core/core/data/kataloge/OrtsteilKatalogEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";

/** Erweiterter KatalogState für Ortsteile mit Filtermethoden nach Ort */
export interface OrtsteileKatalogState extends KatalogState<OrtsteilKatalogEintrag> {
	/** Gibt alle Ortsteile eines Ortes als Liste zurück */
	listByOrtId(idOrt: number | null): List<OrtsteilKatalogEintrag>;
	/** Gibt alle Ortsteile eines Ortes als Map<id, Ortsteil> zurück */
	byOrtId(idOrt: number | null): Map<number, OrtsteilKatalogEintrag>;
}
/**
 * Die Schnittstelle für den Zustand des OrteStates
 */
export interface OrteState {
	readonly orte: KatalogState<OrtKatalogEintrag>;
	readonly ortsteile: OrtsteileKatalogState;
	init(): Promise<void>;
	reset(): void;
}

export const OrteStateKey: InjectionKey<OrteState> = Symbol('OrteState');

export function useOrteState(): OrteState {
	const state = AppContext.instance.inject(OrteStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des OrteState über provide in der main.ts eingebunden");
	}
	return state;
}
