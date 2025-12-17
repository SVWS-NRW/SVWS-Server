import type { BenutzerKompetenz, Fahrschuelerart } from "@core";
import type { FahrschuelerartenListeManager } from "@ui";

export interface FahrschuelerartenDatenProps {
	manager: () => FahrschuelerartenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Fahrschuelerart>) => Promise<void>;
}
