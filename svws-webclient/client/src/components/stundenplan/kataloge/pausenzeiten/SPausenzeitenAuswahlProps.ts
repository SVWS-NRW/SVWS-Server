import type { StundenplanPausenzeit, StundenplanManager } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface PausenzeitenAuswahlProps {
	auswahl: StundenplanPausenzeit | undefined;
	addPausenzeiten: (eintraege: Iterable<Partial<StundenplanPausenzeit>>) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<StundenplanPausenzeit>) => Promise<void>;
	gotoEintrag: (eintrag: StundenplanPausenzeit) => Promise<RoutingStatus>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	setKatalogPausenzeitenImportJSON: (formData: FormData) => Promise<void>;
	stundenplanManager: () => StundenplanManager;
}