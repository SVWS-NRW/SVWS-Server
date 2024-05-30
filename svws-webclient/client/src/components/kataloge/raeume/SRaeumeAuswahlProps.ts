import type { Raum, StundenplanManager } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface RaeumeAuswahlProps {
	auswahl: Raum | undefined;
	addEintrag: (religion: Raum) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<Raum>) => Promise<void>;
	gotoEintrag: (eintrag: Raum) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	returnToKataloge: () => Promise<void>;
	setKatalogRaeumeImportJSON: (formData: FormData) => Promise<void>;
	stundenplanManager: () => StundenplanManager;
}