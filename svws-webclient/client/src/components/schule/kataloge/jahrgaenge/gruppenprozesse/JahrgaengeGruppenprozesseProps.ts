import type { List, ServerMode, BenutzerKompetenz } from "@core";
import type { JahrgaengeListeManager } from "@ui";

export interface JahrgaengeGruppenprozesseProps {
	serverMode: ServerMode;
	manager: () => JahrgaengeListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
