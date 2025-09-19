import type { BenutzerKompetenz, Einwilligungsart } from "@core";
import type { Checkpoint, EinwilligungsartenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchuleEinwilligungsartenNeuProps {
	manager: () => EinwilligungsartenListeManager;
	add: (patchObject: Partial<Einwilligungsart>) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
