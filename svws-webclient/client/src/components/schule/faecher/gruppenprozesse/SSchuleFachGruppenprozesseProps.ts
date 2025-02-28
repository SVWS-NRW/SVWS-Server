import type { Schulform, List, Schulgliederung, FachListeManager, ServerMode, BenutzerKompetenz } from "@core";

export interface SchuleFachGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulgliederungen: List<Schulgliederung>;
	manager: () => FachListeManager;
	deleteFaecher: () => Promise<[boolean, List<string | null>]>;
	deleteFaecherCheck: () => [boolean, List<string>];
}
