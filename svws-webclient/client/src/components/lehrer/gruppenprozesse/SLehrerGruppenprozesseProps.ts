import type { Schulform, List, Schulgliederung, ServerMode, BenutzerKompetenz } from "@core";
import type { LehrerListeManager } from "@core";

export interface LehrerGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	lehrerListeManager: () => LehrerListeManager;
	deleteLehrer: () => Promise<[boolean, List<string | null>]>;
	deleteLehrerCheck: () => [boolean, List<string>];
}
