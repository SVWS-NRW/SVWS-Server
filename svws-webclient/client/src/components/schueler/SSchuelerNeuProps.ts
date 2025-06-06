import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { SchuelerStammdaten, SchuelerListeManager, OrtKatalogEintrag, OrtsteilKatalogEintrag, ReligionEintrag, KatalogEintrag, Kindergarten, EinschulungsartKatalogEintrag, Schuljahresabschnitt, Schulform, SchulEintrag, } from "@core";

export interface SchuelerNeuProps {
	schuelerListeManager: () => SchuelerListeManager;
	addSchueler: (addObject: Partial<SchuelerStammdaten>) => Promise<SchuelerStammdaten>;
	patch: (patchObject: Partial<SchuelerStammdaten>, id: number) => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	mapSchulen: Map<string, SchulEintrag>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	mapReligionen: Map<number, ReligionEintrag>;
	mapFahrschuelerarten: Map<number, KatalogEintrag>;
	mapHaltestellen: Map<number, KatalogEintrag>;
	mapKindergaerten: Map<number, Kindergarten>;
	mapEinschulungsarten: Map<number, EinschulungsartKatalogEintrag>
	aktAbschnitt: Schuljahresabschnitt;
	schulform: Schulform,
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
