import type { Telefonart } from "@core/core/data/schule/Telefonart";
import type { TelefonartenListeManager } from "@ui/ui/manager/kataloge/TelefonartenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface TelefonartenNeuProps {
	manager: () => TelefonartenListeManager;
	add: (patchObject: Partial<Telefonart>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
