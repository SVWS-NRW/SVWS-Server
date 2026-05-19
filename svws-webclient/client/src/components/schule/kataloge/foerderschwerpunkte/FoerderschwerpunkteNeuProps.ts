import type { BenutzerKompetenz, FoerderschwerpunktEintrag } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint, FoerderschwerpunkteListeManager } from "@ui";

export interface FoerderschwerpunkteNeuProps {
	manager: () => FoerderschwerpunkteListeManager;
	add: (foerderschwerpunkt: Partial<FoerderschwerpunktEintrag>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
