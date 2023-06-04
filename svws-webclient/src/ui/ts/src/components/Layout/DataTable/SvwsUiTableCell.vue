<template>
	<div :role="thead ? 'columnheader' : 'cell'" :class="{
		'data-table__th data-table__thead__th': thead,
		'data-table__td data-table__tbody__td': !thead,
		'data-table__td__no-padding': noPadding,
		'data-table__td__align-right': align === 'right',
		'data-table__td__align-center': align === 'center',
		'data-table__td__align-left': align === 'left',
	}">
		<div class="data-table__th-wrapper" v-if="thead">
			<span class="data-table__th-title">
				<svws-ui-tooltip v-if="tooltip">
					<slot />
					<template #content>
						{{ tooltip }}
					</template>
				</svws-ui-tooltip>
				<template v-else>
					<slot />
				</template>

			</span>
		</div>
		<template v-else>
			<slot />
		</template>
	</div>
</template>

<script lang="ts" setup>
	const props = withDefaults(
		defineProps<{
			thead?: boolean;
			tooltip?: string;
			noPadding?: boolean;
			align?: 'left' | 'center' | 'right';
		}>(),
		{
			thead: false,
			tooltip: '',
			noPadding: false,
			align: 'left',
		}
	);
</script>
