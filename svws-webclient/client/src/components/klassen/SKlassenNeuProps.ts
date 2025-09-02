import type { Schulform, KlassenDaten } from "@core";
import type { Checkpoint, KlassenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KlassenNeuProps {
	manager: () => KlassenListeManager;
	schulform: Schulform;
	mapKlassenVorigerAbschnitt: () => Map<number, KlassenDaten>;
	mapKlassenFolgenderAbschnitt: () => Map<number, KlassenDaten>;
	add: (patchObject: Partial<KlassenDaten>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
