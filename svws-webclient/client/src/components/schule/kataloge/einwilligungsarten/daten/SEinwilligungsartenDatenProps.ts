import type {Einwilligungsart} from "@core";
import type {EinwilligungsartenListeManager, SchuelerEinwilligungsartenZusammenfassung} from "@core";

export interface EinwilligungsartenDatenProps {
	patch: (data : Partial<Einwilligungsart>) => Promise<void>;
	einwilligungsartenListeManager: () => EinwilligungsartenListeManager,
	gotoSchueler : (schuelerEinwilligungsartenZusammenfassung : SchuelerEinwilligungsartenZusammenfassung) => Promise<void>;
}
