import type { Floskel } from "@core";
import type { Checkpoint, FloskelnListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FloskelnNeuProps {
	manager: () => FloskelnListeManager;
	add: (patchObject: Partial<Floskel>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
