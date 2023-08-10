import type { StundenplanKalenderwochenzuordnung, StundenplanManager, StundenplanZeitraster } from "@core";

export interface StundenplanAnsichtProps {
	manager: () => StundenplanManager;
	wochentyp: () => number;
	kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
	patchZeitraster: (daten: StundenplanZeitraster, multi: StundenplanZeitraster[]) => Promise<void>;
	addZeitraster: (daten: StundenplanZeitraster, tage: number[]) => Promise<void>;
	removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
}