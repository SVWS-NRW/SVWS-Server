import type { BenutzerKompetenz, Kindergarten } from "@core";
import type { Checkpoint, KindergaertenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface KindergaertenNeuProps {
	manager: () => KindergaertenListeManager;
	addKindergarten: (data: Partial<Kindergarten>) => Promise<void>;
	goToDefaultView: (id?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
