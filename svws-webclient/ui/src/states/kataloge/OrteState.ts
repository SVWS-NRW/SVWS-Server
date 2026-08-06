import type { InjectionKey } from "vue";
import type { KatalogState } from "./KatalogState";
import type { OrtsteilKatalogEintrag } from "../../../../core/src/core/data/kataloge/OrtsteilKatalogEintrag";
import type { List } from "../../../../core/src/java/util/List";
import type { OrtKatalogEintrag } from "../../../../core/src/core/data/kataloge/OrtKatalogEintrag";
import { DeveloperNotificationException } from "../../../../core/src/core/exceptions/DeveloperNotificationException";
import { AppContext } from "../../AppContext";

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
