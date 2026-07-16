import type { List, SchuelerEinwilligung, Einwilligungsart } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SchuelerEinwilligungenProps {
	einwilligungen: () => List<SchuelerEinwilligung>;
	mapEinwilligungsarten: Map<number, Einwilligungsart>;
	patch: (data: Partial<SchuelerEinwilligung>, idEinwilligungsart: number) => Promise<boolean>;
	apiStatus: ApiStatus;
}
