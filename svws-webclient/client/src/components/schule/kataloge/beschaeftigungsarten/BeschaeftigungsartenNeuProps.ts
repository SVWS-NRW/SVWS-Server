import type { BenutzerKompetenz, Beschaeftigungsart } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint, BeschaeftigungsartenListeManager } from "@ui";

export interface BeschaeftigungsartenNeuProps {
	manager: () => BeschaeftigungsartenListeManager;
	addBeschaeftigungsart: (patchObject: Partial<Beschaeftigungsart>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
