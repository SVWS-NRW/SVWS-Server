import { inject, type InjectionKey } from "vue";
import type { Schuljahresabschnitt } from "../../../core/src/asd/data/schule/Schuljahresabschnitt";
import { DeveloperNotificationException } from "../../../core/src/core/exceptions/DeveloperNotificationException";
import type { List } from "../../../core/src/java/util/List";

/**
 * Die Schnittstelle für den Zustand der Schuljahresabschnitte und des aktuell ausgewählten Abschnitts
 */
export interface AbschnittState {

	/**
	 * Die Methode zum Setzen des aktuellen Schuljahresabschnittes
	 *
	 * @param id   die ID des zu setzenden Schuljahresabschnittes
	 *
	 * @returns true, wenn erfolgreich gesetzt
	 */
	setAuswahl(id: number): Promise<void>;

	/**
	 * Die Methode zum Holen eines Schuljahresabschnittes
	 *
	 * @param id   die ID des zu setzenden Schuljahresabschnittes
	 *
	 * @returns der Schuljahresabschnitt, wenn erfolgreich, ansonsten null
	 */
	getOrNull(id: number): Schuljahresabschnitt | null;

	/**
	 * Die Methode zum Holen eines Schuljahresabschnittes
	 *
	 * @param id   die ID des zu setzenden Schuljahresabschnittes
	 *
	 * @returns der Schuljahresabschnitt
	 */
	get(id: number): Schuljahresabschnitt;

	/**
	 * Der Getter zum Holen des aktuellen Schuljahresabschnittes
	 *
	 * @returns der Schuljahresabschnitt
	 */
	get auswahl(): Schuljahresabschnitt;

	/**
	 * Der Getter zum Holen der verfügbaren Schuljahresabschnittes
	 *
	 * @returns der Iterable der Schuljahresabschnitte
	 */
	get alle(): List<Schuljahresabschnitt>;

	/**
	 * Bestimmt den Schuljahresabschnitt anhand des übergebenen Schuljahres und dem Abschnitt.
	 *
	 * @param schuljahr das Schuljahr
	 * @param halbjahr der Abschnitt
	 *
	 * @returns der Schuljahresabschnitt
	 */
	getBySchuljahrUndHalbjahr(schuljahr: number, halbjahr: number): Schuljahresabschnitt | null;

}

export const AbschnittStateKey: InjectionKey<AbschnittState> = Symbol('AbschnittState');

export function useAbschnittState(): AbschnittState {
	const state = inject(AbschnittStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des AbschnittState über provide in der main.ts eingebunden");
	}
	return state;
}
