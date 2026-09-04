import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";

export interface SchuleDatenaustauschUntisImporteProps {
	importUntisStundenplanGPU001: (formData: FormData, ignoreMissing: boolean) => Promise<SimpleOperationResponse>;
	importUntisRaeumeGPU005: (formData: FormData) => Promise<SimpleOperationResponse>;
}
