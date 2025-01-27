import type { List } from "@core";
import type {Einwilligung, Einwilligungsart} from "@core";
import type {ApiStatus} from "~/components/ApiStatus";

export interface SchuelerEinwilligungenProps {
	einwilligungen: () => List<Einwilligung>;
	mapEinwilligungsarten: Map<number, Einwilligungsart>;
	patch: (data : Partial<Einwilligung>, idEinwilligungsart: number) => Promise<void>;
	add: (idEinwilligungsart: number) => Promise<void>;
	remove: (idEinwilligungsart: number) => Promise<void>;
	apiStatus: ApiStatus;
}
