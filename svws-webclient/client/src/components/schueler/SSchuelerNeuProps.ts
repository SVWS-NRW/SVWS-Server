import type { Checkpoint, SchuelerListeManager } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { SchuelerStammdaten, Kindergarten, EinschulungsartKatalogEintrag, Schuljahresabschnitt, Schulform, SchuelerSchulbesuchsdaten, BenutzerKompetenz } from "@core";

export interface SchuelerNeuProps {
	schuelerListeManager: () => SchuelerListeManager;
	addSchueler: (addObject: Partial<SchuelerStammdaten>) => Promise<SchuelerStammdaten>;
	patchSchuelerSchulbesuchdaten: (addObject: Partial<SchuelerSchulbesuchsdaten>, idEintrag: number) => Promise<void>;
	mapKindergaerten: Map<number, Kindergarten>;
	mapEinschulungsarten: Map<number, EinschulungsartKatalogEintrag>;
	gotoSchnelleingabeView: (navigate: boolean, idEintrag?: number | null) => Promise<void>;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	aktAbschnitt: Schuljahresabschnitt;
	schulform: Schulform,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
