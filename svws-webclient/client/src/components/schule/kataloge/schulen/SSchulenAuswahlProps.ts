import type { SchulEintrag } from "@core";
import type { AbschnittAuswahlDaten } from "@ui";

export interface SchulenAuswahlProps {
	auswahl: SchulEintrag | undefined;
	mapKatalogeintraege: () => Map<number, SchulEintrag>;
	removeEintraege: (list: Iterable<SchulEintrag>) => Promise<void>;
	addEintrag: (data: Partial<SchulEintrag>) => Promise<void>;
	gotoEintrag: (eintrag: SchulEintrag) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
}