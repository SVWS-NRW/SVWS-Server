import type { ENMLeistung } from "../../../../core/src/core/data/enm/ENMLeistung";
import type { ENMSchueler } from "../../../../core/src/core/data/enm/ENMSchueler";
import type { EnmLerngruppenAuswahlEintrag, EnmManager } from "./EnmManager";

export interface EnmLeistungenUebersichtProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<EnmLerngruppenAuswahlEintrag>;
	patchLeistung: (data: ENMLeistung, patch: Partial<ENMLeistung>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
	focusFloskelEditor: (schueler: ENMSchueler | null, leistung: ENMLeistung | null, row: number | null, doFocus: boolean) => Promise<void>;
}
