import type { List, SchuelerListeManager, BenutzerKompetenz } from "@core";

export interface SchuelerGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schuelerListeManager: () => SchuelerListeManager;
	deleteSchueler: () => Promise<[boolean, List<string | null>]>;
	deleteSchuelerCheck: () => [boolean, List<string>];
}
