import type { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";

import type { PendingStateManagerLehrerIndividualdaten } from "~/router/apps/lehrer/individualdaten/PendingStateManagerLehrerIndividualdaten";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface LehrerIndividualdatenGruppenprozesseProps {
	lehrerListeManager: () => LehrerListeManager;
	autofocus: boolean;
	patchMultiple: () => Promise<void>;
	pendingStateManager: () => PendingStateManagerLehrerIndividualdaten
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
