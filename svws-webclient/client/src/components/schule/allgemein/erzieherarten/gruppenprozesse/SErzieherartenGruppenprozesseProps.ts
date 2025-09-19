import type { List, BenutzerKompetenz } from "@core";
import type { ErzieherartListeManager } from "@ui";

export interface SErzieherartenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => ErzieherartListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
