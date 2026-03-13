<template>
	<div class="svws-ui-td" role="cell">
		<svws-ui-select title="—" headless v-model="inputBetrieb" :items="betriebeById" :item-text="(i: BetriebListeEintrag) => i.name1 ?? ''" />
	</div>
	<div class="svws-ui-td" role="cell">
		<svws-ui-select v-model="beschaeftigungsart" :items="beschaeftigungsartenById" :item-text="(i: Beschaeftigungsart) => i.bezeichnung ?? ''" headless title="—" />
	</div>
	<div class="svws-ui-td" role="cell">
		<svws-ui-text-input :model-value="betrieb.vertragsbeginn" @change="vertragsbeginn=>patchSchuelerBetriebsdaten({vertragsbeginn}, betrieb.id)" type="date" />
	</div>
	<div class="svws-ui-td" role="cell">
		<svws-ui-text-input :model-value="betrieb.vertragsende" @change="vertragsende=>patchSchuelerBetriebsdaten({vertragsende}, betrieb.id)" type="date" />
	</div>
	<div class="svws-ui-td svws-align-center" role="cell">
		<svws-ui-checkbox v-model="praktikum" />
	</div>
	<div class="svws-ui-td" role="cell">
		<svws-ui-select v-model="inputBetreuungslehrer" :items="mapLehrer" :item-text="(i: LehrerListeEintrag) => i.nachname" headless title="—" />
	</div>
	<div class="svws-ui-td" role="cell">
		<svws-ui-select :model-value="betrieb.idAnsprechpartner === null ? undefined : mapAnsprechpartner.get(betrieb.idAnsprechpartner)"
			@change="(ansprechPartner : BetriebAnsprechpartner) => patchSchuelerBetriebsdaten({ idAnsprechpartner: ansprechPartner === undefined ? null : ansprechPartner.id }, betrieb.id)"
			:items="mapAnsprechpartner"
			:item-text="(i: BetriebAnsprechpartner) => i.name || ''" headless title="—" />
	</div>
	<div class="svws-ui-td" role="cell">
		<svws-ui-text-input :model-value="betrieb.nameAusbilder" @change="nameAusbilder=>patchSchuelerBetriebsdaten({nameAusbilder}, betrieb.id)" type="text" placeholder="—" headless />
	</div>
	<div class="svws-ui-td svws-align-center" role="cell">
		<svws-ui-checkbox v-model="anschreiben" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { Beschaeftigungsart, BetriebAnsprechpartner, BetriebListeEintrag, LehrerListeEintrag, SchuelerBetriebe } from "@core";

	const props = defineProps<{
		patchSchuelerBetriebsdaten: (data: Partial<SchuelerBetriebe>, id: number) => Promise<void>;
		betrieb: SchuelerBetriebe;
		beschaeftigungsartenById: Map<number, Beschaeftigungsart>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		betriebeById: Map<number, BetriebListeEintrag>;
		mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
	}>();

	const inputBetreuungslehrer = computed<LehrerListeEintrag | undefined>({
		get: () => props.betrieb.idBetreuungslehrer === null ? undefined : props.mapLehrer.get(props.betrieb.idBetreuungslehrer),
		set: (value) => void props.patchSchuelerBetriebsdaten({ idBetreuungslehrer: value === undefined ? null : value.id }, props.betrieb.id),
	});

	const inputBetrieb = computed<BetriebListeEintrag | undefined>({
		get: () => props.betriebeById.get(props.betrieb.idBetrieb),
		set: (value) => {
			if (value !== undefined) {
				void props.patchSchuelerBetriebsdaten({ idBetrieb: value.id, idAnsprechpartner: null }, props.betrieb.id);
			}
		},
	});

	const beschaeftigungsart = computed<Beschaeftigungsart | undefined>({
		get: () => (props.betrieb.idBeschaeftigungsart === null) ? undefined : props.beschaeftigungsartenById.get(props.betrieb.idBeschaeftigungsart),
		set: (value) => void props.patchSchuelerBetriebsdaten({ idBeschaeftigungsart: value === undefined ? null : value.id }, props.betrieb.id),
	});

	const praktikum = computed<boolean>({
		get: () => props.betrieb.istPraktikum,
		set: (value) => void props.patchSchuelerBetriebsdaten({ istPraktikum: value }, props.betrieb.id),
	});

	const anschreiben = computed<boolean>({
		get: () => props.betrieb.erhaeltAnschreiben,
		set: (value) => void props.patchSchuelerBetriebsdaten({ erhaeltAnschreiben: value }, props.betrieb.id),
	});

	const ansprechpartner = computed<BetriebAnsprechpartner | undefined>({
		get: () => props.betrieb.idAnsprechpartner === null ? undefined : props.mapAnsprechpartner.get(props.betrieb.idAnsprechpartner),
		set: (value) => {
			void props.patchSchuelerBetriebsdaten({ idAnsprechpartner: value === undefined ? null : value.id }, props.betrieb.id);
		},
	});

</script>

