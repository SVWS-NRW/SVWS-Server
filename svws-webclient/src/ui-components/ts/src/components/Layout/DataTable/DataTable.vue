<template>
	<table class="data-table" v-bind="computedTableAttributes">
		<thead class="data-table__thead">
			<slot name="header"
				:all-rows-selected="allRowsSelected"
				:toggle-all-rows="toggleBulkSelection"
				:columns="columnsComputed">
				<tr class="data-table__tr">
					<th v-if="selectable"
						class="data-table__th data-table__cell-select">
						<Checkbox class="data-table__cell-checkbox"
							:model-value="allRowsSelected"
							@update:model-value="toggleBulkSelection" />
					</th>

					<th v-for="column in columnsComputed"
						:key="column.key"
						class="data-table__th"
						:style="`flex-grow: ${column.span};`"
						@click.exact="column.sortable && toggleSorting(column)">
						<div class="data-table__th-wrapper">
							<span class="data-table__th-title">
								<slot :name="`header(${column.key})`"
									:column="column">
									{{ column.label }}
								</slot>
							</span>

							<span v-if="column.sortable"
								class="data-table__th-sorting"
								aria-hidden="true">
								<div :role="
										column.sortable ? 'button' : undefined
									"
									:tabindex="column.sortable ? 0 : -1">
									<template v-if="sortBy === column.name">
										<Icon
											v-show="sortingOrder === 'asc'">
											<i-ri-sort-asc />
										</Icon>
										<Icon
											v-show="sortingOrder === 'desc'">
											<i-ri-sort-desc />
										</Icon>
									</template>
									<Icon v-show="
										sortBy !== column.name ||
											!sortingOrder
									">
										<i-ri-arrow-up-down-line />
									</Icon>
								</div>
							</span>
						</div>
					</th>
				</tr>
			</slot>
		</thead>

		<tbody class="data-table__tbody">
			<slot name="body" :rows="sortedRows">
				<tr v-if="showNoDataHtml" key="showNoDataHtml">
					<td class="no-data data-table__td"
						:colspan="columnsComputed.length + (selectable ? 1 : 0)"
						v-html="noDataHtml" />
				</tr>

				<tr v-for="(row, index) in sortedRows"
					:key="`table-row_${row}_${index}`"
					class="data-table__tr"
					:class="[
						{ 'data-table__tr--selected': isRowSelected(row) },
						{ 'data-table__tr--clicked': isRowClicked(row) },
					]"
					@click="toggleRowClick(row)">
					<td v-if="selectable"
						class="data-table__td data-table__cell-select"
						:key="`selectable__${row}_${index}`">
						<Checkbox class="data-table__cell-checkbox"
							:model-value="isRowSelected(row)"
							@update:model-value="toggleRowSelection(row)" />
					</td>

					<td v-for="cell in row.cells"
						:key="`table-cell_${cell.column.key + cell.rowIndex}`"
						:style="`flex-grow: ${cell.column.span};`"
						class="data-table__td">
						<slot v-if="`cell(${cell.column.key})` in $slots"
							:name="`cell(${cell.column.key})`"
							v-bind="cell" />

						<slot v-else name="cell" v-bind="cell">
							<span class="data-table__td-content">
								{{ cell.value }}
							</span>
						</slot>
					</td>
				</tr>
			</slot>
		</tbody>

		<tfoot class="data-table__tfoot">
			<slot name="footer"
				v-if="selectable || $slots.footer || $slots['footer-actions']"
				:all-rows-selected="allRowsSelected"
				:toggle-all-rows="toggleBulkSelection"
				:rows="sortedRows">
				<tr class="data-table__tr data-table__tfoot__tr">
					<th v-if="selectable"
						class="data-table__th data-table__cell-select">
						<Checkbox class="data-table__cell-checkbox"
							:model-value="allRowsSelected"
							@update:model-value="toggleBulkSelection" />
					</th>

					<th class="data-table__th data-table__tfoot-actions">
						<slot name="footer-actions"
							v-if="$slots['footer-actions']" />
					</th>
				</tr>
			</slot>
		</tfoot>
	</table>
</template>

<script lang="ts">
	export default defineComponent({
		inheritAttrs: false,
	});
</script>

