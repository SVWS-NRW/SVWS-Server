import type { ApiFile } from "@core/api/BaseApi";
import type { SchuleStammdaten } from "@core/asd/data/schule/SchuleStammdaten";
import type { BenutzerListeEintrag } from "@core/core/data/benutzer/BenutzerListeEintrag";
import type { BenutzerKennwort } from "@core/core/data/BenutzerKennwort";
import type { SchemaListeEintrag } from "@core/core/data/db/SchemaListeEintrag";
import type { SchuleInfo } from "@core/core/data/schule/SchuleInfo";
import type { SchulenKatalogEintrag } from "@core/core/data/schule/SchulenKatalogEintrag";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { List } from "@core/java/util/List";
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
	createEmptySchema: () => Promise<SimpleOperationResponse>;
	addExistingSchemaToConfig: (data: BenutzerKennwort, schema: string) => Promise<void>;
	revision: number | null;
	schuleInfo: () => SchuleInfo | undefined;
	schulen: () => List<SchulenKatalogEintrag>;
	migrationQuellinformationen: () => SchemaMigrationQuelle;
	apiStatus: ApiStatus;
}
