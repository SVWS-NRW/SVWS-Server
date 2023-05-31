import type { List, Raum, Stundenplan, StundenplanAufsichtsbereich, StundenplanManager, StundenplanPausenzeit, StundenplanRaum } from "@svws-nrw/svws-core";

export interface StundenplanDatenProps {
	stundenplanManager: () => StundenplanManager;
	patch: (daten: Partial<Stundenplan>) => Promise<void>;
	patchRaum: (daten: Partial<StundenplanRaum>, id: number) => Promise<void>;
	addRaum: (raum: StundenplanRaum) => Promise<void>;
	removeRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
	listRaeume: List<Raum>;
	importRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	addPausenzeit: (pausenzeit: StundenplanPausenzeit) => Promise<void>;
	removePausenzeiten: (raeume: StundenplanPausenzeit[]) => Promise<void>;
	importPausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	listPausenzeiten: List<StundenplanPausenzeit>;
	patchAufsichtsbereich: (daten: Partial<StundenplanAufsichtsbereich>, id: number) => Promise<void>;
	addAufsichtsbereich: (aufsichtsbereich: StundenplanAufsichtsbereich) => Promise<void>;
	removeAufsichtsbereiche: (raeume: StundenplanAufsichtsbereich[]) => Promise<void>;
	importAufsichtsbereiche: (s: StundenplanAufsichtsbereich[]) => Promise<void>;
	listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
}