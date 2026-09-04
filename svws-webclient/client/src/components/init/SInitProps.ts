import type { SchulenKatalogEintrag } from "@core/core/data/schule/SchulenKatalogEintrag";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { List } from "@core/java/util/List";

export interface InitProps {
	listSchulkatalog: List<SchulenKatalogEintrag>;
	initSchule: (schule: SchulenKatalogEintrag) => Promise<boolean>;
	migrateDB: (data: FormData, restore: boolean, db: string | undefined) => Promise<SimpleOperationResponse>;
	importSQLite: (data: FormData) => Promise<SimpleOperationResponse>;
}