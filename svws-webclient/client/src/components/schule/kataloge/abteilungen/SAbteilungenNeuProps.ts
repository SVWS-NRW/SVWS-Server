import type { AbteilungenListeManager } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint } from "@ui";

export interface AbteilungenNeuProps {
	manager: () => AbteilungenListeManager;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
