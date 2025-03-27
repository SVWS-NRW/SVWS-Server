import type { SchuelerVermerkartZusammenfassung, VermerkartEintrag, VermerkartenListeManager } from "@core";

export interface VermerkartDatenProps {
	patch: (data : Partial<VermerkartEintrag>) => Promise<void>;
	vermerkartenManager: () => VermerkartenListeManager,
	gotoSchueler : (schuelerVermerkartZusammenfassung : SchuelerVermerkartZusammenfassung) => Promise<void>;
}
