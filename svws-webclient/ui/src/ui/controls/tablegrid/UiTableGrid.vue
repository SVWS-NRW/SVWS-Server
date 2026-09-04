<template>
	<table class="ui-table-grid" :aria-label="name">
		<thead>
			<template v-for="i in headerCount" :key="i">
				<tr>
					<slot :i name="header" />
				</tr>
			</template>
		</thead>
		<tbody>
			<template v-for="(row, index) in manager().daten" :key="manager().getRowKey(row)">
				<tr :class="[{ 'hover:bg-ui-hover': showRowHighlight,'bg-ui-selected': !hideSelection && (manager().focusRow === index) }, 'group']"
					@click="rowClicked(row, index)">
					<slot :row :index />
				</tr>
			</template>
		</tbody>
		<tfoot>
			<template v-for="i in footerCount" :key="i">
				<tr>
					<slot :i name="footer" />
				</tr>
			</template>
		</tfoot>
	</table>
</template>

<script setup lang="ts" generic="T,U extends PropertyKey">

	import { computed } from 'vue';
	import type { GridManager } from './GridManager';
	import type { Collection } from '@core/java/util/Collection';

	export interface CellFormat {
		widths: string[];
	}

	function rowClicked(row: T, index: number) {
		props.manager().doFocusRowIfNotFocussed(index);
	}

	const props = withDefaults(defineProps<{
		headerCount?: number,
		footerCount?: number,
		name?: string | undefined,
		manager: () => GridManager<U, T, Collection<T> | T[]>,
		hideSelection?: boolean,
		showRowHighlight?: boolean,
	}>(), {
		headerCount: 1,
		footerCount: 1,
		name: undefined,
		hideSelection: false,
		showRowHighlight: true,
	});

	const gridTemplateColumnsComputed = computed<string>(() => props.manager().getGridTemplateColumns());

</script>

<style scoped>

	tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
	}

</style>
