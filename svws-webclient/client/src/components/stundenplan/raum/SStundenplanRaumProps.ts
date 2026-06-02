import type { StundenplanManager } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface StundenplanRaumProps {
	apiStatus: ApiStatus;
	stundenplanManager: () => StundenplanManager;
	ganzerStundenplanRaeume: () => boolean;
	setGanzerStundenplanRaeume: (value: boolean) => Promise<void>;
}