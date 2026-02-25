import type { BenutzerKompetenz, Einwilligungsart, Schulform } from "@core";
import type { EinwilligungsartenListeManager } from "@ui";

export interface EinwilligungsartenDatenProps {
	manager: () => EinwilligungsartenListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data: Partial<Einwilligungsart>) => Promise<boolean>;
	schuljahr: number,
	schulform: Schulform,
}
