import type { SchuelerSchwerpunkt as Schwerpunkt } from "@core";
import type { Checkpoint, SchwerpunkteListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchwerpunkteNeuProps {
	manager: () => SchwerpunkteListeManager;
	add: (patchObject: Partial<Schwerpunkt>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
