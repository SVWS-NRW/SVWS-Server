<template>
	<svws-ui-secondary-menu>
		<template #headline
			><div>
				<i-ri-arrow-left-line
					class="inline-block cursor-pointer"
					@click="main.config.selected_app = 'Kataloge'"
				/>
				Förderschwerpunktauswahl
			</div>
		</template>
		<template #header> </template>
		<template #content>
			<div class="container">
				<svws-ui-table
					v-model="selected"
					:columns="cols"
					:data="rows"
					:footer="false"
				/>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
	import type { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, Ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const none_selected: Ref<FoerderschwerpunktEintrag> = ref({
		id: -1,
		kuerzel: "",
		text: "",
		kuerzelStatistik: "",
		istSichtbar: false,
		istAenderbar: false
	} as unknown as FoerderschwerpunktEintrag);

	const cols = ref([
		{
			key: "kuerzel",
			label: "Kuerzel",
			width: "6em",
			sortable: true,
			defaultSort: 'asc'
		},
		{
			key: "text",
			label: "Bezeichnung",
			sortable: true
		}
	]);

	const main: Main = injectMainApp();
	const app = main.apps.foerderschwerpunkte;

	const rows: ComputedRef<FoerderschwerpunktEintrag[] | undefined> = computed(
		() => {
			return app.auswahl.liste;
		}
	);

	const selected: WritableComputedRef<FoerderschwerpunktEintrag | undefined> =
		computed({
			get(): FoerderschwerpunktEintrag {
				if (!app.auswahl.ausgewaehlt) return none_selected.value;
				return app.auswahl.ausgewaehlt;
			},
			set(value: FoerderschwerpunktEintrag) {
				if (app) {
					app.auswahl.ausgewaehlt = value;
				}
			}
		});
</script>
