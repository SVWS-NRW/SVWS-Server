import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchuelerNeuProps {
	checkpoint?: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
