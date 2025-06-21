import type { ENMKlasse } from "../../../../core/src/core/data/enm/ENMKlasse";
import type { EnmAuswahlManager, EnmManager } from "./EnmManager";
import type { ENMLeistungBemerkungen } from "../../../../core/src/core/data/enm/ENMLeistungBemerkungen";
import type { ENMLernabschnitt } from "../../../../core/src/core/data/enm/ENMLernabschnitt";
import type { ENMSchueler } from "../../../../core/src/core/data/enm/ENMSchueler";

export interface EnmKlassenleitungProps {
	manager: EnmManager;
	auswahlmanager: EnmAuswahlManager<ENMKlasse, ENMSchueler>;
	patchBemerkungen: (id: number, patch: Partial<ENMLeistungBemerkungen>) => Promise<boolean>;
	patchLernabschnitt: (patch: Partial<ENMLernabschnitt>) => Promise<boolean>;
	columnsVisible: () => Map<string, boolean|null>;
	setColumnsVisible: (columns: Map<string, boolean|null>) => Promise<void>;
	floskelEditorVisible: boolean;
	setFloskelEditorVisible: (value: boolean) => Promise<void>;
}
