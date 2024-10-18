import type { BenutzerKompetenz, List, Schulform, ServerMode, StundenplanAufsichtsbereich, StundenplanManager, StundenplanPausenaufsicht, StundenplanPausenaufsichtBereichUpdate, StundenplanPausenzeit } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface StundenplanPausenProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	stundenplanManager: () => StundenplanManager;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	removePausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	importPausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	listPausenzeiten: () => List<StundenplanPausenzeit>;
	patchAufsichtsbereich: (daten: Partial<StundenplanAufsichtsbereich>, id: number) => Promise<void>;
	addAufsichtsbereich: (aufsichtsbereich: StundenplanAufsichtsbereich) => Promise<void>;
	removeAufsichtsbereiche: (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => Promise<void>;
	importAufsichtsbereiche: (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => Promise<void>;
	listAufsichtsbereiche: () => List<StundenplanAufsichtsbereich>;
	wochentyp: () => number;
	updateAufsichtBereich: (update: StundenplanPausenaufsichtBereichUpdate, idPausenzeit?: number, idLehrer?: number) => Promise<void>;
	addAufsicht: (aufsicht: Partial<StundenplanPausenaufsicht>) => Promise<void>;
	removeAufsicht: (aufsichtID: number) => Promise<void>;
	patchAufsicht: (aufsicht: Partial<StundenplanPausenaufsicht>, id: number) => Promise<void>;
	apiStatus: ApiStatus;
	gotoKatalog: (katalog: 'raeume'|'aufsichtsbereiche'|'pausenzeiten') => Promise<RoutingStatus>;
}

