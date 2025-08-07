import type { Schulform, List, Schulgliederung, ServerMode, BenutzerKompetenz } from "@core";
import type { JahrgaengeListeManager } from "@ui";

export interface SchuleJahrgangGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	manager: () => JahrgaengeListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
