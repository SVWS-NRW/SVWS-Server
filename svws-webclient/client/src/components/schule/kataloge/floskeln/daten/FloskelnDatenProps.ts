import type { BenutzerKompetenz, Floskel, Schulform } from "@core";
import type { FloskelnListeManager } from "@ui";

export interface FloskelnDatenProps {
	manager: () => FloskelnListeManager;
	schuljahr: number,
	schulform: Schulform,
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Floskel>) => Promise<void>;
}
