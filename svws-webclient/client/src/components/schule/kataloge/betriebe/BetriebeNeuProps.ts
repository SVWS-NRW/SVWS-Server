import type { Betrieb } from "@core/core/data/schule/Betrieb";
import type { BetriebeListeManager } from "@ui/ui/manager/kataloge/BetriebeListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface BetriebeNeuProps {
	manager: () => BetriebeListeManager;
	add: (betrieb: Partial<Betrieb>) => Promise<void>;
	goToDefaultView: (idBetrieb?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
