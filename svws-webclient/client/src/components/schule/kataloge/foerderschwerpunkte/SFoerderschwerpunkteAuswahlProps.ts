import type { FoerderschwerpunktEintrag } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface FoerderschwerpunkteAuswahlProps {
	auswahl: FoerderschwerpunktEintrag | undefined;
	mapKatalogeintraege: Map<number, FoerderschwerpunktEintrag>;
	gotoEintrag: (eintrag: FoerderschwerpunktEintrag) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
}