import type { BenutzerKompetenz, List, ServerMode } from "@core";
import type { OrtsteileListeManager } from "@ui";

export interface OrtsteileGruppenprozesseProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => OrtsteileListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	goToDefaultView: (eintragId?: number | null) => Promise<void>;
}
