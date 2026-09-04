import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { JahrgaengeListeManager } from "@ui/ui/manager/kataloge/JahrgaengeListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface JahrgaengeNeuProps {
	manager: () => JahrgaengeListeManager;
	add: (patchObject: Partial<JahrgangsDaten>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
