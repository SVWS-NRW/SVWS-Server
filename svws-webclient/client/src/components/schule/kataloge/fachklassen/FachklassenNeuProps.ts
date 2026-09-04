import type { FachklasseEintrag } from "@core/core/data/schule/FachklasseEintrag";
import type { FachklassenListeManager } from "@ui/ui/manager/kataloge/FachklassenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FachklassenNeuProps {
	manager: () => FachklassenListeManager;
	add: (patchObject: Partial<FachklasseEintrag>) => Promise<void>;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
