import type { Ankreuzkompetenz, BenutzerKompetenz, List, Schulform } from "@core";
import type { Checkpoint, AnkreuzkompetenzenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface AnkreuzkompetenzenNeuProps {
	manager: () => AnkreuzkompetenzenListeManager;
	addAnkreuzkompetenz: (patchObject: Partial<Ankreuzkompetenz>, idsJahrgaenge: List<number>) => Promise<Ankreuzkompetenz>;
	addJahrgaengezuordnungen: (idAnkreuzkompetenz: number, idsJahrgaenge: List<number>) => Promise<void>;
	schuljahr: number,
	schulform: Schulform,
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
