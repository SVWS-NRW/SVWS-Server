export type DataTableItem = Record<string, any>;

export type DataTableColumn = {
	key: string;
	label?: string;
	sortable?: boolean;
	span?: string;
	defaultSort?: 'asc' | 'desc' | null;
};
