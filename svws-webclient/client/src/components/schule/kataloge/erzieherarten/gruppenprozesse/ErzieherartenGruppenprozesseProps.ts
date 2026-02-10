import type { List, BenutzerKompetenz, ServerMode } from "@core";
import type { ErzieherartListeManager } from "@ui";

export interface ErzieherartenGruppenprozesseProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => ErzieherartListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
