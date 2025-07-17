import type { BenutzerKompetenz, List, Schulform, ServerMode, SportbefreiungenListeManager } from "@core";

export interface SportbefreiungenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => SportbefreiungenListeManager;
	deleteSportbefreiungen: () => Promise<[boolean, List<string | null>]>;
}
