import type { BenutzerKompetenz, Fahrschuelerart, FahrschuelerartenListeManager } from "@core";

export interface FahrschuelerartenDatenProps {
	manager: () => FahrschuelerartenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Fahrschuelerart>) => Promise<void>;
}
