import type { Leitungsfunktion } from "@core";
import type { Checkpoint, LeitungsfunktionenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface LeitungsfunktionenNeuProps {
	manager: () => LeitungsfunktionenListeManager;
	add: (patchObject: Partial<Leitungsfunktion>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
