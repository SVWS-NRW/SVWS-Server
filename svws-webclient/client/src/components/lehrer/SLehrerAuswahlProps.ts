import type { LehrerListeEintrag, LehrerListeManager, ServerMode } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface LehrerAuswahlProps {
	serverMode: ServerMode;
	lehrerListeManager: () => LehrerListeManager;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	gotoLehrer: (value: LehrerListeEintrag) => Promise<void>;
	setFilter: () => Promise<void>;
}