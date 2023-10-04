<template>
	<div :role="thead ? 'columnheader' : 'cell'" :class="{
		'data-table__th data-table__thead__th': thead,
		'data-table__td data-table__tbody__td': !thead,
		'data-table__td__no-padding': noPadding,
		'data-table__td__padding-sm': paddingSm,
		'data-table__td__align-right': align === 'right',
		'data-table__td__align-center': align === 'center',
		'data-table__td__align-left': align === 'left',
		'data-table__td__disabled': disabled,
		'col-span-full': span === 'full',
		'data-table__th__separate': separate && thead,
		'data-table__td__separate': separate && !thead,
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
	console.warn("Diese Komponente ist veraltet, bitte entsprechend der Doku auf eine andere Komponente umstellen. In einer der folgenden Versionen wird diese Komponente entfernt.");
	const props = withDefaults(
		defineProps<{
			thead?: boolean;
			tooltip?: string;
			noPadding?: boolean;
			paddingSm?: boolean;
			align?: 'left' | 'center' | 'right';
			disabled?: boolean;
			span?: 'full';
			separate?: boolean;
		}>(),
		{
			thead: false,
			tooltip: '',
			noPadding: false,
			paddingSm: false,
			align: 'left',
			disabled: false,
			separate: false,
		}
	);
</script>
