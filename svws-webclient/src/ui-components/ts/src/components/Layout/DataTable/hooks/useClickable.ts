import { Ref } from 'vue'

import type { DataTableRow, DataTableItem } from '../types'
import useSafeVModel from './useSafeVModel'

interface UseClickableProps {
	clicked: DataTableItem | undefined
	clickable: boolean
}

export default function useClickable(
	props: UseClickableProps,
) {
	const clickedItem = useSafeVModel(props, null as DataTableItem | null, 'clicked');

	function isRowClicked(row: DataTableRow) {
		return clickedItem.value === row.source;
	}

	function resetClickedRow() {
		clickedItem.value = null;
	}

	function toggleRowClick(row: DataTableRow) {
		if (!props.clickable) {
			return
		}

		if (isRowClicked(row)) {
			resetClickedRow();
		} else {
			setClickedRow(row);
		}
	}

	function setClickedRow(row: DataTableRow) {
		if (!props.clickable) {
			return
		}

		clickedItem.value = row.source;
	}

	return {
		isRowClicked,
		toggleRowClick,
		setClickedRow
	}
}
