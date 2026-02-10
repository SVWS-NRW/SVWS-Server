import type { BenutzerKompetenz, FoerderschwerpunktEintrag, Schulform } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint, FoerderschwerpunkteListeManager } from "@ui";

export interface FoerderschwerpunkteNeuProps {
	manager: () => FoerderschwerpunkteListeManager;
	add: (foerderschwerpunkt: Partial<FoerderschwerpunktEintrag>) => Promise<void>;
	schuljahr: number,
	schulform: Schulform,
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
