import type { OrtsteilKatalogEintrag } from "@core";
import type { Checkpoint, OrtsteileListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface OrtsteileNeuProps {
	manager: () => OrtsteileListeManager;
	add: (ortsteil: Partial<OrtsteilKatalogEintrag>) => Promise<void>;
	goToDefaultView: (idortsteil?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
