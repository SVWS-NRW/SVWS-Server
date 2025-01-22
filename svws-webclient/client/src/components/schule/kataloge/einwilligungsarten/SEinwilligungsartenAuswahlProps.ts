import type { Einwilligungsart } from "@core";
import type { AbschnittAuswahlDaten } from "@ui";

export interface SEinwilligungsartenAuswahlProps {
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	auswahl: () => Einwilligungsart | undefined;
	mapKatalogeintraege: Map<number, Einwilligungsart>;
	addEintrag: (einwilligung: Partial<Einwilligungsart>) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<Einwilligungsart>) => Promise<void>;
	gotoEintrag: (einwilligung: Einwilligungsart) => Promise<void>;
}
