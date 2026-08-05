import { type InjectionKey } from "vue";
import type { SchuleStammdaten } from "../../../core/src/asd/data/schule/SchuleStammdaten";
import { DeveloperNotificationException } from "../../../core/src/core/exceptions/DeveloperNotificationException";
import type { ValidatorKontext } from "../../../core/src/asd/validate/ValidatorKontext";
import type { Schulform } from "../../../core/src/asd/types/schule/Schulform";
import type { Schulgliederung } from "../../../core/src/asd/types/schule/Schulgliederung";
import type { List } from "../../../core/src/java/util/List";
import type { Schuljahresabschnitt } from "../../../core/src/asd/data/schule/Schuljahresabschnitt";
import { AppContext } from "../AppContext";

/**
 * Die Schnittstelle für die Daten der Schule inkl. des aktuellen Abschnitts der Schule
 */
export interface SchuleState {

	/**
	 * Gibt die Stammdaten der Schule zurück, sofern bereits ein Login stattgefunden hat.
	 *
	 * @returns die Stammdaten
	 */
	get stammdaten(): SchuleStammdaten;

	/**
	 * Gibt den Validator-Kontext für die Validierung von Statistik-relevanten Daten zurück.
	 *
	 * @returns der Validator-Kontext
	 */
	get validatorKontext(): ValidatorKontext;

	/**
	 * Gibt die Schulform der Schule zurück, wo der Benutzer angemeldet ist.
	 *
	 * @returns die Schulform
	 */
	get schulform(): Schulform;

	/**
	 * Gibt die zulässigen Schulgliederungen für die Schule zurück, wo der
	 * Benutzer angemeldet ist.
	 *
	 * @returns eine Liste mit den Schulgliederungen
	 */
	get schulgliederungen(): List<Schulgliederung>;


	/**
	 * Gibt die Schulform der Schule zurück, wo der Benutzer angemeldet ist.
	 *
	 * @returns die Schulform
	 */
	get schuljahr(): number;


	/**
	 * Gibt den aktuellen Schulabschnitt zurück, in dem sich die Schule befindet.
	 *
	 * @returns der aktuelle Schulabschnitt
	 */
	get abschnitt(): Schuljahresabschnitt;

}

export const SchuleStateKey: InjectionKey<SchuleState> = Symbol('SchuleState');

export function useSchuleState(): SchuleState {
	const state = AppContext.instance.inject(SchuleStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des SchuleState über provide in der main.ts eingebunden");
	}
	return state;
}
