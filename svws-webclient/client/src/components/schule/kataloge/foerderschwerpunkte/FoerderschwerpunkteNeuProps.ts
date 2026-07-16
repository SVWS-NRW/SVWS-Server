import type { FoerderschwerpunktEintrag } from "@core";
import type { Checkpoint, FoerderschwerpunkteListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FoerderschwerpunkteNeuProps {
	manager: () => FoerderschwerpunkteListeManager;
	add: (foerderschwerpunkt: Partial<FoerderschwerpunktEintrag>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
