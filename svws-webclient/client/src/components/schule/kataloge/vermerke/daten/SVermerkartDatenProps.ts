import type { SchuelerVermerkartZusammenfassung, VermerkartEintrag } from "@core";
import type { VermerkartenListeManager } from "@ui";

export interface VermerkartDatenProps {
	patch: (data : Partial<VermerkartEintrag>) => Promise<void>;
	vermerkartenManager: () => VermerkartenListeManager,
	gotoSchueler : (schuelerVermerkartZusammenfassung : SchuelerVermerkartZusammenfassung) => Promise<void>;
}
