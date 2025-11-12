import type { BenutzerKompetenz, Einwilligungsart, SchuelerEinwilligungsartenZusammenfassung } from "@core";
import type { EinwilligungsartenListeManager } from "@ui";

export interface EinwilligungsartenDatenProps {
	patch: (data: Partial<Einwilligungsart>) => Promise<void>;
	manager: () => EinwilligungsartenListeManager,
	gotoSchueler: (schuelerEinwilligungsartenZusammenfassung: SchuelerEinwilligungsartenZusammenfassung) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
