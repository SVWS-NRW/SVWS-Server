import type { SchemaListeEintrag } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SchemagruppeProps {
	apiStatus: ApiStatus;
	apiUsername: string;
	auswahlGruppe: SchemaListeEintrag[];
	removeSchemata: () => Promise<void>;
}
