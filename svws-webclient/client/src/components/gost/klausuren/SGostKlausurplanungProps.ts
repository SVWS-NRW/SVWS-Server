import type { TabManager } from "@ui/ui/nav/TabManager";
import type { ApiStatus } from "~/components/ApiStatus";

export interface GostKlausurplanungProps {
	apiStatus: ApiStatus;
	tabManager: () => TabManager;
}
