import type { JahrgangsDaten, VermerkartenListeManager } from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchuleVermerkartNeuProps {
	manager: () => VermerkartenListeManager;
	add: (patchObject: Partial<JahrgangsDaten>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
