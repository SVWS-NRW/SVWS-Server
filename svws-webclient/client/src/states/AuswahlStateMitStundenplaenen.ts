import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";

/**
 * Die Schnittstelle für Auswahl-States, welche die Stundenpläne des aktuellen Schuljahresabschnittes bereitstellen.
 */
export interface AuswahlStateMitStundenplaenen {

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
