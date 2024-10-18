import type { BenutzerKompetenz, List, Schulform, ServerMode, StundenplanKalenderwochenzuordnung, StundenplanManager } from "@core";

export interface StundenplanKalenderwochenProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	stundenplanManager: () => StundenplanManager;
	patchKalenderwochenzuordnungen: (daten: List<StundenplanKalenderwochenzuordnung>) => Promise<void>;
}