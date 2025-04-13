import type { List, BenutzerKompetenz, TelefonArtListeManager } from "@core";

export interface STelefonArtenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => TelefonArtListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
