import type { Ankreuzkompetenz } from "@core/core/data/schule/Ankreuzkompetenz";
import type { List } from "@core/java/util/List";
import type { AnkreuzkompetenzenListeManager } from "@ui/ui/manager/kataloge/AnkreuzkompetenzenListeManager";
import type { Checkpoint } from "@ui/ui/modal/Checkpoint";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface AnkreuzkompetenzenNeuProps {
	manager: () => AnkreuzkompetenzenListeManager;
	addAnkreuzkompetenz: (patchObject: Partial<Ankreuzkompetenz>, idsJahrgaenge: List<number>) => Promise<Ankreuzkompetenz>;
	addJahrgaengezuordnungen: (idAnkreuzkompetenz: number, idsJahrgaenge: List<number>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
