import type { BenutzerKompetenz, EntlassgruendeListeManager, Schulform, ServerMode } from "@core";

export interface EntlassgruendeGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerkompetenzen: Set<BenutzerKompetenz>;
	manager: () => EntlassgruendeListeManager;
}
