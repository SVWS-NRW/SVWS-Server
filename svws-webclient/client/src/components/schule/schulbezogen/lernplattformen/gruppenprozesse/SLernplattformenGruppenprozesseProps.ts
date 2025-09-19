import type { Schulform, List, Schulgliederung, ServerMode, BenutzerKompetenz } from "@core";
import type { LernplattformListeManager } from "@ui";

export interface SLernplattformenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulgliederungen: List<Schulgliederung>;
	manager: () => LernplattformListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
