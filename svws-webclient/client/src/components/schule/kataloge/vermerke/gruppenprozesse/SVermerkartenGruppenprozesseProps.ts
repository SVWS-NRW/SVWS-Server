import type { Schulform, List, Schulgliederung, ServerMode, VermerkartenListeManager, BenutzerKompetenz } from "@core";

export interface SchuleVermerkartenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulgliederungen: List<Schulgliederung>;
	manager: () => VermerkartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
