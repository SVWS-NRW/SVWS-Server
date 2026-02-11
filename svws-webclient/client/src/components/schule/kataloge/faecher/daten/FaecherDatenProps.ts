import type { BenutzerKompetenz, FachDaten, Schulform } from "@core";
import type { FaecherListeManager } from "@ui";

export interface FaecherDatenProps {
	schuljahr: number,
	patch: (data: Partial<FachDaten>) => Promise<void>;
	manager: () => FaecherListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulform: Schulform;
}
