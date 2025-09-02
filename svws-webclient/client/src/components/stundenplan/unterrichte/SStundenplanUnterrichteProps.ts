import type { BenutzerKompetenz, Schulform, ServerMode, StundenplanManager, StundenplanUnterricht } from "@core";
import type { StundenplanUnterrichtListeManager } from "@ui";

export interface StundenplanUnterrichteProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	stundenplanManager: () => StundenplanManager;
	stundenplanUnterrichtListeManager: () => StundenplanUnterrichtListeManager;
	setFilter: () => Promise<void>;
	patchUnterricht: (daten: Iterable<StundenplanUnterricht>) => Promise<void>;
}
