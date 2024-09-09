import type { SchuleStammdaten, SchuelerLernabschnittManager, GostKlausurplanManager, GostSchuelerklausurTermin } from "@core";

export interface SchuelerLernabschnittGostKlausurenProps {
	schule: SchuleStammdaten;
	manager: () => SchuelerLernabschnittManager;
	kMan: () => GostKlausurplanManager;
	hatKlausurManager: () => boolean;
	createSchuelerklausurTermin: (id: number) => Promise<void>;
	deleteSchuelerklausurTermin: (sk : GostSchuelerklausurTermin) => Promise<void>;
	patchSchuelerklausurTermin: (id: number, data : Partial<GostSchuelerklausurTermin>) => Promise<void>;
	gotoPlanung: () => Promise<void>;
}