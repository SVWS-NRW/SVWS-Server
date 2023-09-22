<template>
	<svws-ui-content-card title="Beschäftigungen">
		<svws-ui-table :items="[]" :no-data="listSchuelerbetriebe().size() === 0" :columns="cols" clickable no-data-text="Noch kein Schülerbetrieb vorhanden.">
			<template #header(Anschreiben)>
				<svws-ui-tooltip>
					<i-ri-mail-send-line />
					<template #content>
						Erhält Anschreiben
					</template>
				</svws-ui-tooltip>
			</template>
			<template #body>
				<div class="svws-ui-tr" role="row" v-for="(betrieb, index) in listSchuelerbetriebe()" :key="betrieb.id" @click="select(betrieb)" :class="{'svws-clicked': clickedBetrieb ? clickedBetrieb === betrieb.id : index === 0}">
					<s-card-schueler-beschaeftigung-tabelle :betrieb="betrieb" :map-beschaeftigungsarten="mapBeschaeftigungsarten"
						:map-lehrer="mapLehrer" :map-betriebe="mapBetriebe" :map-ansprechpartner="getAnsprechpartnervonBetrieb(betrieb.betrieb_id)"
						:patch-schueler-betriebsdaten="patchSchuelerBetriebsdaten" />
				</div>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { BetriebAnsprechpartner, BetriebListeEintrag, KatalogEintrag, LehrerListeEintrag, List, SchuelerBetriebsdaten } from "@core";
	import type { DataTableColumn } from "@ui";
	import { computed, ComputedRef, onMounted, ref } from "vue";

	const props = defineProps<{
		patchSchuelerBetriebsdaten: (data : Partial<SchuelerBetriebsdaten>, id : number) => Promise<void>;
		setSchuelerBetrieb: (betrieb : SchuelerBetriebsdaten | undefined) => Promise<void>;
		listSchuelerbetriebe: () => List<SchuelerBetriebsdaten>;
		mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapBetriebe: Map<number, BetriebListeEintrag>;
		mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;

	}>();

	const clickedBetrieb = ref<number | undefined>(undefined);

	const cols: DataTableColumn[] = [
		{ key: "Betrieb", label: "Betrieb"},
		{ key: "Beschäftigungsart", label: "Beschäftigungsart"},
		{ key: "Beginn", label: "Beginn", span: 0.5},
		{ key: "Ende", label: "Ende", span: 0.5},
		{ key: "Praktikum", label: "Praktikum", span: 0.25, tooltip: 'Praktikum', align: "center"},
		{ key: "Betreuungslehrer", label: "Betreuungslehrer"},
		{ key: "Ansprechpartner", label: "Ansprechpartner"},
		{ key: "Ausbilder", label: "Sonstiger Betreuer"},
		{ key: "Anschreiben", label: "Anschreiben", tooltip: "Betrieb erhält Anschreiben", fixedWidth: 3, align: "center"}
	];

	async function select(betrieb : SchuelerBetriebsdaten) {
		await props.setSchuelerBetrieb(betrieb);
		clickedBetrieb.value = betrieb.id;
	}
	function getAnsprechpartnervonBetrieb ( id : number): Map<number, BetriebAnsprechpartner>{
		const t = new Map<number, BetriebAnsprechpartner>();
		for (let entry of props.mapAnsprechpartner.entries()) {
			let mapKey = entry[0];
			let mapValue = entry[1];
			if( mapValue.betrieb_id === id)
				t.set(mapKey,mapValue)
		}
		return t;
	}

</script>
