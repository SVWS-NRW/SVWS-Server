import { type InjectionKey } from "vue";

import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";

import type { AuswahlKatalog } from "../AuswahlKatalog";
import type { GenericAuswahlState } from "../GenericAuswahlState";
import type { SchuelerStatusFilter } from "../SchuelerStatusFilter";

import type { KurseAuswahlFilter } from "./KurseAuswahlFilter";
import type { KurseStammdatenState } from "./KurseStammdatenState";

/**
 * Das Interface für den State für die Auswahlliste der Kurse
 */
export interface KurseAuswahlState extends GenericAuswahlState<KursDaten, KursDaten> {

	/**
	 * Fügt einen neuen Kurs hinzu
	 *
	 * @param partialKurs   die Daten zum Anlegen des neuen Kurses
	 *
	 * @returns eine leere Promise
	 */
	add(partialKurs: Partial<KursDaten>): Promise<void>;

	/**
	 * Gibt den Filter der Auswahlliste zurück.
	 *
	 * @returns der Filter
	 */
	get filter(): KurseAuswahlFilter;

	/**
	 * Gibt die Jahrgänge zurück.
	 *
	 * @returns die Liste der Jahrgänge
	 */
	get jahrgaenge(): AuswahlKatalog<JahrgangsDaten>;

	/**
	 * Gibt die Fächer zurück, die sichtbar oder in einem Kurs des Schuljahresabschnittes verwendet sind.
	 *
	 * @returns die Liste der Fächer
	 */
	get faecher(): AuswahlKatalog<FachDaten>;

	/**
	 * Gibt den Schuljahresabschnitt des gewählten Kurses zurück. Dieser kann vom Schuljahresabschnitt der Auswahlliste abweichen.
	 *
	 * @throws DeveloperNotificationException   wenn kein Kurs gewählt ist oder der Schuljahresabschnitt fehlt
	 */
	get schuljahresabschnitt(): Schuljahresabschnitt;

	/**
	 * Gibt den State für die Stammdaten der Kurse zurück.
	 *
	 * @returns der State für die Stammdaten
	 */
	get stammdaten(): KurseStammdatenState;

	/**
	 * Gibt den State für die Lehrer des Schuljahresabschnittes zurück.
	 *
	 * @returns der State für die Lehrer
	 */
	get lehrer(): AuswahlKatalog<LehrerListeEintrag>;

	/**
	 * Gibt den Filter nach dem Schüler-Status für die Schülerliste in den Daten zurück.
	 *
	 * @returns der Filter
	 */
	get schuelerstatus(): SchuelerStatusFilter;

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
