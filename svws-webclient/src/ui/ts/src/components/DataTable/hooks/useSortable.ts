import type { Ref } from 'vue';
import { computed } from 'vue'

import type { DataTableColumnInternal, DataTableRow, DataTableSortingOrder } from '../types'
import { DataTableSortingOptions } from '../types';
import useSafeVModel from './useSafeVModel';

export type UseSortableProps = {
	sortBy: string | undefined
	sortingOrder: DataTableSortingOrder | undefined
}

export default function useSortable(columns: Ref<DataTableColumnInternal[]>, computedRows: Ref<DataTableRow[]>, props: UseSortableProps) {
	const sortBy = useSafeVModel(props, '', 'sortBy');
	const sortingOrder = useSafeVModel(props, null as DataTableSortingOrder, 'sortingOrder');

	const sortedRows = computed(() => {
		if (computedRows.value.length <= 1)
			return computedRows.value;
		const columnIndex = columns.value.findIndex( ({ key, sortable }) => sortBy.value === key && sortable, );
		const column = columns.value[columnIndex];
		if (!column)
			return computedRows.value;
		const sortingOrderRatio = sortingOrder.value === 'desc' ? -1 : 1
		return [...computedRows.value].sort((a, b) => {
			if (sortingOrder.value === null)
				return a.initialIndex - b.initialIndex;
			const firstValue = String(a.cells[columnIndex].value);
			const secondValue = String(b.cells[columnIndex].value);
			return sortingOrderRatio * (firstValue.localeCompare(secondValue));
		})
	})

	const cycleSorting = (value: DataTableSortingOrder) => {
		const index = DataTableSortingOptions.findIndex((sortingValue) => sortingValue === value)
		return (index !== -1)
			? DataTableSortingOptions[(index + 1) % DataTableSortingOptions.length]
			: DataTableSortingOptions[0];
	}

	function toggleSorting(column: DataTableColumnInternal) {
		if (column.key === sortBy.value) {
			sortingOrder.value = cycleSorting(sortingOrder.value);
		} else {
			sortBy.value = column.key;
			sortingOrder.value = DataTableSortingOptions[0];
		}
	}

	return { sortBy, sortingOrder, toggleSorting, sortedRows };
}
