import type { SchemaListeEintrag } from "@core/core/data/db/SchemaListeEintrag";
import type { ApiFile } from "@core/index";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SchemagruppeProps {
	apiStatus: ApiStatus;
	apiUsername: string;
	auswahlGruppe: SchemaListeEintrag[];
	removeSchemata: () => Promise<void>;
	backupSchemata: () => Promise<void>;
	backupFiles: () => ApiFile[];
}
