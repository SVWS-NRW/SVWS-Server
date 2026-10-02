import type { LehrerEinwilligung } from "@core/core/data/lehrer/LehrerEinwilligung";
import type { List } from "@core/java/util/List";

import type { ApiStatus } from "~/components/ApiStatus";

export interface LehrerEinwilligungenProps {
	einwilligungen: () => List<LehrerEinwilligung>;
	patch: (data: Partial<LehrerEinwilligung>, idEinwilligungsart: number) => Promise<boolean>;
	apiStatus: ApiStatus;
}
