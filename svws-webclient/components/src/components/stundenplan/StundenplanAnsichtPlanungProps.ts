import type { Schulform, StundenplanManager, StundenplanZeitraster  } from "@core";

export type StundenplanAnsichtPlanungProps = {
	manager: () => StundenplanManager;
	addZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	importZeitraster: undefined | (() => Promise<void>);
	schulform?: Schulform;
}