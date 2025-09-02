import type { BenutzerKompetenz, ReligionEintrag } from "@core";
import type { ReligionListeManager } from "@ui";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KatalogReligionNeuProps {
	religionListeManager: () => ReligionListeManager;
	add: (patchObject: Partial<ReligionEintrag>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
