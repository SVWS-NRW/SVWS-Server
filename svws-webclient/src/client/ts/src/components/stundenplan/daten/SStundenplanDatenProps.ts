import type { Stundenplan, StundenplanAufsichtsbereich, StundenplanManager, StundenplanPausenzeit, StundenplanRaum } from "@svws-nrw/svws-core";

export interface StundenplanDatenProps {
	stundenplanManager: () => StundenplanManager;
	patch: (daten: Partial<Stundenplan>) => Promise<void>;
	patchRaum: (daten: Partial<StundenplanRaum>, id: number) => Promise<void>;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	patchAufsichtsbereich: (daten: Partial<StundenplanAufsichtsbereich>, id: number) => Promise<void>;
}