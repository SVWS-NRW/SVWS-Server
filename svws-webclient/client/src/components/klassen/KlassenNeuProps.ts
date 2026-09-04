import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { KlassenListeManager } from "~/states/klassen/KlassenListeManager";

export interface KlassenNeuProps {
	manager: () => KlassenListeManager;
	add: (patchObject: Partial<KlassenDaten>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
