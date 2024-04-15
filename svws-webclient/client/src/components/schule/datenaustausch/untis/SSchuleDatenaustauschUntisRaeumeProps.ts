import type { SimpleOperationResponse } from "@core";

export interface SchuleDatenaustauschUntisRaeumeProps {
	importUntisRaeumeGPU005: (formData: FormData) => Promise<SimpleOperationResponse>;
}