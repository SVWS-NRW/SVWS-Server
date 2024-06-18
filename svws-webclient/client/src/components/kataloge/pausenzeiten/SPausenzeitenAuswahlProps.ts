import type { StundenplanPausenzeit, StundenplanManager } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface PausenzeitenAuswahlProps {
	auswahl: StundenplanPausenzeit | undefined;
	addPausenzeiten: (eintraege: Iterable<Partial<StundenplanPausenzeit>>) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<StundenplanPausenzeit>) => Promise<void>;
	gotoEintrag: (eintrag: StundenplanPausenzeit) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	returnToKataloge: () => Promise<void>;
	setKatalogPausenzeitenImportJSON: (formData: FormData) => Promise<void>;
	stundenplanManager: () => StundenplanManager;
}