import type { AbteilungenListeManager, BenutzerKompetenz, List, Schulform, ServerMode } from "@core";

export interface AbteilungenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => AbteilungenListeManager;
	deleteAbteilungen: () => Promise<[boolean, List<string | null>]>;
}
