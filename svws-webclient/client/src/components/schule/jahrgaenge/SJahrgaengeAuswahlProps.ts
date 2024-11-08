import type { BenutzerKompetenz, JahrgangListeManager, ServerMode } from "@core";
import type { ViewType } from "@ui";
import type { AbschnittAuswahlDaten } from "@comp";

export interface JahrgaengeAuswahlProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	jahrgangListeManager: () => JahrgangListeManager;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	gotoGruppenprozessView: (navigate: boolean) => Promise<void>;
	gotoHinzufuegenView: (navigate: boolean) => Promise<void>;
	activeViewType: ViewType;
}
