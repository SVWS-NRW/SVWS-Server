import type { BenutzerKompetenz, FoerderschwerpunktEintrag, Schulform } from "@core";
import type { FoerderschwerpunkteListeManager } from "@ui";

export interface FoerderschwerpunkteDatenProps {
	manager: () => FoerderschwerpunkteListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (foerderschwerpunkt: Partial<FoerderschwerpunktEintrag>) => Promise<boolean>;
	schuljahr: number,
	schulform: Schulform,
}
