import type { BenutzerKompetenz, Lernplattform } from "@core";
import type { Checkpoint, LernplattformListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SLernplattformenNeuProps {
	manager: () => LernplattformListeManager;
	add: (patchObject: Partial<Lernplattform>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
