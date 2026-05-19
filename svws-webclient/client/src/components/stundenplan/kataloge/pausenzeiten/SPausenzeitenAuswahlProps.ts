import type { StundenplanPausenzeit, StundenplanManager, StundenplanKonfiguration, BenutzerKompetenz } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface PausenzeitenAuswahlProps {
	auswahl: StundenplanPausenzeit | undefined;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	addPausenzeiten: (eintraege: Iterable<Partial<StundenplanPausenzeit>>) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<StundenplanPausenzeit>) => Promise<void>;
	gotoEintrag: (eintrag: StundenplanPausenzeit) => Promise<RoutingStatus>;
	setKatalogPausenzeitenImportJSON: (formData: FormData) => Promise<void>;
	stundenplanManager: () => StundenplanManager;
	setSettingsDefaults: (value: StundenplanKonfiguration) => Promise<void>;
}