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

	import { computed, ComputedRef, ShallowRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { JahrgangsListeEintrag, KursListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { routeKurse } from "~/router/apps/RouteKurse";
	import { Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataTableColumn } from "@svws-nrw/svws-ui";

	const { schule, listJahrgaenge, listLehrer } = defineProps<{ 
		item: ShallowRef<KursListeEintrag | undefined>;
		schule: DataSchuleStammdaten;
		listJahrgaenge: ListJahrgaenge;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		listLehrer: ListLehrer;
		mapLehrer: Map<Number, LehrerListeEintrag>;
	}>();

	const selected = routeKurse.auswahl;

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "lehrer_name", label: "Fachlehrer", sortable: true },
		{ key: "jahrgang", label: "Jahrgang", sortable: true }
	];
	const main: Main = injectMainApp();

	// FIXME: Typing: const rows: ComputedRef<KursEintrag[] | undefined> = computed(() => {
	const rows = computed(() => {
		return routeKurse.liste.liste.map((e: KursListeEintrag) => ({
			...e,
			lehrer_name: listLehrer.liste.find(l => l.id === e.lehrer)?.kuerzel || "",
			jahrgang: listJahrgaenge.liste.find(j => e.idJahrgaenge.toArray(new Array<number>()).includes(j.id))?.kuerzel?.toString() || ""
		}));
	});

	const schule_abschnitte: ComputedRef<Array<Schuljahresabschnitt> | undefined> = computed(() => {
		const liste = schule.daten?.abschnitte;
		return liste?.toArray(new Array<Schuljahresabschnitt>()) || [];
	});

	const akt_abschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get: () => main.config.akt_abschnitt,
		set: (abschnitt) => main.config.akt_abschnitt = abschnitt
	});

	function item_sort(a: Schuljahresabschnitt, b: Schuljahresabschnitt) {
		return b.schuljahr + b.abschnitt * 0.1 - (a.schuljahr + a.abschnitt * 0.1);
	}

	function item_text(item: Schuljahresabschnitt) {
		return item.schuljahr ? `${item.schuljahr}, ${item.abschnitt}. HJ` : "Abschnitt";
	}

</script>
