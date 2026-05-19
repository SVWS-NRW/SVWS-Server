import type { List, BenutzerKompetenz } from "@core";
import type { EinwilligungsartenListeManager } from "@ui";

export interface EinwilligungsartenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => EinwilligungsartenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
