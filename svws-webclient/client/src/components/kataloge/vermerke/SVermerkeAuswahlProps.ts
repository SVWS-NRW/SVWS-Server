import type { VermerkartEintrag, Schuljahresabschnitt } from "@core";

export interface VermerkeAuswahlProps {
	auswahl: VermerkartEintrag | undefined;
	mapKatalogeintraege: Map<number, VermerkartEintrag>;
	addEintrag: (vermerk: Partial<VermerkartEintrag>) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<VermerkartEintrag>) => Promise<void>;
	gotoEintrag: (vermerk: VermerkartEintrag) => Promise<void>;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
}
