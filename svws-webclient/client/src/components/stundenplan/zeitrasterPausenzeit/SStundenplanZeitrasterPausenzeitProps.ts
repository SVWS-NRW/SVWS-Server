import type { LehrerListeEintrag, List, Schulform, StundenplanManager, StundenplanPausenzeit, StundenplanZeitraster, Wochentag, } from "@core";

export interface StundenplanZeitrasterPausenzeitProps {
	schulform: Schulform;
	stundenplanManager: () => StundenplanManager;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	removePausenzeiten: (pausenzeiten: Iterable<StundenplanPausenzeit>) => Promise<void>;
	importPausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	patchZeitraster: (zeitraster : Iterable<StundenplanZeitraster>) => Promise<void>;
	addZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	removeZeitraster: (multi: Iterable<StundenplanZeitraster>) => Promise<void>;
	importZeitraster: undefined | (() => Promise<void>);
	listLehrer: List<LehrerListeEintrag>;
	selected: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined;
	setSelection: (value: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined) => void;
}