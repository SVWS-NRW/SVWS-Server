import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
import type { FloskelgruppenListeManager } from "@ui/ui/manager/kataloge/FloskelgruppenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FloskelgruppenNeuProps {
	manager: () => FloskelgruppenListeManager;
	add: (patchObject: Partial<Floskelgruppe>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
