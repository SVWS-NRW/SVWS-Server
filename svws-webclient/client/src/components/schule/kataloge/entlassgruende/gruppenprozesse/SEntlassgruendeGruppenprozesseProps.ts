import type { BenutzerKompetenz, EntlassgruendeListeManager, List, Schulform, ServerMode } from "@core";

export interface EntlassgruendeGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => EntlassgruendeListeManager;
	deleteEntlassgruende: () => Promise<[boolean, List<string | null>]>;
}
