import type { BenutzerKompetenz, SchuelerStammdaten, Schulform, Schuljahresabschnitt, ServerMode, ErzieherStammdaten, KlassenDaten, List,
	SchuelerLernabschnittsdaten, SchuelerSchulbesuchsdaten, SchuelerTelefon, SchuelerVermerke } from "@core";
import type { SchuelerSchnelleingabeManager } from "../../../../../ui/src/ui/manager/schueler/SchuelerSchnelleingabeManager";

export interface SchuelerSchnelleingabeProps {
	manager: () => SchuelerSchnelleingabeManager;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	gotoSchuelerNeuView: (navigate: boolean) => Promise<void>;
	aktAbschnitt: Schuljahresabschnitt;
	serverMode: ServerMode;
	schulform: Schulform,
	benutzerKompetenzen: Set<BenutzerKompetenz>,

	patch: (patchObject: Partial<SchuelerStammdaten>, id: number) => Promise<void>;
	getListSchuelerErziehereintraege: () => List<ErzieherStammdaten>;
	addSchuelerErziehereintrag: (data: Partial<ErzieherStammdaten>, idEintrag: number, pos: number) => Promise<ErzieherStammdaten>;
	patchSchuelerErziehereintrag: (data: Partial<ErzieherStammdaten>, idEintrag: number) => Promise<void>;
	patchSchuelerErzieherAnPosition: (data: Partial<ErzieherStammdaten>, idEintrag: number, idSchueler: number, pos: number) => Promise<void>;
	deleteSchuelerErziehereintrage: (idsEintraege: List<number>) => Promise<void>;
	getListSchuelerTelefoneintraege: () => List<SchuelerTelefon>;
	addSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idSchueler: number) => Promise<void>;
	patchSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idEintrag: number) => Promise<void>;
	deleteSchuelerTelefoneintrage: (idsEintraege: List<number>) => Promise<void>;
	getListSchuelerVermerkeintraege: () => List<SchuelerVermerke>;
	addSchuelerVermerkeintrag: (data: Partial<SchuelerVermerke>) => Promise<void>;
	patchSchuelerVermerkeintrag: (data: Partial<SchuelerVermerke>, idEintrag: number) => Promise<void>;
	deleteSchuelerVermerkeintraege: (idsEintraege: List<number>) => Promise<void>;
	patchSchuelerSchulbesuchsdaten: (data: Partial<SchuelerSchulbesuchsdaten>, idSchueler: number) => Promise<void>;
	patchSchuelerLernabschnittsdaten: (data: Partial<SchuelerLernabschnittsdaten>, idEintrag: number) => Promise<void>;
	getSchuelerKlassenFuerAbschnitt: (idAbschnitt: number) => Promise<List<KlassenDaten>>;
}
