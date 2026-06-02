import type { ApiFile } from "@core/api/BaseApi";
import type { ApiStatus } from "../ApiStatus";
import type { TLSCertificateInfo } from "@core/core/data/TLSCertificateInfo";

export interface ConfigAppProps {
	getCert: () => Promise<ApiFile>;
	createCert: (tlsInfo: TLSCertificateInfo, alias: string) => Promise<boolean>;
	uploadCert: (formData: FormData, alias: string) => Promise<boolean>;
	apiStatus: ApiStatus;
}
