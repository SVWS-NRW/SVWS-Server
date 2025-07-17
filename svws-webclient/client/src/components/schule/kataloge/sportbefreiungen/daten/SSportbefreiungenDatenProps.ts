import type { BenutzerKompetenz, Sportbefreiung, SportbefreiungenListeManager } from "@core";

export interface SportbefreiungenDatenProps {
	manager: () => SportbefreiungenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Sportbefreiung>) => Promise<void>;
}
