import type { SchulinterneFunktion } from "@core/core/data/schule/SchulinterneFunktion";
import type { SchulinterneFunktionenListeManager } from "@ui/ui/manager/kataloge/SchulinterneFunktionenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";

import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchulinterneFunktionenNeuProps {
	manager: () => SchulinterneFunktionenListeManager;
	add: (patchObject: Partial<SchulinterneFunktion>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
