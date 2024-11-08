import type { BenutzerKompetenz, SchuelerListeManager, ServerMode } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";
import type { ViewType } from "@ui";

export interface SchuelerAuswahlProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schuelerListeManager: () => SchuelerListeManager;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	gotoDefaultView: (idSchueler?: number | null) => Promise<void>;
	gotoHinzufuegenView: (navigate: boolean) => Promise<void>;
	gotoGruppenprozessView: (navigate: boolean) => Promise<void>;
	setFilter: () => Promise<void>;
	activeViewType: ViewType;
}

