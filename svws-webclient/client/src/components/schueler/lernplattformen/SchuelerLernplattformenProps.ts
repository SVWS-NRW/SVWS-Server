import type { SchuelerLernplattform } from "@core/core/data/schueler/SchuelerLernplattform";
import type { List } from "@core/java/util/List";

import type { ApiStatus } from "~/components/ApiStatus";

export interface SchuelerLernplattformenProps {
	schuelerLernplattformen: () => List<SchuelerLernplattform>;
	patch: (data: Partial<SchuelerLernplattform>, idLernplattform: number) => Promise<boolean>;
	apiStatus: ApiStatus;
}
