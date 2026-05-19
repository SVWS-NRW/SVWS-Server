import type { BenutzerKompetenz, List } from "@core";
import type { FloskelnListeManager } from "@ui";

export interface FloskelnGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => FloskelnListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
