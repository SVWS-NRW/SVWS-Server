import { computed } from 'vue'
import getKeys from "../utils/get-keys";
import capitalizeFirstLetter from "../utils/capitalize-first-letter";

import type { DataTableColumnSource, DataTableColumnInternal, DataTableItem } from '../types'

type UseColumnProps = {
	columns: DataTableColumnSource[];
	items: Iterable<DataTableItem>;
}

export const buildTableColumn = (source: DataTableColumnSource, initialIndex: number): DataTableColumnInternal => {
	const input = typeof source === 'string' ? { key: source } : source;
	return {
		source,
		initialIndex,
		key: input.key,
		name: input.name || input.key,
		label: input.label || capitalizeFirstLetter(input.key),
		sortable: input.sortable || false,
		span: input.span || 1,
		fixedWidth: input.fixedWidth || 0,
		minWidth: input.minWidth || 0,
		align: input.align || 'left',
	}
}

const buildColumnsFromItems = (props: UseColumnProps) => {
	return getKeys(props.items).map((item, index) => buildTableColumn(item, index));
}

const buildNormalizedColumns = (props: UseColumnProps) => {
	return props.columns.map((item, index) => buildTableColumn(item, index));
}

export default function useColumns(props: UseColumnProps) {
	const columnsComputed = computed(() =>
		(props.columns.length === 0) ? buildColumnsFromItems(props) : buildNormalizedColumns(props)
	)
	const gridTemplateColumns = computed(() => {
		return columnsComputed.value.map(column =>
			`minmax(${
				column.fixedWidth ? (column.fixedWidth + (typeof column.fixedWidth === "number" ? 'rem' : '')) : (column.minWidth ? (column.minWidth + (typeof column.minWidth === "number" ? 'rem' : '')) : '4rem')
			}, ${
				column.fixedWidth ? (column.fixedWidth + (typeof column.fixedWidth === "number" ? 'rem' : '')) : column.span + 'fr'
			})`
		).join(' ');
	})
	return { columnsComputed, gridTemplateColumns };
}
