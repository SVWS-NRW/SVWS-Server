import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { StundenplanKonfiguration } from "@core/core/data/stundenplan/StundenplanKonfiguration";
import type { StundenplanPausenzeit } from "@core/core/data/stundenplan/StundenplanPausenzeit";
import type { StundenplanZeitraster } from "@core/core/data/stundenplan/StundenplanZeitraster";
import type { Wochentag } from "@core/core/types/Wochentag";
import type { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
import type { List } from "@core/java/util/List";

export interface StundenplanZeitrasterPausenzeitProps {
	stundenplanManager: () => StundenplanManager;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	removePausenzeiten: (pausenzeiten: Iterable<StundenplanPausenzeit>) => Promise<void>;
	patchZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	addZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	removeZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	importZeitraster: undefined | (() => Promise<void>);
	listLehrer: List<LehrerListeEintrag>;
	selected: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined;
	setSelection: (value: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined) => void;
	setSettingsDefaults: (value: StundenplanKonfiguration) => Promise<void>;
}