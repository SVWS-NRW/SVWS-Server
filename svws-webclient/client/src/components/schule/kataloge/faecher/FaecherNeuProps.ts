import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { FaecherListeManager } from "@ui/ui/manager/kataloge/FaecherListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FaecherNeuProps {
	manager: () => FaecherListeManager;
	add: (patchObject: Partial<FachDaten>) => Promise<void>;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
