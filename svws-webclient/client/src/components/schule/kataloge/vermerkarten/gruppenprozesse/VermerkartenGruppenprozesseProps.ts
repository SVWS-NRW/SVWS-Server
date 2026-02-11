import type { List, ServerMode, BenutzerKompetenz } from "@core";
import type { VermerkartenListeManager } from "@ui";

export interface VermerkartenGruppenprozesseProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => VermerkartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
