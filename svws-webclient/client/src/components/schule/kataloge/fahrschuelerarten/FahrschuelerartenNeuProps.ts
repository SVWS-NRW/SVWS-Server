import type { Fahrschuelerart } from "@core/core/data/schule/Fahrschuelerart";
import type { FahrschuelerartenListeManager } from "@ui/ui/manager/kataloge/FahrschuelerartenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FahrschuelerartenNeuProps {
	manager: () => FahrschuelerartenListeManager;
	add: (data: Partial<Fahrschuelerart>) => Promise<void>;
	goToDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
