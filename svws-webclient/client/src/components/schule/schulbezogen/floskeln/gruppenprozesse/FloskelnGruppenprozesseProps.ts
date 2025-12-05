import type { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import type { FloskelnListeManager } from "@ui";

export interface FloskelnGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => FloskelnListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
