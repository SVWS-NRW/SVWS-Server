import type { Leitungsfunktion } from "@core/core/data/schule/Leitungsfunktion";
import type { LeitungsfunktionenListeManager } from "@ui/ui/manager/kataloge/LeitungsfunktionenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface LeitungsfunktionenNeuProps {
	manager: () => LeitungsfunktionenListeManager;
	add: (patchObject: Partial<Leitungsfunktion>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
