import type { FachDaten, FachListeManager } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface FaecherAuswahlProps {
	fachListeManager: () => FachListeManager;
	gotoEintrag: (eintrag: FachDaten) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	returnToKataloge: () => Promise<void>;
	setFilter: () => Promise<void>;
	setzeDefaultSortierungSekII: () => Promise<void>;
}