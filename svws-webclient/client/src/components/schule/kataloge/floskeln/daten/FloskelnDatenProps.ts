import type { BenutzerKompetenz, Floskel } from "@core";
import type { FloskelnListeManager } from "@ui";

export interface FloskelnDatenProps {
	manager: () => FloskelnListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Floskel>) => Promise<boolean>;
}
