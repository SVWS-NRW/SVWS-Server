import type { BenutzerKompetenz, FachDaten, FachListeManager } from "@core";

export interface FachDatenProps {
	patch: (data : Partial<FachDaten>) => Promise<void>;
	manager: () => FachListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
