export type DataTableItem = Record<string, unknown>;

export type DataTableColumn = {
	key: string;
	label?: string;
	sortable?: boolean;
	defaultSort?: 'asc' | 'desc' | null;
};
