import type { SchulenKatalogEintrag, List } from "@core";

export interface InitProps {
	listSchulkatalog: List<SchulenKatalogEintrag>;
	initSchule: (schule: SchulenKatalogEintrag) => Promise<boolean>;
	migrateDB: (data: FormData, restore: boolean, db: string | undefined) => Promise<boolean>;
	importSQLite: (data: FormData) => Promise<boolean>;
}