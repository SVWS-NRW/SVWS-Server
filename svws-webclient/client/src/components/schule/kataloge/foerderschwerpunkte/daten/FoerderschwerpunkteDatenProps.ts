import type { FoerderschwerpunktEintrag } from "@core";
import type { FoerderschwerpunkteListeManager } from "@ui";

export interface FoerderschwerpunkteDatenProps {
	manager: () => FoerderschwerpunkteListeManager;
	patch: (foerderschwerpunkt: Partial<FoerderschwerpunktEintrag>) => Promise<boolean>;
}
