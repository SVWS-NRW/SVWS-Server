<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal" size="medium">
		<template #modalTitle>Schülerbetrieb hinzufügen</template>
		<template #modalDescription />
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2" class="mb-9">
				<svws-ui-multi-select title="Betrieb" v-model="betrieb" :items="mapBetriebe" :item-text="(i: BetriebListeEintrag) => i.name1 ?? ''" span="full" />
				<svws-ui-text-input placeholder="Ausbilder" v-model="schuelerBetriebsdaten.ausbilder" type="text" />
				<svws-ui-multi-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="mapBeschaeftigungsarten" :item-text="(i: KatalogEintrag) => i.text ?? ''" />
				<svws-ui-checkbox v-model="schuelerBetriebsdaten.praktikum"> Praktikum </svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Vertragsbeginn" v-model="schuelerBetriebsdaten.vertragsbeginn" type="date" />
				<svws-ui-text-input placeholder="Vertragsende" v-model="schuelerBetriebsdaten.vertragsende" type="date" />
				<svws-ui-multi-select title="Betreuungslehrer" v-model="betreuungslehrer" :items="mapLehrer" :item-text="(i: LehrerListeEintrag) => i.nachname" />
				<svws-ui-multi-select title="Ansprechpartner" v-model="ansprechpartner" :items="mapAnsprechpartner" :item-text="(i: BetriebAnsprechpartner) => i.name ?? ''" />
				<svws-ui-checkbox v-model="schuelerBetriebsdaten.allgadranschreiben"> Erhält Anschreiben </svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal()"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="save"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { BetriebAnsprechpartner, BetriebListeEintrag, KatalogEintrag, LehrerListeEintrag} from "@core";
	import { SchuelerBetriebsdaten } from "@core";
	import type { Ref, WritableComputedRef } from "vue";
	import { computed, ref } from "vue";

	const props = defineProps<{
		createSchuelerBetriebsdaten: (data: SchuelerBetriebsdaten) => Promise<void>;
		idSchueler: number;
		mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapBetriebe: Map<number, BetriebListeEintrag>;
		mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
	}>();

	const modal = ref();
	const schuelerBetriebsdaten : Ref<SchuelerBetriebsdaten> = ref(new SchuelerBetriebsdaten());

	const betrieb: WritableComputedRef<BetriebListeEintrag | undefined> = computed({
		get: () => {
			const betrieb = props.mapBetriebe.get(schuelerBetriebsdaten.value.betrieb_id);
			return betrieb === null ? undefined : betrieb;
		},
		set: (value) => {
			if (value === undefined)
				throw new Error("Ungültiger Betrieb ausgewählt");
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

	const ansprechpartner: WritableComputedRef<BetriebAnsprechpartner | undefined> = computed({
		get: () => (schuelerBetriebsdaten.value.ansprechpartner_id === null) ? undefined : props.mapAnsprechpartner.get(schuelerBetriebsdaten.value.ansprechpartner_id),
		set: (value) => schuelerBetriebsdaten.value.ansprechpartner_id = (value === undefined) ? null : value.id
	});

	const beschaeftigungsart: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => (schuelerBetriebsdaten.value.beschaeftigungsart_id === null) ? undefined : props.mapBeschaeftigungsarten.get(schuelerBetriebsdaten.value.beschaeftigungsart_id),
		set: (value) => schuelerBetriebsdaten.value.beschaeftigungsart_id = (value === undefined) ? null : value.id
	})

	const betreuungslehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => (schuelerBetriebsdaten.value.betreuungslehrer_id === null) ? undefined : props.mapLehrer.get(schuelerBetriebsdaten.value.betreuungslehrer_id),
		set: (value) => schuelerBetriebsdaten.value.betreuungslehrer_id = (value === undefined) ? null : value.id
	});

	async function save() {
		schuelerBetriebsdaten.value.schueler_id = props.idSchueler;
		if (schuelerBetriebsdaten.value.betrieb_id === undefined){
			alert("Es muss ein Betrieb ausgewählt werden.");
			return;
		}
		await props.createSchuelerBetriebsdaten(schuelerBetriebsdaten.value);
		modal.value.closeModal();
	}

	const openModal = () => {
		modal.value.openModal();
	}
</script>
