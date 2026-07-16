import type { JahrgangsDaten } from "@core";
import type { Checkpoint, JahrgaengeListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface JahrgaengeNeuProps {
	manager: () => JahrgaengeListeManager;
	add: (patchObject: Partial<JahrgangsDaten>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
