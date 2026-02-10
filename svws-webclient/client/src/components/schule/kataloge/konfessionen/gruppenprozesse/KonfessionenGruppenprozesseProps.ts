import type { BenutzerKompetenz, List, ServerMode } from "@core";
import type { KonfessionenListeManager } from "@ui";

export interface KonfessionenGruppenprozesseProps {
	serverMode: ServerMode;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => KonfessionenListeManager;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
