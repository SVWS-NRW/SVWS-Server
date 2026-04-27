import type { SchulenKatalogEintrag, List, SimpleOperationResponse } from "@core";

export interface InitProps {
	listSchulkatalog: List<SchulenKatalogEintrag>;
	initSchule: (schule: SchulenKatalogEintrag) => Promise<boolean>;
	migrateDB: (data: FormData, restore: boolean, db: string | undefined) => Promise<SimpleOperationResponse>;
	importSQLite: (data: FormData) => Promise<SimpleOperationResponse>;
}