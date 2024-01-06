import type { SchuleStammdaten, SchuelerLernabschnittsdaten, SchuelerLernabschnittBemerkungen, SchuelerLernabschnittManager, GostKursklausurManager, GostSchuelerklausur, GostSchuelerklausurTermin } from "@core";

export interface SchuelerLernabschnittGostKlausurenProps {
	schule: SchuleStammdaten;
	manager: () => SchuelerLernabschnittManager;
	klausurManager: () => GostKursklausurManager;
	hatKlausurManager: () => boolean;
	createSchuelerklausurTermin: (id: number) => Promise<void>;
	deleteLastSchuelerklausurTermin: (sk : GostSchuelerklausur) => Promise<void>;
	patchSchuelerklausurTermin: (id: number, data : Partial<GostSchuelerklausurTermin>) => Promise<void>;
}