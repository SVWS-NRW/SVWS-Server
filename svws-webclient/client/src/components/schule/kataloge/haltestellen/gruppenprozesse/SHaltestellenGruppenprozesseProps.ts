import type { BenutzerKompetenz, HaltestellenListeManager, List, Schulform, ServerMode } from "@core";

export interface HaltestellenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => HaltestellenListeManager;
	deleteHaltestellen: () => Promise<[boolean, List<string | null>]>;
}
