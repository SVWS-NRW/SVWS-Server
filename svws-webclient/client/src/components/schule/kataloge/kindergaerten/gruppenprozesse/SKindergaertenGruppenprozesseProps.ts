import type { BenutzerKompetenz, KindergaertenListeManager, List, Schulform, ServerMode } from "@core";

export interface KindergaertenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => KindergaertenListeManager;
	deleteKindergaerten: () => Promise<[boolean, List<string | null>]>;
}
