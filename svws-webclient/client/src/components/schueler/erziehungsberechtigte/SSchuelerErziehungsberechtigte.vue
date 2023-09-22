<template>
	<div class="page--content">
		<svws-ui-content-card title="Erziehungsberechtigte" class="col-span-full">
			<svws-ui-table :items="[]" :columns="cols" :no-data="data.size() === 0" clickable no-data-html="Noch keine Einträge vorhanden.">
				<template #header(anschreiben)>
					<svws-ui-tooltip>
						<i-ri-mail-send-line />
						<template #content>
							Erhält Anschreiben
						</template>
					</svws-ui-tooltip>
				</template>
				<template #body>
					<div class="svws-ui-tr" role="row" v-for="(e, i) in data" :key="i" @click="select(clickedErzieher === e.id ? undefined : e.id)" :class="{'svws-clicked': clickedErzieher === e.id}">
						<div class="svws-ui-td" role="cell">
							{{ e.idErzieherArt ? mapErzieherarten.get(e.idErzieherArt)?.bezeichnung : '' }}
						</div>
						<div class="svws-ui-td" role="cell">
							{{ e.vorname }} {{ e.nachname }}
						</div>
						<div class="svws-ui-td" role="cell">
							<span v-if="e.eMail?.length">{{ e.eMail }}</span>
							<span v-else class="opacity-25">–</span>
						</div>
						<div class="svws-ui-td" role="cell">
							{{ e.strassenname }}{{ e.wohnortID && mapOrte?.get(e.wohnortID) ? `, ${mapOrte.get(e.wohnortID)?.plz} ${mapOrte?.get(e.wohnortID)?.ortsname}` : '' }}
						</div>
						<div class="svws-ui-td svws-align-center" role="cell">
							<span v-if="e.erhaeltAnschreiben">&check;</span>
							<span v-else>&times;</span>
						</div>
					</div>
				</template>
			</svws-ui-table>
			<svws-ui-button class="mt-4">Person hinzufügen</svws-ui-button>
			<template v-for="(e, i) in data" :key="i">
				<s-card-schueler-erziehungsberechtigte :erzieher="e" @patch="patch" :map-erzieherarten="mapErzieherarten" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile" v-if="clickedErzieher === e.id" />
			</template>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { DataTableColumn } from "@ui";
	import type { SchuelerErziehungsberechtigteProps } from "./SSchuelerErziehungsberechtigteProps";
	import { ref } from "vue";

	const props = defineProps<SchuelerErziehungsberechtigteProps>();

	const clickedErzieher = ref<number | undefined>(undefined);

	const cols: DataTableColumn[] = [
		{ key: "erzieherart", label: "Art", span: 0.5},
		{ key: "name", label: "Name"},
		{ key: "email", label: "E-Mail"},
		{ key: "adresse", label: "Adresse"},
		{ key: "anschreiben", label: "Anschreiben", tooltip: "Erhält Anschreiben", fixedWidth: 3, align: "center"},
	];

	async function select(erzieher: number | undefined) {
		clickedErzieher.value = erzieher;
	}

</script>
