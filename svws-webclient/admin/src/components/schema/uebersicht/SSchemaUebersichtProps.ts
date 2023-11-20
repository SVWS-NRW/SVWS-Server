import type { ApiFile, SchemaListeEintrag } from "@core";

export interface SchemaUebersichtProps {
	data: () => SchemaListeEintrag | undefined;
	backupSchema: () => Promise<ApiFile>;
}
