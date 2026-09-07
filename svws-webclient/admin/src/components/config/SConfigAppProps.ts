import type { ApiFile } from "@core/api/BaseApi";
import type { TLSCertificateInfo } from "@core/core/data/TLSCertificateInfo";

import type { ApiStatus } from "../ApiStatus";

export interface ConfigAppProps {
	getCert: () => Promise<ApiFile>;
	createCert: (tlsInfo: TLSCertificateInfo, alias: string) => Promise<boolean>;
	uploadCert: (formData: FormData, alias: string) => Promise<boolean>;
	apiStatus: ApiStatus;
}
