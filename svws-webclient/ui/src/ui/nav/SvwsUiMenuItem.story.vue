<template>
	<Story title="Sidebar/Menu Item" id="svws-ui-menu-item" :layout="{ type: 'single', iframe: false }" group="app">
		<Variant v-for="[id, props] in variantControlsMap" :key="id" :id :title="id">
			<div class="w-full max-w-[80px] mx-auto">
				<svws-ui-menu-item v-bind="props" @click="onClick">
					<template #icon>
						<span class="icon i-ri-team-line" />
					</template>
					<template #label>Menüpunkt</template>
				</svws-ui-menu-item>
			</div>
		</Variant>
	</Story>
</template>


<script setup lang="ts">
	import { reactive } from "vue";
	import { logEvent } from '../../stories/helper';

	const variantControlsMap = new Map([
		['Default', reactive({ active: false, collapsed: false, disabled: false, subline: '' })],
		['Active', reactive({ active: true, collapsed: false, disabled: false, subline: '' })],
		['Collapsed', reactive({ active: false, collapsed: true, disabled: false, subline: '' })],
		['Disabled', reactive({ active: false, collapsed: false, disabled: true, subline: '' })],
		['Subline', reactive({ active: false, collapsed: false, disabled: false, subline: 'Subline' })],
	]);

	function onClick(event: MouseEvent) {
		logEvent("click", event);
	}
</script>
