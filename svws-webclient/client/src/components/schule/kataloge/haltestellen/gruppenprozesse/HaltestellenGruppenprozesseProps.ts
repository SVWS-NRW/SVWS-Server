import type { BenutzerKompetenz, List } from "@core";
import type { HaltestellenListeManager } from "@ui";

export interface HaltestellenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	manager: () => HaltestellenListeManager;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
