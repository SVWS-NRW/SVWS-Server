import type { ENMKlasse } from "@core/core/data/enm/ENMKlasse";
import type { EnmAuswahlManager, EnmManager } from "./EnmManager";
import type { ENMLeistungBemerkungen } from "@core/core/data/enm/ENMLeistungBemerkungen";
import type { ENMLernabschnitt } from "@core/core/data/enm/ENMLernabschnitt";
import type { ENMSchueler } from "@core/core/data/enm/ENMSchueler";

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
