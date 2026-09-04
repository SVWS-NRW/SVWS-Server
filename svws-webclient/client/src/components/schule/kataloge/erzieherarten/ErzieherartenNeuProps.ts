import type { Erzieherart } from "@core/core/data/erzieher/Erzieherart";
import type { ErzieherartListeManager } from "@ui/ui/manager/kataloge/ErzieherartListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface ErzieherartenNeuProps {
	manager: () => ErzieherartListeManager;
	add: (patchObject: Partial<Erzieherart>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
