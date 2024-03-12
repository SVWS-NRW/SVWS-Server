import type { SchemaListeEintrag } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SchemagruppeProps {
	auswahlGruppe: SchemaListeEintrag[];
	removeSchemata: () => Promise<void>;
	apiStatus: ApiStatus;
}
