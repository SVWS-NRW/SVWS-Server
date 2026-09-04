import type { Abteilung } from "@core/core/data/schule/Abteilung";
import type { List } from "@core/java/util/List";
import type { AbteilungenListeManager } from "@ui/ui/manager/kataloge/AbteilungenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface AbteilungenNeuProps {
	manager: () => AbteilungenListeManager;
	add: (abteilung: Partial<Abteilung>, assignedKlassenIds: List<number>, addAbteilungFolgeAbschnitt: boolean) => Promise<number>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
