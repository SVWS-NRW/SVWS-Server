import type { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import type { KindergaertenListeManager } from "@ui";

export interface KindergaertenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => KindergaertenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
