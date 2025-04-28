import type { AbteilungenListeManager, BenutzerKompetenz, Schulform, ServerMode } from "@core";

export interface AbteilungenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => AbteilungenListeManager;
}
