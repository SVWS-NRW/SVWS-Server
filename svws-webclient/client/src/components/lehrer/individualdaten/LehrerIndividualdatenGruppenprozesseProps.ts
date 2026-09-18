import type { Checkpoint } from "@ui/ui/modal/Checkpoint";

import type { PendingStateManagerLehrerIndividualdaten } from "~/router/apps/lehrer/individualdaten/PendingStateManagerLehrerIndividualdaten";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface LehrerIndividualdatenGruppenprozesseProps {
	pendingStateManager: () => PendingStateManagerLehrerIndividualdaten
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
