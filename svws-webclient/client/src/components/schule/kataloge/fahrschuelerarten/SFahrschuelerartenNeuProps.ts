import type { BenutzerKompetenz, FahrschuelerartenListeManager, KatalogEintrag } from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface FahrschuelerartenNeuProps {
	manager: () => FahrschuelerartenListeManager;
	addFahrschuelerart: (data: Partial<KatalogEintrag>) => Promise<void>;
	goToDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
