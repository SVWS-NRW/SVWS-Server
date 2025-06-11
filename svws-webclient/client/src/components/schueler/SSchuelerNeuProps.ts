import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { SchuelerStammdaten, SchuelerListeManager, OrtKatalogEintrag, OrtsteilKatalogEintrag, ReligionEintrag, KatalogEintrag, Kindergarten, EinschulungsartKatalogEintrag, Schuljahresabschnitt, Schulform, SchulEintrag, TelefonArt, List, SchuelerTelefon, } from "@core";

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
	mapEinschulungsarten: Map<number, EinschulungsartKatalogEintrag>;
	mapTelefonArten: Map<number, TelefonArt>;
	getListSchuelerTelefoneintraege: () => List<SchuelerTelefon>;
	addSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idSchueler: number) => Promise<void>;
	patchSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idEintrag: number) => Promise<void>;
	deleteSchuelerTelefoneintrage: (idsEintraege: List<number>) => Promise<void>;
	aktAbschnitt: Schuljahresabschnitt;
	schulform: Schulform,
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
