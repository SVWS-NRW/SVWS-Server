import type { ENMLeistung } from "@core";
import type { EnmManager } from "./EnmManager";

export interface EnmLeistungenProps {
	manager: EnmManager;
	patchLeistung: (patch: Partial<ENMLeistung>) => Promise<boolean>;
}
