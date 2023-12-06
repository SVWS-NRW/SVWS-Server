import type { ApiFile, BenutzerListeEintrag, SchemaListeEintrag, SimpleOperationResponse, List, SchuleInfo } from "@core";

export interface SchemaUebersichtProps {
	data: () => SchemaListeEintrag | undefined;
	admins: () => List<BenutzerListeEintrag>;
	backupSchema: () => Promise<ApiFile>;
	restoreSchema: (data: FormData) => Promise<SimpleOperationResponse>;
	migrateSchema: (data: FormData) => Promise<SimpleOperationResponse>;
	upgradeSchema: () => Promise<SimpleOperationResponse>;
	initSchema: (schulnummer: number) => Promise<SimpleOperationResponse>;
	revision: number | null;
	schuleInfo: SchuleInfo | undefined;
}
