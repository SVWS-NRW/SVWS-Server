import type { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import type { SportbefreiungenListeManager } from "@ui";



export interface SportbefreiungenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => SportbefreiungenListeManager;
	deleteSportbefreiungen: () => Promise<[boolean, List<string | null>]>;
}
