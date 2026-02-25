import type { BenutzerKompetenz, FachDaten, Schulform } from "@core";
import type { FaecherListeManager } from "@ui";

export interface FaecherDatenProps {
	schuljahr: number,
	patch: (data: Partial<FachDaten>) => Promise<boolean>;
	manager: () => FaecherListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulform: Schulform;
}
