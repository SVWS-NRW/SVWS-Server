import type { Abteilung, List } from "@core";
import type { AbteilungenListeManager, Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface AbteilungenNeuProps {
	manager: () => AbteilungenListeManager;
	add: (abteilung: Partial<Abteilung>, assignedKlassenIds: List<number>, addAbteilungFolgeAbschnitt: boolean) => Promise<number>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
