import type { BenutzerKompetenz, Floskelgruppe, Schulform } from "@core";
import type { FloskelgruppenListeManager } from "@ui";

export interface FloskelgruppenDatenProps {
	manager: () => FloskelgruppenListeManager;
	schuljahr: number,
	schulform: Schulform,
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Floskelgruppe>) => Promise<void>;
}
