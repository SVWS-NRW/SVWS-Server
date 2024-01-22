import type { LehrerListeEintrag, List, Schulform, StundenplanManager, StundenplanPausenaufsicht, StundenplanPausenzeit, StundenplanZeitraster, Wochentag, } from "@core";

export interface StundenplanZeitrasterPausenzeitProps {
	stundenplanManager: () => StundenplanManager;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	addPausenzeit: (pausenzeit: StundenplanPausenzeit) => Promise<void>;
	removePausenzeiten: (pausenzeiten: Iterable<StundenplanPausenzeit>) => Promise<void>;
	importPausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	patchZeitraster: (zeitraster : Iterable<StundenplanZeitraster>) => Promise<void>;
	addZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	removeZeitraster: (multi: Iterable<StundenplanZeitraster>) => Promise<void>;
	importZeitraster: undefined | (() => Promise<void>);
	listLehrer: List<LehrerListeEintrag>;
	addAufsichtUndBereich: (pausenaufsicht: Partial<StundenplanPausenaufsicht>) => Promise<void>;
	selected: () => Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined;
	setSelection: (value: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined) => void;
	schulform: Schulform;
}