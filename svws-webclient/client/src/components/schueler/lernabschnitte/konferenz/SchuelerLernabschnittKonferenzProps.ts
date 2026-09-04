import type { SchuelerLernabschnittBemerkungen } from "@core/asd/data/schueler/SchuelerLernabschnittBemerkungen";
import type { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";
import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";

export interface SchuelerLernabschnittKonferenzProps {
	manager: () => SchuelerLernabschnittManager;
	patch: (data: Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	patchBemerkungen: (data: Partial<SchuelerLernabschnittBemerkungen>) => Promise<void>;
}
