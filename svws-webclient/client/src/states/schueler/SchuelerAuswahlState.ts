import type { InjectionKey } from "vue";

import type { SchuelerNeu } from "@core/asd/data/schueler/SchuelerNeu";
import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { SchuelerTelefon } from "@core/core/data/schueler/SchuelerTelefon";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";
import type { PendingStateManager } from "@ui/ui/wrapper/PendingStateManager";

import type { GenericAuswahlState } from "../GenericAuswahlState";

import type { SchuelerListeManager } from "./SchuelerListeManager";

/**
 * Das Interface für den State für die Auswahlliste der Schüler
 */
export interface SchuelerAuswahlState extends GenericAuswahlState<SchuelerListeManager> {

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
	 * Fügt einen neuen Schüler hinzu
	 *
	 * @param data   die Daten zum Anlegen des neuen Schülers
	 *
	 * @returns eine leere Promise
	 */
	add(data: Partial<SchuelerNeu>): Promise<SchuelerStammdaten>;

	/**
	 * Führt die Patches aus dem übergebenen Pending State aus
	 *
	 * @param pendingStateManager   der Manager mit dem Pending State
	 *
	 * @returns eine leere Promise
	 */
	patchMultiple(pendingStateManager: PendingStateManager<any>): Promise<void>;

	/**
	 * Prüft, ob die Berechtigung zum Löschen von Schülern vorliegt oder nicht.
	 *
	 * @returns die Information, ob die Berechtigung vorliegt mit einem Log, falls dies nicht der Fall ist
	 */
	deleteSchuelerCheck(): [boolean, List<string>];

	/**
	 * Aktualisiert die Map für die Stundenpläne über eine Abfrage bei der API
	 *
	 * @returns eine leere Promise
	 */
	updateMapStundenplaene(): Promise<void>;

	/**
	 * Gibt die Map der Stundenpläne für den aktuellen Schuljahresabschnitt zurück.
	 *
	 * @returns die Map mit den Stundenplänen.
	 */
	get mapStundenplaene(): Map<number, StundenplanListeEintrag>;

	get listTelefoneintraege(): List<SchuelerTelefon>;

	addTelefoneintrag(data: Partial<SchuelerTelefon>, idSchueler: number): Promise<void>;

	patchTelefoneintrag(data: Partial<SchuelerTelefon>, idEintrag: number): Promise<void>;

	deleteTelefoneintrage(idsEintraege: List<number>): Promise<void>;
}

export const SchuelerAuswahlStateKey: InjectionKey<SchuelerAuswahlState> = Symbol('SchuelerAuswahlState');

export function useSchuelerAuswahlState(): SchuelerAuswahlState {
	const state = AppContext.instance.inject(SchuelerAuswahlStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des SchuelerAuswahlState über provide in der main.ts eingebunden");
	}
	return state;
}
