import type { Betriebsart } from "@core/core/data/schule/Betriebsart";
import type { BetriebsartenListeManager } from "@ui/ui/manager/kataloge/BetriebsartenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface BetriebsartenNeuProps {
	manager: () => BetriebsartenListeManager;
	add: (patchObject: Partial<Betriebsart>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
