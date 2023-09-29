import type { StundenplanManager, StundenplanZeitraster, Wochentag } from "@core";

export interface ZeitrasterDatenProps {
	stundenplanManager: () => StundenplanManager;
	patchZeitraster: (daten: Partial<StundenplanZeitraster>, zeitraster: StundenplanZeitraster) => Promise<void>;
	addZeitraster: (wochentag: Wochentag | undefined, stunde: number | undefined) => Promise<void>;
	removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
}