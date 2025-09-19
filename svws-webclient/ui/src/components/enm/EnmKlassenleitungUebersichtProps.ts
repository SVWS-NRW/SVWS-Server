import type { BemerkungenHauptgruppe, EnmManager } from "./EnmManager";
import type { ENMLeistungBemerkungen } from "../../../../core/src/core/data/enm/ENMLeistungBemerkungen";
import type { ENMLernabschnitt } from "../../../../core/src/core/data/enm/ENMLernabschnitt";
import type { ENMKlasse } from "../../../../core/src/core/data/enm/ENMKlasse";
import type { ENMSchueler } from "../../../../core/src/core/data/enm/ENMSchueler";

export interface EnmKlassenleitungUebersichtProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMKlasse>;
	patchBemerkungen: (id: number, data: ENMLeistungBemerkungen, patch: Partial<ENMLeistungBemerkungen>) => Promise<void>;
	patchLernabschnitt: (data: ENMLernabschnitt, patch: Partial<ENMLernabschnitt>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
	floskelEditorVisible: boolean;
	focusFloskelEditor: (hauptgruppe: BemerkungenHauptgruppe | null, schueler: ENMSchueler | null, klasse: ENMKlasse | null, row: number | null, doFocus: boolean) => Promise<void>;
}
