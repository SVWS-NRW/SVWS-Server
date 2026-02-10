import type { Schulform, List, Schulgliederung, ServerMode, BenutzerKompetenz } from "@core";
import type { SchulenListeManager } from "@ui";

export interface SchulenGruppenprozesseProps {
	serverMode: ServerMode;
	manager: () => SchulenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
}
