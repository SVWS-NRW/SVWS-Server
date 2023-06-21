import type { List, StundenplanZeitraster } from "@core";

export interface ZeitrasterDatenProps {
	listKatalogeintraege: () => List<StundenplanZeitraster>;
	patchZeitraster: (daten: StundenplanZeitraster, multi: StundenplanZeitraster[]) => Promise<void>;
	addZeitraster: (daten: StundenplanZeitraster, tage: number[]) => Promise<void>;
	removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
}