import type { LehrerEinwilligung, Einwilligungsart, List } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface LehrerEinwilligungenProps {
	einwilligungen: () => List<LehrerEinwilligung>;
	mapEinwilligungsarten: Map<number, Einwilligungsart>;
	patch: (data: Partial<LehrerEinwilligung>, idEinwilligungsart: number) => Promise<void>;
	apiStatus: ApiStatus;
}
