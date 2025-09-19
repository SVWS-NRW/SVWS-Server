import type { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import type { MerkmaleListeManager } from "@ui";

export interface MerkmaleGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => MerkmaleListeManager;
	deleteMerkmale: () => Promise<[boolean, List<string | null>]>;
}
