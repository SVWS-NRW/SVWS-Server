import type { Abteilung, BenutzerKompetenz, List } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint, AbteilungenListeManager } from "@ui";

export interface AbteilungenNeuProps {
	manager: () => AbteilungenListeManager;
	add: (abteilung: Partial<Abteilung>, assignedKlassenIds: List<number>, addAbteilungFolgeAbschnitt: boolean) => Promise<number>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
