import type { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";
import type { SchuelerSchulbesuchsdaten } from "@core/asd/data/schueler/SchuelerSchulbesuchsdaten";
import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { ErzieherStammdaten } from "@core/core/data/erzieher/ErzieherStammdaten";
import type { SchuelerTelefon } from "@core/core/data/schueler/SchuelerTelefon";
import type { SchuelerVermerke } from "@core/core/data/schueler/SchuelerVermerke";
import type { List } from "@core/java/util/List";
import type { SchuelerSchnelleingabeManager } from "@ui/ui/manager/schueler/SchuelerSchnelleingabeManager";

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
