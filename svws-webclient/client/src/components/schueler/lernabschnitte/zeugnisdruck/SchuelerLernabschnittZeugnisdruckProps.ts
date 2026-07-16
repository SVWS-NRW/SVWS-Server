import type { SchuelerLernabschnittsdaten, SchuelerLernabschnittBemerkungen } from "@core";
import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";

export interface SchuelerLernabschnittZeugnisdruckProps {
	manager: () => SchuelerLernabschnittManager;
	patch: (data: Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	patchBemerkungen: (data: Partial<SchuelerLernabschnittBemerkungen>) => Promise<void>;
}