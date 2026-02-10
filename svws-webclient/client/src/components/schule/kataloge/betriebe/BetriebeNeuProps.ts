import type { BetriebeListeManager } from "../../../../../../ui/src/ui/manager/kataloge/BetriebeListeManager";
import type { Checkpoint } from "@ui";
import type { BenutzerKompetenz, Betrieb } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface BetriebeNeuProps {
	manager: () => BetriebeListeManager;
	add: (betrieb: Partial<Betrieb>) => Promise<void>;
	goToDefaultView: (idBetrieb?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
