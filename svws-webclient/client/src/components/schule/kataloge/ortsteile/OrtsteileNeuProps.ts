import type { OrtsteilKatalogEintrag } from "@core/core/data/kataloge/OrtsteilKatalogEintrag";
import type { OrtsteileListeManager } from "@ui/ui/manager/kataloge/OrtsteileListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface OrtsteileNeuProps {
	manager: () => OrtsteileListeManager;
	add: (ortsteil: Partial<OrtsteilKatalogEintrag>) => Promise<void>;
	goToDefaultView: (idortsteil?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
