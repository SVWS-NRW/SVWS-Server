import type { SimpleOperationResponse } from "@core";
import type { WenomAuswahlListeManager } from "@ui";

export interface NotenmodulSynchronisationProps {
	manager: () => WenomAuswahlListeManager;
	synchronize: () => Promise<SimpleOperationResponse>;
	download: () => Promise<SimpleOperationResponse>;
	upload: () => Promise<SimpleOperationResponse>;
	truncate: () => Promise<SimpleOperationResponse>;
	reset: () => Promise<SimpleOperationResponse>;
}
