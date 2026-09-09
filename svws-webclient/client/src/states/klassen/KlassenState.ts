import { type InjectionKey } from "vue";

import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { GenericAuswahlState } from "../GenericAuswahlState";

import type { KlassenListeManager } from "./KlassenListeManager";

/**
 * Das Interface für den State für die Auswahlliste der Klassen
 */
export interface KlassenState extends GenericAuswahlState<KlassenListeManager> {

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
	 * Setzt bei der Klassenliste die default-Sortierung
	 *
	 * @returns eine leere Promise
	 */
	setzeDefaultSortierung(): Promise<void>;

	/**
	 * Fügt eine neue Klasse hinzu
	 *
	 * @param partialKlasse   die Daten zum Anlegen der neuen Klasse
	 *
	 * @returns eine leere Promise
	 */
	add(partialKlasse: Partial<KlassenDaten>): Promise<void>;

	/**
	 * Triggert das Routing zu den Daten des übergebenen Schülers
	 *
	 * @param eintrag   der Schüler, zu dem geroutet werden soll
	 *
	 * @returns eine leere Promise
	 */
	gotoSchueler(eintrag: Schueler): Promise<void>;

	/**
	 * Triggert das Routing zu den Daten des übergebenen Lehrers
	 *
	 * @param eintrag   der Lehrer, zu dme geroutet werden soll
	 *
	 * @returns eine leere Promise
	 */
	gotoLehrer(eintrag: LehrerListeEintrag): Promise<void>;

	/**
	 * Fügt einen Datensatz für eine Klassenleitung hinzu
	 *
	 * @param idLehrer   die ID der Klassenleitung (Lehrer)
	 * @param idKlasse   die ID der Klasse, wo der Lehrer die Klassenleitung übernimmt.
	 *
	 * @returns eine leere Promise
	 */
	addKlassenleitung(idLehrer: number, idKlasse: number): Promise<void>;

	/**
	 * Entfernt einen Datensatz für eine Klassenleitung bei der aktuell ausgewählten Klasse
	 *
	 * @param eintrag   der Lehrer, welcher auch der Klassenleitung herausgenommen wird
	 *
	 * @returns eine leere Promise
	 */
	removeKlassenleitung(eintrag: LehrerListeEintrag): Promise<void>;

	/**
	 * Aktualisiert die Reihenfolge bei der Klassenleitung, in dem der Lehrer-Eintrag
	 * um eine Position nach oben oder unten verschoben wird.
	 *
	 * @param idLehrer   die ID des Klassenleitung, deren Position angepasst werden soll
	 * @param erhoehe    true, wenn nach oben verschoben werden soll, und false, wenn nach unten verschoben werden soll
	 *
	 * @returns eine leere Promise
	 */
	updateReihenfolgeKlassenleitung(idLehrer: number, erhoehe: boolean): Promise<void>;

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
}

export const KlassenStateKey: InjectionKey<KlassenState> = Symbol('KlassenState');

export function useKlassenState(): KlassenState {
	const state = AppContext.instance.inject(KlassenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des KlassenState über provide in der main.ts eingebunden");
	}
	return state;
}
