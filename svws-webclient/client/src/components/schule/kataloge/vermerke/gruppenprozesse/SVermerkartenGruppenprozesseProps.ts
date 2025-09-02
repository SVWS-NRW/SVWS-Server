import type { Schulform, List, Schulgliederung, ServerMode, BenutzerKompetenz } from "@core";
import type { VermerkartenListeManager } from "@ui";

export interface SchuleVermerkartenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulgliederungen: List<Schulgliederung>;
	manager: () => VermerkartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
