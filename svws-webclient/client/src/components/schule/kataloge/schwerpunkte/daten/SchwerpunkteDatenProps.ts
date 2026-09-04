import type { SchuelerSchwerpunkt } from "@core/core/data/kataloge/SchuelerSchwerpunkt";
import type { SchwerpunkteListeManager } from "@ui/ui/manager/kataloge/SchwerpunkteListeManager";

export interface SchwerpunkteDatenProps {
	patch: (data: Partial<SchuelerSchwerpunkt>) => Promise<boolean>;
	manager: () => SchwerpunkteListeManager,
}
