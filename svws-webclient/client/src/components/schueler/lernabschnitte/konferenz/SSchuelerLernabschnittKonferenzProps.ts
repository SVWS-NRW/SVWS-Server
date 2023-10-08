import type { SchuleStammdaten, SchuelerLernabschnittsdaten, SchuelerLernabschnittBemerkungen, SchuelerLernabschnittManager } from "@core";

export interface SchuelerLernabschnittKonferenzProps {
	schule: SchuleStammdaten;
	manager: () => SchuelerLernabschnittManager;
	patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	patchBemerkungen: (data : Partial<SchuelerLernabschnittBemerkungen>) => Promise<void>;
}