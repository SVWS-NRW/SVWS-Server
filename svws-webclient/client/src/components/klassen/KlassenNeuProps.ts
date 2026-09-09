import type { Checkpoint } from "@ui/ui/modal/Checkpoint";

import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KlassenNeuProps {
	checkpoint: () => Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
