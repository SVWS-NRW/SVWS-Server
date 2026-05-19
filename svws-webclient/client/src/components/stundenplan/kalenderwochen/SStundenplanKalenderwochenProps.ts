import type { BenutzerKompetenz, List, StundenplanKalenderwochenzuordnung, StundenplanManager } from "@core";

export interface StundenplanKalenderwochenProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	stundenplanManager: () => StundenplanManager;
	patchKalenderwochenzuordnungen: (daten: List<StundenplanKalenderwochenzuordnung>) => Promise<void>;
	deleteKalenderwochenzuordnungen: () => Promise<void>;
}