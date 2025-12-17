import type { List, ServerMode, BenutzerKompetenz } from "@core";
import { OrteListeManager } from "@ui";

export interface OrteGruppenprozesseProps {
	serverMode: ServerMode;
	manager: () => OrteListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
