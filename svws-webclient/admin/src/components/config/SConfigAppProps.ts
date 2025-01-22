import type { ApiFile } from "@core/api/BaseApi";

export interface ConfigAppProps {
	getCert: () => Promise<ApiFile>;
}
