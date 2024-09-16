import type { SchuelerVermerkartZusammenfassung, VermerkartEintrag, VermerkartenManager } from "@core";

export interface VermerkartDatenProps {
	patch: (data : Partial<VermerkartEintrag>) => Promise<void>;
	vermerkartenManager: () => VermerkartenManager,
	gotoSchueler : (schuelerVermerkartZusammenfassung : SchuelerVermerkartZusammenfassung) => Promise<void>;
}
