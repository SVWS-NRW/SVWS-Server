import type { BenutzerKompetenz, FoerderschwerpunktEintrag } from "@core";
import type { FoerderschwerpunkteListeManager } from "@ui";

export interface FoerderschwerpunkteDatenProps {
	manager: () => FoerderschwerpunkteListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<FoerderschwerpunktEintrag>) => Promise<void>;
}
