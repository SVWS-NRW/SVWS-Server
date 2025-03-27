import type { Lernplattform, LernplattformListeManager } from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SLernplattformenNeuProps {
	manager: () => LernplattformListeManager;
	add: (patchObject: Partial<Lernplattform>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
