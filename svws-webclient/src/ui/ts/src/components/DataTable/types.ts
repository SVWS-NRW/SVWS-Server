import type { InputType } from "src/types"
import type { Ref } from "vue"

export type DataTableSortingOrder = 'asc' | 'desc' | null
export const DataTableSortingOptions = ['asc', 'desc', null] as const

export type DataTableColumn = {
  [key: string]: unknown
  key: string
  name?: string
  label?: string
  sortable?: boolean
  span?: number
  fixedWidth?: string | number
  minWidth?: string | number
  align?: 'left' | 'center' | 'right'
  tooltip?: string
	disabled?: boolean
  type?: InputType
}

export type DataTableColumnSource = DataTableColumn | string

export interface DataTableColumnInternal {
  [key: string]: unknown
  source: DataTableColumnSource
  initialIndex: number
  key: string
  name: string
  label: string
  sortable: boolean
  span: number
  fixedWidth: string | number
  minWidth: string | number
  align: 'left' | 'center' | 'right'
  tooltip: string
	disabled: boolean
  type: InputType
}

export type DataTableItem = Record<string, any>

export interface DataTableCell {
  rowIndex: number
  rowData: DataTableItem
  column: DataTableColumnInternal
  value: any
}

export interface DataTableRow {
  initialIndex: number
  source: DataTableItem
  cells: DataTableCell[]
  isEditing?: boolean
}

export type UseColumnProps = {
	columns: DataTableColumnSource[];
	items: Iterable<DataTableItem>;
}

export interface UseClickableOptions {
	clickedItem: () => DataTableItem | undefined,
	emit: (v: DataTableItem | null) => void,
	isActive: () => boolean,
	canReset: () => boolean,
	scrollIntoView: () => boolean | undefined,
}

export type UseRowsProps = {
	items: Iterable<DataTableItem>;
}

interface UseSelectableOptions {
	selectedItems: () => DataTableItem[] | undefined,
	emit: (v: DataTableItem[]) => void,
	sortedRows: Ref<DataTableRow[]>,
	isActive: () => boolean
}