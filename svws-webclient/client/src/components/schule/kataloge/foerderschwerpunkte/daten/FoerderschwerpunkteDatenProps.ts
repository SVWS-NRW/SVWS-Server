import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import type { FoerderschwerpunkteListeManager } from "@ui/ui/manager/kataloge/FoerderschwerpunkteListeManager";

export interface FoerderschwerpunkteDatenProps {
	manager: () => FoerderschwerpunkteListeManager;
	patch: (foerderschwerpunkt: Partial<FoerderschwerpunktEintrag>) => Promise<boolean>;
}
