<template>
	<svws-ui-content-card :title="erzieher.vorname || erzieher.nachname ? `${erzieher.vorname ? erzieher.vorname + ' ' : '' }${erzieher.zusatzNachname ? erzieher.zusatzNachname + ' ' : ''}${erzieher.nachname}` : '(Ohne Namen)'">
		<template #actions>
			<svws-ui-checkbox :model-value="erzieher.erhaeltAnschreiben || undefined"
				@update:model-value="doPatch({ erhaeltAnschreiben: Boolean($event) }, erzieher.id)" class="mr-2">
				Erhält Anschreiben
			</svws-ui-checkbox>
			<svws-ui-button type="danger">
				<i-ri-delete-bin-line />
				Person löschen
			</svws-ui-button>
		</template>
		<svws-ui-input-wrapper :grid="2" class="input-wrapper--erziehungsberechtigte">
			<svws-ui-input-wrapper :grid="2" class="col-span-1">
				<svws-ui-multi-select title="Erzieherart" v-model="idErzieherArt" :items="mapErzieherarten"
					:item-sort="erzieherArtSort" :item-text="(i: Erzieherart) => i.bezeichnung ?? ''" />
				<svws-ui-text-input placeholder="Name" :model-value="erzieher.nachname || undefined"
					@update:model-value="doPatch({ nachname: String($event) }, erzieher.id)" type="text" />
				<svws-ui-text-input placeholder="Zusatz zum Nachnamen" :model-value="erzieher.zusatzNachname || undefined"
					@update:model-value="doPatch({ zusatzNachname: String($event) }, erzieher.id)" type="text" />
				<svws-ui-text-input placeholder="Vorname" :model-value="erzieher.vorname || undefined"
					@update:model-value="doPatch({ vorname: String($event) }, erzieher.id)" type="text" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="E-Mail Adresse" :model-value="erzieher.eMail || undefined"
					@update:model-value="doPatch({ eMail: String($event) }, erzieher.id)" type="email" verify-email />
				<svws-ui-multi-select title="1. Staatsangehörigkeit" v-model="staatsangehoerigkeit" :items="Nationalitaeten.values()"
					:item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
					:item-filter="staatsangehoerigkeitKatalogEintragFilter" autocomplete />
			</svws-ui-input-wrapper>
			<svws-ui-input-wrapper :grid="2" class="col-span-1">
				<svws-ui-text-input placeholder="Straße und Hausnummer" v-model="strassenname" type="text" />
				<svws-ui-text-input placeholder="Adresszusatz" v-model="hausnummerZusatz" type="text" />
				<svws-ui-multi-select title="Wohnort" v-model="wohnort" :items="mapOrte" :item-filter="orte_filter"
					:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
				<svws-ui-multi-select title="Ortsteil" v-model="ortsteil" :items="mapOrtsteile"
					:item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" :item-sort="ortsteilSort" :item-filter="ortsteilFilter" />
				<svws-ui-spacing />
				<svws-ui-textarea-input placeholder="Bemerkungen" v-model:value="bemerkungen" span="full" autoresize />
			</svws-ui-input-wrapper>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { Erzieherart, ErzieherStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@svws-nrw/svws-core";
	import { Nationalitaeten } from "@svws-nrw/svws-core";
	import type { WritableComputedRef } from "vue";
	import { computed, ComputedRef } from "vue";
	import { erzieherArtSort, staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort,
		orte_filter, orte_sort, ortsteilFilter, ortsteilSort } from "~/helfer";

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

	const strassenname: WritableComputedRef<string> = computed({
		get: () => props.erzieher.strassenname ?? "",
		set: (value) => void doPatch({ strassenname: value }, props.erzieher.id)
	});

	const hausnummerZusatz: WritableComputedRef<string> = computed({
		get: () => props.erzieher.hausnummerZusatz ?? "",
		set: (value) => void doPatch({ hausnummerZusatz: value }, props.erzieher.id)
	});

	const bemerkungen: WritableComputedRef<string> = computed({
		get: () => props.erzieher.bemerkungen ?? "",
		set: (value) => void doPatch({ bemerkungen: value }, props.erzieher.id)
	});

</script>

<style lang="postcss" scoped>
.input-wrapper--erziehungsberechtigte {
	@apply lg:gap-x-9 3xl:gap-x-12 4xl:gap-x-16 items-start;

	.col-span-1 {
		@apply max-lg:col-span-full;
	}
}
</style>
