import type { OrtKatalogEintrag } from "@core/core/data/kataloge/OrtKatalogEintrag";
import type { OrteListeManager } from "@ui/ui/manager/kataloge/OrteListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface OrteNeuProps {
	manager: () => OrteListeManager;
	add: (patchObject: Partial<OrtKatalogEintrag>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
