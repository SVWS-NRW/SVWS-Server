import type { ApiFile, ENMv2Daten, LehrerListeEintrag, List } from "@core";

export interface SchuleDatenaustauschENMProps {
	listLehrer: List<LehrerListeEintrag>;
	exportLehrerENM: (id: number) => Promise<ENMv2Daten>;
	exportGzipENM: () => Promise<ApiFile>;
	importGzipENM: (data: FormData) => Promise<void>;
	importENM: (file: File) => Promise<void>;
}