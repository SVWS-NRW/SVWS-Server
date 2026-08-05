import { type InjectionKey } from "vue";
import { DeveloperNotificationException } from "../../../core/src/core/exceptions/DeveloperNotificationException";
import { AppContext } from "../AppContext";

/**
 * Die Schnittstelle für den Zustand der Auskunftinfos "Datenschutz" und "Impressum"
 */
export interface AuskunftState {

	/**
	 * Wenn vorhanden, gibt es den Markdown-String der Datenschutzauskunft zurück.
	 *
	 * @returns den Markdown-String der Datenschutzauskunft oder null, wenn nicht vorhanden
	 */
	get datenschutz(): string | null;

	/**
	 * Wenn vorhanden, gibt es den Markdown-String des Impressums zurück.
	 *
	 * @returns den Markdown-String des Impressums oder null, wenn nicht vorhanden
	 */
	get impressum(): string | null


}

export const AuskunftStateKey: InjectionKey<AuskunftState> = Symbol('AuskunftState');

export function useAuskunftState(): AuskunftState {
	const state = AppContext.instance.inject(AuskunftStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des AuskunftState über provide in der main.ts eingebunden");
	}
	return state;
}
