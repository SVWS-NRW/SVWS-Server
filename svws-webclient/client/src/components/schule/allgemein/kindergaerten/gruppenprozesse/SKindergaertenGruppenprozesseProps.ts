import type { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import type { KindergaertenListeManager } from "@ui";

export interface KindergaertenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => KindergaertenListeManager;
	deleteKindergaerten: () => Promise<[boolean, List<string | null>]>;
}
