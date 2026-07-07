import type { BenutzerKompetenz, List } from "@core";
import type { TeilleistungsartenListeManager } from "../../../../../states/teilleistungsarten/TeilleistungsartenListeManager";

export interface TeilleistungsartenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => TeilleistungsartenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (id: number | null) => Promise<void>;
}
