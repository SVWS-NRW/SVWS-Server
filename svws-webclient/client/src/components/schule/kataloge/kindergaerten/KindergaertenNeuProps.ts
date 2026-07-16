import type { Kindergarten } from "@core";
import type { Checkpoint, KindergaertenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KindergaertenNeuProps {
	manager: () => KindergaertenListeManager;
	add: (data: Partial<Kindergarten>) => Promise<void>;
	goToDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
