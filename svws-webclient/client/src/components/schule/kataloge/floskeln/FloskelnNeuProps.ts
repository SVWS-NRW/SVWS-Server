import type { Floskel } from "@core/core/data/schule/Floskel";
import type { FloskelnListeManager } from "@ui/ui/manager/kataloge/FloskelnListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FloskelnNeuProps {
	manager: () => FloskelnListeManager;
	add: (patchObject: Partial<Floskel>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
