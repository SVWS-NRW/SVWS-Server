import type { BenutzerKompetenz, Fahrschuelerart } from "@core";
import type { Checkpoint, FahrschuelerartenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FahrschuelerartenNeuProps {
	manager: () => FahrschuelerartenListeManager;
	add: (data: Partial<Fahrschuelerart>) => Promise<void>;
	goToDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
