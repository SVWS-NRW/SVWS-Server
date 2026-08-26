import type { ApiStatus } from "~/components/ApiStatus";
import type { TabManager } from "@ui";

export interface GostKlausurplanungProps {
	apiStatus: ApiStatus;
	tabManager: () => TabManager;
}
