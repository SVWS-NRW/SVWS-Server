import type { Schulform, KlasseDetails } from "@core";
import type { Checkpoint, KlassenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KlassenNeuProps {
	manager: () => KlassenListeManager;
	schulform: Schulform;
	mapKlassenVorigerAbschnitt: () => Map<number, KlasseDetails>;
	mapKlassenFolgenderAbschnitt: () => Map<number, KlasseDetails>;
	add: (patchObject: Partial<KlasseDetails>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
