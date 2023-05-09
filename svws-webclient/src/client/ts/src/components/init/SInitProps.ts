import type { SchulenKatalogEintrag, List } from "@svws-nrw/svws-core";

export interface InitProps {
	setSource: (source: string) => Promise<void>;
	setDB: (db: string) => Promise<void>;
	listSchulkatalog: List<SchulenKatalogEintrag>;
	initSchule: (schule: SchulenKatalogEintrag) => Promise<boolean>;
	migrateDB: (data: FormData) => Promise<boolean>;
	source?: 'schulkatalog'|'schild2'|'backup';
	db?: 'mysql'|'mariadb'|'mssql'|'mdb';
}