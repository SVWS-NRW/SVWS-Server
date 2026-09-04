import type { GostSchuelerklausur } from "@core/core/data/gost/klausuren/GostSchuelerklausur";
import type { GostSchuelerklausurtermin } from "@core/core/data/gost/klausuren/GostSchuelerklausurtermin";
import type { GostKlausurplanManager } from "@core/core/utils/gost/klausuren/GostKlausurplanManager";
import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";

export interface SchuelerLernabschnittGostKlausurenProps {
	manager: () => SchuelerLernabschnittManager;
	kMan: () => GostKlausurplanManager;
	hatKlausurManager: () => boolean;
	createSchuelerklausurtermin: (skt: Partial<GostSchuelerklausurtermin>) => Promise<void>;
	deleteSchuelerklausurtermin: (sk: GostSchuelerklausurtermin) => Promise<void>;
	patchSchuelerklausurtermin: (id: number, data: Partial<GostSchuelerklausurtermin>) => Promise<void>;
	patchSchuelerklausur: (id: number, data: Partial<GostSchuelerklausur>) => Promise<void>;
	gotoPlanung: () => Promise<void>;
}
