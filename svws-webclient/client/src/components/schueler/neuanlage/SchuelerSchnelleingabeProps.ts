import type { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";
import type { SchuelerSchulbesuchsdaten } from "@core/asd/data/schueler/SchuelerSchulbesuchsdaten";
import type { ErzieherStammdaten } from "@core/core/data/erzieher/ErzieherStammdaten";
import type { SchuelerVermerke } from "@core/core/data/schueler/SchuelerVermerke";
import type { List } from "@core/java/util/List";
import type { SchuelerSchnelleingabeManager } from "@ui/ui/manager/schueler/SchuelerSchnelleingabeManager";

export interface SchuelerSchnelleingabeProps {
	manager: () => SchuelerSchnelleingabeManager;
	getErzieher: () => List<ErzieherStammdaten>;
	addErzieher: (data: Partial<ErzieherStammdaten>, pos: number) => Promise<ErzieherStammdaten>;
	patchErzieher: (data: Partial<ErzieherStammdaten>, id: number) => Promise<boolean>;
	patchErzieherAnPosition: (data: Partial<ErzieherStammdaten>, id: number, pos: number) => Promise<boolean>;
	deleteErzieher: (idsEintraege: List<number>) => Promise<boolean>;
	getVermerke: () => List<SchuelerVermerke>;
	addVermerk: (data: Partial<SchuelerVermerke>) => Promise<void>;
	patchVermerk: (data: Partial<SchuelerVermerke>, idEintrag: number) => Promise<boolean>;
	deleteVermerke: (idsEintraege: List<number>) => Promise<void>;
	patchSchulbesuchsdaten: (data: Partial<SchuelerSchulbesuchsdaten>, idSchueler: number) => Promise<boolean>;
	patchLernabschnittsdaten: (data: Partial<SchuelerLernabschnittsdaten>, idEintrag: number) => Promise<boolean>;
}
