import type { BenutzerKompetenz, Merkmal } from "@core";
import type { Checkpoint, MerkmaleListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface MerkmaleNeuProps {
	manager: () => MerkmaleListeManager;
	addMerkmal: (data: Partial<Merkmal>) => Promise<void>;
	goToDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
