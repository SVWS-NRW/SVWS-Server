import type { StundenplanKalenderwochenzuordnung, StundenplanManager, StundenplanZeitraster, Wochentag } from "@core";

export interface StundenplanAnsichtProps {
	manager: () => StundenplanManager;
	patchZeitraster: (daten: StundenplanZeitraster, multi: StundenplanZeitraster[]) => Promise<void>;
	addZeitraster: (wochentag: Wochentag | undefined, stunde : number | undefined) => Promise<void>;
	removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
}