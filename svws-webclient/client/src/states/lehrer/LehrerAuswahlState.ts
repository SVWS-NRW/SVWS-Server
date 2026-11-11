import type { InjectionKey } from "vue";

import type { LehrerStammdaten } from "@core/asd/data/lehrer/LehrerStammdaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";

import type { AuswahlStateMitStundenplaenen } from "../AuswahlStateMitStundenplaenen";
import type { GenericAuswahlState } from "../GenericAuswahlState";

import type { LehrerAuswahlFilter } from "./LehrerAuswahlFilter";
import type { LehrerPersonaldatenState } from "./LehrerPersonaldatenState";
import type { LehrerStammdatenState } from "./LehrerStammdatenState";

/**
 * Das Interface für den State für die Auswahlliste der Lehrer
 */
export interface LehrerAuswahlState extends GenericAuswahlState<LehrerListeEintrag, LehrerStammdaten>, AuswahlStateMitStundenplaenen {

	/**
	 * Gibt den Filter der Auswahlliste zurück.
	 *
	 * @returns der Filter
	 */
	get filter(): LehrerAuswahlFilter;

	/**
	 * Gibt den State für die Stammdaten der Lehrer zurück.
	 *
	 * @returns der State für die Stammdaten
	 */
	get stammdaten(): LehrerStammdatenState;

	/**
	 * Fügt einen neuen Lehrer hinzu
	 *
	 * @param data   die Daten zum Anlegen des neuen Lehrers
	 *
	 * @returns eine leere Promise
	 */
	add(data: Partial<LehrerStammdaten>): Promise<LehrerStammdaten>;

	/**
	 * Prüft, ob die Berechtigung zum Löschen vorliegt oder nicht.
	 *
	 * @returns die Information, ob die Berechtigung vorliegt mit einem Log, falls dies nicht der Fall ist
	 */
	deleteCheck(): [boolean, List<string>];

	/**
	 * Gibt den State für die Personaldaten des ausgewählten Lehrers zurück.
	 *
	 * @returns der State für die Personaldaten
	 */
	get personaldaten(): LehrerPersonaldatenState;

	/**
	 * Lädt die Personaldaten und die Unterrichtsfächer des ausgewählten Lehrers.
	 *
	 * @returns eine leere Promise
	 */
	loadPersonaldaten(): Promise<void>;

	unloadPersonaldaten(): Promise<void>;

}

export const LehrerAuswahlStateKey: InjectionKey<LehrerAuswahlState> = Symbol('LehrerAuswahlState');

export function useLehrerAuswahlState(): LehrerAuswahlState {
	const state = AppContext.instance.inject(LehrerAuswahlStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des LehrerAuswahlState über provide in der main.ts eingebunden");
	}
	return state;
}
