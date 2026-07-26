import type { GostKlausurplanManager, GostSchuelerklausurtermin, GostSchuelerklausur } from "@core";
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
