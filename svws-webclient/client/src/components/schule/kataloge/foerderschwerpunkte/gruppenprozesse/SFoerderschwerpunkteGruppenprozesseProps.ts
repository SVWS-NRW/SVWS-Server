import type { BenutzerKompetenz, FoerderschwerpunkteListeManager, List, Schulform, ServerMode } from "@core";

export interface FoerderschwerpunkteGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => FoerderschwerpunkteListeManager;
	deleteFoerderschwerpunkte: () => Promise<[boolean, List<string | null>]>;
}
