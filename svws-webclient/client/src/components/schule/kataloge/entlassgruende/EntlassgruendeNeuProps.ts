import type { KatalogEntlassgrund } from "@core/core/data/kataloge/KatalogEntlassgrund";
import type { EntlassgruendeListeManager } from "@ui/ui/manager/kataloge/EntlassgruendeListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface EntlassgruendeNeuProps {
	manager: () => EntlassgruendeListeManager;
	add: (patchObject: Partial<KatalogEntlassgrund>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
