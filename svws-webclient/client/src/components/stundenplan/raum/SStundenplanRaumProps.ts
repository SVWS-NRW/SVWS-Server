import type { StundenplanManager, ApiFile, ReportingParameter, ServerMode } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface StundenplanRaumProps {
	// schulform: Schulform;
	serverMode: ServerMode;
	// benutzerKompetenzen: Set<BenutzerKompetenz>,
	getPDF: (parameter: ReportingParameter) => Promise<ApiFile>;
	apiStatus: ApiStatus;
	stundenplanManager: () => StundenplanManager;
	ganzerStundenplanRaeume: () => boolean;
	setGanzerStundenplanRaeume: (value: boolean) => Promise<void>;
}