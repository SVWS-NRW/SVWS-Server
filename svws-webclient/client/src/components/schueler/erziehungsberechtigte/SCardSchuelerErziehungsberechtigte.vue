<template>
	<svws-ui-content-card :title="erzieher.vorname || erzieher.nachname ? `Daten zu ${erzieher.vorname ? erzieher.vorname + ' ' : '' }${erzieher.nachname}` : 'Daten zur Person'" class="col-span-full mt-16 lg:mt-20">
		<template #actions>
			<svws-ui-checkbox :model-value="erzieher.erhaeltAnschreiben === true" @update:model-value="patch({ erhaeltAnschreiben: Boolean($event) }, erzieher.id)" class="mr-2">
				Erhält Anschreiben
			</svws-ui-checkbox>
			<svws-ui-button type="danger">
				<i-ri-delete-bin-line />
				Person löschen
			</svws-ui-button>
		</template>
		<svws-ui-input-wrapper :grid="4">
			<svws-ui-select title="Erzieherart" v-model="idErzieherArt" :items="mapErzieherarten" :item-sort="erzieherArtSort" :item-text="(i: Erzieherart) => i.bezeichnung ?? ''" />
			<svws-ui-text-input placeholder="Name" :model-value="erzieher.nachname" @change="nachname=>patch({ nachname }, erzieher.id)" type="text" />
			<svws-ui-text-input placeholder="Rufname" :model-value="erzieher.vorname" @change="vorname=>patch({ vorname }, erzieher.id)" type="text" />
			<svws-ui-text-input placeholder="E-Mail Adresse" :model-value="erzieher.eMail" @change="eMail=>patch({ eMail }, erzieher.id)" type="email" verify-email />
			<svws-ui-select title="1. Staatsangehörigkeit" v-model="staatsangehoerigkeit" :items="Nationalitaeten.values()"
				:item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
				:item-filter="staatsangehoerigkeitKatalogEintragFilter" autocomplete />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Straße und Hausnummer" :model-value="erzieher.strassenname" @change="strassenname=>patch({ strassenname }, erzieher.id)" type="text" />
			<svws-ui-text-input placeholder="Adresszusatz" :model-value="erzieher.hausnummerZusatz" @change="hausnummerZusatz=>patch({ hausnummerZusatz }, props.erzieher.id)" type="text" />
			<svws-ui-select title="Wohnort" v-model="wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<svws-ui-select title="Ortsteil" v-model="ortsteil" :items="ortsteile" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" :item-sort="ortsteilSort" :item-filter="ortsteilFilter" removable />
			<svws-ui-spacing />
			<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="erzieher.bemerkungen" span="full" autoresize
				@change="bemerkungen => patch({ bemerkungen: bemerkungen === null ? '' : bemerkungen }, erzieher.id)" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { Erzieherart, ErzieherStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import { erzieherArtSort, staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort, orte_filter, orte_sort, ortsteilFilter, ortsteilSort } from "~/utils/helfer";
	import { Nationalitaeten } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		erzieher: ErzieherStammdaten;
		mapErzieherarten: Map<number, Erzieherart>;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
		patch: (data : Partial<ErzieherStammdaten>, id: number) => Promise<void>;
	}>();

	const wohnort = computed<OrtKatalogEintrag | undefined>({
		get: () => props.erzieher.wohnortID === null ? undefined : props.mapOrte.get(props.erzieher.wohnortID),
		set: (value) => void props.patch({ wohnortID: value === undefined ? null : value.id }, props.erzieher.id)
	});

	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result : Array<OrtsteilKatalogEintrag> = [];
		for (const ortsteil of props.mapOrtsteile.values())
			if ((ortsteil.ort_id === null) || (ortsteil.ort_id === props.erzieher.wohnortID))
				result.push(ortsteil);
		return result;
	});

	const ortsteil = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => props.erzieher.ortsteilID === null ? undefined : props.mapOrtsteile.get(props.erzieher.ortsteilID),
		set: (value) => void props.patch({ ortsteilID: value === undefined ? null : value.id }, props.erzieher.id)
	});

	const staatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(props.erzieher.staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ staatsangehoerigkeitID: value.daten.iso3 }, props.erzieher.id)
	});

	const idErzieherArt = computed<Erzieherart | undefined>({
		get: () => props.erzieher.idErzieherArt === null ? undefined : props.mapErzieherarten.get(props.erzieher.idErzieherArt),
		set: (value) => void props.patch({ idErzieherArt: value === undefined ? null : value.id }, props.erzieher.id)
	});

</script>
