import type { Floskelgruppe } from "@core";
import type { Checkpoint, FloskelgruppenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FloskelgruppenNeuProps {
	manager: () => FloskelgruppenListeManager;
	add: (patchObject: Partial<Floskelgruppe>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