<script lang="ts" setup>
	import { defineComponent, computed, TableHTMLAttributes } from "vue";

	import useColumns from "./hooks/useColumns";
	import useRows from "./hooks/useRows";
	import useSortable from "./hooks/useSortable";
	import useSelectable from "./hooks/useSelectable";
	import useClickable from "./hooks/useClickable";

	import type {
		DataTableColumnSource,
		DataTableItem,
		DataTableSortingOrder,
	} from "./types";

	const props = withDefaults(
		defineProps<{
			columns?: DataTableColumnSource[];
			items?: Iterable<DataTableItem>;
			modelValue?: DataTableItem[];
			sortingOrder?: DataTableSortingOrder | undefined;
			sortBy?: string | undefined;
			selectable?: boolean;
			clicked?: DataTableItem | undefined;
			clickable?: boolean;
			noDataHtml?: string;
			uniqueKey?: string;
		}>(),
		{
			columns: () => [],
			items: () => [],
			modelValue: undefined,
			sortingOrder: undefined,
			sortBy: undefined,
			clicked: undefined,
			clickable: false,
			selectable: false,
			noDataHtml: "Keine Einträge vorhanden",
			uniqueKey: undefined
		}
	);

	const emit = defineEmits<{
		(e: "update:modelValue", items: DataTableItem[]): void;
		(e: "update:sortBy", sortBy: string): void;
		(e: "update:sortingOrder", sortingOrder: DataTableSortingOrder): void;
		(e: "update:clicked", items: DataTableItem): void;
	}>();

	const attrs = useAttrs();

	const { columnsComputed } = useColumns(props);

	const { rowsComputed } = useRows(columnsComputed, props);

	const { sortBy, sortingOrder, toggleSorting, sortedRows } = useSortable(
		columnsComputed,
		rowsComputed,
		props
	);

	const {
		toggleBulkSelection,
		isRowSelected,
		allRowsSelected,
		toggleRowSelection,
	} = useSelectable({
		isActive: () => props.selectable,
		sortedRows,
		selectedItems: () => props.modelValue,
		emit: (v: DataTableItem[]) => emit('update:modelValue', v),
	});

	const { isRowClicked, toggleRowClick } = useClickable(props);

	const showNoDataHtml = computed(() => {
		if (Array.isArray(props.items))
			return props.items.length === 0;
		const iterator = props.items[Symbol.iterator]();
		const res = iterator.next();
		return res.done !== true && res.value === undefined
	});

	const computedTableAttributes = computed(
		() =>
			({
				...Object.fromEntries(
					Object.entries(attrs).filter(
						([key]) => !["class", "style"].includes(key)
					)
				),
			} as TableHTMLAttributes)
	);
</script>

<style lang="postcss">
.data-table {
	@apply flex w-full flex-col border-l border-dark-20 bg-white;

	&__thead {
		@apply sticky top-0 left-0 z-10 w-full bg-white;
		@apply border-t border-dark-20 text-sm-bold uppercase shadow shadow-black/25;

		tr {
			@apply border-b-0;
		}

		th {
			@apply text-left;
			padding: 0.8rem 0.5rem;

			> div,
			> div > span {
				@apply w-full;
			}
		}

		.data-table__cell-select {
			@apply min-h-[2.6rem];
		}

		.data-table__th-title {
			@apply overflow-hidden text-ellipsis;
			max-width: calc(100% - 1.25rem);
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 1;
			word-break: break-all;
			min-width: 1.6rem;
			line-height: 1.25;
		}

		.data-table__th-sorting {
			@apply basis-0;
		}
	}

	&__tbody {
		tr {
			@apply bg-white;
			height: 1.72rem;

			&:hover {
				@apply cursor-pointer;
			}

			&:focus {
				@apply outline-none;
			}

			&:last-child {
				@apply border-b-0;
			}
		}

		td {
			.button {
				@apply border;
				font-size: 0.833rem;
				padding: 0.1em 0.7em;
			}
		}

		.data-table__td-content {
			@apply overflow-hidden text-ellipsis;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 1;
			word-break: break-all;
		}

		.text-input-component input {
			@apply h-auto w-full p-0;
		}
	}

	&__tfoot {
		@apply sticky bottom-0 left-0 z-20 flex w-full bg-white;
		@apply border-t border-dark-20;
		position: -webkit-sticky;
		box-shadow: 0 -6px 12px -12px rgba(var(--color-black), 0.25);

		&__tr {
			@apply flex justify-between;
			@apply w-full;
			min-height: 2rem;

			@apply overflow-visible;
		}

		.data-table__cell-select {
			@apply border-r-0;
		}

		&-actions {
			@apply flex flex-row items-center justify-end space-x-1 overflow-visible;

			.button--icon {
				@apply h-8 w-8 p-2;
			}

			.dropdown--button {
				@apply border-0;
			}

			> * {
				min-height: 2.7rem;
			}
		}
	}

	&__th,
	&__td {
		@apply flex h-full w-0 grow items-center overflow-hidden border-r border-dark-20 leading-none;
		padding: 0.3rem 0.5rem 0.25rem;
		line-height: 1.25;
	}

	&__tr {
		@apply relative flex w-full items-center border-b border-dark-20;

		&--clicked {
			@apply bg-primary bg-opacity-5 font-bold text-primary;
		}
	}

	&__cell-select {
		@apply flex items-center justify-center p-0;
		flex-grow: 0.25;
		min-width: 2rem;
		max-width: 2rem;
	}

	&__cell-checkbox {
		@apply m-0;
	}

	&__th-wrapper {
		@apply inline-flex flex-row items-center;
		@apply cursor-pointer select-none;
		@apply space-x-1;
		@apply text-sm-bold;
	}
}
</style>
