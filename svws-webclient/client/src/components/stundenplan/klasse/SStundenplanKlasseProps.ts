import type { StundenplanUnterricht, StundenplanZeitraster, StundenplanManager } from "@core";

export interface StundenplanKlasseProps {
	stundenplanManager: () => StundenplanManager;
	patchUnterricht: (unterricht: Iterable<StundenplanUnterricht>, zeitraster: StundenplanZeitraster) => Promise<void>;
	addUnterrichtKlasse: (data: Iterable<Partial<StundenplanUnterricht>>) => Promise<void>;
	removeUnterrichtKlasse: (unterrichte: Iterable<StundenplanUnterricht>) => Promise<void>;
}