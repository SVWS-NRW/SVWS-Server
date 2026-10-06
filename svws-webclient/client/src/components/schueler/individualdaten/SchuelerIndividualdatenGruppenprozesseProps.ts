import type { Checkpoint } from "@ui/ui/modal/Checkpoint";

import type { PendingStateManagerSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/PendingStateManagerSchuelerIndividualdaten";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchuelerIndividualdatenGruppenprozesseProps {
	pendingStateManager: () => PendingStateManagerSchuelerIndividualdaten
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
