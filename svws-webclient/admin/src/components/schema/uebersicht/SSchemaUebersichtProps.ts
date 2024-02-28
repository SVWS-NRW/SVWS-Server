import type { ApiFile, BenutzerListeEintrag, SchemaListeEintrag, SimpleOperationResponse, List, SchuleInfo, SchuleStammdaten, SchulenKatalogEintrag } from "@core";
import type { SchemaMigrationQuelle } from "../SchemaMigrationQuelle";
import type { ApiStatus } from "~/components/ApiStatus";

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
	migrationQuellinformationen: () => SchemaMigrationQuelle;
	apiStatus: ApiStatus;
}
