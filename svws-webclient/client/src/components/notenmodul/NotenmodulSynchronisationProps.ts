import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { WenomAuswahlListeManager } from "@ui/components/enm/WenomAuswahlListeManager";

export interface NotenmodulSynchronisationProps {
	manager: () => WenomAuswahlListeManager;
	synchronize: () => Promise<SimpleOperationResponse>;
	download: () => Promise<SimpleOperationResponse>;
	upload: () => Promise<SimpleOperationResponse>;
	truncate: () => Promise<SimpleOperationResponse>;
	reset: () => Promise<SimpleOperationResponse>;
}
