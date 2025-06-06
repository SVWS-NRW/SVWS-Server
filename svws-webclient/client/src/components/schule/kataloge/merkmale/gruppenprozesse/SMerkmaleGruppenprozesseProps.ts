import type { BenutzerKompetenz, List, MerkmaleListeManager, Schulform, ServerMode } from "@core";

export interface MerkmaleGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => MerkmaleListeManager;
	deleteMerkmale: () => Promise<[boolean, List<string | null>]>;
}
