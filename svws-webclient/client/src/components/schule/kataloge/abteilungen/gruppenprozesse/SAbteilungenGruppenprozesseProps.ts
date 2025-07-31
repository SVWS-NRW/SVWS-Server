import type { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import type { AbteilungenListeManager } from "@ui";

export interface AbteilungenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => AbteilungenListeManager;
	deleteAbteilungen: () => Promise<[boolean, List<string | null>]>;
}
