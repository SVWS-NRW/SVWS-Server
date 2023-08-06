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
				<svws-ui-radio-group :row="true"
					class="justify-center">
					<svws-ui-radio-option value="algNormal" v-model="algMode"
						name="blockAlgo"
						label="Normal" />
					<svws-ui-radio-option value="algFaecher" v-model="algMode"
						name="blockAlgo"
						label="Fächerweise" />
					<svws-ui-radio-option value="algSchienen" v-model="algMode"
						name="blockAlgo"
						label="Schienenweise" />
				</svws-ui-radio-group>
				<svws-ui-radio-group :row="true"
					class="justify-center">
					<svws-ui-radio-option value="lkgkMix" v-model="lkgkMode"
						name="lkgkMode"
						label="Gemischt" />
					<svws-ui-radio-option value="lkgkSep" v-model="lkgkMode"
						name="lkgkMode"
						label="Getrennt" />
					<svws-ui-radio-option value="lkgkOnlyLk" v-model="lkgkMode"
						name="lkgkMode"
						label="Nur LK" />
					<svws-ui-radio-option value="lkgkOnlyGk" v-model="lkgkMode"
						name="lkgkMode"
						label="Nur GK" />
				</svws-ui-radio-group>
				<svws-ui-checkbox v-model="blockGleicheLehrkraft" v-if="algMode === 'algNormal'">
					Falls gleiche Lehrkraft, Fach und Kursart, dann gleicher Termin?
				</svws-ui-checkbox>
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary"
					@click="blocken">
					Blocken
				</svws-ui-button>
				<svws-ui-button type="secondary"
					@click="modal.closeModal()">
					Abbrechen
				</svws-ui-button>
			</template>
		</svws-ui-modal>

		<svws-ui-content-card class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
			<div class="flex flex-row gap-8 mt-4">
				<div class="flex flex-col">
					<div class="text-headline-md">Zu verplanen:</div>
					<s-gost-klausurplanung-schienen-termin :quartal="quartalsauswahl.value"
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

	import type { GostKursklausur, GostKlausurtermin} from "@core";
	import { KlausurterminblockungAlgorithmus, KlausurterminblockungAlgorithmusConfig } from "@core";
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

	const algMode = ref("algNormal");
	const lkgkMode = ref("lkgkMix");
	const blockGleicheLehrkraft = ref(false);

	const blocken = async () => {
		loading.value = true;
		modal.value.closeModal();
		const klausurenUngeblockt = props.kursklausurmanager().getKursklausurenOhneTerminByQuartal(props.quartalsauswahl.value);
		// Aufruf von Blockungsalgorithmus
		const blockConfig = new KlausurterminblockungAlgorithmusConfig();
		blockConfig.set_quartals_modus_getrennt();
		if (algMode.value === "algNormal")
			blockConfig.set_algorithmus_normal();
		else if (algMode.value === "algFaecher")
			blockConfig.set_algorithmus_faecherweise();
		else if (algMode.value === "algSchienen")
			blockConfig.set_algorithmus_schienenweise();
		if (lkgkMode.value === "lkgkMix")
			blockConfig.set_lk_gk_modus_beide();
		else if (lkgkMode.value === "lkgkSep")
			blockConfig.set_lk_gk_modus_getrennt();
		else if (lkgkMode.value === "lkgkOnlyLk")
			blockConfig.set_lk_gk_modus_nur_lk();
		else if (lkgkMode.value === "lkgkOnlyGk")
			blockConfig.set_lk_gk_modus_nur_gk();
		blockConfig.set_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin(blockGleicheLehrkraft.value);
		const blockAlgo = new KlausurterminblockungAlgorithmus();
		await new Promise((resolve) => setTimeout(() => resolve(true), 0));
		const klausurTermine = blockAlgo.berechne(klausurenUngeblockt, blockConfig);
		// await props.persistKlausurblockung(klausurTermine);
		for await (const klausurList of klausurTermine) {
			let termin = null;
			for await (const klausurId of klausurList) {
				const klausur = props.kursklausurmanager().gibKursklausur(klausurId);
				if (klausur !== null) {
					if (termin === null)
						termin = await props.erzeugeKlausurtermin(klausur.quartal);
					await props.setTerminToKursklausur(termin.id, klausur);
				}
			}
		}
		loading.value = false;
	};

	const loescheTermine = async () => await props.loescheKlausurtermine(termine.value);


</script>
