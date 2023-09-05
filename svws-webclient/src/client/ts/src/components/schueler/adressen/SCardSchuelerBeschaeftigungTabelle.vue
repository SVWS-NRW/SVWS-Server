<template>
	<svws-ui-table-cell no-padding>
		<svws-ui-multi-select title="—" headless v-model="inputBetrieb" :items="mapBetriebe" :item-text="(i: BetriebListeEintrag) => i.name1 ?? ''" />
	</svws-ui-table-cell>
	<svws-ui-table-cell>
		<svws-ui-text-input :model-value="betrieb.ausbilder" @change="ausbilder=>patchSchuelerBetriebsdaten({ausbilder}, betrieb.id)" type="text" placeholder="—" headless />
	</svws-ui-table-cell>
	<svws-ui-table-cell no-padding>
		<svws-ui-multi-select v-model="beschaeftigungsart" :items="mapBeschaeftigungsarten" :item-text="(i: KatalogEintrag) => i.text ?? ''" headless title="—" />
	</svws-ui-table-cell>
	<svws-ui-table-cell no-padding>
		<svws-ui-text-input :model-value="betrieb.vertragsbeginn" @change="vertragsbeginn=>patchSchuelerBetriebsdaten({vertragsbeginn}, betrieb.id)" type="date" />
	</svws-ui-table-cell>
	<svws-ui-table-cell no-padding>
		<svws-ui-text-input :model-value="betrieb.vertragsende" @change="vertragsende=>patchSchuelerBetriebsdaten({vertragsende}, betrieb.id)" type="date" />
	</svws-ui-table-cell>
	<svws-ui-table-cell align="center">
		<svws-ui-checkbox v-model="praktikum" />
	</svws-ui-table-cell>
	<svws-ui-table-cell no-padding>
		<svws-ui-multi-select v-model="inputBetreuungslehrer" :items="mapLehrer" :item-text="(i: LehrerListeEintrag) => i.nachname" headless title="—" />
	</svws-ui-table-cell>
	<svws-ui-table-cell no-padding>
		<svws-ui-multi-select v-model="ansprechpartner" :items="mapAnsprechpartner" :item-text="(i: BetriebAnsprechpartner) => i.name || ''" headless title="—" />
	</svws-ui-table-cell>
	<svws-ui-table-cell align="center">
		<svws-ui-checkbox v-model="anschreiben" />
	</svws-ui-table-cell>
</template>

<script setup lang="ts">

	import type { BetriebAnsprechpartner, BetriebListeEintrag, KatalogEintrag, LehrerListeEintrag, SchuelerBetriebsdaten } from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		patchSchuelerBetriebsdaten: (data : Partial<SchuelerBetriebsdaten>, id : number) => Promise<void>;
		betrieb: SchuelerBetriebsdaten;
		mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapBetriebe: Map<number, BetriebListeEintrag>;
		mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
	}>();

	const inputBetreuungslehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => props.betrieb.betreuungslehrer_id === null ? undefined : props.mapLehrer.get(props.betrieb.betreuungslehrer_id),
		set: (value) => void props.patchSchuelerBetriebsdaten({ betreuungslehrer_id: value === undefined ? null : value.id }, props.betrieb.id)
	});

	const inputBetrieb: WritableComputedRef<BetriebListeEintrag | undefined> = computed({
		get: () => props.mapBetriebe.get(props.betrieb.betrieb_id),
		set: (value) => {
			if (value !== undefined)
				void props.patchSchuelerBetriebsdaten({ betrieb_id: value.id }, props.betrieb.id);
		}
	});

	const beschaeftigungsart: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => (props.betrieb.beschaeftigungsart_id === null) ? undefined : props.mapBeschaeftigungsarten.get(props.betrieb.beschaeftigungsart_id),
		set: (value) => void props.patchSchuelerBetriebsdaten({ beschaeftigungsart_id: value === undefined ? null : value.id }, props.betrieb.id)
	});

	const praktikum: WritableComputedRef<boolean> = computed({
		get: () => props.betrieb.praktikum === null ? false : props.betrieb.praktikum,
		set: (value) => void props.patchSchuelerBetriebsdaten({ praktikum: value }, props.betrieb.id)
	});

	const anschreiben: WritableComputedRef<boolean> = computed({
		get: () => props.betrieb.allgadranschreiben === null ? false : props.betrieb.allgadranschreiben,
		set: (value) => void props.patchSchuelerBetriebsdaten({ allgadranschreiben: value }, props.betrieb.id)
	});

	const ansprechpartner: WritableComputedRef<BetriebAnsprechpartner | undefined> = computed({
		get: () => props.betrieb.ansprechpartner_id === null ? undefined : props.mapAnsprechpartner.get(props.betrieb.ansprechpartner_id),
		set: (value) => {
			void props.patchSchuelerBetriebsdaten({ ansprechpartner_id: value === undefined ? null : value.id }, props.betrieb.id);
		}
	});

</script>

