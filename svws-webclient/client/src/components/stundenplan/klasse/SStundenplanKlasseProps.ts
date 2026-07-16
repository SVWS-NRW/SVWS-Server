import type { List, StundenplanManager, StundenplanUnterricht, StundenplanZeitraster } from "@core";

export interface StundenplanKlasseProps {
	stundenplanManager: () => StundenplanManager;
	patchUnterrichte: (unterricht: Iterable<StundenplanUnterricht>, zeitraster?: StundenplanZeitraster, wochentyp?: number) => Promise<void>;
	addUnterrichte: (data: Iterable<Partial<StundenplanUnterricht>>) => Promise<void>;
	removeUnterrichte: (unterrichte: Iterable<StundenplanUnterricht>) => Promise<void>;
	mergeUnterrichte: (list: Iterable<List<StundenplanUnterricht>>) => Promise<void>;
	doppelstundenmodus: () => boolean;
	setDoppelstundenmodus: (value: boolean) => Promise<void>;
}