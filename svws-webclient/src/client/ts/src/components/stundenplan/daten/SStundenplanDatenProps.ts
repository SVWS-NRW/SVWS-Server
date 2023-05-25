import type { Stundenplan, StundenplanAufsichtsbereich, StundenplanManager, StundenplanPausenzeit, StundenplanRaum } from "@svws-nrw/svws-core";

export interface StundenplanDatenProps {
	stundenplanManager: () => StundenplanManager;
	patch: (daten: Partial<Stundenplan>) => Promise<void>;
	patchRaum: (daten: Partial<StundenplanRaum>, id: number) => Promise<void>;
	addRaum: () => Promise<void>;
	removeRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	addPausenzeit: () => Promise<void>;
	removePausenzeiten: (raeume: StundenplanPausenzeit[]) => Promise<void>;
	patchAufsichtsbereich: (daten: Partial<StundenplanAufsichtsbereich>, id: number) => Promise<void>;
	addAufsichtsbereich: () => Promise<void>;
	removeAufsichtsbereiche: (raeume: StundenplanAufsichtsbereich[]) => Promise<void>;
}