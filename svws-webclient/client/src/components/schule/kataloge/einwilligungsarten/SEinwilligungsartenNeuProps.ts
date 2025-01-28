import type {Einwilligungsart, EinwilligungsartenListeManager} from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchuleEinwilligungsartenNeuProps {
	manager: () => EinwilligungsartenListeManager;
	add: (patchObject: Partial<Einwilligungsart>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
