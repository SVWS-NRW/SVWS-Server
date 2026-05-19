import type { BenutzerKompetenz, List } from "@core";
import type { AnkreuzkompetenzenListeManager } from "@ui";

export interface AnkreuzkompetenzenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => AnkreuzkompetenzenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	gotoDefaultView: (id: number | null) => Promise<void>;
}
