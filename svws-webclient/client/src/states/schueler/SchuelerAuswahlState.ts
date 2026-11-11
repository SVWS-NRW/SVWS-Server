import type { InjectionKey } from "vue";

import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { SchuelerNeu } from "@core/asd/data/schueler/SchuelerNeu";
import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";

import type { AuswahlKatalog } from "../AuswahlKatalog";
import type { AuswahlStateMitStundenplaenen } from "../AuswahlStateMitStundenplaenen";
import type { GenericAuswahlState } from "../GenericAuswahlState";

import type { SchuelerAuswahlFilter } from "./SchuelerAuswahlFilter";
import type { SchuelerStammdatenState } from "./SchuelerStammdatenState";

/**
 * Das Interface für den State für die Auswahlliste der Schüler
 */
export interface SchuelerAuswahlState extends GenericAuswahlState<SchuelerListeEintrag, SchuelerStammdaten>, AuswahlStateMitStundenplaenen {

	/**
	 * Gibt den Filter der Auswahlliste zurück.
	 *
	 * @returns der Filter
	 */
	get filter(): SchuelerAuswahlFilter;

	/**
	 * Gibt die Jahrgänge zurück.
	 *
	 * @returns die Liste der Jahrgänge
	 */
	get jahrgaenge(): AuswahlKatalog<JahrgangsDaten>;

	/**
	 * Gibt die Klassen des Schuljahresabschnittes der Auswahl zurück.
	 *
	 * @returns die Liste der Klassen
	 */
	get klassen(): AuswahlKatalog<KlassenDaten>;

	/**
	 * Gibt die Abiturjahrgänge des Schuljahresabschnittes zurück.
	 *
	 * @returns die Liste der Abiturjahrgänge
	 */
	get abiturjahrgaenge(): AuswahlKatalog<GostJahrgang>;

	/**
	 * Gibt die Daten zu der Klasse mit der ID zurück. Dies schließt auch Klassen anderer Schuljahresabschnitte ein, in denen Schüler der Liste geführt werden.
	 *
	 * @param idKlasse   die ID der Klasse
	 *
	 * @returns die Daten der Klasse oder null, falls die ID unbekannt ist
	 */
	klasseGetOrNull(idKlasse: number): KlassenDaten | null;

	/**
	 * Gibt zurück, ob der Schüler im Schuljahresabschnitt der Auswahl geführt wird.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @returns true, wenn der Schüler im Schuljahresabschnitt der Auswahl geführt wird, und ansonsten false
	 */
	schuelerIstImSchuljahresabschnitt(idSchueler: number): boolean;

	/**
	 * Gibt den Schuljahresabschnitt, in dem der Schüler geführt wird, als Text zurück.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @returns der Schuljahresabschnitt als Text (Schuljahr.Abschnitt) oder ein Platzhalter
	 */
	schuelerSchuljahresabschnittAsString(idSchueler: number): string;

	/**
	 * Gibt den Schuljahresabschnitt des gewählten Schülers zurück. Dieser kann vom Schuljahresabschnitt der Auswahlliste abweichen.
	 *
	 * @throws DeveloperNotificationException   wenn kein Schüler gewählt ist oder der Schuljahresabschnitt fehlt
	 */
	get schuljahresabschnitt(): Schuljahresabschnitt;

	/**
	 * Gibt den Schuljahresabschnitt zurück, in dem der gewählte Schüler geführt wird. Dieser kann vom Schuljahresabschnitt der Auswahlliste abweichen (z.B. bei einer Neuaufnahme).
	 *
	 * @throws DeveloperNotificationException   wenn kein Schüler gewählt ist oder der Schuljahresabschnitt fehlt
	 */
	get schuljahresabschnittSchueler(): Schuljahresabschnitt;

	/**
	 * Gibt den State für die Stammdaten der Schüler zurück.
	 *
	 * @returns der State für die Stammdaten
	 */
	get stammdaten(): SchuelerStammdatenState;

	/**
	 * Gibt den State für die Lehrer zurück.
	 *
	 * @returns der State für die Lehrer
	 */
	get lehrer(): AuswahlKatalog<LehrerListeEintrag>;

	/**
	 * Fügt einen neuen Schüler hinzu
	 *
	 * @param data   die Daten zum Anlegen des neuen Schülers
	 *
	 * @returns eine leere Promise
	 */
	add(data: Partial<SchuelerNeu>): Promise<SchuelerStammdaten>;

	/**
	 * Prüft, ob die Berechtigung zum Löschen von Schülern vorliegt oder nicht.
	 *
	 * @returns die Information, ob die Berechtigung vorliegt mit einem Log, falls dies nicht der Fall ist
	 */
	deleteCheck(): [boolean, List<string>];

}

export const SchuelerAuswahlStateKey: InjectionKey<SchuelerAuswahlState> = Symbol('SchuelerAuswahlState');

export function useSchuelerAuswahlState(): SchuelerAuswahlState {
	const state = AppContext.instance.inject(SchuelerAuswahlStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des SchuelerAuswahlState über provide in der main.ts eingebunden");
	}
	return state;
}
