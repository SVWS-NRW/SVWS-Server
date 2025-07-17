import type { BenutzerKompetenz, FoerderschwerpunktEintrag, FoerderschwerpunkteListeManager } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint } from "@ui";

export interface FoerderschwerpunkteNeuProps {
	manager: () => FoerderschwerpunkteListeManager;
	addFoerderschwerpunkt: (patchObject: Partial<FoerderschwerpunktEintrag>) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
