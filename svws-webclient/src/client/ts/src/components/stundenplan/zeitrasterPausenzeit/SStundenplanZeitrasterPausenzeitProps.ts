import type { List, Stundenplan, StundenplanManager, StundenplanPausenzeit, StundenplanZeitraster, Wochentag } from "@core";

export interface StundenplanZeitrasterPausenzeitProps {
	stundenplanManager: () => StundenplanManager;
	patch: (daten: Partial<Stundenplan>) => Promise<void>;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	addPausenzeit: (pausenzeit: StundenplanPausenzeit) => Promise<void>;
	removePausenzeiten: (pausenzeiten: Iterable<StundenplanPausenzeit>) => Promise<void>;
	importPausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	listPausenzeiten: List<StundenplanPausenzeit>;
	patchZeitraster: (data: Partial<StundenplanZeitraster>, zeitraster: StundenplanZeitraster) => Promise<void>;
	addZeitraster: (wochentag: Wochentag | undefined, stunde : number | undefined) => Promise<void>;
	removeZeitraster: (multi: Iterable<StundenplanZeitraster>) => Promise<void>;
	importZeitraster: () => Promise<void>;
}