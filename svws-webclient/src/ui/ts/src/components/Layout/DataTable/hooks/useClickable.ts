import { computed, toRaw } from 'vue'

import type { DataTableRow, DataTableItem } from '../types'

interface UseClickableOptions {
	clickedItem: () => DataTableItem | undefined,
	emit: (v: DataTableItem | null) => void,
	isActive: () => boolean,
	canReset: () => boolean,
}

export default function useClickable({ clickedItem: data, emit, isActive, canReset }: UseClickableOptions) {
	// internally, we only work with raw values so we don't run into identity hazard when comparing them
	const clickedItemRaw = computed(() => (toRaw(data()) ?? null));

	function isRowClicked(row: DataTableRow) {
		return row.source === clickedItemRaw.value;
	}

	function resetClickedRow() {
		if (canReset())
			emit(null);
	}

	function toggleRowClick(row: DataTableRow) {
		if (!isActive)
			return;
		if (isRowClicked(row))
			resetClickedRow();
		else
			setClickedRow(row);
	}

	function setClickedRow(row: DataTableRow) {
		if (!isActive)
			return;
		emit(row.source);
	}

	return { isRowClicked, toggleRowClick, setClickedRow };
}
