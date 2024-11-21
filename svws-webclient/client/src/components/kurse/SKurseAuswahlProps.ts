import type { BenutzerKompetenz, KursListeManager, ServerMode } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";
import type { ViewType } from "@ui";

export interface KurseAuswahlProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => KursListeManager;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	gotoGruppenprozessView: (navigate: boolean) => Promise<void>;
	gotoHinzufuegenView: (navigate: boolean) => Promise<void>;
	setFilter: () => Promise<void>;
	activeViewType: ViewType;
}
