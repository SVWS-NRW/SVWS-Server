import type { BenutzerKompetenz, Sportbefreiung } from "@core";
import type { Checkpoint, SportbefreiungenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SportbefreiungenNeuProps {
	manager: () => SportbefreiungenListeManager;
	addSportbefreiung: (data: Partial<Sportbefreiung>) => Promise<void>;
	goToDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
