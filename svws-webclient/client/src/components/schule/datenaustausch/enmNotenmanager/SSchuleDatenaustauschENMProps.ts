import type { ApiFile } from "@core/api/BaseApi";
import type { ENMv2Daten } from "@core/core/data/enm/v2/ENMv2Daten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { List } from "@core/java/util/List";

export interface SchuleDatenaustauschENMProps {
	listLehrer: List<LehrerListeEintrag>;
	exportLehrerENM: (id: number) => Promise<ENMv2Daten>;
	exportGzipENM: () => Promise<ApiFile>;
	importGzipENM: (data: FormData) => Promise<void>;
	importENM: (file: File) => Promise<void>;
}