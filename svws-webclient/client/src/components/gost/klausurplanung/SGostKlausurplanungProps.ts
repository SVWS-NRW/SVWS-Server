import type { ApiStatus } from "~/components/ApiStatus";
import type { DownloadPDFTypen } from "./DownloadPDFTypen";
import type { ApiFile, GostKlausurplanManager } from "@core";

export interface GostKlausurplanungProps {
	apiStatus: ApiStatus;
	kMan: () => GostKlausurplanManager;
	getPDF: (title: DownloadPDFTypen) => Promise<ApiFile>;
}