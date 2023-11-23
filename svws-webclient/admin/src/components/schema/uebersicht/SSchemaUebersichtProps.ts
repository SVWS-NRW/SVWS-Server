import type { ApiFile, SchemaListeEintrag, SimpleOperationResponse } from "@core";

export interface SchemaUebersichtProps {
	data: () => SchemaListeEintrag | undefined;
	backupSchema: () => Promise<ApiFile>;
	restoreSchema: (data: FormData) => Promise<SimpleOperationResponse>;
	migrateSchema: (data: FormData) => Promise<SimpleOperationResponse>;
	upgradeSchema: () => Promise<SimpleOperationResponse>;
	revision: number | null;
}
