<template>
	<p v-if="focusSwitchingEnabled && ($slots.search || $slots.filter || $slots.filterAdvanced || toggleColumns)" v-show="focusHelpVisible" class="region-enumeration">3</p>
	<div v-if="$slots.search || $slots.filter || $slots.filterAdvanced || toggleColumns" class="svws-ui-table-filter focus-region" :class="{'svws-open': $slots.filter && isFilterOpen, 'highlighted': focusHelpVisible}">
		<div class="flex gap-2 p-0.5 w-full overflow-hidden">
			<div class="grow" v-if="$slots.search">
				<slot name="search" />
			</div>
			<div v-if="$slots.filter" class="flex shrink-0 items-center gap-1" :class="{'mr-5': ($slots.filterAdvanced || toggleColumns), 'ml-2': $slots.search, 'ml-auto': !$slots.search}">
				<slot name="filter" />
			</div>
			<div v-if="toggleColumns" :class="{'ml-auto': !$slots.filter}">
				<svws-ui-tooltip :indicator="false" :hover="false" :show-arrow="false" position="top" class="h-full">
					<svws-ui-button type="transparent" class="h-full">
						<span class="icon i-ri-table-line" />
						<template #badge v-if="hiddenColumns.size">
							<span />
						</template>
						<span>Daten</span>
					</svws-ui-button>
					<template #content>
						<ul class="min-w-[10rem] flex flex-col gap-0.5 pt-1">
							<li v-for="(column, index) in columns.filter(col => (typeof col !== 'string') && !col.toggleInvisible)" :key="index">
								<template v-if="typeof column !== 'string'">
									<svws-ui-checkbox :model-value="!hiddenColumns.has(column.key)" :disabled="!column.toggle" @update:model-value="ok => updateHiddenColumns(column.key, ok)">
										{{ column.label }}
									</svws-ui-checkbox>
								</template>
							</li>
						</ul>
					</template>
				</svws-ui-tooltip>
			</div>
			<div v-if="$slots.filterAdvanced && filterHide" class="flex shrink-0" :class="{'ml-auto': !$slots.filter && !toggleColumns}">
				<svws-ui-button type="transparent" @click="toggleFilterOpen" class="h-full" :class="{'opacity-50 hover:opacity-100 focus-visible:opacity-100': !filtered && isFilterOpen}" filter-button>
					<template #badge v-if="filtered">
						<span />
					</template>
					<template v-if="!filterReset || !filtered">
						<span class="icon i-ri-filter-line" v-if="!isFilterOpen" />
						<span class="icon i-ri-arrow-up-s-line" v-else />
					</template>
					<span>Filter</span>
				</svws-ui-button>
				<svws-ui-button v-if="filterReset && filtered" type="icon" @click="filterReset" class="h-full" title="Filter zurücksetzen">
					<span class="icon i-ri-filter-off-line" />
				</svws-ui-button>
			</div>
		</div>
		<div v-if="$slots.filterAdvanced && isFilterOpen" class="svws-ui-table-filter--advanced">
			<slot name="filterAdvanced" />
		</div>
	</div>
	<div class="svws-ui-table focus-region" role="table" aria-label="Tabelle" v-bind="$attrs" style="scrollbar-gutter: stable; scrollbar-width: thin; scrollbar-color: rgba(0,0,0,0.2) transparent;"
		:class="{
			'svws-clickable': clickable && (typeof noData !== 'undefined' ? !noData : !noDataCalculated),
			'svws-selectable': selectable,
			'svws-has-selection': selectable && selectedItemsRaw.length > 0,
			'svws-sortable': sortByAndOrder.key,
			'svws-no-data': typeof noData !== 'undefined' ? noData : noDataCalculated,
			'svws-type-navigation': type === 'navigation',
			'svws-type-grid': type === 'grid',
			'svws-has-background': hasBackground,
			'overflow-visible': !scroll,
			'overflow-auto': scroll,
			'pr-4': scroll && win11FForMacOS,
			'highlighted': focusHelpVisible,
		}">
		<p v-if="focusHelpVisible" class="region-enumeration">4</p>
		<div v-if="!disableHeader" class="svws-ui-thead" role="rowgroup" aria-label="Tabellenkopf">
			<slot name="header" :all-rows-selected="allRowsSelected" :toggle-all-rows="toggleBulkSelection" :columns="columnsComputed">
				<div role="row" class="svws-ui-tr" :style="getGridTemplateColumns">
					<div v-if="selectable" class="svws-ui-td svws-align-center" role="columnheader" aria-label="Alle auswählen">
						<svws-ui-checkbox :model-value="allRowsSelected" :indeterminate="someNotAllRowsSelected" @update:model-value="toggleBulkSelection" :disabled="typeof noData !== 'undefined' ? noData : noDataCalculated" />
					</div>
					<div v-for="column in columnsComputed" class="svws-ui-td" role="columnheader" :key="column.key" @click.exact="column.sortable && toggleSorting(column)" @keyup.enter="column.sortable && toggleSorting(column)"
						:class="[
							`svws-column-key--${column.key}`, `svws-align-${column.align}`,
							{
								'svws-disabled': column.disabled,
								'svws-sortable-column': column.sortable,
								'svws-active': column.sortable && (internalSortByAndOrder.key === column.name) && (typeof internalSortByAndOrder.order === 'boolean'),
								'svws-divider': column.divider,
							}]" :tabindex="column.sortable ? 0 : -1">
						<slot :name="`header(${column.key})`" :column="column">
							<svws-ui-tooltip v-if="column.tooltip">
								<span class="line-clamp-1 break-all leading-tight">{{ column.label }}</span>
								<template #content>
									{{ column.tooltip }}
								</template>
							</svws-ui-tooltip>
							<template v-else>
								<span class="line-clamp-1 break-all leading-tight">{{ column.label }}</span>
							</template>
						</slot>
						<span v-if="column.sortable" class="svws-sorting-icon" :class="{'-order-1': column.align === 'right'}">
							<span class="icon i-ri-arrow-up-down-line svws-sorting-asc" :class="{'svws-active': ((internalSortByAndOrder.key === column.name) || (sortByMulti?.has(column.name))) && ((internalSortByAndOrder.order === true) || (sortByMulti?.get(column.name) === true))}" />
							<span class="icon i-ri-arrow-up-down-line svws-sorting-desc" :class="{'svws-active': ((internalSortByAndOrder.key === column.name) || (sortByMulti?.has(column.name))) && ((internalSortByAndOrder.order === false) || (sortByMulti?.get(column.name) === false))}" />
						</span>
					</div>
				</div>
			</slot>
		</div>
		<div class="svws-ui-tbody" role="rowgroup" aria-label="Tabelleninhalt">
			<slot name="body" :rows="sortedRows">
				<div v-if="noDataCalculated && noData" class="svws-ui-tr" role="row" :style="getGridTemplateColumns">
					<div class="col-span-full svws-no-data-text svws-ui-td" role="cell">
						<span class="py-2 font-normal">
							<slot name="noData">{{ noDataText }}</slot>
						</span>
					</div>
				</div>
				<template v-for="(row, index) in sortedRows">
					<slot name="rowCustom" :row="row.source">
						<div class="svws-ui-tr" :style="getGridTemplateColumns" role="row" :key="`table-row_${row}_${index}`" @click.exact="toggleRowClick(row)" :ref="el => itemRefs.set(index, el)"
							:class="{ 'svws-selected': isRowSelected(row), 'svws-clicked': isRowClicked(row), 'listFocusField': isRowClicked(row)}" tabindex="0" @keydown.enter="toggleRowClick(row)"
							@keydown.down.prevent="switchElement($event, itemRefs, index, false)" @keydown.up.prevent="switchElement($event, itemRefs, index, true)">
							<slot name="row" :row="row.source">
								<template v-if="selectable">
									<div v-if="row.selectable" class="svws-ui-td svws-align-center" role="cell" :key="`selectable__${row}_${index}`">
										<input type="checkbox" :checked="isRowSelected(row)" @input="toggleRowSelection(row)" @click.stop :ref="el => selectionRefs.set(index, el)" @keydown.down.prevent.stop="switchElement($event, selectionRefs, index, false)" @keydown.up.prevent.stop="switchElement($event, selectionRefs, index, true)">
									</div>
									<div v-else class="svws-ui-td svws-align-center" role="cell" />
								</template>
								<slot name="rowSelectable" :row="row.source">
									<div class="svws-ui-td" role="cell" v-for="cell in row.cells" :key="`table-cell_${cell.column.key + cell.rowIndex}`"
										:class="[
											`svws-column-key--${cell.column.key}`, `svws-align-${cell.column.align}`,
											{
												'svws-disabled': cell.column.disabled,
												'svws-no-value': cell.value === null || cell.value === undefined || cell.value === '',
												'svws-divider': cell.column.divider,
											}]">
										<slot v-if="`cell(${cell.column.key})` in $slots" :name="`cell(${cell.column.key})`" v-bind="cell" />
										<slot v-else name="cell" v-bind="cell">
											<svws-ui-text-input v-if="row.isEditing && cell.column.type !== 'number'" v-model="cell.value" :headless="true" :type="(cell.column.type)" @click.stop="setClickedRow(row) " />
											<svws-ui-input-number v-if="row.isEditing && cell.column.type === 'number'" v-model="cell.value" :headless="true" :type="(cell.column.type)" @click.stop="setClickedRow(row) " />
											<template v-else-if="cell.value">
												{{ cell.column.type === 'date' ? (new Date(cell.value).toLocaleDateString('de', {day: '2-digit', month: '2-digit', year: 'numeric', timeZone: 'Europe/Berlin'})) : cell.value }}
											</template>
											<template v-else>
												<span class="text-ui-secondary">—</span>
											</template>
										</slot>
									</div>
								</slot>
							</slot>
						</div>
					</slot>
				</template>
			</slot>
		</div>
		<div v-if="$slots.dataFooter" class="svws-ui-tfoot--data" role="rowgroup">
			<slot name="dataFooter" />
		</div>
		<div v-if="!disableFooter && (selectable || $slots.footer || $slots.actions || count)" class="svws-ui-tfoot" role="rowgroup" aria-label="Fußzeile">
			<slot name="footer" :all-rows-selected="allRowsSelected" :toggle-all-rows="toggleBulkSelection" :rows="sortedRows">
				<div class="svws-ui-tr" :style="getGridTemplateColumns" role="row">
					<div v-if="selectable" class="svws-ui-td svws-align-center" role="columnheader" aria-label="Alle auswählen">
						<svws-ui-checkbox :model-value="allRowsSelected" :indeterminate="someNotAllRowsSelected" @update:model-value="toggleBulkSelection" :disabled="typeof noData !== 'undefined' ? noData : noDataCalculated" />
					</div>
					<div v-if="count" class="text-sm svws-ui-td font-medium" role="cell">
						<template v-if="allRowsSelected && modelValue">Alle {{ modelValue.length - selectedItemsNotListed.length }} ausgewählt<template v-if="selectedItemsNotListed.length > 0">, <svws-ui-button type="transparent" size="small" title="Weitere ausgewählte Einträge, die nicht angezeigt werden. Klicken entfernt aus der Liste." @click="unselectAllNotListedRows"><span class="icon-sm i-ri-close-line" />{{ selectedItemsNotListed.length }} Weitere nicht angezeigt</svws-ui-button>, <svws-ui-button type="transparent" size="small" title="Alle ausgwählten Einträge. Klicken, um alle aus der Auswahl zu entfernen." @click="unselectAllRows"><span class="icon-sm i-ri-close-line" />{{ modelValue.length }}</svws-ui-button> insgesamt</template></template>
						<template v-else-if="someNotAllRowsSelected && modelValue">{{ modelValue.length - selectedItemsNotListed.length }}/<span class="text-ui-secondary">{{ sortedRows.length }}</span> ausgewählt<template v-if="selectedItemsNotListed.length > 0"><svws-ui-button type="transparent" size="small" title="Weitere ausgewählte Einträge, die nicht angezeigt werden. Klicken entfernt aus der Liste." @click="unselectAllNotListedRows"><span class="icon-sm i-ri-close-line" />{{ selectedItemsNotListed.length }} Weitere</svws-ui-button><svws-ui-button type="transparent" size="small" title="Alle ausgwählten Einträge. Klicken, um alle aus der Auswahl zu entfernen." @click="unselectAllRows"><span class="icon-sm i-ri-close-line" />{{ modelValue.length }} insgesamt</svws-ui-button></template></template>
						<template v-else><span class="text-ui-secondary">{{ sortedRows.length === 1 ? '1 Eintrag': `${sortedRows.length} Einträge` }}</span><template v-if="selectedItemsNotListed.length > 0">, <svws-ui-button type="transparent" size="small"><span class="icon-sm i-ri-close-line" />{{ selectedItemsNotListed.length }} Ausgewählte nicht angezeigt</svws-ui-button></template></template>
					</div>
					<div v-if="$slots.actions" class="grow justify-end svws-ui-td" role="cell">
						<slot name="actions" />
					</div>
				</div>
			</slot>
		</div>
	</div>
