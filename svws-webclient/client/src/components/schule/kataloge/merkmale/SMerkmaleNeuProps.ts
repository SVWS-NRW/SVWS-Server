import type { BenutzerKompetenz, Merkmal, MerkmaleListeManager } from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface MerkmaleNeuProps {
	manager: () => MerkmaleListeManager;
	addMerkmal: (data: Partial<Merkmal>) => Promise<void>;
	goToDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
