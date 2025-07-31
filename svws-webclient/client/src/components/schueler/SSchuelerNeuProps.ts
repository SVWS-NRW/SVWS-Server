import type { Checkpoint } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";
import type { SchuelerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag, ReligionEintrag, KatalogEintrag, Kindergarten, EinschulungsartKatalogEintrag, Schuljahresabschnitt, Schulform, SchulEintrag, TelefonArt, List, SchuelerTelefon, SchuelerSchulbesuchsdaten, Haltestelle,	BenutzerKompetenz, Erzieherart, ErzieherStammdaten} from "@core";
import type { SchuelerListeManager } from "@ui";

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
	mapHaltestellen: Map<number, Haltestelle>;
	mapKindergaerten: Map<number, Kindergarten>;
	mapEinschulungsarten: Map<number, EinschulungsartKatalogEintrag>;
	mapTelefonArten: Map<number, TelefonArt>;
	mapErzieherarten: Map<number, Erzieherart>;
	getListSchuelerErziehereintraege: () => List<ErzieherStammdaten>;
	addSchuelerErziehereintrag: (data: Partial<ErzieherStammdaten>, idEintrag: number, pos: number) => Promise<ErzieherStammdaten>;
	patchSchuelerErziehereintrag: (data: Partial<ErzieherStammdaten>, idEintrag: number) => Promise<void>;
	patchSchuelerErzieherAnPosition: (data : Partial<ErzieherStammdaten>, idEintrag: number, idSchueler: number, pos: number) => Promise<void>;
	deleteSchuelerErziehereintrage: (idsEintraege: List<number>) => Promise<void>;
	getListSchuelerTelefoneintraege: () => List<SchuelerTelefon>;
	addSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idSchueler: number) => Promise<void>;
	patchSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idEintrag: number) => Promise<void>;
	deleteSchuelerTelefoneintrage: (idsEintraege: List<number>) => Promise<void>;
	patchSchuelerKindergarten: (data: Partial<SchuelerSchulbesuchsdaten>, idSchueler: number) => Promise<void>;
	aktAbschnitt: Schuljahresabschnitt;
	schulform: Schulform,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	checkpoint: Checkpoint;
	continueRoutingAfterCheckpoint: () => Promise<RoutingStatus>;
}
