import type { Ankreuzkompetenz, List } from "@core";
import type { AnkreuzkompetenzenListeManager, Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface AnkreuzkompetenzenNeuProps {
	manager: () => AnkreuzkompetenzenListeManager;
	addAnkreuzkompetenz: (patchObject: Partial<Ankreuzkompetenz>, idsJahrgaenge: List<number>) => Promise<Ankreuzkompetenz>;
	addJahrgaengezuordnungen: (idAnkreuzkompetenz: number, idsJahrgaenge: List<number>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
