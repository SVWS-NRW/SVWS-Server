import type { SchuleStammdaten, SchuelerLernabschnittManager, GostKursklausurManager, GostSchuelerklausurTermin } from "@core";

export interface SchuelerLernabschnittGostKlausurenProps {
	schule: SchuleStammdaten;
	manager: () => SchuelerLernabschnittManager;
	kMan: () => GostKursklausurManager;
	hatKlausurManager: () => boolean;
	createSchuelerklausurTermin: (id: number) => Promise<void>;
	deleteSchuelerklausurTermin: (sk : GostSchuelerklausurTermin) => Promise<void>;
	patchSchuelerklausurTermin: (id: number, data : Partial<GostSchuelerklausurTermin>) => Promise<void>;
}