</template>

<script lang="ts" setup generic="DataTableItem extends Record<string, any>">

	import { computed, toRef, toRaw, ref, watch, nextTick, onMounted } from "vue";
	import type { DataTableColumn, InputType, SortByAndOrder } from "../../types";

	type DataTableColumnSource = DataTableColumn | string

	type DataTableColumnInternal = {
		[key: string]: unknown;
		source: DataTableColumnSource;
		initialIndex: number;
		key: string;
		name: string;
		label: string;
		sortable: boolean;
		span: number;
		fixedWidth: number;
		minWidth: number;
		align: 'left' | 'center' | 'right';
		tooltip: string;
		disabled: boolean;
		type: InputType;
		divider: boolean;
		toggle: boolean;
		toggleInvisible: boolean;
	}

	type DataTableCell = {
		rowIndex: number;
		rowData: DataTableItem;
		column: DataTableColumnInternal;
		value: any;
	}

	type DataTableRow = {
		selectable: boolean;
		initialIndex: number;
		source: DataTableItem;
		cells: DataTableCell[];
		isEditing?: boolean;
	}

	defineOptions({ inheritAttrs: false });

	const props = withDefaults(
		defineProps<{
			columns?: DataTableColumnSource[];
			hiddenColumns?: Set<string>;
			items?: Iterable<DataTableItem> | DataTableItem[];
			modelValue?: DataTableItem[];
			uniqueKey?: string;
			clickable?: boolean;
			// allowUnclick?: boolean;
			clicked?: DataTableItem | undefined | null;
			selectable?: boolean;
			disableHeader?: boolean;
			disableFooter?: boolean;
			count?: boolean;
			noData?: boolean;
			noDataText?: string;
			scrollIntoView?: boolean;
			type?: 'table' | 'navigation' | 'grid';
			hasBackground?: boolean;
			filterOpen?: boolean;
			filterHide?: boolean;
			filtered?: boolean;
			filterReset?: () => any;
			sortByAndOrder?: SortByAndOrder;
			sortByMulti?: Map<string, (boolean | null)>;
			toggleColumns?: boolean;
			scroll?: boolean;
			allowArrowKeySelection?: boolean;
			unselectable?: Set<DataTableItem>;
			focusFirstElement?: boolean;
			focusSwitchingEnabled? : boolean;
			focusHelpVisible? : boolean;
		}>(),
		{
			columns: () => [],
			hiddenColumns: () => new Set<string>(),
			items: () => [],
			modelValue: undefined,
			uniqueKey: undefined,
			disableHeader: false,
			clickable: false,
			// allowUnclick: false,
			clicked: undefined,
			selectable: false,
			disableFooter: false,
			count: false,
			noData: undefined,
			noDataText: "Keine Einträge gefunden.",
			scrollIntoView: undefined,
			type: 'table',
			hasBackground: false,
			filterOpen: false,
			filterHide: true,
			filtered: false,
			filterReset: undefined,
			sortByAndOrder: () => ({key: null, order: null}),
			sortByMulti: undefined,
			toggleColumns: false,
			scroll: false,
			allowArrowKeySelection: false,
			unselectable: () => new Set<DataTableItem>(),
			focusFirstElement: false,
			focusSwitchingEnabled: false,
			focusHelpVisible: false,
		}
	);

	const emit = defineEmits<{
		"update:modelValue": [items: any[]];
		"update:sortByAndOrder": [obj: SortByAndOrder];
		"update:clicked": [items: DataTableItem];
		"update:filterOpen": [open: boolean];
		"update:hiddenColumns": [keys: Set<string>];
	}>();

	// const attrs = useAttrs();
	const itemRefs = ref(new Map());
	const selectionRefs = ref(new Map());

	function capitalizeFirstLetter(string: string) {
		return string.charAt(0).toUpperCase() + string.slice(1);
	}
	function getKeys<K extends object>(items: Iterable<K>): string[] {
		const accumulatedObject = [...items].reduce((accumulator, value) => {
			return {...accumulator, ...value};
		}, {});
		return Object.keys(accumulatedObject);
	}

	function switchElement(event: KeyboardEvent, list: Map<number, HTMLElement | null>, index: number, backwards: boolean) {
		if (!props.allowArrowKeySelection)
			return;
		let targetIndex;
		if (index === list.size-1 && !backwards)
			targetIndex = 0;
		else if (index === 0 && backwards)
			targetIndex = list.size-1;
		else
			targetIndex = backwards ? index-1 : index+1;
		const ele = list.get(targetIndex);
		if ((ele !== null) && (ele !== undefined))
			ele.focus();
	}

	const buildTableColumn = (source: DataTableColumnSource, initialIndex: number): DataTableColumnInternal => {
		const input = typeof source === 'string' ? { key: source } : source;
		return {
			source,
			initialIndex,
			key: input.key,
			name: input.name ?? input.key,
			label: input.label ?? capitalizeFirstLetter(input.key),
			sortable: input.sortable ?? false,
			span: input.span ?? 1,
			fixedWidth: (input.fixedWidth === undefined) ? 0 : Number(input.fixedWidth),
			minWidth: (input.minWidth === undefined) ? 0 : Number(input.minWidth),
			align: input.align ?? 'left',
			tooltip: input.tooltip ?? '',
			disabled: input.disabled ?? false,
			type: input.type ?? 'text',
			divider: input.divider ?? false,
			toggle: input.toggle ?? false,
			toggleInvisible: input.toggleInvisible ?? false,
		}
	}

	const columnsComputed = computed(() =>
		(props.columns.length === 0)
			? getKeys(props.items).map((item, index) => buildTableColumn(item, index))
			: props.columns.map((column, index) => buildTableColumn(column, index)).filter(column => !props.hiddenColumns.has(column.key)));

	const gridTemplateColumns = computed<string>(() =>
		columnsComputed.value.map(column =>
			`minmax(${ column.fixedWidth > 0 ? (column.fixedWidth + 'rem') : (column.minWidth > 0 ? (column.minWidth + 'rem') : '4rem') }, ${ column.fixedWidth > 0 ? (column.fixedWidth + 'rem') : column.span + 'fr' })`
		).join(' '));

	const gridTemplateColumnsComputed = computed(() => gridTemplateColumns.value.length > 0 ? gridTemplateColumns.value : 'repeat(auto-fit, minmax(0, 1fr))');

	const getGridTemplateColumns = computed(() => `grid-template-columns: ${props.selectable ? 'auto': ''} ${gridTemplateColumnsComputed.value}`);

	const rowsComputed = computed<DataTableRow[]>(() => [...props.items].map((source, index) =>
		({ selectable: !props.unselectable.has(toRaw(source)), initialIndex: index, source: toRaw(source), cells:
			columnsComputed.value.map(column => ({ rowIndex: index, rowData: toRaw(source), column, value: source[column.key] ?? '' })),
		})));

	const sortedRows = computed(() => {
		if (rowsComputed.value.length < 0 || props.sortByMulti !== undefined)
			return rowsComputed.value;
		const columnIndex = columnsComputed.value.findIndex( ({ key, sortable }) => (internalSortByAndOrder.value.key === key) && sortable);
		if ((columnIndex < 0) || (columnIndex > columnsComputed.value.length -1))
			return rowsComputed.value;
		const sortingOrderRatio = internalSortByAndOrder.value.order === false ? -1 : 1
		return [...rowsComputed.value].sort((a, b) => {
			if (internalSortByAndOrder.value.order === null)
				return a.initialIndex - b.initialIndex;
			const firstValue = String(a.cells[columnIndex].value);
			const secondValue = String(b.cells[columnIndex].value);
			return sortingOrderRatio * (firstValue.localeCompare(secondValue));
		})
	})

	function cycleSorting(value: boolean | null | undefined) {
		if (value === null || value === undefined)
			return true;
		if (value === true)
			return false;
		return null;
	}

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const internalSortByAndOrder = ref<SortByAndOrder>(props.sortByAndOrder)

	function toggleSorting(column: DataTableColumnInternal) {
		const neu: SortByAndOrder = {key: column.key, order: true};
		if (column.key === internalSortByAndOrder.value.key)
			neu.order = cycleSorting(internalSortByAndOrder.value.order);
		else if (props.sortByMulti !== undefined){
			const alt = props.sortByMulti.get(column.key);
			neu.order = cycleSorting(alt);
		}
		internalSortByAndOrder.value = neu;
		emit('update:sortByAndOrder', neu);
	}

	const filterOpenProp = toRef(props, 'filterOpen');
	const isFilterOpen = ref(filterOpenProp.value);

	function toggleFilterOpen() {
		isFilterOpen.value = !isFilterOpen.value;
		emit('update:filterOpen', isFilterOpen.value);
	}

	const selectedItemsRaw = computed(() => (props.modelValue ?? []).map(i => toRaw(i)))
	const allRowsSelected = computed(() => (sortedRows.value.length === 0) ? false : sortedRows.value.filter(row => !props.unselectable.has(row.source)).every(isRowSelected));
	const someNotAllRowsSelected = computed(() => (sortedRows.value.length === 0) ? false : sortedRows.value.some(isRowSelected) && !allRowsSelected.value);
	const selectedItemsNotListed = computed(() => selectedItemsRaw.value.filter(i => !sortedRows.value.some(r => toRaw(r.source) === i)));

	function isRowSelected(row: DataTableRow) {
		return selectedItemsRaw.value.includes(row.source)
	}

	function selectAllRows() {
		emit('update:modelValue', [...sortedRows.value.filter(row => !props.unselectable.has(row.source)).map(row => row.source).concat(selectedItemsNotListed.value)]);
	}

	function unselectAllVisibleRows() {
		emit('update:modelValue', [...selectedItemsNotListed.value]);
	}

	function unselectAllRows() {
		emit('update:modelValue', []);
	}

	function unselectAllNotListedRows() {
		emit('update:modelValue', [...selectedItemsRaw.value.filter(i => sortedRows.value.some(r => toRaw(r.source) === i))]);
	}

	function selectRow(row: DataTableRow) {
		emit('update:modelValue', selectedItemsRaw.value.concat(row.source))
	}

	function unselectRow(row: DataTableRow) {
		const index = selectedItemsRaw.value.indexOf(row.source)
		const newSelection = selectedItemsRaw.value.slice()
		newSelection.splice(index, 1)
		emit('update:modelValue', newSelection)
	}

	function toggleRowSelection(row: DataTableRow) {
		if (!props.selectable)
			return;
		if (isRowSelected(row))
			unselectRow(row);
		else
			selectRow(row);
	}

	function toggleBulkSelection() {
		if (allRowsSelected.value)
			unselectAllVisibleRows();
		else
			selectAllRows();
	}

	const clickedItemRaw = computed(() => (toRaw(props.clicked) ?? null));
	const clickedItemIndex = ref<number|undefined>();

	function isRowClicked(row: DataTableRow) {
		const is = row.source === clickedItemRaw.value;
		if (!is)
			return false;
		clickedItemIndex.value = sortedRows.value.indexOf(row);
		return true;
	}

	function toggleRowClick(row: DataTableRow) {
		if (!props.clickable)
			return;
		setClickedRow(row);
	}

	function setClickedRow(row: DataTableRow) {
		if (!props.clickable)
			return;
		emit('update:clicked', row.source);
	}

	watch(() => props.clicked, (neu, alt) => {
		if ((neu === undefined) || (neu === null))
			return;
		const index = clickedItemIndex.value = sortedRows.value.map(r => r.source).indexOf(neu);
		const clickedElementHtml: unknown = itemRefs.value.get(index);
		if ((alt !== neu) && (clickedElementHtml instanceof HTMLElement))
			clickedElementHtml.focus();
	});

	watch(() => props.items, () => nextTick(scrollToClickedElement));

	function isInView(el: Element) {
		const box = el.getBoundingClientRect();
		return box.top < window.innerHeight && box.bottom >= 0;
	}

	function scrollToClickedElement() {
		// wenn eine Mehrfachauswahl durchgeführt wird, springe nicht zum ausgewählten Item
		if (props.modelValue !== undefined && props.modelValue.length > 0)
			return;
		if ((props.scrollIntoView === undefined) || (props.scrollIntoView === false) || (clickedItemIndex.value === undefined))
			return;
		// TODO scrollIntoViewIfNeeded wird nicht von FF unterstützt as of 116
		const clickedElementHtml: any = itemRefs.value.get(clickedItemIndex.value);
		const scrollOptions: ScrollIntoViewOptions = { behavior: "auto", block: "center" };
		if ((clickedElementHtml !== undefined) && (clickedElementHtml !== null)) {
			if (typeof clickedElementHtml.scrollIntoViewIfNeeded === "function")
				clickedElementHtml.scrollIntoViewIfNeeded(scrollOptions);
			else if(!isInView(clickedElementHtml))
				clickedElementHtml.scrollIntoView(scrollOptions);
		}
	}

	const noDataCalculated = computed(() => (sortedRows.value.length === 0));

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const data = ref<Set<string>>(props.hiddenColumns);

	watch(() => props.hiddenColumns, (value: Set<string>) => updateHiddenColumnsSet(value), { immediate: false });

	function updateHiddenColumnsSet(value: Set<string>) {
		if ((data.value.size === value.size) && [...data.value].every(dataVal => value.has(dataVal)))
			return;
		data.value = value;
	}

	function updateHiddenColumns(columnKey: string, ok: boolean) {
		if (ok) {
			data.value.delete(columnKey);
		} else {
			data.value.add(columnKey);
		}
		emit("update:hiddenColumns", data.value);
	}

	const win11FForMacOS = computed<boolean>(() => {
		const userAgent = window.navigator.userAgent;
		if (userAgent.includes("Mac"))
			return true;
		if (userAgent.includes("Win") && userAgent.includes("Firefox"))
			return true;
		return false;
	});

	onMounted(() => {
		if (props.focusFirstElement)
			itemRefs.value.get(0)?.focus();
		else
			itemRefs.value.get(clickedItemIndex.value)?.focus();
	});

</script>
