import type { StundenplanManager, StundenplanUnterricht, StundenplanUnterrichtListeManager } from "@core";

export interface StundenplanUnterrichteProps {
	stundenplanManager: () => StundenplanManager;
	stundenplanUnterrichtListeManager: () => StundenplanUnterrichtListeManager;
	setFilter: () => Promise<void>;
	patchUnterricht: (daten: Iterable<StundenplanUnterricht>) => Promise<void>;
}