import type { ServerMode, StundenplanListeEintrag } from "@core";
import type { AbschnittAuswahlDaten } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface StundenplanAuswahlProps {
	serverMode: ServerMode;
	auswahl: StundenplanListeEintrag | undefined;
	mapKatalogeintraege: () => Map<number, StundenplanListeEintrag>;
	gotoEintrag: (religion: StundenplanListeEintrag) => Promise<RoutingStatus>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	removeEintraege: (eintraege: StundenplanListeEintrag[]) => Promise<void>;
	addEintrag: () => Promise<void>;
}