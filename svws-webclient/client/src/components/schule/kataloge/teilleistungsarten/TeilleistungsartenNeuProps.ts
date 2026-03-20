import type { BenutzerKompetenz, Teilleistungsart } from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { TeilleistungsartenListeManager } from "./manager/TeilleistungsartenListeManager";

export interface TeilleistungsartenNeuProps {
	manager: () => TeilleistungsartenListeManager;
	add: (patchObject: Partial<Teilleistungsart>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
