import type { ApiFile, ENMv2Daten, LehrerListeEintrag, List, ServerMode } from "@core";

export interface SchuleDatenaustauschENMProps {
	serverMode: ServerMode;
	listLehrer: List<LehrerListeEintrag>;
	setImportENM: (file: File, password: string, salt: string) => Promise<boolean>;
	exportLehrerENM: (id: number) => Promise<ENMv2Daten>;
	exportGzipENM: () => Promise<ApiFile>;
	importGzipENM: (data: FormData) => Promise<void>;
	importENM: (file: File) => Promise<void>;
}