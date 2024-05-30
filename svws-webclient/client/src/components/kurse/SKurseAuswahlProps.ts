import type { KursDaten, KursListeManager, ServerMode } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface KurseAuswahlProps {
	serverMode: ServerMode;
	kursListeManager: () => KursListeManager;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	gotoEintrag: (eintrag: KursDaten) => Promise<void>;
	setFilter: () => Promise<void>;
}
