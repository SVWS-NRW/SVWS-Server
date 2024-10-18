import type { JahrgangsDaten } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface JahrgaengeAuswahlProps {
	auswahl: () => JahrgangsDaten | undefined;
	mapKatalogeintraege: () => Map<number, JahrgangsDaten>;
	gotoEintrag: (eintrag: JahrgangsDaten) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
}