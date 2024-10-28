import type { KursListeManager, ServerMode } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";
import type { ViewType } from "@ui";

export interface KurseAuswahlProps {
	serverMode: ServerMode;
	kursListeManager: () => KursListeManager;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	gotoGruppenprozessView: (navigate: boolean) => Promise<void>;
	gotoHinzufuegenView: (navigate: boolean) => Promise<void>;
	setFilter: () => Promise<void>;
	activeViewType: ViewType;
}
