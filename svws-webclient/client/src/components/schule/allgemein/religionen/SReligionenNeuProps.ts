import type { BenutzerKompetenz, ReligionEintrag } from "@core";
import type { ReligionenListeManager } from "@ui";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface ReligionenNeuProps {
	manager: () => ReligionenListeManager;
	add: (patchObject: Partial<ReligionEintrag>) => Promise<void>;
	gotoDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
