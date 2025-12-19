import type { List, BenutzerKompetenz } from "@core";
import type { TelefonartenListeManager } from "@ui";

export interface TelefonartenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => TelefonartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
