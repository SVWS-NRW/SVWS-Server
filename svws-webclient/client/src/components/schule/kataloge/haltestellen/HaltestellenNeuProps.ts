import type { Haltestelle } from "@core";
import type { Checkpoint, HaltestellenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface HaltestellenNeuProps {
	manager: () => HaltestellenListeManager;
	add: (patchObject: Partial<Haltestelle>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
