<template>
	<div class="page--content">
		<svws-ui-content-card title="Erziehungsberechtigte" class="col-span-full">
			<template v-if="data.size() > 0">
				<svws-ui-data-table :items="[]" :columns="cols" :no-data="false" clickable>
					<template #header(anschreiben)>
						<svws-ui-tooltip>
							<i-ri-mail-send-line />
							<template #content>
								Erhält Anschreiben
							</template>
						</svws-ui-tooltip>
					</template>
					<template #body>
						<svws-ui-table-row v-for="(e, i) in data" :key="i" @click="select(e.id)" :clicked="clickedErzieher ? clickedErzieher === e.id : i === 0">
							<svws-ui-table-cell>
								{{ e.idErzieherArt ? mapErzieherarten.get(e.idErzieherArt)?.bezeichnung : '' }}
							</svws-ui-table-cell>
							<svws-ui-table-cell>
								{{ e.vorname }} {{ e.nachname }}
							</svws-ui-table-cell>
							<svws-ui-table-cell>
								{{ e.eMail }}
							</svws-ui-table-cell>
							<svws-ui-table-cell>
								{{ e.strassenname }}{{ e.wohnortID && mapOrte?.get(e.wohnortID) ? `, ${mapOrte.get(e.wohnortID)?.plz} ${mapOrte?.get(e.wohnortID)?.ortsname}` : '' }}
							</svws-ui-table-cell>
							<svws-ui-table-cell align="center">
								<i-ri-check-fill v-if="e.erhaeltAnschreiben" />
								<i-ri-close-line v-else />
							</svws-ui-table-cell>
						</svws-ui-table-row>
					</template>
				</svws-ui-data-table>
			</template>
			<template v-else>
				<svws-ui-data-table :items="[]" :columns="cols" no-data-html="Noch keine Einträge vorhanden.">
					<template #header(anschreiben)>
						<svws-ui-tooltip>
							<i-ri-mail-send-line />
							<template #content>
								Erhält Anschreiben
							</template>
						</svws-ui-tooltip>
					</template>
				</svws-ui-data-table>
			</template>
			<svws-ui-button class="mt-4">Person hinzufügen</svws-ui-button>
			<template v-for="(e, i) in data" :key="i">
				<s-card-schueler-erziehungsberechtigte :erzieher="e" @patch="patch" :map-erzieherarten="mapErzieherarten" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile" v-if="clickedErzieher ? clickedErzieher === e.id : i === 0" />
			</template>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { SchuelerErziehungsberechtigteProps } from "./SSchuelerErziehungsberechtigteProps";
	import type { DataTableColumn } from "@ui";
	import { ref } from "vue";

	const props = defineProps<SchuelerErziehungsberechtigteProps>();

	const clickedErzieher = ref<number | undefined>(undefined);

	const cols: Array<DataTableColumn> = [
		{ key: "erzieherart", label: "Art", span: 0.5},
		{ key: "name", label: "Name"},
		{ key: "email", label: "E-Mail"},
		{ key: "adresse", label: "Adresse"},
		{ key: "anschreiben", label: "Anschreiben", tooltip: "Erhält Anschreiben", fixedWidth: 3, align: "center"},
	];

	async function select(erzieher: number) {
		clickedErzieher.value = erzieher;
	}

</script>
