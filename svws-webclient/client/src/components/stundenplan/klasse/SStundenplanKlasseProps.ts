import type { StundenplanUnterricht } from "@core/core/data/stundenplan/StundenplanUnterricht";
import type { StundenplanZeitraster } from "@core/core/data/stundenplan/StundenplanZeitraster";
import type { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
import type { List } from "@core/java/util/List";

export interface StundenplanKlasseProps {
	stundenplanManager: () => StundenplanManager;
	patchUnterrichte: (unterricht: Iterable<StundenplanUnterricht>, zeitraster?: StundenplanZeitraster, wochentyp?: number) => Promise<void>;
	addUnterrichte: (data: Iterable<Partial<StundenplanUnterricht>>) => Promise<void>;
	removeUnterrichte: (unterrichte: Iterable<StundenplanUnterricht>) => Promise<void>;
	mergeUnterrichte: (list: Iterable<List<StundenplanUnterricht>>) => Promise<void>;
	doppelstundenmodus: () => boolean;
	setDoppelstundenmodus: (value: boolean) => Promise<void>;
}