import type { BenutzerKompetenz, List } from "@core";
import type { BetriebeListeManager } from "@ui";

export interface BetriebeGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => BetriebeListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
}
