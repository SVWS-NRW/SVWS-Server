<template>
	<svws-ui-sub-nav>
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" />
		<svws-ui-button type="primary" @click="erzeugeKursklausurenAusVorgaben(quartalsauswahl.value)">Erstelle Klausuren</svws-ui-button>
		<svws-ui-button type="secondary" @click="erzeugeKlausurtermin(quartalsauswahl.value)" :disabled="quartalsauswahl.value <= 0">Neuer Termin</svws-ui-button>
		<svws-ui-button type="secondary" @click="modal.openModal()" :disabled="termine.size() > 0"><svws-ui-spinner :spinning="loading" /> Automatisch blocken</svws-ui-button>
		<svws-ui-button type="danger" @click="loescheTermine" :disabled="termine.size() === 0">Alle Termine löschen</svws-ui-button>
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-schienen-hilfe /> </svws-ui-modal-hilfe>
	</svws-ui-sub-nav>

	<div>
		<svws-ui-modal ref="modal" size="small">
			<template #modalTitle>
				Automatisch Blocken
			</template>
			<template #modalDescription>
				Hier können Sie weitere Einstellungen vornehmen:
			</template>
			<template #modalContent>
				<svws-ui-radio-group :row="true" class="justify-center">
					<svws-ui-radio-option v-for="a in KlausurterminblockungAlgorithmen.values()" :key="a.id" :value="a" v-model="algMode" :name="a.bezeichnung" :label="a.bezeichnung" />
				</svws-ui-radio-group>
				<svws-ui-radio-group :row="true" class="justify-center">
					<svws-ui-radio-option v-for="k in KlausurterminblockungModusKursarten.values()" :key="k.id" :value="k" v-model="lkgkMode" :name="k.bezeichnung" :label="k.bezeichnung" />
				</svws-ui-radio-group>
				<svws-ui-checkbox v-model="blockeGleicheLehrkraft" v-if="algMode === KlausurterminblockungAlgorithmen.NORMAL">
					Falls gleiche Lehrkraft, Fach und Kursart, dann gleicher Termin?
				</svws-ui-checkbox>
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="blocken"> Blocken </svws-ui-button>
				<svws-ui-button type="secondary" @click="modal.closeModal()"> Abbrechen </svws-ui-button>
			</template>
		</svws-ui-modal>

		<svws-ui-content-card class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
			<div class="flex flex-row gap-8 mt-4">
				<div class="flex flex-col">
					<div class="text-headline-md">Zu verplanen:</div>
					<s-gost-klausurplanung-schienen-termin :quartal="quartalsauswahl.value"
						:jahrgangsdaten="jahrgangsdaten"
						:kursklausurmanager="kursklausurmanager"
						:termin="null"
						:alle-termine="termine"
						:faecher-manager="faecherManager"
						:map-lehrer="mapLehrer"
						:set-termin-to-kursklausur="setTerminToKursklausur"
						:map-schueler="mapSchueler"
						@drag-start-klausur="dragStartKlausur"
						@drag-end-klausur="dragEndKlausur"
						:kursmanager="kursmanager" />
				</div>
				<div class="flex flex-col">
					<div class="flex flex-row flex-wrap gap-4 items-start">
						<s-gost-klausurplanung-schienen-termin v-for="termin of termine" :key="termin.id"
							:jahrgangsdaten="jahrgangsdaten"
							:class="dropOverCssClasses(termin)"
							:kursklausurmanager="kursklausurmanager"
							:termin="termin"
							:alle-termine="termine"
							:faecher-manager="faecherManager"
							:map-lehrer="mapLehrer"
							:set-termin-to-kursklausur="setTerminToKursklausur"
							:drag-klausur="dragKlausur"
							:map-schueler="mapSchueler"
							:loesche-klausurtermine="loescheKlausurtermine"
							:patch-klausurtermin-datum="patchKlausurterminDatum"
							@drag-start-klausur="dragStartKlausur"
							@drag-end-klausur="dragEndKlausur"
							:kursmanager="kursmanager" />
					</div>
				</div>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { GostKursklausur, GostKlausurtermin } from "@core";
	import { KlausurterminblockungAlgorithmen, KlausurterminblockungAlgorithmus, KlausurterminblockungAlgorithmusConfig,
		KlausurterminblockungModusKursarten, KlausurterminblockungModusQuartale } from "@core";
	import { computed, ref } from 'vue';
	import type { GostKlausurplanungSchienenProps } from './SGostKlausurplanungSchienenProps';

	const modal = ref<any>(null);

	const props = defineProps<GostKlausurplanungSchienenProps>();

	const loading = ref<boolean>(false);

	const dragKlausur = ref<GostKursklausur | null>(null);

	const dragStartKlausur = (e: DragEvent, klausur: GostKursklausur) =>	{
		dragKlausur.value = klausur;
	}

	const dragEndKlausur = (e: DragEvent) =>	{
		dragKlausur.value = null;
	}


	const dropOverCssClasses = (termin: GostKlausurtermin) => ({
		"bg-success": dragKlausur.value !== null && dragKlausur.value.quartal === termin.quartal,
		"opacity-40": dragKlausur.value !== null && dragKlausur.value.quartal !== termin.quartal,
	});

	const termine = computed(() => props.quartalsauswahl.value === 0 ? props.kursklausurmanager().getKlausurtermine() : props.kursklausurmanager().getKlausurtermineByQuartal(props.quartalsauswahl.value));

	const algMode = ref<KlausurterminblockungAlgorithmen>(KlausurterminblockungAlgorithmen.NORMAL);
	const lkgkMode = ref<KlausurterminblockungModusKursarten>(KlausurterminblockungModusKursarten.BEIDE);
	const blockeGleicheLehrkraft = ref(false);

	const blocken = async () => {
		loading.value = true;
		modal.value.closeModal();
		const klausurenUngeblockt = props.kursklausurmanager().getKursklausurenOhneTerminByQuartal(props.quartalsauswahl.value);
		// Aufruf von Blockungsalgorithmus
		const blockConfig = new KlausurterminblockungAlgorithmusConfig();
		blockConfig.modusQuartale = KlausurterminblockungModusQuartale.GETRENNT.id;
		blockConfig.algorithmus = algMode.value.id;
		blockConfig.modusKursarten = lkgkMode.value.id;
		blockConfig.regelBeiTerminenGleicheLehrkraftFachKursart = blockeGleicheLehrkraft.value;
		const blockAlgo = new KlausurterminblockungAlgorithmus();
		await new Promise((resolve) => setTimeout(() => resolve(true), 0));
		const klausurTermine = blockAlgo.berechne(klausurenUngeblockt, blockConfig);
		// await props.persistKlausurblockung(klausurTermine);
		for await (const klausurList of klausurTermine) {
			let termin = null;
			for await (const klausurId of klausurList) {
				const klausur = props.kursklausurmanager().gibKursklausurById(klausurId);
				// if (klausur !== null) {
				if (termin === null)
					termin = await props.erzeugeKlausurtermin(klausur.quartal);
				await props.setTerminToKursklausur(termin.id, klausur);
				// }
			}
		}
		loading.value = false;
	};

	const loescheTermine = async () => await props.loescheKlausurtermine(termine.value);


</script>
