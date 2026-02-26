import type { SchuleStammdaten, GostKlausurplanManager, GostSchuelerklausurTermin, GostSchuelerklausur } from "@core";
import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";

export interface SchuelerLernabschnittGostKlausurenProps {
	schule: SchuleStammdaten;
	manager: () => SchuelerLernabschnittManager;
	kMan: () => GostKlausurplanManager;
	hatKlausurManager: () => boolean;
	createSchuelerklausurTermin: (skt: Partial<GostSchuelerklausurTermin>) => Promise<void>;
	deleteSchuelerklausurTermin: (sk: GostSchuelerklausurTermin) => Promise<void>;
	patchSchuelerklausurTermin: (id: number, data: Partial<GostSchuelerklausurTermin>) => Promise<void>;
	patchSchuelerklausur: (id: number, data: Partial<GostSchuelerklausur>) => Promise<void>;
	gotoPlanung: () => Promise<void>;
}
