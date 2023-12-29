import type { SchuleStammdaten, SchuelerLernabschnittsdaten, SchuelerLernabschnittBemerkungen, SchuelerLernabschnittManager, GostKursklausurManager } from "@core";

export interface SchuelerLernabschnittGostKlausurenProps {
	schule: SchuleStammdaten;
	manager: () => SchuelerLernabschnittManager;
	klausurManager: () => GostKursklausurManager;
	hatKlausurManager: () => boolean;
	patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
}