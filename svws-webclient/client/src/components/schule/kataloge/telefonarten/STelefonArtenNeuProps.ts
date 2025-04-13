import type { TelefonArt, TelefonArtListeManager } from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface STelefonArtenNeuProps {
	manager: () => TelefonArtListeManager;
	add: (patchObject: Partial<TelefonArt>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
