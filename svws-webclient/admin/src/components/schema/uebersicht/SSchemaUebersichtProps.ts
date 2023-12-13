import type { ApiFile, BenutzerListeEintrag, SchemaListeEintrag, SimpleOperationResponse, List, SchuleInfo, SchuleStammdaten, SchulenKatalogEintrag } from "@core";

export interface SchemaUebersichtProps {
	data: () => SchemaListeEintrag | undefined;
	admins: () => List<BenutzerListeEintrag>;
	backupSchema: () => Promise<ApiFile>;
	restoreSchema: (data: FormData) => Promise<SimpleOperationResponse>;
	migrateSchema: (data: FormData) => Promise<SimpleOperationResponse>;
	upgradeSchema: () => Promise<SimpleOperationResponse>;
	initSchema: (schulnummer: number) => Promise<SchuleStammdaten>;
	revision: number | null;
	schuleInfo: () => SchuleInfo | undefined;
	schulen: () => List<SchulenKatalogEintrag>;
}
