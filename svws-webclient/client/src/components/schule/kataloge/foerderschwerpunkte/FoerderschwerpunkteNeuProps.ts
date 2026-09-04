import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import type { FoerderschwerpunkteListeManager } from "@ui/ui/manager/kataloge/FoerderschwerpunkteListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FoerderschwerpunkteNeuProps {
	manager: () => FoerderschwerpunkteListeManager;
	add: (foerderschwerpunkt: Partial<FoerderschwerpunktEintrag>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
