import { type InjectionKey } from "vue";

import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { KursLehrer } from "@core/asd/data/kurse/KursLehrer";
import type { Schueler } from "@core/asd/data/schueler/Schueler";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";

import type { GenericAuswahlState } from "../GenericAuswahlState";

import type { KursListeManager } from "./KursListeManager";

/**
 * Das Interface für den State für die Auswahlliste der Kurse
 */
export interface KurseAuswahlState extends GenericAuswahlState<KursListeManager> {

	/**
	 * Initialisiert den State für den ausgewählten Schuljahresabschnitt. Es werden die Daten für diesen Abschnitt geladen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param force                    gibt an, ob das Laden der Daten auch erzwungen werden soll, wenn die ID des
	 *                                 Schuljahresabschnittes bereits gesetzt ist (z.B. beim Betreten einer Route)
	 *
	 * @returns ein Promise mit der ID der Auswahl oder null
	 */
	init(idSchuljahresabschnitt: number, force: boolean): Promise<number | null>;

	/**
	 * Fügt einen neuen Kurs hinzu
	 *
	 * @param partialKurs   die Daten zum Anlegen des neuen Kurses
	 *
	 * @returns eine leere Promise
	 */
	add(partialKurs: Partial<KursDaten>): Promise<void>;

	get filterNurSichtbar(): boolean;

	setFilterNurSichtbar(value: boolean): Promise<void>;

	addKursLehrer(data: Partial<KursLehrer>, idKurs: number): Promise<void>;

	patchKursLehrer(data: Partial<KursLehrer>, idKurs: number, idLehrer: number): Promise<void>;

	deleteKursLehrer(lehrerIds: List<number>, idKurs: number): Promise<void>

	/**
	 * Triggert das Routing zu den Daten des übergebenen Schülers
	 *
	 * @param eintrag   der Schüler, zu dem geroutet werden soll
	 *
	 * @returns eine leere Promise
	 */
	gotoSchueler(eintrag: Schueler): Promise<void>;

}

export const KurseAuswahlStateKey: InjectionKey<KurseAuswahlState> = Symbol('KurseAuswahlState');

export function useKurseAuswahlState(): KurseAuswahlState {
	const state = AppContext.instance.inject(KurseAuswahlStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des KurseAuswahlState über provide in der main.ts eingebunden");
	}
	return state;
}
