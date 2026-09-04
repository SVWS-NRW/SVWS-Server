import type { LehrerStammdaten } from "@core/asd/data/lehrer/LehrerStammdaten";
import type { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface LehrerNeuProps {
	lehrerListeManager: () => LehrerListeManager;
	add: (patchObject: Partial<LehrerStammdaten>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
