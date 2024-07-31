import type { ApiStatus } from "~/components/ApiStatus";
import type { DownloadPDFTypen } from "./DownloadPDFTypen";
import type { ApiFile, GostKursklausurManager } from "@core";

export interface GostKlausurplanungProps {
	apiStatus: ApiStatus;
	kMan: () => GostKursklausurManager;
	getPDF: (title: DownloadPDFTypen) => Promise<ApiFile>;
}