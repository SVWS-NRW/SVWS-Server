import type { KursListeManager } from "~/states/kurse/KursListeManager";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";

export interface KurseNeuProps {
	manager: () => KursListeManager;
	add: (patchObject: Partial<KursDaten>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
}
