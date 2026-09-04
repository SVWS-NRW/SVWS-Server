import type { Teilleistungsart } from "@core/core/data/kataloge/Teilleistungsart";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { TeilleistungsartenListeManager } from "~/states/teilleistungsarten/TeilleistungsartenListeManager";

export interface TeilleistungsartenNeuProps {
	manager: () => TeilleistungsartenListeManager;
	add: (patchObject: Partial<Teilleistungsart>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
