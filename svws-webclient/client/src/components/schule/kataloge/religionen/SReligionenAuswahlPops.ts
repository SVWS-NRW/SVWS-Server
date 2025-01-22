import type { ReligionEintrag, ReligionListeManager } from "@core";
import type { AbschnittAuswahlDaten } from "@ui";

export interface ReligionenAuswahlProps {
	religionListeManager: () => ReligionListeManager;
	setFilter: () => Promise<void>;
	addEintrag: (religion: Partial<ReligionEintrag>) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<ReligionEintrag>) => Promise<void>;
	gotoEintrag: (religion: ReligionEintrag) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
}