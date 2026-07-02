<template>
	<div class="ui-table-actions flex h-full items-center justify-end">
		<div :class="{ 'opacity-0 group-hover:opacity-100 group-focus-within:opacity-100 transition-opacity duration-200' : !alwaysVisible }">
			<div class="flex items-center justify-end">
				<template v-for="action of actions" :key="action.label">
					<svws-ui-button :type="action.trash ? 'trash' : 'icon'"
						class="ui-table-actions--button"
						:disabled="action.disabled"
						:title="action.label"
						:aria-label="action.label"
						@click="() => action.action(items)">
						<span :class="['icon', iconClass(action)]" />
					</svws-ui-button>
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

	const iconClass = ((action: TableActions<T>) => {
		return (action.trash === true) ? "" : action.iconClasses;
	});

</script>
