<template>
	<svws-ui-secondary-menu>
		<template #headline>Kurse</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rows" :footer="false" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { KursListeEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
	import { computed, ShallowRef } from "vue";
	import { routeKurse } from "~/router/apps/RouteKurse";
	import { ListJahrgaenge } from "~/apps/kataloge/jahrgaenge/ListJahrgaenge";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataTableColumn } from "@svws-nrw/svws-ui";

	const props = defineProps<{
		item: ShallowRef<KursListeEintrag | undefined>;
		listJahrgaenge: ListJahrgaenge;
		listLehrer: ListLehrer;
		abschnitte: List<Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	}>();

	const selected = routeKurse.auswahl;

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "lehrer_name", label: "Fachlehrer", sortable: true },
		{ key: "jahrgang", label: "Jahrgang", sortable: true }
	];

	// FIXME: Typing: const rows: ComputedRef<KursEintrag[] | undefined> = computed(() => {
	const rows = computed(() => {
		return routeKurse.liste.liste.map((e: KursListeEintrag) => ({
			...e,
			lehrer_name: props.listLehrer.liste.find(l => l.id === e.lehrer)?.kuerzel || "",
			jahrgang: props.listJahrgaenge.liste.find(j => e.idJahrgaenge.toArray(new Array<number>()).includes(j.id))?.kuerzel ?? ""
		}));
	});
</script>
