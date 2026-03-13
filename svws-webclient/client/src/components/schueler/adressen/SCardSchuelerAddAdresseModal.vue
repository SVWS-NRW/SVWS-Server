<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="medium">
		<template #modalTitle>Schülerbetrieb hinzufügen</template>
		<template #modalDescription />
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2" class="mb-9">
				<svws-ui-select title="Betrieb" v-model="betrieb" :items="betriebeById" :item-text="(i: BetriebListeEintrag) => i.name1 ?? ''" class="col-span-full" />
				<svws-ui-text-input placeholder="Ausbilder" v-model="schuelerBetriebsdaten.nameAusbilder" type="text" />
				<svws-ui-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="beschaeftigungsartenById" :item-text="(i: Beschaeftigungsart) => i.bezeichnung ?? ''" />
				<svws-ui-checkbox v-model="schuelerBetriebsdaten.istPraktikum"> Praktikum </svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Vertragsbeginn" v-model="schuelerBetriebsdaten.vertragsbeginn" type="date" statistics />
				<svws-ui-text-input placeholder="Vertragsende" v-model="schuelerBetriebsdaten.vertragsende" type="date" statistics />
				<svws-ui-select title="Betreuungslehrer" v-model="betreuungslehrer" :items="mapLehrer" :item-text="(i: LehrerListeEintrag) => i.nachname" />
				<svws-ui-select title="Ansprechpartner" removable :disabled="betrieb === undefined" v-model="ansprechpartner" :items="listAnpsrechpartner" :item-text="(i: BetriebAnsprechpartner) => i.name ?? ''" />
				<svws-ui-checkbox v-model="schuelerBetriebsdaten.erhaeltAnschreiben"> Erhält Anschreiben </svws-ui-checkbox>
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
	import type { Beschaeftigungsart, BetriebAnsprechpartner, BetriebListeEintrag, LehrerListeEintrag } from "@core";
	import { DeveloperNotificationException, SchuelerBetriebe } from "@core";

	const props = defineProps<{
		createSchuelerBetriebsdaten: (data: SchuelerBetriebe) => Promise<void>;
		idSchueler: number;
		beschaeftigungsartenById: Map<number, Beschaeftigungsart>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		betriebeById: Map<number, BetriebListeEintrag>;
		mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
	}>();

	const show = ref<boolean>(false);

	const schuelerBetriebsdaten = ref<SchuelerBetriebe>(new SchuelerBetriebe());

	const betrieb = computed<BetriebListeEintrag | undefined>({
		get: () => props.betriebeById.get(schuelerBetriebsdaten.value.idBetrieb),
		set: (value) => {
			if (value === undefined) {
				throw new DeveloperNotificationException("Ungültiger Betrieb ausgewählt");
			}
			schuelerBetriebsdaten.value.idBetrieb = value.id;
			schuelerBetriebsdaten.value.idAnsprechpartner = null;
			for (const ap of props.mapAnsprechpartner.values()) {
				if (ap.betrieb_id === schuelerBetriebsdaten.value.idBetrieb) {
					schuelerBetriebsdaten.value.idAnsprechpartner = ap.id;
					break;
				}
			}
		},
	});

	const ansprechpartner = computed<BetriebAnsprechpartner | undefined>({
		get: () => (schuelerBetriebsdaten.value.idAnsprechpartner === null) ? undefined : props.mapAnsprechpartner.get(schuelerBetriebsdaten.value.idAnsprechpartner),
		set: (value) => schuelerBetriebsdaten.value.idAnsprechpartner = (value === undefined) ? null : value.id,
	});

	const beschaeftigungsart = computed<Beschaeftigungsart | undefined>({
		get: () => (schuelerBetriebsdaten.value.idBeschaeftigungsart === null) ? undefined : props.beschaeftigungsartenById.get(schuelerBetriebsdaten.value.idBeschaeftigungsart),
		set: (value) => schuelerBetriebsdaten.value.idBeschaeftigungsart = (value === undefined) ? null : value.id,
	});

	const betreuungslehrer = computed<LehrerListeEintrag | undefined>({
		get: () => (schuelerBetriebsdaten.value.idBetreuungslehrer === null) ? undefined : props.mapLehrer.get(schuelerBetriebsdaten.value.idBetreuungslehrer),
		set: (value) => schuelerBetriebsdaten.value.idBetreuungslehrer = (value === undefined) ? null : value.id,
	});

	async function save() {
		schuelerBetriebsdaten.value.idSchueler = props.idSchueler;
		await props.createSchuelerBetriebsdaten(schuelerBetriebsdaten.value);
		show.value = false;
	}

	const openModal = () => {
		show.value = true;
	};

	const listAnpsrechpartner = computed<Map<number, BetriebAnsprechpartner>>(() => {
		const t = new Map();
		console.log(props.mapAnsprechpartner);
		for (const a of props.mapAnsprechpartner.entries()) {
			console.log(a);
			if (betrieb.value?.id === a[1].betrieb_id) {
				t.set(a[0], a[1]);
			}
		}
		return t;
	});

</script>
