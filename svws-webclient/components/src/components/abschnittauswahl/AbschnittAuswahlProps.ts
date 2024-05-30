import type { Schuljahresabschnitt } from "../../../../core/src/core/data/schule/Schuljahresabschnitt";

/**
 * Dieses Interface stellt die Daten und Methoden zur Steuerung der
 * Auswahl des aktuellen Schuljahresabschnittsauswahl in den einzelnen
 * Komponenten bereit.
 */
export interface AbschnittAuswahlDaten {

	/** Gibt an, ob die Abschnittsauswahl aktiv sein oder oder deaktiv sein soll,
	 * so dass nur der aktuelle Abschnitt angezeigt wird */
	aktiv: boolean;

	/** Eine Map mit den Schuljahresabschnitten zugeordnet zu deren IDs */
	abschnitte: Map<number, Schuljahresabschnitt>;

	/** Der aktuell ausgewählte Schuljahresabschnitts */
	aktuell: Schuljahresabschnitt;

	/** Die ID des aktuellen Schuljahresabschnitts der Schule */
	schule: Schuljahresabschnitt;

	/** Die Methode zum Setzen des aktuellen Schuljahresabschnittes */
	set: (abschnitt: Schuljahresabschnitt) => void;

}


/**
 * Dieses Interface stellt die Daten und Methoden zur Steuerung der
 * Auswahl des aktuellen Schuljahresabschnittsauswahl in den einzelnen
 * Komponenten bereit.
 */
export interface AbschnittAuswahlProps {

	/** Die Daten für die Abschnittsauswahl */
	daten: () => AbschnittAuswahlDaten;

	/** Gibt an, ob das Select der Komponente nicht headless dargestellt werden soll (Default: headless)). */
	disableHeadless?: boolean;

}
