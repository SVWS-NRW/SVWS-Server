import type { KatalogEntlassgrund } from "@core";
import type { Checkpoint, EntlassgruendeListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface EntlassgruendeNeuProps {
	manager: () => EntlassgruendeListeManager;
	add: (patchObject: Partial<KatalogEntlassgrund>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
