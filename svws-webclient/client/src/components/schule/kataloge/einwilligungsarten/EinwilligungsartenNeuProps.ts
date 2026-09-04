import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import type { EinwilligungsartenListeManager } from "@ui/ui/manager/kataloge/EinwilligungsartenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface EinwilligungsartenNeuProps {
	manager: () => EinwilligungsartenListeManager;
	add: (einwilligungsart: Partial<Einwilligungsart>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
