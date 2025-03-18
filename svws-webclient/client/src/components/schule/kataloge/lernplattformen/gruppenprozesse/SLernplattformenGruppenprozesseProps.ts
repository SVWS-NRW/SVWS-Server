import type { Schulform, List, Schulgliederung, ServerMode, BenutzerKompetenz, LernplattformListeManager } from "@core";

export interface SLernplattformenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulgliederungen: List<Schulgliederung>;
	manager: () => LernplattformListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
