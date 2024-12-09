import type { StundenplanUnterricht, StundenplanZeitraster, StundenplanManager, BenutzerKompetenz, Schulform, ServerMode, List } from "@core";

export interface StundenplanKlasseProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	stundenplanManager: () => StundenplanManager;
	patchUnterricht: (unterricht: Iterable<StundenplanUnterricht>, zeitraster?: StundenplanZeitraster, wochentyp?: number) => Promise<void>;
	addUnterrichtKlasse: (data: Iterable<Partial<StundenplanUnterricht>>) => Promise<void>;
	removeUnterrichtKlasse: (unterrichte: Iterable<StundenplanUnterricht>) => Promise<void>;
	mergeUnterrichte: (list: Array<List<StundenplanUnterricht>>) => Promise<void>;
}