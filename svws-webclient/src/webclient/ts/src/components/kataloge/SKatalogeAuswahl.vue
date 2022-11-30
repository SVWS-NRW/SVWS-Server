<template>
	<svws-ui-secondary-menu>
		<template #headline> Kataloge </template>
		<template #header> </template>
		<template #content>
			<div class="secondary-menu--navigation container">
				<svws-ui-sidebar-menu-item
					v-for="item in menu_items"
					:key="item.value"
					@click="router.push({ name: item.value })"
				>
					<template #label>
						<span>{{ item.title }}</span>
					</template>
				</svws-ui-sidebar-menu-item>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
	import { computed, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router";

	const menu_items = [
		{ title: "Fächer", value: "faecher" },
		{ title: "Jahrgänge", value: "jahrgaenge" },
		{ title: "Religionen", value: "religionen" },
		{ title: "Förderschwerpunkte", value: "foerderschwerpunkte" },
		{ title: "Haltestellen", value: "haltestellen" },
		{ title: "Betriebe", value: "betriebe" }
	];
	const main: Main = injectMainApp();

	// const menubar_selected: WritableComputedRef<string | undefined> = computed({
	// 	get(): string | undefined {
	// 		return main.config.selected_app;
	// 	},
	// 	set(val: string | undefined) {
	// 		if (
	// 			val &&
	// 			val !== menubar_selected.value &&
	// 			main.config.selected_app
	// 		) {
	// 			main.config.selected_app = val;
	// 		}
	// 	}
	// });
</script>
