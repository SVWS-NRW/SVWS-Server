import type { List, BenutzerKompetenz } from "@core";
import type { SchulenListeManager } from "@ui";

export interface SchulenGruppenprozesseProps {
	manager: () => SchulenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
}
