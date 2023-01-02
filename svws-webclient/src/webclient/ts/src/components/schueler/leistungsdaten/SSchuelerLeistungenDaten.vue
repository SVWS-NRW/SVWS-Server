<template>
	<div v-if="visible">
		<svws-ui-table v-model="selected" :columns="cols" :data="liste" :footer="false">
			<template #body>
				<s-schueler-leistung v-for="(fach, i) of liste" :key="i" :fach="fach" />
			</template>
		</svws-ui-table>
	</div>
</template>

<script setup lang="ts">

	import { SchuelerLeistungsdaten, SchuelerLernabschnittListeEintrag, SchuelerLernabschnittsdaten, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, WritableComputedRef } from "vue";
	import { DataSchuelerAbschnittsdaten } from "~/apps/schueler/DataSchuelerAbschnittsdaten";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { routeSchuelerLeistungenDaten } from "~/router/apps/schueler/leistungsdaten/RouteSchuelerLeistungenDaten";

	const props = defineProps<{ id?: number; item?: SchuelerListeEintrag, stammdaten: DataSchuelerStammdaten, lernabschnitt?: SchuelerLernabschnittListeEintrag, data: DataSchuelerAbschnittsdaten, routename: string }>();
	
	const lernabschnittsdaten: ComputedRef<SchuelerLernabschnittsdaten> = computed(() => props.data.daten || new SchuelerLernabschnittsdaten());
	const liste: ComputedRef<SchuelerLeistungsdaten[]> = computed(() =>
		lernabschnittsdaten.value.leistungsdaten.toArray() as SchuelerLeistungsdaten[] || []
	);

	const selected: WritableComputedRef<SchuelerLernabschnittListeEintrag | undefined> = 
		routeSchuelerLeistungenDaten.auswahl;

	const cols = ref([
		{ key: "schuljahr", label: "Fach", span: "1", sortable: false },
		{ key: "abschnitt", label: "Lehrer", span: "1", sortable: false },
		{ key: "abschnitt", label: "Note", span: "1", sortable: false },
	]);

	const visible: ComputedRef<boolean> = computed<boolean>(() => {
		return (!routeSchuelerLeistungenDaten.hidden) && (props.lernabschnitt !== undefined);
	});

</script>
