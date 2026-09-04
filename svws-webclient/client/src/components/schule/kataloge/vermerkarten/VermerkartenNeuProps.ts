import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
import type { VermerkartenListeManager } from "@ui/ui/manager/kataloge/VermerkartenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface VermerkartenNeuProps {
	manager: () => VermerkartenListeManager;
	add: (patchObject: Partial<VermerkartEintrag>) => Promise<void>;
	goToDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
