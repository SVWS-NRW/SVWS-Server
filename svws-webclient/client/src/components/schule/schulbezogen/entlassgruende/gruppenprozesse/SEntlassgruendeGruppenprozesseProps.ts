import type { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import type { EntlassgruendeListeManager } from "@ui";

export interface EntlassgruendeGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => EntlassgruendeListeManager;
	deleteEntlassgruende: () => Promise<[boolean, List<string | null>]>;
}
