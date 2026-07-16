import type { Beschaeftigungsart } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint, BeschaeftigungsartenListeManager } from "@ui";

export interface BeschaeftigungsartenNeuProps {
	manager: () => BeschaeftigungsartenListeManager;
	add: (patchObject: Partial<Beschaeftigungsart>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
