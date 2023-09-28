import type { StundenplanManager, StundenplanZeitraster, Wochentag } from "@core";

export type StundenplanAnsichtPlanungProps = {
	manager: () => StundenplanManager;
	patchZeitraster: (data: Partial<StundenplanZeitraster>, zeitraster: StundenplanZeitraster) => Promise<void>;
	addZeitraster: (wochentag: Wochentag | undefined, stunde : number | undefined) => Promise<void>;
	removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	importZeitraster: () => Promise<void>;
}