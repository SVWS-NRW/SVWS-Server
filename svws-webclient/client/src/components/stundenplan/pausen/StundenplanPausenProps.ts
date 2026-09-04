import type { StundenplanAufsichtsbereich } from "@core/core/data/stundenplan/StundenplanAufsichtsbereich";
import type { StundenplanPausenaufsicht } from "@core/core/data/stundenplan/StundenplanPausenaufsicht";
import type { StundenplanPausenaufsichtBereichUpdate } from "@core/core/data/stundenplan/StundenplanPausenaufsichtBereichUpdate";
import type { StundenplanPausenzeit } from "@core/core/data/stundenplan/StundenplanPausenzeit";
import type { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
import type { List } from "@core/java/util/List";
import type { ApiStatus } from "~/components/ApiStatus";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface StundenplanPausenProps {
	stundenplanManager: () => StundenplanManager;
	patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
	removePausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
	listPausenzeiten: () => List<StundenplanPausenzeit>;
	patchAufsichtsbereich: (daten: Partial<StundenplanAufsichtsbereich>, id: number) => Promise<void>;
	addAufsichtsbereich: (aufsichtsbereich: StundenplanAufsichtsbereich) => Promise<void>;
	removeAufsichtsbereiche: (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => Promise<void>;
	listAufsichtsbereiche: () => List<StundenplanAufsichtsbereich>;
	wochentyp: () => number;
	updateAufsichtBereich: (update: StundenplanPausenaufsichtBereichUpdate, idPausenzeit?: number, idLehrer?: number) => Promise<void>;
	addAufsicht: (aufsicht: Partial<StundenplanPausenaufsicht>) => Promise<void>;
	removeAufsicht: (aufsichtID: number) => Promise<void>;
	patchAufsicht: (aufsicht: Partial<StundenplanPausenaufsicht>, id: number) => Promise<void>;
	apiStatus: ApiStatus;
	gotoKatalog: (katalog: 'raeume' | 'aufsichtsbereiche' | 'pausenzeiten') => Promise<RoutingStatus>;
}

