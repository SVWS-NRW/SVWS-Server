import type { List, BenutzerKompetenz } from "@core";
import type { OrteListeManager } from "@ui";

export interface OrteGruppenprozesseProps {
	manager: () => OrteListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
