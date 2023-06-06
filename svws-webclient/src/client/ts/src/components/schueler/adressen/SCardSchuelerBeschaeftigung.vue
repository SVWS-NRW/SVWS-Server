<template>
	<svws-ui-content-card title="Beschäftigungen">
		<svws-ui-data-table :items="[]" :no-data="false" :columns="cols" clickable>
			<template #body>
				<svws-ui-table-row v-for="(betrieb, index) in listSchuelerbetriebe" :key="betrieb.id" @click="select(betrieb)" :clicked="clickedBetrieb ? clickedBetrieb === betrieb.id : index === 0">
					<s-card-schueler-beschaeftigung-tabelle :betrieb="betrieb" :map-beschaeftigungsarten="mapBeschaeftigungsarten"
						:map-lehrer="mapLehrer" :map-betriebe="mapBetriebe" :map-ansprechpartner="mapAnsprechpartner"
						:patch-schueler-betriebsdaten="patchSchuelerBetriebsdaten" />
				</svws-ui-table-row>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { BetriebAnsprechpartner, BetriebListeEintrag, KatalogEintrag, LehrerListeEintrag, List, SchuelerBetriebsdaten } from "@core";
	import type { DataTableColumn } from "@ui";
	import { ref } from "vue";

	const props = defineProps<{
		patchSchuelerBetriebsdaten: (data : Partial<SchuelerBetriebsdaten>, id : number) => Promise<void>;
		setSchuelerBetrieb: (betrieb : SchuelerBetriebsdaten | undefined) => Promise<void>;
		listSchuelerbetriebe : List<SchuelerBetriebsdaten>;
		mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapBetriebe: Map<number, BetriebListeEintrag>;
		mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
	}>();

	const clickedBetrieb = ref<number | undefined>(undefined);

	const cols: Array<DataTableColumn> = [
		{ key: "Betrieb", label: "Betrieb"},
		{ key: "Ausbilder", label: "Ausbilder"},
		{ key: "Beschäftigungsart", label: "Beschäftigungsart"},
		{ key: "Beginn", label: "Beginn", span: 0.5},
		{ key: "Ende", label: "Ende", span: 0.5},
		{ key: "Praktikum", label: "Praktikum", span: 0.25, tooltip: 'Praktikum', align: "center"},
		{ key: "Betreuungslehrer", label: "Betreuungslehrer"},
		{ key: "Ansprechpartner", label: "Ansprechpartner"},
		{ key: "Anschreiben", label: "Anschreiben", tooltip: "Betrieb erhält Anschreiben", span: 0.25, align: "center"}
	];

	async function select(betrieb : SchuelerBetriebsdaten) {
		await props.setSchuelerBetrieb(betrieb);
		clickedBetrieb.value = betrieb.id;
	}

</script>
