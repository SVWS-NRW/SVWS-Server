import type { StundenplanManager, ApiFile, ReportingParameter } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface StundenplanRaumProps {
	getPDF: (parameter: ReportingParameter) => Promise<ApiFile>;
	apiStatus: ApiStatus;
	stundenplanManager: () => StundenplanManager;
	ganzerStundenplanRaeume: () => boolean;
	setGanzerStundenplanRaeume: (value: boolean) => Promise<void>;
}