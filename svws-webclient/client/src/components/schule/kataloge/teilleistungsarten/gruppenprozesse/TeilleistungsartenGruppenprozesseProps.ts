import type { BenutzerKompetenz, List, ServerMode } from "@core";
import type { TeilleistungsartenListeManager } from "../manager/TeilleistungsartenListeManager";

export interface TeilleistungsartenGruppenprozesseProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => TeilleistungsartenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (id: number | null) => Promise<void>;
}
