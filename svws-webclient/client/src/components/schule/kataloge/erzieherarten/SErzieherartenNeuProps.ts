import type { Erzieherart, ErzieherartListeManager } from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SErzieherartenNeuProps {
	manager: () => ErzieherartListeManager;
	add: (patchObject: Partial<Erzieherart>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
