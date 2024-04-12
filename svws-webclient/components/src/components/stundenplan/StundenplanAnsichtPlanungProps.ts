import type { StundenplanZeitraster } from "../../../../core/src/core/data/stundenplan/StundenplanZeitraster";
import type { Schulform } from "../../../../core/src/core/types/schule/Schulform";
import type { StundenplanManager } from "../../../../core/src/core/utils/stundenplan/StundenplanManager";

export type StundenplanAnsichtPlanungProps = {
	manager: () => StundenplanManager;
	addZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	importZeitraster: undefined | (() => Promise<void>);
	schulform?: Schulform;
}