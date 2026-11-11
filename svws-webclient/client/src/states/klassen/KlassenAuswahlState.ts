import { type InjectionKey } from "vue";

import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KlassenDatenMinimal } from "@core/asd/data/klassen/KlassenDatenMinimal";
import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";

import type { AuswahlKatalog } from "../AuswahlKatalog";
import type { AuswahlStateMitStundenplaenen } from "../AuswahlStateMitStundenplaenen";
import type { GenericAuswahlState } from "../GenericAuswahlState";
import type { SchuelerStatusFilter } from "../SchuelerStatusFilter";

import type { KlassenAuswahlFilter } from "./KlassenAuswahlFilter";
import type { KlassenStammdatenState } from "./KlassenStammdatenState";

/**
 * Das Interface für den State für die Auswahlliste der Klassen
 */
export interface KlassenAuswahlState extends GenericAuswahlState<KlassenListeEintrag, KlassenDaten>, AuswahlStateMitStundenplaenen {

	/**
	 * Gibt den Filter der Auswahlliste zurück.
	 *
	 * @returns der Filter
	 */
	get filter(): KlassenAuswahlFilter;

	/**
	 * Gibt die Jahrgänge zurück.
	 *
	 * @returns die Liste der Jahrgänge
	 */
	get jahrgaenge(): AuswahlKatalog<JahrgangsDaten>;

	/**
	 * Gibt die Klassen des Vorabschnittes zugeordnet zu deren ID zurück.
	 *
	 * @returns die Map mit den Klassen des Vorabschnittes
	 */
	get klassenByIdVorabschnitt(): AuswahlKatalog<KlassenDatenMinimal>;

	/**
	 * Gibt die Klassen des Folgeabschnittes zugeordnet zu deren ID zurück.
	 *
	 * @returns die Map mit den Klassen des Folgeabschnittes
	 */
	get klassenByIdFolgeAbschnitt(): AuswahlKatalog<KlassenDatenMinimal>;

	/**
	 * Gibt die IDs der Klassen der Mehrfachauswahl zurück, denen Schüler zugeordnet sind.
	 *
	 * @returns die Menge der IDs der Klassen mit Schülern
	 */
	getKlassenIDsMitSchuelern(): JavaSet<number>;

	/**
	 * Gibt die IDs der Schüler zurück, die den Klassen der Mehrfachauswahl zugeordnet sind. Eine Filterung nach
	 * dem Schüler-Status erfolgt hier nicht, diese übernimmt ggf. das Reporting.
	 *
	 * @returns die Liste der IDs der Schüler
	 */
	getSchuelerIDsDerAuswahl(): List<number>;

	/**
	 * Gibt den Schuljahresabschnitt der gewählten Klasse zurück. Dieser kann vom Schuljahresabschnitt der Auswahlliste abweichen.
	 *
	 * @throws DeveloperNotificationException   wenn keine Klasse gewählt ist oder der Schuljahresabschnitt fehlt
	 */
	get schuljahresabschnitt(): Schuljahresabschnitt;

	/**
	 * Gibt den State für die Stammdaten der Klassen zurück.
	 *
	 * @returns der State für die Stammdaten
	 */
	get stammdaten(): KlassenStammdatenState;

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

}

export const KlassenAuswahlStateKey: InjectionKey<KlassenAuswahlState> = Symbol('KlassenAuswahlState');

export function useKlassenAuswahlState(): KlassenAuswahlState {
	const state = AppContext.instance.inject(KlassenAuswahlStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des KlassenState über provide in der main.ts eingebunden");
	}
	return state;
}
