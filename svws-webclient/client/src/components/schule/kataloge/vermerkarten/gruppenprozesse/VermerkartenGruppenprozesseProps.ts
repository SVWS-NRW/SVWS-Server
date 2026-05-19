import type { List, BenutzerKompetenz } from "@core";
import type { VermerkartenListeManager } from "@ui";

export interface VermerkartenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => VermerkartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
