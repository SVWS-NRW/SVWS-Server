import type { VermerkartEintrag } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface VermerkeAuswahlProps {
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	auswahl: VermerkartEintrag | undefined;
	mapKatalogeintraege: Map<number, VermerkartEintrag>;
	addEintrag: (vermerk: Partial<VermerkartEintrag>) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<VermerkartEintrag>) => Promise<void>;
	gotoEintrag: (vermerk: VermerkartEintrag) => Promise<void>;
	returnToKataloge: () => Promise<void>;
}
