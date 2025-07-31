import type { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import type { HaltestellenListeManager } from "@ui";

export interface HaltestellenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => HaltestellenListeManager;
	deleteHaltestellen: () => Promise<[boolean, List<string | null>]>;
}
