import type { SchuelerSchwerpunkt } from "@core/core/data/kataloge/SchuelerSchwerpunkt";
import type { SchwerpunkteListeManager } from "@ui/ui/manager/kataloge/SchwerpunkteListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchwerpunkteNeuProps {
	manager: () => SchwerpunkteListeManager;
	add: (patchObject: Partial<SchuelerSchwerpunkt>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
