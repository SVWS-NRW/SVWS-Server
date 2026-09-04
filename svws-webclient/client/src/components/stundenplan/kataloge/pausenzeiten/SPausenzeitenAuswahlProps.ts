import type { StundenplanKonfiguration } from "@core/core/data/stundenplan/StundenplanKonfiguration";
import type { StundenplanPausenzeit } from "@core/core/data/stundenplan/StundenplanPausenzeit";
import type { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface PausenzeitenAuswahlProps {
	auswahl: StundenplanPausenzeit | undefined;
	addPausenzeiten: (eintraege: Iterable<Partial<StundenplanPausenzeit>>) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<StundenplanPausenzeit>) => Promise<void>;
	gotoEintrag: (eintrag: StundenplanPausenzeit) => Promise<RoutingStatus>;
	setKatalogPausenzeitenImportJSON: (formData: FormData) => Promise<void>;
	stundenplanManager: () => StundenplanManager;
	setSettingsDefaults: (value: StundenplanKonfiguration) => Promise<void>;
}