<template>
	<svws-ui-content-card title="Beschäftigungen">
		<svws-ui-table :items="[]" :no-data="listSchuelerbetriebe().size() === 0" :columns clickable no-data-text="Noch kein Schülerbetrieb vorhanden.">
			<template #header(Anschreiben)>
				<svws-ui-tooltip>
					<span class="icon i-ri-mail-send-line" />
					<template #content>
						Erhält Anschreiben
					</template>
				</svws-ui-tooltip>
			</template>
			<template #body>
				<div class="svws-ui-tr" role="row" v-for="(betrieb, index) in listSchuelerbetriebe()" :key="betrieb.id" @click="select(betrieb)" :class="{'svws-clicked': clickedBetrieb ? clickedBetrieb === betrieb.id : index === 0}">
					<s-card-schueler-beschaeftigung-tabelle :betrieb :map-beschaeftigungsarten :map-lehrer :map-betriebe :map-ansprechpartner="getAnsprechpartnervonBetrieb(betrieb.betrieb_id)" :patch-schueler-betriebsdaten />
				</div>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { BetriebAnsprechpartner, BetriebListeEintrag, KatalogEintrag, LehrerListeEintrag, List, SchuelerBetriebsdaten } from "@core";
	import type { DataTableColumn } from "@ui";

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

	const columns: DataTableColumn[] = [
		{ key: "Betrieb", label: "Betrieb"},
		{ key: "Beschäftigungsart", label: "Beschäftigungsart"},
		{ key: "Beginn", label: "Beginn", span: 0.5, statistic: true },
		{ key: "Ende", label: "Ende", span: 0.5, statistic: true },
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
		for (const entry of props.mapAnsprechpartner.entries()) {
			const mapKey = entry[0];
			const mapValue = entry[1];
			if (mapValue.betrieb_id === id)
				t.set(mapKey,mapValue)
		}
		return t;
	}

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: repeat(2, minmax(4rem, 1fr)) repeat(2, minmax(4rem, 0.5fr)) minmax(4rem, 0.25fr) repeat(3, minmax(4rem, 1fr)) 3rem;
	}

</style>
