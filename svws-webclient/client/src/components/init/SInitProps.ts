import type { SchulenKatalogEintrag, List } from "@core";

export interface InitProps {
	setSource: (source: 'init'|'restore'|'migrate') => Promise<void>;
	setDB: (db: 'mysql'|'mariadb'|'mssql'|'mdb') => Promise<void>;
	listSchulkatalog: List<SchulenKatalogEintrag>;
	initSchule: (schule: SchulenKatalogEintrag) => Promise<boolean>;
	migrateDB: (data: FormData) => Promise<boolean>;
	importSQLite: (data: FormData) => Promise<boolean>;
	source?: 'init'|'restore'|'migrate';
	db?: 'mysql'|'mariadb'|'mssql'|'mdb';
}