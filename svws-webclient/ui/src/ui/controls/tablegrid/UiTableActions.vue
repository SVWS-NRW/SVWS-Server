<template>
	<div class="ui-table-actions flex h-full items-center justify-end">
		<div :class="{ 'opacity-0 group-hover:opacity-100 group-focus-within:opacity-100 transition-opacity duration-200' : !alwaysVisible }">
			<div class="flex items-center justify-end">
				<template v-for="action of actions" :key="action.label">
					<svws-ui-tooltip>
						<svws-ui-button v-if="action.trash" type="trash" @click="() => action.action(items)" :disabled="action.disabled" />
						<svws-ui-button v-else type="icon" @click="() => action.action(items)" :disabled="action.disabled">
							<span :class="[action.iconClasses, 'icon']" />
						</svws-ui-button>
						<template #content>
							{{ action.label }}
						</template>
					</svws-ui-tooltip>
				</template>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts" generic="T">

	export type TableActions<T> =
		{
			trash: true;
			label: string;
			action: (item: T) => void;
			disabled?: boolean; }
		| {
			trash?: false;
			label: string;
			action: (item: T) => void;
			disabled?: boolean;
			iconClasses: string;
		};

	withDefaults(defineProps<{
		alwaysVisible?: boolean;
		actions?: TableActions<T>[];
		items: T,
	}>(), {
		alwaysVisible: false,
		actions: () => [],
	});

</script>
