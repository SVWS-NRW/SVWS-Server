import type { SchuelerLernabschnittsdaten, SchuelerLernabschnittBemerkungen, BenutzerKompetenz } from "@core";
import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";

export interface SchuelerLernabschnittKonferenzProps {
	manager: () => SchuelerLernabschnittManager;
	patch: (data: Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	patchBemerkungen: (data: Partial<SchuelerLernabschnittBemerkungen>) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}