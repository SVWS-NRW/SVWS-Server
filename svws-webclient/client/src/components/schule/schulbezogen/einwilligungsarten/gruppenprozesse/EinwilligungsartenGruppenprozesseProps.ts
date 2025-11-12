import type { Schulform, List, Schulgliederung, ServerMode, BenutzerKompetenz } from "@core";
import type { EinwilligungsartenListeManager } from "@ui";

export interface EinwilligungsartenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulgliederungen: List<Schulgliederung>;
	manager: () => EinwilligungsartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
