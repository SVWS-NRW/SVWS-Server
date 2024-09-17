import type { ServerMode, StundenplanListeEintrag } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface StundenplanAuswahlProps {
	serverMode: ServerMode;
	auswahl: StundenplanListeEintrag | undefined;
	mapKatalogeintraege: () => Map<number, StundenplanListeEintrag>;
	gotoEintrag: (religion: StundenplanListeEintrag) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	removeEintraege: (eintraege: StundenplanListeEintrag[]) => Promise<void>;
	addEintrag: () => Promise<void>;
}