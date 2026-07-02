import type { KlassenDaten } from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { KlassenListeManager } from "~/states/klassen/KlassenListeManager";

export interface KlassenNeuProps {
	manager: () => KlassenListeManager;
	add: (patchObject: Partial<KlassenDaten>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
