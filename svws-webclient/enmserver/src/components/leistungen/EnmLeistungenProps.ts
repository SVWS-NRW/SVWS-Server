import type { ENMLeistung } from "@core/core/data/enm/ENMLeistung";
import type { EnmAuswahlManager, EnmLerngruppenAuswahlEintrag, EnmManager } from "./EnmManager";
import type { PairNN } from "@core/asd/adt/PairNN";
import type { ENMSchueler } from "@core/core/data/enm/ENMSchueler";

export interface EnmLeistungenProps {
	manager: EnmManager;
	auswahlmanager: EnmAuswahlManager<EnmLerngruppenAuswahlEintrag[],PairNN<ENMLeistung, ENMSchueler>>;
	patchLeistung: (patch: Partial<ENMLeistung>) => Promise<boolean>;
	columnsVisible: () => Map<string, boolean|null>;
	setColumnsVisible: (columns: Map<string, boolean|null>) => Promise<void>;
	floskelEditorVisible: boolean;
	setFloskelEditorVisible: (value: boolean) => Promise<void>;
}
