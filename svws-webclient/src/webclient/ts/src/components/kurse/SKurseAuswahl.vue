<template>
	<svws-ui-secondary-menu>
		<template #headline>Kurse</template>
		<template #abschnitt>
			<svws-ui-multi-select v-if="schule_abschnitte" v-model="akt_abschnitt" :items="schule_abschnitte" :item-sort="item_sort" :item-text="item_text"></svws-ui-multi-select>
		</template>
		<template #header> </template>
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rows" :footer="false" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { KursListeEintrag } from "@svws-nrw/svws-core-ts";
	import { routeKurse } from "~/router/apps/RouteKurse";
	import {Schuljahresabschnitt} from "@svws-nrw/svws-core-ts";
	import {Schule} from "~/apps/schule/Schule";

	const props = defineProps<{ id?: number; item?: KursListeEintrag, routename: string }>();
	const selected = routeKurse.auswahl;

	const cols = [
		{ key: "kuerzel", label: "Kürzel", width: "6em", sortable: true, defaultSort: "asc" },
		{ key: "lehrer_name", label: "Fachlehrer", sortable: true },
		{ key: "jahrgang", label: "Jahrgang", sortable: true }
	];
	const main: Main = injectMainApp();
	const app = main.apps.kurse;
	const appLehrer = main.apps.lehrer;
	const appJahrgaenge = main.apps.jahrgaenge;

	// FIXME: Typing: const rows: ComputedRef<KursEintrag[] | undefined> = computed(() => {
	const rows = computed(() => {
		return app.auswahl.liste.map((e: KursListeEintrag) => ({
			...e,
			lehrer_name: appLehrer.auswahl.liste.find(l => l.id === e.lehrer)?.kuerzel || "",
			jahrgang: appJahrgaenge.auswahl.liste.find(j => e.idJahrgaenge.toArray(new Array<number>()).includes(j.id))?.kuerzel?.toString() || ""
		}));
	});


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
