import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
import type { LernplattformListeManager } from "@ui/ui/manager/kataloge/LernplattformListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface LernplattformenNeuProps {
	manager: () => LernplattformListeManager;
	add: (patchObject: Partial<Lernplattform>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
