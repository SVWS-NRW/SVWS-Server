import type { BenutzerKompetenz, Sportbefreiung } from "@core";
import type { SportbefreiungenListeManager } from "@ui";

export interface SportbefreiungenDatenProps {
	manager: () => SportbefreiungenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Sportbefreiung>) => Promise<void>;
}
