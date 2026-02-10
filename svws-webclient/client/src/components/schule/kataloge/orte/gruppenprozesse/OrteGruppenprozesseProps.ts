import type { List, ServerMode, BenutzerKompetenz } from "@core";
import type { OrteListeManager } from "@ui";

export interface OrteGruppenprozesseProps {
	serverMode: ServerMode;
	manager: () => OrteListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
