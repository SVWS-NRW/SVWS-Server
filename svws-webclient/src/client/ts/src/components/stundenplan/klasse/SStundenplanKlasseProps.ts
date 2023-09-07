import type { StundenplanUnterricht, StundenplanZeitraster, StundenplanManager, StundenplanKlassenunterricht } from "@core";

export interface StundenplanKlasseProps {
	stundenplanManager: () => StundenplanManager;
	patchUnterricht: (unterricht: StundenplanUnterricht, zeitraster: StundenplanZeitraster) => Promise<void>;
	addUnterrichtKlasse: (unterricht: StundenplanKlassenunterricht, zeitraster: StundenplanZeitraster) => Promise<void>;
	removeUnterrichtKlasse: (unterrichte: Iterable<StundenplanUnterricht>) => Promise<void>;
}