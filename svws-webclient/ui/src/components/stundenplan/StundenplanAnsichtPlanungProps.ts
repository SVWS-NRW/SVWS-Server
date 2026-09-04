import type { StundenplanKonfiguration } from "@core/core/data/stundenplan/StundenplanKonfiguration";
import type { StundenplanPausenzeit } from "@core/core/data/stundenplan/StundenplanPausenzeit";
import type { StundenplanZeitraster } from "@core/core/data/stundenplan/StundenplanZeitraster";
import type { Wochentag } from "@core/core/types/Wochentag";
import type { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";

export type StundenplanAnsichtPlanungProps = {
	manager: () => StundenplanManager;
	addZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	removeZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	importZeitraster: undefined | (() => Promise<void>);
	setSelection: (value: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined) => void;
	selected: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined;
	setSettingsDefaults?: (value: StundenplanKonfiguration) => Promise<void>;
};