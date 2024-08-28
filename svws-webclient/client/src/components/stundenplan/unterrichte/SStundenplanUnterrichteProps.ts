import type { BenutzerKompetenz, Schulform, ServerMode, StundenplanManager, StundenplanUnterricht, StundenplanUnterrichtListeManager } from "@core";

export interface StundenplanUnterrichteProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	stundenplanManager: () => StundenplanManager;
	stundenplanUnterrichtListeManager: () => StundenplanUnterrichtListeManager;
	setFilter: () => Promise<void>;
	patchUnterricht: (daten: Iterable<StundenplanUnterricht>) => Promise<void>;
}