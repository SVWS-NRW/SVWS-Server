import type { BenutzerKompetenz, StundenplanManager, StundenplanUnterricht } from "@core";
import type { StundenplanUnterrichtListeManager } from "@ui";

export interface StundenplanUnterrichteProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	stundenplanManager: () => StundenplanManager;
	stundenplanUnterrichtListeManager: () => StundenplanUnterrichtListeManager;
	setFilter: () => Promise<void>;
	patchUnterricht: (daten: Iterable<StundenplanUnterricht>) => Promise<void>;
}
