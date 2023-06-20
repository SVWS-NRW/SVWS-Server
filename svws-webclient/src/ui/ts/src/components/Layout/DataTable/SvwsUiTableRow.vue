<template>
	<div role="row" class="data-table__tr" :class="{
		'data-table__thead__tr': thead,
		'data-table__tfoot__tr': tfoot,
		'data-table__tbody__tr': !thead && !tfoot,
		'data-table__tr--selected': selected,
		'data-table__tr--clicked': clicked,
		'data-table__tr__depth-1': depth === 1,
		'data-table__tr__depth-2': depth === 2,
		'data-table__tr__depth-3': depth === 3,
		'data-table__tr__depth-4': depth === 4,
		'data-table__tr__depth-5': depth === 5,
		'data-table__tr__depth-6': depth === 6,
		'data-table__tr__collapsed': collapsed,
		'data-table__tr__expanded': expanded,
		'data-table__thead__tr__compact': compact,
	}">
		<slot />
	</div>
</template>

<script lang="ts" setup>
	const props = withDefaults(
		defineProps<{
			thead?: boolean;
			tfoot?: boolean;
			selected?: boolean;
			clicked?: boolean;
			collapsed?: boolean;
			expanded?: boolean;
			depth?: 0 | 1 | 2 | 3 | 4 | 5 | 6;
			compact?: boolean;
		}>(),
		{
			thead: false,
			tfoot: false,
			selected: false,
			clicked: false,
			collapsed: false,
			expanded: false,
			depth: 0,
			compact: false,
		}
	);
</script>
