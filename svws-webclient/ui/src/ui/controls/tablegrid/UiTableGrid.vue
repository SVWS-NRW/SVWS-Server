<template>
	<table class="ui-table-grid" :aria-label="name">
		<thead>
			<template v-for="i in headerCount" :key="i">
				<tr>
					<slot :i="i" name="header" />
				</tr>
			</template>
		</thead>
		<tbody>
			<template v-for="(row, index) in manager().daten" :key="manager().getRowKey(row)">
				<tr :class="{ 'bg-ui-selected': !hideSelection && (manager().focusRow === index) }" @click="rowClicked(row, index)">
					<slot :row="row" :index="index" />
				</tr>
			</template>
		</tbody>
		<tfoot>
			<template v-for="i in footerCount" :key="i">
				<tr>
					<slot :i="i" name="footer" />
				</tr>
			</template>
		</tfoot>
	</table>
</template>

<script setup lang="ts" generic="T,U">

	import { computed } from 'vue';
	import type { Collection } from '../../../../../core/src/java/util/Collection';
	import type { List } from '../../../../../core/src/java/util/List';
	import type { GridManager } from './GridManager';

	export interface CellFormat {
		widths: string[];
	}

	function rowClicked(row: T, index: number) {
		props.manager().doFocusRowIfNotFocussed(index);
	}

	const props = withDefaults(defineProps<{
		cellFormat: CellFormat,
		headerCount?: number,
		footerCount?: number,
		name?: string | undefined,
		manager: () => GridManager<U, T, Collection<T> | List<T>>,
		hideSelection?: boolean,
	}>(), {
		headerCount: 1,
		footerCount: 1,
		name: undefined,
		hideSelection: false,
	});

	const gridTemplateColumnsComputed = computed<string>(() => props.cellFormat.widths.join(" "));

</script>

<style scoped>

	tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
	}

</style>
