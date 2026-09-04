import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { HaltestellenListeManager } from "@ui/ui/manager/kataloge/HaltestellenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface HaltestellenNeuProps {
	manager: () => HaltestellenListeManager;
	add: (patchObject: Partial<Haltestelle>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
