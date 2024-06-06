import type { List, StundenplanAufsichtsbereich, StundenplanManager, StundenplanPausenaufsicht, StundenplanPausenaufsichtBereichUpdate, StundenplanPausenzeit } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

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
	updateAufsichtBereich: (update: StundenplanPausenaufsichtBereichUpdate) => Promise<void>;
	addAufsicht: (aufsicht: Partial<StundenplanPausenaufsicht>) => Promise<void>;
	removeAufsicht: (aufsichtID: number) => Promise<void>;
	patchAufsicht: (aufsicht: Partial<StundenplanPausenaufsicht>, id: number) => Promise<void>;
	apiStatus: ApiStatus;
	gotoKatalog: (katalog: 'raeume'|'aufsichtsbereiche'|'pausenzeiten') => Promise<void>;
}

