import type { Telefonart } from "@core";
import type { Checkpoint, TelefonartenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface TelefonartenNeuProps {
	manager: () => TelefonartenListeManager;
	add: (patchObject: Partial<Telefonart>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
