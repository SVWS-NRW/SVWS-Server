import type { BenutzerKompetenz, BeschaeftigungsartenListeManager, List, Schulform, ServerMode } from "@core";

export interface BeschaeftigungsartenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => BeschaeftigungsartenListeManager;
	deleteBeschaeftigungsarten: () => Promise<[boolean, List<string | null>]>;
}
