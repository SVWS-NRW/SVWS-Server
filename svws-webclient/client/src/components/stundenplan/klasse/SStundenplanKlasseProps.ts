import type { StundenplanUnterricht, StundenplanZeitraster, StundenplanManager } from "@core";

export interface StundenplanKlasseProps {
	stundenplanManager: () => StundenplanManager;
	patchUnterricht: (unterricht: StundenplanUnterricht, zeitraster: StundenplanZeitraster) => Promise<void>;
	addUnterrichtKlasse: (data: Partial<StundenplanUnterricht>) => Promise<void>;
	removeUnterrichtKlasse: (unterrichte: Iterable<StundenplanUnterricht>) => Promise<void>;
}