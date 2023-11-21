import type { ApiFile, SchemaListeEintrag } from "@core";

export interface SchemaUebersichtProps {
	data: () => SchemaListeEintrag | undefined;
	backupSchema: () => Promise<ApiFile>;
	restoreSchema: (data: FormData) => Promise<void>;
	migrateSchema: (data: FormData) => Promise<void>;
}
