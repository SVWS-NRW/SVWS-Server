import type { Ankreuzkompetenz, AnkreuzkompetenzJahrgangszuordnung, BenutzerKompetenz, List, Schulform } from "@core";
import type { Checkpoint, AnkreuzkompetenzenListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface AnkreuzkompetenzenNeuProps {
	manager: () => AnkreuzkompetenzenListeManager;
	addAnkreuzkompetenz: (patchObject: Partial<Ankreuzkompetenz>) => Promise<Ankreuzkompetenz>;
	addJahrgaengezuordnungen: (data: List<AnkreuzkompetenzJahrgangszuordnung>, idAnkreuzkompetenz: number) => Promise<void>;
	schuljahr: number,
	schulform: Schulform,
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
