import type { BenutzerKompetenz, Leitungsfunktion } from "@core";
import type { LeitungsfunktionenListeManager } from "@ui";

export interface LeitungsfunktionenDatenProps {
	manager: () => LeitungsfunktionenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Leitungsfunktion>) => Promise<boolean>;
}