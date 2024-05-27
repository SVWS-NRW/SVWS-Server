import type { List, StundenplanAufsichtsbereich, StundenplanManager, StundenplanPausenaufsicht, StundenplanPausenzeit } from "@core";

export interface StundenplanPausenProps {
	stundenplanManager: () => StundenplanManager;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	addPausenzeit: (pausenzeit: Partial<StundenplanPausenzeit>) => Promise<void>;
	removePausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	importPausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	listPausenzeiten: () => List<StundenplanPausenzeit>;
	patchAufsichtsbereich: (daten: Partial<StundenplanAufsichtsbereich>, id: number) => Promise<void>;
	addAufsichtsbereich: (aufsichtsbereich: StundenplanAufsichtsbereich) => Promise<void>;
	removeAufsichtsbereiche: (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => Promise<void>;
	importAufsichtsbereiche: (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => Promise<void>;
	listAufsichtsbereiche: () => List<StundenplanAufsichtsbereich>;
	wochentyp: () => number;
	removeAufsicht: (aufsichtID: number) => Promise<void>;
	patchAufsicht: (aufsicht: Partial<StundenplanPausenaufsicht>, id: number) => Promise<void>;
	gotoKatalog: (katalog: 'raeume'|'aufsichtsbereiche'|'pausenzeiten') => Promise<void>;
}

