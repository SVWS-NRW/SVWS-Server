import type { BenutzerKompetenz, List, ServerMode } from "@core";
import type { BetriebeListeManager } from "../../../../../../../ui/src/ui/manager/kataloge/BetriebeListeManager";

export interface BetriebeGruppenprozesseProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => BetriebeListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
}
