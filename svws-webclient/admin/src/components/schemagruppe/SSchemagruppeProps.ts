import type { ApiFile } from "@core/api/BaseApi";
import type { SchemaListeEintrag } from "@core/core/data/db/SchemaListeEintrag";
import type { ApiStatus } from "@admin/components/ApiStatus";

export interface SchemagruppeProps {
	apiStatus: ApiStatus;
	apiUsername: string;
	auswahlGruppe: SchemaListeEintrag[];
	removeSchemata: () => Promise<void>;
	backupSchemata: () => Promise<void>;
	backupFiles: () => ApiFile[];
}
