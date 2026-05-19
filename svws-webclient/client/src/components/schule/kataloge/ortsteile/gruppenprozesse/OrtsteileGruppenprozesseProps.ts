import type { BenutzerKompetenz, List } from "@core";
import type { OrtsteileListeManager } from "@ui";

export interface OrtsteileGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => OrtsteileListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	goToDefaultView: (eintragId?: number | null) => Promise<void>;
}
