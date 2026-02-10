import type { Schulform, List, ServerMode, BenutzerKompetenz } from "@core";
import type { LernplattformListeManager } from "@ui";

export interface LernplattformenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => LernplattformListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
