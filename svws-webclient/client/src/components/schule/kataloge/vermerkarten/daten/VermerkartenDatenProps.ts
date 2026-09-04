import type { SchuelerVermerkartZusammenfassung } from "@core/core/data/schueler/SchuelerVermerkartZusammenfassung";
import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
import type { VermerkartenListeManager } from "@ui/ui/manager/kataloge/VermerkartenListeManager";

export interface VermerkartenDatenProps {
	patch: (data: Partial<VermerkartEintrag>) => Promise<boolean>;
	manager: () => VermerkartenListeManager,
	gotoSchueler: (schuelerVermerkartZusammenfassung: SchuelerVermerkartZusammenfassung) => Promise<void>;
}
