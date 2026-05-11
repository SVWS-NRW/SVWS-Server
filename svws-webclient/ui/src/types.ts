export type Type = "primary" | "secondary" | "success" | "error" | "highlight" | "light";
export type Size = "big" | "medium" | "normal" | "small" | "tiny";
export type ButtonType = Exclude<Type, 'success' | 'highlight' | 'light'> | 'danger' | 'icon' | 'transparent' | 'trash';
export type DropdownType = Exclude<Type, 'success' | 'highlight' | 'light'> | 'icon';
export type InputType = "text" | "number" | "date" | "email" | "search" | "tel" | "password";
export type Placement = "auto" | "auto-start" | "auto-end" | "top" | "top-start" | "top-end" | "bottom" | "bottom-start" | "bottom-end" | "right" | "right-start" | "right-end" | "left" | "left-start" | "left-end";

export type DataTableColumn = {
	[key: string]: unknown;
	key: string;
	name?: string;
	label?: string;
	sortable?: boolean;
	span?: number;
	fixedWidth?: string | number;
	minWidth?: string | number;
	align?: 'left' | 'center' | 'right';
	tooltip?: string;
	disabled?: boolean;
	type?: InputType;
	divider?: boolean;
	toggle?: boolean;
	toggleInvisible?: boolean;
	statistic?: boolean;
};

export type SortByAndOrder = { key: string | null; order: boolean | null; };
