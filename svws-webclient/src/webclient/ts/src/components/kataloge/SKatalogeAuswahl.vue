<template>
	<svws-ui-secondary-menu>
		<template #headline>Kataloge</template>
		<template #abschnitt>
			<svws-ui-multi-select v-if="schule_abschnitte" v-model="akt_abschnitt" :items="schule_abschnitte" :item-sort="item_sort" :item-text="item_text"></svws-ui-multi-select>
		</template>
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
	import { computed, WritableComputedRef, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router";
	import {Schuljahresabschnitt} from "@svws-nrw/svws-core-ts";
	import {Schule} from "~/apps/schule/Schule";

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

	const schule_abschnitte: ComputedRef<
		Array<Schuljahresabschnitt> | undefined
	> = computed(() => {
		const liste = appSchule.value.schuleStammdaten.daten?.abschnitte;
		return liste?.toArray(new Array<Schuljahresabschnitt>()) || [];
	});

	const akt_abschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get(): Schuljahresabschnitt {
			return main.config.akt_abschnitt;
		},
		set(abschnitt: Schuljahresabschnitt) {
			main.config.akt_abschnitt = abschnitt;
		}
	});

	const appSchule: ComputedRef<Schule> = computed(() => {
		return main.apps.schule;
	});
	function item_sort(a: Schuljahresabschnitt, b: Schuljahresabschnitt) {
		return (
			b.schuljahr + b.abschnitt * 0.1 - (a.schuljahr + a.abschnitt * 0.1)
		);
	}

	function item_text(item: Schuljahresabschnitt) {
		return item.schuljahr
			? `${item.schuljahr}, ${item.abschnitt}. HJ`
			: "Abschnitt";
	}
</script>
