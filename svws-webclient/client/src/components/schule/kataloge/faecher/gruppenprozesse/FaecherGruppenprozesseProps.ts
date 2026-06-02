import type { BenutzerKompetenz, List } from "@core";
import type { FaecherListeManager } from "@ui";

export interface FaecherGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => FaecherListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	sortFaecher: () => Promise<void>;
}
