import type { Schulform, List, ServerMode, BenutzerKompetenz } from "@core";
import type { EinwilligungsartenListeManager } from "@ui";

export interface EinwilligungsartenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => EinwilligungsartenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
