import type { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import type { FoerderschwerpunkteListeManager } from "@ui";

export interface FoerderschwerpunkteGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => FoerderschwerpunkteListeManager;
	deleteCheck: () => [boolean, List<string>];
	delete: () => Promise<[boolean, List<string | null>]>;
}
