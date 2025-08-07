import type { List, BenutzerKompetenz } from "@core";
import type { TelefonArtListeManager } from "@ui"

export interface STelefonArtenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => TelefonArtListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
