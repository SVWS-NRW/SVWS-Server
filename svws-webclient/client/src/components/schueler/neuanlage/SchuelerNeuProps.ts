import type { Checkpoint, SchuelerListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { SchuelerStammdaten, Kindergarten, EinschulungsartKatalogEintrag, Schuljahresabschnitt, Schulform, SchuelerSchulbesuchsdaten, BenutzerKompetenz,
	KlassenDaten, List, SchuelerNeu } from "@core";

export interface SchuelerNeuProps {
	schuelerListeManager: () => SchuelerListeManager;
	add: (addObject: Partial<SchuelerNeu>) => Promise<SchuelerStammdaten>;
	getSchuelerKlassenFuerAbschnitt: (idAbschnitt: number) => Promise<List<KlassenDaten>>;
	patchSchuelerSchulbesuchdaten: (addObject: Partial<SchuelerSchulbesuchsdaten>, idEintrag: number) => Promise<void>;
	mapKindergaerten: Map<number, Kindergarten>;
	mapEinschulungsarten: Map<number, EinschulungsartKatalogEintrag>;
	initialeSchuelerDaten: () => SchuelerNeu | null;
	gotoSchnelleingabeView: (navigate: boolean, idEintrag?: number | null) => Promise<void>;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	aktAbschnitt: Schuljahresabschnitt;
	schulform: Schulform,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
