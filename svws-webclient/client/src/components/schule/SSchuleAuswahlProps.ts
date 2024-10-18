import type { TabManager } from "@ui";
import type { SchuleStammdaten } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface SchuleAuswahlProps {
	schule: () => SchuleStammdaten;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	tabManager: () => TabManager;
}
