import type { Beschaeftigungsart } from "@core/core/data/schule/Beschaeftigungsart";
import type { BeschaeftigungsartenListeManager } from "@ui/ui/manager/kataloge/BeschaeftigungsartenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface BeschaeftigungsartenNeuProps {
	manager: () => BeschaeftigungsartenListeManager;
	add: (patchObject: Partial<Beschaeftigungsart>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
