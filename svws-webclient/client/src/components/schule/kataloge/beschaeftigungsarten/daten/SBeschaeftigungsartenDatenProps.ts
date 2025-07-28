import type { BenutzerKompetenz, Beschaeftigungsart, BeschaeftigungsartenListeManager } from "@core";

export interface BeschaeftigungsartenDatenProps {
	manager: () => BeschaeftigungsartenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Beschaeftigungsart>) => Promise<void>;
}
