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

	import { SchuelerLernabschnittListeEintrag, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, WritableComputedRef } from "vue";
	import { DataSchuelerAbschnittsdaten } from "~/apps/schueler/DataSchuelerAbschnittsdaten";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { routeSchuelerLeistungenDaten } from "~/router/apps/schueler/leistungsdaten/RouteSchuelerLeistungenDaten";

	const props = defineProps<{ id?: number; item?: SchuelerListeEintrag, stammdaten: DataSchuelerStammdaten, lernabschnitt?: SchuelerLernabschnittListeEintrag, data: DataSchuelerAbschnittsdaten, routename: string }>();
	
	const liste: ComputedRef<SchuelerLernabschnittListeEintrag[]> = computed(() => routeSchuelerLeistungenDaten.data.auswahl.liste);

	const selected: WritableComputedRef<SchuelerLernabschnittListeEintrag | undefined> = routeSchuelerLeistungenDaten.auswahl;

	const cols = ref([
		{ key: "schuljahresabschnitt", label: "Abschnitt" },
	]);

	const visible: ComputedRef<boolean> = computed<boolean>(() => {
		return !(routeSchuelerLeistungenDaten.hidden);
	});

</script>
