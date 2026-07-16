import type { SchuelerStammdaten, ErzieherStammdaten, List,
	SchuelerLernabschnittsdaten, SchuelerSchulbesuchsdaten, SchuelerTelefon, SchuelerVermerke } from "@core";
import type { SchuelerSchnelleingabeManager } from "@ui";

export interface SchuelerSchnelleingabeProps {
	manager: () => SchuelerSchnelleingabeManager;
	gotoDefaultView: (idEintrag?: number | null) => Promise<void>;
	gotoSchuelerNeuView: (navigate: boolean) => Promise<void>;
	patchSchueler: (data: Partial<SchuelerStammdaten>, id: number) => Promise<void>;
	getErzieher: () => List<ErzieherStammdaten>;
	addErzieher: (data: Partial<ErzieherStammdaten>, idEintrag: number, pos: number) => Promise<ErzieherStammdaten>;
	patchErzieher: (data: Partial<ErzieherStammdaten>, idEintrag: number) => Promise<void>;
	patchErzieherAnPosition: (data: Partial<ErzieherStammdaten>, idEintrag: number, idSchueler: number, pos: number) => Promise<void>;
	deleteErzieher: (idsEintraege: List<number>) => Promise<void>;
	getTelefone: () => List<SchuelerTelefon>;
	addTelefon: (data: Partial<SchuelerTelefon>, idSchueler: number) => Promise<void>;
	patchTelefon: (data: Partial<SchuelerTelefon>, idEintrag: number) => Promise<void>;
	deleteTelefone: (idsEintraege: List<number>) => Promise<void>;
	getVermerke: () => List<SchuelerVermerke>;
	addVermerk: (data: Partial<SchuelerVermerke>) => Promise<void>;
	patchVermerk: (data: Partial<SchuelerVermerke>, idEintrag: number) => Promise<void>;
	deleteVermerke: (idsEintraege: List<number>) => Promise<void>;
	patchSchulbesuchsdaten: (data: Partial<SchuelerSchulbesuchsdaten>, idSchueler: number) => Promise<void>;
	patchLernabschnittsdaten: (data: Partial<SchuelerLernabschnittsdaten>, idEintrag: number) => Promise<void>;
}
