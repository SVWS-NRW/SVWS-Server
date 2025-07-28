import type { JahrgangsDaten, JahrgaengeListeManager } from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchuleJahrgangNeuProps {
	manager: () => JahrgaengeListeManager;
	add: (patchObject: Partial<JahrgangsDaten>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
