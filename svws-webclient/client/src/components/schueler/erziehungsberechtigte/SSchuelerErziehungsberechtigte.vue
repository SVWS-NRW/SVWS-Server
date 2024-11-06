<template>
	<div class="page--content">
		<svws-ui-content-card title="Erziehungsberechtigte" class="col-span-full">
			<svws-ui-table :items="data()" :columns :no-data="data().size() === 0" clickable :clicked="erzieher" @update:clicked="value => erzieher = value" focus-first-element>
				<template #header(anschreiben)>
					<svws-ui-tooltip>
						<span class="icon i-ri-mail-send-line" />
						<template #content>
							Erhält Anschreiben
						</template>
					</svws-ui-tooltip>
				</template>
				<template #cell(idErzieherArt)="{ value: idErzieherArt }">
					{{ idErzieherArt ? mapErzieherarten.get(idErzieherArt)?.bezeichnung : '' }}
				</template>
				<template #cell(name)="{ rowData }">
					{{ rowData.vorname }} {{ rowData.nachname }}
				</template>
				<template #cell(email)="{ value: eMail }">
					{{ eMail ? eMail : '–' }}
				</template>
				<template #cell(adresse)="{ rowData }">
					{{ strasse(rowData) }}{{ rowData.wohnortID && mapOrte?.get(rowData.wohnortID) ? `, ${mapOrte.get(rowData.wohnortID)?.plz} ${mapOrte?.get(rowData.wohnortID)?.ortsname}` : '' }}
				</template>
				<template #cell(anschreiben)="{ value: erhaeltAnschreiben }">
					{{ erhaeltAnschreiben ? '&check;' : '&times;' }}
				</template>
			</svws-ui-table>
			<!-- TODO: API erweitern <svws-ui-button class="mt-4" @click="hinzufuegen">Person hinzufügen</svws-ui-button> -->
			<s-card-schueler-erziehungsberechtigte v-if="erzieher" :erzieher :patch :map-erzieherarten :map-orte :map-ortsteile />
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { ref } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { SchuelerErziehungsberechtigteProps } from "./SSchuelerErziehungsberechtigteProps";
	import { AdressenUtils, type ErzieherStammdaten } from "@core";

	const props = defineProps<SchuelerErziehungsberechtigteProps>();

	const erzieher = ref<ErzieherStammdaten | undefined>();

	async function hinzufuegen() {
		const neu = await props.add();
		erzieher.value = neu;
	}

	function strasse(erzieher: ErzieherStammdaten) {
		return AdressenUtils.combineStrasse(erzieher.strassenname ?? "", erzieher.hausnummer ?? "", erzieher.hausnummerZusatz ?? "");
	}

	const columns: DataTableColumn[] = [
		{ key: "idErzieherArt", label: "Art",},
		{ key: "name", label: "Name"},
		{ key: "email", label: "E-Mail"},
		{ key: "adresse", label: "Adresse"},
		{ key: "anschreiben", label: "Anschreiben", tooltip: "Erhält Anschreiben", fixedWidth: 3, align: "center"},
	];

</script>
