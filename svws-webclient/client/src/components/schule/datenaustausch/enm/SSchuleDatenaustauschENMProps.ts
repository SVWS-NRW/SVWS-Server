export interface SchuleDatenaustauschENMProps {
	setImportENM: (file: File, password: string, salt: string) => Promise<boolean>;
}