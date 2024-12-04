import type { ApiFile } from "../../../../core/src/api/BaseApi";


export interface ConfigAppProps {
	getCert: () => Promise<ApiFile>;
}
