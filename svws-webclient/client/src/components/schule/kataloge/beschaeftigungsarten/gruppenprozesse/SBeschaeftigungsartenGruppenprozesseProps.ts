import type { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import type { BeschaeftigungsartenListeManager } from "@ui";

export interface BeschaeftigungsartenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => BeschaeftigungsartenListeManager;
	deleteBeschaeftigungsarten: () => Promise<[boolean, List<string | null>]>;
}
