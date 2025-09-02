import type { BenutzerKompetenz, Beschaeftigungsart } from "@core";
import type { BeschaeftigungsartenListeManager } from "@ui";

export interface BeschaeftigungsartenDatenProps {
	manager: () => BeschaeftigungsartenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Beschaeftigungsart>) => Promise<void>;
}
