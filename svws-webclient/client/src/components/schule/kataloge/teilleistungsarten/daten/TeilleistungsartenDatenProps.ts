import type { BenutzerKompetenz, Teilleistungsart } from "@core";
import type { TeilleistungsartenListeManager } from "../manager/TeilleistungsartenListeManager";

export interface TeilleistungsartenDatenProps {
	patch: (data: Partial<Teilleistungsart>) => Promise<boolean>;
	manager: () => TeilleistungsartenListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
