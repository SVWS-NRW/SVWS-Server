import type { BenutzerKompetenz, FachDaten, Schulform } from "@core";
import type { FachListeManager } from "@ui";

export interface FaecherDatenProps {
	patch: (data: Partial<FachDaten>) => Promise<void>;
	manager: () => FachListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulform: Schulform;
}
