import type { ApiStatus } from "~/components/ApiStatus";
import type { DownloadPDFTypen } from "./DownloadPDFTypen";
import type { ApiFile, GostHalbjahr, GostJahrgangsdaten, GostKlausurplanManager } from "@core";
import type { WritableComputedRef } from "vue";
import type { TabManager } from "@ui";

export interface GostKlausurplanungProps {
	apiStatus: ApiStatus;
	kMan: () => GostKlausurplanManager;
	getPDF: (title: DownloadPDFTypen) => Promise<ApiFile>;
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	halbjahr: GostHalbjahr;
	tabManager: () => TabManager;
	getConfigNumberValue: (value: string) => number;
}