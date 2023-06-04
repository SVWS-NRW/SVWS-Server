<template>
	<div role="row" class="data-table__tr" :class="{
		'data-table__thead__tr': thead,
		'data-table__tfoot__tr': tfoot,
		'data-table__tbody__tr': !thead && !tfoot,
		'data-table__tr--selected': selected,
		'data-table__tr--clicked': clicked,
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
		}>(),
		{
			thead: false,
			tfoot: false,
			selected: false,
			clicked: false,
		}
	);
</script>
