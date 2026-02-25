import type { BenutzerKompetenz, List, ServerMode } from "@core";
import type { AnkreuzkompetenzenListeManager } from "@ui";

export interface AnkreuzkompetenzenGruppenprozesseProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => AnkreuzkompetenzenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (id: number | null) => Promise<void>;
}
