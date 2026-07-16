import type { BetriebeListeManager, Checkpoint } from "@ui";
import type { Betrieb } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface BetriebeNeuProps {
	manager: () => BetriebeListeManager;
	add: (betrieb: Partial<Betrieb>) => Promise<void>;
	goToDefaultView: (idBetrieb?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
