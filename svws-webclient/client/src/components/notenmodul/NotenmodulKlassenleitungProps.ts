import type { ENMKlasse, ENMLeistungBemerkungen, ENMLernabschnitt } from "@core";
import type { EnmManager } from "@ui";

export interface NotenmodulKlassenleitungProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMKlasse>;
	patchBemerkungen: (id: number, patch: Partial<ENMLeistungBemerkungen>) => Promise<boolean>;
	patchLernabschnitt: (patch: Partial<ENMLernabschnitt>) => Promise<boolean>;
	columnsVisible: () => Map<string, boolean|null>;
	setColumnsVisible: (columns: Map<string, boolean|null>) => Promise<void>;
	floskelEditorVisible: boolean;
	setFloskelEditorVisible: (value: boolean) => Promise<void>;
}
