import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";

export interface SchuleDatenaustauschKurs42BlockungProps {
	setGostKurs42ImportZip: (formData: FormData) => Promise<SimpleOperationResponse>;
}