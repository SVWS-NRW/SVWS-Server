import type { StundenplanKalenderwochenzuordnung, StundenplanManager } from "@core";

export interface StundenplanAnsichtProps {
	manager: () => StundenplanManager;
	wochentyp: () => number;
	kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
}