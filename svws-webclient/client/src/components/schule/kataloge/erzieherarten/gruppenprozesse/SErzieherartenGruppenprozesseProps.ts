import type { List, BenutzerKompetenz, ErzieherartListeManager } from "@core";

export interface SErzieherartenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => ErzieherartListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
