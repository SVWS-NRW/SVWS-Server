import type { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import type { FloskelgruppenListeManager } from "@ui";

export interface FloskelgruppenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => FloskelgruppenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
