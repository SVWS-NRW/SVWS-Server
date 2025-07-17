import type { ENMKlasse, ENMLeistungBemerkungen, ENMLernabschnitt } from "@core";
import type { EnmManager } from "@ui";

export interface NotenmodulKlassenleitungProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMKlasse>;
	patchBemerkungen: (id: number, data: ENMLeistungBemerkungen, patch: Partial<ENMLeistungBemerkungen>) => Promise<void>;
	patchLernabschnitt: (data: ENMLernabschnitt, patch: Partial<ENMLernabschnitt>) => Promise<void>;
	columnsVisible: () => Map<string, boolean|null>;
	setColumnsVisible: (columns: Map<string, boolean|null>) => Promise<void>;
	floskelEditorVisible: boolean;
	setFloskelEditorVisible: (value: boolean) => Promise<void>;
}
