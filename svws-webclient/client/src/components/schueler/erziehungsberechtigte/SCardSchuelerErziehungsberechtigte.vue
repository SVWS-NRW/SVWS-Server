<template>
	<svws-ui-content-card :title="erzieher.vorname || erzieher.nachname ? `Daten zu ${erzieher.vorname ? erzieher.vorname + ' ' : '' }${erzieher.nachname}` : 'Daten zur Person'" class="col-span-full mt-16 lg:mt-20">
		<template #actions>
			<svws-ui-checkbox :model-value="erzieher.erhaeltAnschreiben || undefined" @update:model-value="doPatch({ erhaeltAnschreiben: Boolean($event) }, erzieher.id)" class="mr-2">
				Erhält Anschreiben
			</svws-ui-checkbox>
			<svws-ui-button type="danger">
				<i-ri-delete-bin-line />
				Person löschen
			</svws-ui-button>
		</template>
		<svws-ui-input-wrapper :grid="4">
			<svws-ui-select title="Erzieherart" v-model="idErzieherArt" :items="mapErzieherarten" :item-sort="erzieherArtSort" :item-text="(i: Erzieherart) => i.bezeichnung ?? ''" />
			<svws-ui-text-input placeholder="Name" :model-value="erzieher.nachname" @change="nachname=>doPatch({ nachname }, erzieher.id)" type="text" />
			<svws-ui-text-input placeholder="Rufname" :model-value="erzieher.vorname" @change="vorname=>doPatch({ vorname }, erzieher.id)" type="text" />
			<svws-ui-text-input placeholder="E-Mail Adresse" :model-value="erzieher.eMail" @change="eMail=>doPatch({ eMail }, erzieher.id)" type="email" verify-email />
			<svws-ui-select title="1. Staatsangehörigkeit" v-model="staatsangehoerigkeit" :items="Nationalitaeten.values()"
				:item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
				:item-filter="staatsangehoerigkeitKatalogEintragFilter" autocomplete />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Straße und Hausnummer" :model-value="erzieher.strassenname" @change="strassenname=>doPatch({ strassenname }, erzieher.id)" type="text" />
			<svws-ui-text-input placeholder="Adresszusatz" :model-value="erzieher.hausnummerZusatz" @change="hausnummerZusatz=>doPatch({ hausnummerZusatz }, props.erzieher.id)" type="text" />
			<svws-ui-select title="Wohnort" v-model="wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<svws-ui-select title="Ortsteil" v-model="ortsteil" :items="mapOrtsteile" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" :item-sort="ortsteilSort" :item-filter="ortsteilFilter" />
			<svws-ui-spacing />
			<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="erzieher.bemerkungen" span="full" autoresize
				@change="bemerkungen => doPatch({ bemerkungen: bemerkungen === null ? '' : bemerkungen }, erzieher.id)" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { Erzieherart, ErzieherStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import type { WritableComputedRef } from "vue";
	import { erzieherArtSort, staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort, orte_filter, orte_sort, ortsteilFilter, ortsteilSort } from "~/utils/helfer";
	import { Nationalitaeten } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		erzieher: ErzieherStammdaten;
		mapErzieherarten: Map<number, Erzieherart>;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<ErzieherStammdaten>, id: number) : void;
	}>()

	function doPatch(data: Partial<ErzieherStammdaten>, id: number) {
		emit('patch', data, id);
	}

	const wohnort: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get: () => props.erzieher.wohnortID === null ? undefined : props.mapOrte.get(props.erzieher.wohnortID),
		set: (value) => void doPatch({ wohnortID: value === undefined ? null : value.id }, props.erzieher.id)
	});

	const ortsteil: WritableComputedRef<OrtsteilKatalogEintrag | undefined> = computed({
		get: () => props.erzieher.ortsteilID === null ? undefined : props.mapOrtsteile.get(props.erzieher.ortsteilID),
		set: (value) => void doPatch({ ortsteilID: value === undefined ? null : value.id }, props.erzieher.id)
	});

	const staatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.erzieher.staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => void doPatch({ staatsangehoerigkeitID: value.daten.iso3 }, props.erzieher.id)
	});

	const idErzieherArt: WritableComputedRef<Erzieherart | undefined> = computed({
		get: () => props.erzieher.idErzieherArt === null ? undefined : props.mapErzieherarten.get(props.erzieher.idErzieherArt),
		set: (value) => void doPatch({ idErzieherArt: value === undefined ? null : value.id }, props.erzieher.id)
	});

</script>
