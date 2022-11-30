import getKeys from "../utils/get-keys";
import capitalizeFirstLetter from "../utils/capitalize-first-letter";
import type { DataTableColumn, DataTableItem } from "../types";

export const buildTableColumn = (
	source: DataTableColumn | string
): DataTableColumn => {
	const input = typeof source === "string" ? { key: source } : source;

	return {
		key: input.key,
		label: input.label || capitalizeFirstLetter(input.key),
		sortable: input.sortable ?? false,
		span: input.span ?? '1',
		defaultSort: input.defaultSort ?? null
	};
};

const buildColumnsFromItems = (items: DataTableItem[]) => {
	return getKeys(items).map(item => buildTableColumn(item));
};

const buildNormalizedColumns = (columns: DataTableColumn[]) => {
	return columns.map(item => buildTableColumn(item));
};

export default function useColumns(
	items: DataTableItem[],
	columns: DataTableColumn[]
) {
	const columnsComputed = computed(() => {
		if (columns.length === 0) {
			return buildColumnsFromItems(items);
		} else {
			return buildNormalizedColumns(columns);
		}
	});

	return {
		columnsComputed
	};
}
