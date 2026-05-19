import type { BenutzerKompetenz, Floskelgruppe } from "@core";
import type { FloskelgruppenListeManager } from "@ui";

export interface FloskelgruppenDatenProps {
	manager: () => FloskelgruppenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Floskelgruppe>) => Promise<boolean>;
}
