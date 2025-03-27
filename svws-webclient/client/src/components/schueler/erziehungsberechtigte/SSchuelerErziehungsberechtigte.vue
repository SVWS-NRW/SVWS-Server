<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Erziehungsberechtigte" class="col-span-full">
			<svws-ui-table class="contentFocusField" :items="data()" :columns :no-data="data().size() === 0" clickable :clicked="erzieher" @update:clicked="value => erzieher = value" focus-first-element>
				<template #header(anschreiben)>
					<svws-ui-tooltip>
						<span class="icon i-ri-mail-send-line" />
						<template #content>
							Erhält Anschreiben
						</template>
					</svws-ui-tooltip>
				</template>
				<template #cell(idErzieherArt)="{ value }">
					{{ idErzieherArt ? mapErzieherarten.get(value)?.bezeichnung : '' }}
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
			<svws-ui-content-card v-if="erzieher !== undefined" :title="(erzieher.vorname !== null) || (erzieher.nachname !== null) ? `Daten zu ${erzieher.vorname ? erzieher.vorname + ' ' : '' }${erzieher.nachname}` : 'Daten zur Person'" class="col-span-full mt-16 lg:mt-20">
				<template #actions>
					<svws-ui-checkbox :model-value="erzieher.erhaeltAnschreiben === true" @update:model-value="erhaeltAnschreiben => erzieher !== undefined && patch({ erhaeltAnschreiben }, erzieher.id)" class="mr-2">
						Erhält Anschreiben
					</svws-ui-checkbox>
					<!-- TOS: Api erweitern <svws-ui-button type="danger">
				<span class="icon i-ri-delete-bin-line" />
				Person löschen
			</svws-ui-button> -->
				</template>
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-select title="Erzieherart" v-model="idErzieherArt" :items="mapErzieherarten" :item-sort="erzieherArtSort" :item-text="i => i.bezeichnung ?? ''" />
					<svws-ui-text-input placeholder="Name" :model-value="erzieher.nachname" @change="nachname=>erzieher !== undefined && patch({ nachname }, erzieher.id)" type="text" />
					<svws-ui-text-input placeholder="Vorname" :model-value="erzieher.vorname" @change="vorname=>erzieher !== undefined && patch({ vorname }, erzieher.id)" type="text" />
					<svws-ui-text-input placeholder="E-Mail Adresse" :model-value="erzieher.eMail" @change="eMail=>erzieher !== undefined && patch({ eMail }, erzieher.id)" type="email" verify-email />
					<svws-ui-select title="Staatsangehörigkeit" v-model="staatsangehoerigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" autocomplete />
					<svws-ui-spacing />
					<svws-ui-text-input placeholder="Straße und Hausnummer" :model-value="strasse(erzieher)" @change="patchStrasse" type="text" />
					<svws-ui-select title="Wohnort" v-model="wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="i => `${i.plz} ${i.ortsname}`" autocomplete />
					<svws-ui-select title="Ortsteil" v-model="ortsteil" :items="ortsteile" :item-text="i => i.ortsteil ?? ''" :item-sort="ortsteilSort" :item-filter="ortsteilFilter" removable />
					<svws-ui-spacing />
					<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="erzieher.bemerkungen" span="full" autoresize
						@change="bemerkungen => erzieher !== undefined && patch({ bemerkungen: bemerkungen === null ? '' : bemerkungen }, erzieher.id)" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { SchuelerErziehungsberechtigteProps } from "./SSchuelerErziehungsberechtigteProps";
	import type { Erzieherart, OrtKatalogEintrag, OrtsteilKatalogEintrag} from "@core";
	import { AdressenUtils, Nationalitaeten, type ErzieherStammdaten } from "@core";
	import { erzieherArtSort, staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort, orte_filter, orte_sort, ortsteilFilter, ortsteilSort } from "~/utils/helfer";

	const props = defineProps<SchuelerErziehungsberechtigteProps>();

	const erzieher = ref<ErzieherStammdaten | undefined>();

	watch(() => props.data(), (neu) => {
		if (neu.isEmpty())
			erzieher.value = undefined;
		else
			erzieher.value = neu.getFirst();
	}, { immediate: true });

	const columns: DataTableColumn[] = [
		{ key: "idErzieherArt", label: "Art"},
		{ key: "name", label: "Name"},
		{ key: "email", label: "E-Mail"},
		{ key: "adresse", label: "Adresse"},
		{ key: "anschreiben", label: "Anschreiben", tooltip: "Erhält Anschreiben", fixedWidth: 3, align: "center"},
	];

	function strasse(erzieher: ErzieherStammdaten) {
		return AdressenUtils.combineStrasse(erzieher.strassenname ?? "", erzieher.hausnummer ?? "", erzieher.hausnummerZusatz ?? "");
	}

	function patchStrasse(value: string | null) {
		if ((value !== null) && (erzieher.value !== undefined)) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patch({ strassenname: vals[0], hausnummer: vals[1], hausnummerZusatz: vals[2] }, erzieher.value.id);
		}
	}

	const wohnort = computed<OrtKatalogEintrag | undefined>({
		get: () => ((erzieher.value === undefined) || (erzieher.value.wohnortID === null)) ? undefined : props.mapOrte.get(erzieher.value.wohnortID),
		set: (value) => (erzieher.value !== undefined) && void props.patch({ wohnortID: value === undefined ? null : value.id }, erzieher.value.id),
	});

	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result : Array<OrtsteilKatalogEintrag> = [];
		for (const ortsteil of props.mapOrtsteile.values())
			if ((ortsteil.ort_id === null) || (ortsteil.ort_id === erzieher.value?.wohnortID))
				result.push(ortsteil);
		return result;
	});

	const ortsteil = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => ((erzieher.value === undefined) || (erzieher.value.ortsteilID === null)) ? undefined : props.mapOrtsteile.get(erzieher.value.ortsteilID),
		set: (value) => (erzieher.value !== undefined) && void props.patch({ ortsteilID: value === undefined ? null : value.id }, erzieher.value.id),
	});

	const staatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => ((erzieher.value !== undefined) && Nationalitaeten.getByISO3(erzieher.value.staatsangehoerigkeitID)) || Nationalitaeten.getDEU(),
		set: (value) => (erzieher.value !== undefined) && void props.patch({ staatsangehoerigkeitID: value.historie().getLast().iso3 }, erzieher.value.id),
	});

	const idErzieherArt = computed<Erzieherart | undefined>({
		get: () => ((erzieher.value === undefined) || (erzieher.value.idErzieherArt === null)) ? undefined : props.mapErzieherarten.get(erzieher.value.idErzieherArt),
		set: (value) => (erzieher.value !== undefined) && void props.patch({ idErzieherArt: value === undefined ? null : value.id }, erzieher.value.id),
	});

</script>
