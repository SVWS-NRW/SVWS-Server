import type { StundenplanManager, StundenplanUnterricht } from "@core";
import type { StundenplanUnterrichtListeManager } from "@ui";

export interface StundenplanUnterrichteProps {
	stundenplanManager: () => StundenplanManager;
	stundenplanUnterrichtListeManager: () => StundenplanUnterrichtListeManager;
	setFilter: () => Promise<void>;
	patchUnterricht: (daten: Iterable<StundenplanUnterricht>) => Promise<void>;
}
