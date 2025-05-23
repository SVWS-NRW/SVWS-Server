import type { Abteilung, AbteilungenListeManager, AbteilungKlassenzuordnung, List } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { Checkpoint } from "@ui";

export interface AbteilungenNeuProps {
	manager: () => AbteilungenListeManager;
	addAbteilung: (patchObject: Partial<Abteilung>) => Promise<number>;
	addKlassenzuordnungen: (data: List<AbteilungKlassenzuordnung>, idAbteilung : number) => Promise<void>;
	goToLehrer: (idAbteilungsleiter : number) => Promise<void>;
	goToDefaultView: (idEintrag?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
