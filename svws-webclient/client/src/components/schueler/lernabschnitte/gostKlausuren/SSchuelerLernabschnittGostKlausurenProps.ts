import type { SchuleStammdaten, SchuelerLernabschnittsdaten, SchuelerLernabschnittBemerkungen, SchuelerLernabschnittManager, GostKursklausurManager, GostSchuelerklausur } from "@core";

export interface SchuelerLernabschnittGostKlausurenProps {
	schule: SchuleStammdaten;
	manager: () => SchuelerLernabschnittManager;
	klausurManager: () => GostKursklausurManager;
	hatKlausurManager: () => boolean;
	createSchuelerklausurTermin: (sk : GostSchuelerklausur) => Promise<void>;
	patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
}