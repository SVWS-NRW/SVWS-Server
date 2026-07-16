import type { SchuelerSchwerpunkt } from "@core";
import type { SchwerpunkteListeManager } from "@ui";

export interface SchwerpunkteDatenProps {
	patch: (data: Partial<SchuelerSchwerpunkt>) => Promise<boolean>;
	manager: () => SchwerpunkteListeManager,
}
