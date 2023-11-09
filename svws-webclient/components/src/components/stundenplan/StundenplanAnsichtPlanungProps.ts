import type { StundenplanManager, StundenplanPausenzeit, StundenplanZeitraster, Wochentag } from "@core";

export type StundenplanAnsichtPlanungProps = {
	manager: () => StundenplanManager;
	addZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	importZeitraster: () => Promise<void>;
}