import type { List, BenutzerKompetenz, ServerMode } from "@core";
import type { TelefonartenListeManager } from "@ui";

export interface TelefonartenGruppenprozesseProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => TelefonartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	gotoDefaultView: (eintrag?: number | null) => Promise<void>;
}
