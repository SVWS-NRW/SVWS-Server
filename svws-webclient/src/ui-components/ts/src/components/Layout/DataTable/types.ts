export type DataTableSortingOrder = 'asc' | 'desc' | null
export const DataTableSortingOptions = ['asc', 'desc', null] as const

export type DataTableColumn = {
  [key: string]: unknown
  key: string
  name?: string
  label?: string
  sortable?: boolean
  span?: number
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
}