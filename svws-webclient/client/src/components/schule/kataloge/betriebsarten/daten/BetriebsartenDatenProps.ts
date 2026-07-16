import type { Betriebsart } from "@core";
import type { BetriebsartenListeManager } from "@ui";

export interface BetriebsartenDatenProps {
	patch: (data: Partial<Betriebsart>) => Promise<boolean>;
	manager: () => BetriebsartenListeManager,
}
