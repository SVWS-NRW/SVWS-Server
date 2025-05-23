<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="medium">
		<template #modalTitle>Schülerbetrieb hinzufügen</template>
		<template #modalDescription />
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2" class="mb-9">
				<svws-ui-select title="Betrieb" v-model="betrieb" :items="mapBetriebe" :item-text="(i: BetriebListeEintrag) => i.name1 ?? ''" class="col-span-full" />
				<svws-ui-text-input placeholder="Ausbilder" v-model="schuelerBetriebsdaten.ausbilder" type="text" />
				<svws-ui-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="mapBeschaeftigungsarten" :item-text="(i: KatalogEintrag) => i.text ?? ''" />
				<svws-ui-checkbox v-model="schuelerBetriebsdaten.praktikum"> Praktikum </svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Vertragsbeginn" v-model="schuelerBetriebsdaten.vertragsbeginn" type="date" statistics />
				<svws-ui-text-input placeholder="Vertragsende" v-model="schuelerBetriebsdaten.vertragsende" type="date" statistics />
				<svws-ui-select title="Betreuungslehrer" v-model="betreuungslehrer" :items="mapLehrer" :item-text="(i: LehrerListeEintrag) => i.nachname" />
				<svws-ui-select title="Ansprechpartner" removable :disabled="betrieb === undefined" v-model="ansprechpartner" :items="listAnpsrechpartner" :item-text="(i: BetriebAnsprechpartner) => i.name ?? ''" />
				<svws-ui-checkbox v-model="schuelerBetriebsdaten.allgadranschreiben"> Erhält Anschreiben </svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="save"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { BetriebAnsprechpartner, BetriebListeEintrag, KatalogEintrag, LehrerListeEintrag} from "@core";
	import { DeveloperNotificationException, SchuelerBetriebsdaten } from "@core";

	const props = defineProps<{
		createSchuelerBetriebsdaten: (data: SchuelerBetriebsdaten) => Promise<void>;
		idSchueler: number;
		mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapBetriebe: Map<number, BetriebListeEintrag>;
		mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
	}>();

	const show = ref<boolean>(false);

	const schuelerBetriebsdaten = ref<SchuelerBetriebsdaten>(new SchuelerBetriebsdaten());

	const betrieb = computed<BetriebListeEintrag | undefined>({
		get: () => props.mapBetriebe.get(schuelerBetriebsdaten.value.betrieb_id),
		set: (value) => {
			if (value === undefined)
				throw new DeveloperNotificationException("Ungültiger Betrieb ausgewählt");
			schuelerBetriebsdaten.value.betrieb_id = value.id;
			schuelerBetriebsdaten.value.ansprechpartner_id = null;
			for (const ap of props.mapAnsprechpartner.values()) {
				if (ap.betrieb_id === schuelerBetriebsdaten.value.betrieb_id) {
					schuelerBetriebsdaten.value.ansprechpartner_id = ap.id;
					break;
				}
			}
		}
	});

	const ansprechpartner = computed<BetriebAnsprechpartner | undefined>({
		get: () => (schuelerBetriebsdaten.value.ansprechpartner_id === null) ? undefined : props.mapAnsprechpartner.get(schuelerBetriebsdaten.value.ansprechpartner_id),
		set: (value) => schuelerBetriebsdaten.value.ansprechpartner_id = (value === undefined) ? null : value.id
	});

	const beschaeftigungsart = computed<KatalogEintrag | undefined>({
		get: () => (schuelerBetriebsdaten.value.beschaeftigungsart_id === null) ? undefined : props.mapBeschaeftigungsarten.get(schuelerBetriebsdaten.value.beschaeftigungsart_id),
		set: (value) => schuelerBetriebsdaten.value.beschaeftigungsart_id = (value === undefined) ? null : value.id
	})

	const betreuungslehrer = computed<LehrerListeEintrag | undefined>({
		get: () => (schuelerBetriebsdaten.value.betreuungslehrer_id === null) ? undefined : props.mapLehrer.get(schuelerBetriebsdaten.value.betreuungslehrer_id),
		set: (value) => schuelerBetriebsdaten.value.betreuungslehrer_id = (value === undefined) ? null : value.id
	});

	async function save() {
		schuelerBetriebsdaten.value.schueler_id = props.idSchueler;
		await props.createSchuelerBetriebsdaten(schuelerBetriebsdaten.value);
		show.value = false;
	}

	const openModal = () => {
		show.value = true;
	}

	const listAnpsrechpartner = computed<Map<number, BetriebAnsprechpartner>>(() => {
		const t = new Map();
		console.log(props.mapAnsprechpartner)
		for( const a of props.mapAnsprechpartner.entries()){
			console.log(a)
			if(betrieb.value?.id=== a[1].betrieb_id)
				t.set(a[0],a[1])
		}
		return t;
	});

</script>
