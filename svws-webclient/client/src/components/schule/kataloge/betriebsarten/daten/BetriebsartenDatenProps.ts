import type { Betriebsart } from "@core/core/data/schule/Betriebsart";
import type { BetriebsartenListeManager } from "@ui/ui/manager/kataloge/BetriebsartenListeManager";

export interface BetriebsartenDatenProps {
	patch: (data: Partial<Betriebsart>) => Promise<boolean>;
	manager: () => BetriebsartenListeManager,
}
