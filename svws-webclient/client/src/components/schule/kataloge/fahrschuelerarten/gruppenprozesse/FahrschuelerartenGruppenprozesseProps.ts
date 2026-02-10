import type { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import type { FahrschuelerartenListeManager } from "@ui";

export interface FahrschuelerartenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => FahrschuelerartenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
