import type { BenutzerKompetenz, KindergaertenListeManager, Kindergarten } from "@core";
import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KindergaertenNeuProps {
	manager: () => KindergaertenListeManager;
	addKindergarten: (data: Partial<Kindergarten>) => Promise<void>;
	goToDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
