<template>
	<template v-if="visible">
		<svws-ui-table v-model="selected" :columns="cols" :data="liste" :footer="false">
			<template #cell-schuljahresabschnitt="{row}">
				{{ row.schuljahr + "." + row.abschnitt + (row.wechselNr === null ? "" : " (alt)") }}
			</template>
		</svws-ui-table>
	</template>
</template>

<script setup lang="ts">

	import { FaecherListeEintrag, LehrerListeEintrag, SchuelerLernabschnittListeEintrag, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, ShallowRef, WritableComputedRef } from "vue";
	import { DataSchuelerAbschnittsdaten } from "~/apps/schueler/DataSchuelerAbschnittsdaten";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { routeSchuelerLeistungenDaten } from "~/router/apps/schueler/leistungsdaten/RouteSchuelerLeistungenDaten";

	const props = defineProps<{
		item: ShallowRef<SchuelerListeEintrag | undefined>;
		stammdaten: DataSchuelerStammdaten,
		lernabschnitt?: SchuelerLernabschnittListeEintrag,
		data: DataSchuelerAbschnittsdaten,
		mapFaecher: Map<number, FaecherListeEintrag>,
		mapLehrer: Map<number, LehrerListeEintrag>,
	}>();

	const liste: ComputedRef<SchuelerLernabschnittListeEintrag[]> = computed(() => routeSchuelerLeistungenDaten.data.auswahl.liste);

	const selected: WritableComputedRef<SchuelerLernabschnittListeEintrag | undefined> = routeSchuelerLeistungenDaten.auswahl;

	const cols = ref([
		{ key: "schuljahresabschnitt", label: "Abschnitt" },
	]);

	const visible: ComputedRef<boolean> = computed<boolean>(() => {
		return !(routeSchuelerLeistungenDaten.hidden());
	});

</script>
