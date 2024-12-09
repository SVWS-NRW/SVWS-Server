import type { Schulform, KursListeManager, KursDaten } from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KurseNeuProps {
	manager: () => KursListeManager;
	schulform: Schulform;
	add: (patchObject: Partial<KursDaten>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
