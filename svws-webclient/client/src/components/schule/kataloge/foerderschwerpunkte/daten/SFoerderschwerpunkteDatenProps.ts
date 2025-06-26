import type { BenutzerKompetenz, FoerderschwerpunktEintrag, FoerderschwerpunkteListeManager } from "@core";

export interface FoerderschwerpunkteDatenProps {
	manager: () => FoerderschwerpunkteListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<FoerderschwerpunktEintrag>) => Promise<void>;
}
