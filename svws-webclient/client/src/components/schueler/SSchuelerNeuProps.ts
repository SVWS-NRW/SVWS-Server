import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { SchuelerStammdaten, SchuelerListeManager, OrtKatalogEintrag, OrtsteilKatalogEintrag, ReligionEintrag, KatalogEintrag } from "@core";

export interface SchuelerNeuProps {
	schuelerListeManager: () => SchuelerListeManager;
	addSchueler: (patchOject: Partial<SchuelerStammdaten>) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	mapReligionen: Map<number, ReligionEintrag>;
	mapFahrschuelerarten: Map<number, KatalogEintrag>;
	mapHaltestellen: Map<number, KatalogEintrag>;
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
