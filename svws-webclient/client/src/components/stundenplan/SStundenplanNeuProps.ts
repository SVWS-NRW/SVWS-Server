import type { Stundenplan } from "@core";
import type { Checkpoint, StundenplanListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface StundenplanNeuProps {
	manager: () => StundenplanListeManager;
	add: (patchObject: Partial<Stundenplan>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
