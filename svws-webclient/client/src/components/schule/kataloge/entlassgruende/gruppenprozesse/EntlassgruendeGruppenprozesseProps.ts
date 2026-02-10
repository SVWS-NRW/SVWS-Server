import type { BenutzerKompetenz, List, ServerMode } from "@core";
import type { EntlassgruendeListeManager } from "@ui";

export interface EntlassgruendeGruppenprozesseProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => EntlassgruendeListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
}
