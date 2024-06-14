import type { List, StundenplanKalenderwochenzuordnung, StundenplanManager } from "@core";

export interface StundenplanKalenderwochenProps {
	stundenplanManager: () => StundenplanManager;
	patchKalenderwochenzuordnungen: (daten: List<StundenplanKalenderwochenzuordnung>) => Promise<void>;
}