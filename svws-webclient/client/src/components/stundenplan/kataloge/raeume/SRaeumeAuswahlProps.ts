import type { Raum, RaumListeManager } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface RaeumeAuswahlProps {
	raumListeManager: () => RaumListeManager;
	addEintrag: (religion: Raum) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<Raum>) => Promise<void>;
	gotoEintrag: (eintrag: Raum) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	returnToKataloge: () => Promise<void>;
	returnToStundenplan: () => Promise<void>;
	setKatalogRaeumeImportJSON: (formData: FormData) => Promise<void>;
}