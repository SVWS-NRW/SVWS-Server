import type { BenutzerKompetenz, FahrschuelerartenListeManager, List, Schulform, ServerMode } from "@core";

export interface FahrschuelerartenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => FahrschuelerartenListeManager;
	deleteFahrschuelerarten: () => Promise<[boolean, List<string | null>]>;
}
