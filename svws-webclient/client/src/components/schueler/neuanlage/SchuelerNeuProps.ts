import type { SchuelerNeuManager } from "@ui/ui/manager/schueler/SchuelerNeuManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";

import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchuelerNeuProps {
	manager: () => SchuelerNeuManager;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
