import type { SchuelerEinwilligung } from "@core/core/data/schueler/SchuelerEinwilligung";
import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import type { List } from "@core/java/util/List";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SchuelerEinwilligungenProps {
	einwilligungen: () => List<SchuelerEinwilligung>;
	mapEinwilligungsarten: Map<number, Einwilligungsart>;
	patch: (data: Partial<SchuelerEinwilligung>, idEinwilligungsart: number) => Promise<boolean>;
	apiStatus: ApiStatus;
}
