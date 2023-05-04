export interface InitBackupProps {
	migrateBackup: (data: FormData) => Promise<boolean>;
}