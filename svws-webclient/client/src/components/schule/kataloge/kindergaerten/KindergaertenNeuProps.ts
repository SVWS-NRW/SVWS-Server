import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
import type { KindergaertenListeManager } from "@ui/ui/manager/kataloge/KindergaertenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KindergaertenNeuProps {
	manager: () => KindergaertenListeManager;
	add: (data: Partial<Kindergarten>) => Promise<void>;
	goToDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
