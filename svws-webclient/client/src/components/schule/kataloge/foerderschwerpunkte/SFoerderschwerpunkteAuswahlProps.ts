import type { FoerderschwerpunktEintrag } from "@core";
import type { AbschnittAuswahlDaten } from "@ui";

export interface FoerderschwerpunkteAuswahlProps {
	auswahl: FoerderschwerpunktEintrag | undefined;
	mapKatalogeintraege: Map<number, FoerderschwerpunktEintrag>;
	gotoEintrag: (eintrag: FoerderschwerpunktEintrag) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
}