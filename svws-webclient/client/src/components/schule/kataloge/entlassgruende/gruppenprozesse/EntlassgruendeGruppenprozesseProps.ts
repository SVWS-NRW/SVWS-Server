import type { BenutzerKompetenz, List } from "@core";
import type { EntlassgruendeListeManager } from "@ui";

export interface EntlassgruendeGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => EntlassgruendeListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
}
