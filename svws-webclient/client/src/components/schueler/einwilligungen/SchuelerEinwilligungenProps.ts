import type { SchuelerEinwilligung } from "@core/core/data/schueler/SchuelerEinwilligung";
import type { List } from "@core/java/util/List";

import type { ApiStatus } from "~/components/ApiStatus";

export interface SchuelerEinwilligungenProps {
	einwilligungen: () => List<SchuelerEinwilligung>;
	patch: (data: Partial<SchuelerEinwilligung>, idEinwilligungsart: number) => Promise<boolean>;
	apiStatus: ApiStatus;
}
