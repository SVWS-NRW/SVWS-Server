import type { StundenplanManager, StundenplanUnterrichtListeManager } from "@core";

export interface StundenplanUnterrichteProps {
	stundenplanManager: () => StundenplanManager;
	stundenplanUnterrichtListeManager: () => StundenplanUnterrichtListeManager;
	setFilter: () => Promise<void>;
}