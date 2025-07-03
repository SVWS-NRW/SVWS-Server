import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { SchuelerStammdaten, SchuelerListeManager, OrtKatalogEintrag, OrtsteilKatalogEintrag, ReligionEintrag } from "@core";

export interface SchuelerNeuProps {
	schuelerListeManager: () => SchuelerListeManager;
	addSchueler: (patchOject: Partial<SchuelerStammdaten>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	mapReligionen: Map<number, ReligionEintrag>
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
