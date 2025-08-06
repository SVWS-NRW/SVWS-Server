import type { BenutzerKompetenz, Einwilligungsart } from "@core";
import type { SchuelerEinwilligungsartenZusammenfassung } from "@core";
import type { EinwilligungsartenListeManager } from "@ui";

export interface EinwilligungsartenDatenProps {
	patch: (data : Partial<Einwilligungsart>) => Promise<void>;
	einwilligungsartenListeManager: () => EinwilligungsartenListeManager,
	gotoSchueler : (schuelerEinwilligungsartenZusammenfassung : SchuelerEinwilligungsartenZusammenfassung) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
