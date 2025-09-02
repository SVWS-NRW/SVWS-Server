import type { Raum } from "@core";
import type { AbschnittAuswahlDaten, RaumListeManager } from "@ui";

export interface RaeumeAuswahlProps {
	raumListeManager: () => RaumListeManager;
	addEintrag: (eintrag: Raum) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<Raum>) => Promise<void>;
	gotoEintrag: (eintrag: Raum) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	setKatalogRaeumeImportJSON: (formData: FormData) => Promise<void>;
}
