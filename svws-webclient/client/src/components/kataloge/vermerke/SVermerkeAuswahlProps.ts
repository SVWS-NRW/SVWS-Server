import type { VermerkartEintrag, VermerkartenManager } from "@core";

export interface VermerkeAuswahlProps {
	vermerkartenManager: () => VermerkartenManager,
	addEintrag: (vermerk: Partial<VermerkartEintrag>) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<VermerkartEintrag>) => Promise<void>;
	gotoEintrag: (vermerk: VermerkartEintrag) => Promise<void>;
	commit: () => Promise<void>;
	returnToKataloge: () => Promise<void>;
}
