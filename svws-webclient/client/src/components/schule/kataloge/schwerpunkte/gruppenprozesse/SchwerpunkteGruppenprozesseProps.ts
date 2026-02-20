import type { BenutzerKompetenz, List, ServerMode } from "@core";
import type { SchwerpunkteListeManager } from "@ui";


export interface SchwerpunkteGruppenprozesseProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => SchwerpunkteListeManager;
	deleteCheck: () => [boolean, List<string>];
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (id: number | null) => Promise<void>;
}
