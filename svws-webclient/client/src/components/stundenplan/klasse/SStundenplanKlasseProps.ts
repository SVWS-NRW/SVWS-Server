import type { StundenplanUnterricht, StundenplanZeitraster, StundenplanManager, BenutzerKompetenz, Schulform, ServerMode, List } from "@core";

export interface StundenplanKlasseProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	stundenplanManager: () => StundenplanManager;
	patchUnterrichte: (unterricht: Iterable<StundenplanUnterricht>, zeitraster?: StundenplanZeitraster, wochentyp?: number) => Promise<void>;
	addUnterrichte: (data: Iterable<Partial<StundenplanUnterricht>>) => Promise<void>;
	removeUnterrichte: (unterrichte: Iterable<StundenplanUnterricht>) => Promise<void>;
	mergeUnterrichte: (list: Iterable<List<StundenplanUnterricht>>) => Promise<void>;
}