import type { List, Stundenplan, StundenplanManager, StundenplanPausenzeit, StundenplanZeitraster } from "@core";

export interface StundenplanZeitrasterPausenzeitProps {
	stundenplanManager: () => StundenplanManager;
	patch: (daten: Partial<Stundenplan>) => Promise<void>;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	addPausenzeit: (pausenzeit: StundenplanPausenzeit) => Promise<void>;
	removePausenzeiten: (raeume: StundenplanPausenzeit[]) => Promise<void>;
	importPausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	listPausenzeiten: List<StundenplanPausenzeit>;
	patchZeitraster: (daten: StundenplanZeitraster, multi: StundenplanZeitraster[]) => Promise<void>;
	addZeitraster: (daten: StundenplanZeitraster, tage: number[]) => Promise<void>;
	removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	importZeitraster: () => Promise<void>;
}