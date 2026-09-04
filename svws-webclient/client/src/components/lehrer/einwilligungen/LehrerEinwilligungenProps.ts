import type { LehrerEinwilligung } from "@core/core/data/lehrer/LehrerEinwilligung";
import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import type { List } from "@core/java/util/List";
import type { ApiStatus } from "~/components/ApiStatus";

export interface LehrerEinwilligungenProps {
	einwilligungen: () => List<LehrerEinwilligung>;
	mapEinwilligungsarten: Map<number, Einwilligungsart>;
	patch: (data: Partial<LehrerEinwilligung>, idEinwilligungsart: number) => Promise<void>;
	apiStatus: ApiStatus;
}
