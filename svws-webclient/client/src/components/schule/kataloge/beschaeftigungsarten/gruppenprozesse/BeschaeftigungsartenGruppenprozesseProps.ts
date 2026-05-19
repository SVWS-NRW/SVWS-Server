import type { BenutzerKompetenz, List } from "@core";
import type { BeschaeftigungsartenListeManager } from "@ui";

export interface BeschaeftigungsartenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => BeschaeftigungsartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
}
