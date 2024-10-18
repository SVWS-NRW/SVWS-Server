import type { ApiStatus } from "~/components/ApiStatus";
import type { SchemaListeEintrag } from "../../../../core/src/core/data/db/SchemaListeEintrag";

export interface SchemagruppeProps {
	apiStatus: ApiStatus;
	apiUsername: string;
	auswahlGruppe: SchemaListeEintrag[];
	removeSchemata: () => Promise<void>;
}
