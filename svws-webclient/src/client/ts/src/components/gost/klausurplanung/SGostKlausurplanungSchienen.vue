<template>
	<div>
		<div class="flex flex-row items-center gap-x-5">
			<label for="rgQuartalAuswahl">Quartalauswahl: </label>
			<svws-ui-radio-group id="rgQuartalAuswahl" :row="true">
				<svws-ui-radio-option name="rgQuartalAuswahl" label="beide" value="0" @input="chooseQuartal(0)" model-value="0" />
				<svws-ui-radio-option name="rgQuartalAuswahl" label="1" value="1" @input="chooseQuartal(1)" />
				<svws-ui-radio-option name="rgQuartalAuswahl" label="2" value="2" @input="chooseQuartal(2)" />
			</svws-ui-radio-group>
			<svws-ui-button class="secondary" @click="erzeugeKursklausurenAusVorgaben(quartal)">Erstelle Klausuren</svws-ui-button>
			<svws-ui-button class="secondary" @click="erzeugeKlausurtermin(quartal)" :disabled="quartal <= 0">Neuer Termin</svws-ui-button>
			<!--<svws-ui-button class="secondary" @click="blocken" :disabled="quartal <= 0 || termine.size() > 0"><svws-ui-spinner :spinning="loading" /> Automatisch blocken</svws-ui-button>-->
			<svws-ui-dropdown type="primary" :disabled="quartal <= 0 || termine.size() > 0">
				<template #dropdownButton>
					<svws-ui-spinner :spinning="loading" />&nbsp;Automatisch blocken
				</template>
				<template #dropdownItems>
					<SvwsUiDropdownItem
						@click="blocken(KlausurterminblockungAlgorithmusConfig.ALGORITHMUS_NORMAL, KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_BEIDE)">
						Normal - Kursarten mischen
					</SvwsUiDropdownItem>
					<SvwsUiDropdownItem @click="blocken(KlausurterminblockungAlgorithmusConfig.ALGORITHMUS_NORMAL, KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_GETRENNT)">
						Normal - Kursarten trennen
					</SvwsUiDropdownItem>
					<SvwsUiDropdownItem @click="blocken(KlausurterminblockungAlgorithmusConfig.ALGORITHMUS_SCHIENENWEISE, KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_BEIDE)">
						Schienenweise
					</SvwsUiDropdownItem>
					<SvwsUiDropdownItem @click="blocken(KlausurterminblockungAlgorithmusConfig.ALGORITHMUS_FAECHERWEISE, KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_BEIDE)">
						Fächerweise
					</SvwsUiDropdownItem>
				</template>
			</svws-ui-dropdown>
			<svws-ui-button class="secondary" @click="loescheTermine" :disabled="termine.size() === 0">Alle Termine löschen</svws-ui-button>
		</div>
		<div class="flex flex-row gap-8 mt-5">
			<s-gost-klausurplanung-schienen-termin :quartal="quartal"
				:kursklausurmanager="kursklausurmanager"
				:termin="null"
				:alle-termine="termine"
				:faecher-manager="faecherManager"
				:map-lehrer="mapLehrer"
				:set-termin-to-kursklausur="setTerminToKursklausur"
				:drag-status="dragStatus"
				:map-schueler="mapSchueler" />
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
						:drag-status="dragStatus"
						:drag-klausur="dragKlausur"
						:map-schueler="mapSchueler"
						:loesche-klausurtermin="loescheKlausurtermin"
						:patch-klausurtermin="patchKlausurtermin" />
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostKursklausur, GostKlausurtermin, KlausurblockungSchienenAlgorithmus, KlausurterminblockungAlgorithmus, KlausurterminblockungAlgorithmusConfig } from "@svws-nrw/svws-core";
	import { computed, ref } from 'vue';
	import { GostKlausurplanungSchienenProps } from './SGostKlausurplanungSchienenProps';

	const props = defineProps<GostKlausurplanungSchienenProps>();
	const loading = ref<boolean>(false);

	const quartal = ref(0);
	const chooseQuartal = (q: number) => quartal.value = q;

	const dragKlausur = ref<GostKursklausur | null>(null);

	const dragStatus = (klausur: GostKursklausur | null) =>	dragKlausur.value = klausur;

	const dropOverCssClasses = (termin: GostKlausurtermin) => ({
		"bg-success": dragKlausur.value !== null && dragKlausur.value.quartal === termin.quartal,
		"bg-error": dragKlausur.value !== null && dragKlausur.value.quartal !== termin.quartal,
	});

	const termine = computed(() => quartal.value <= 0 ? props.kursklausurmanager().getKlausurtermine() : props.kursklausurmanager().getKlausurtermine(quartal.value));

	const blocken = async (mode: number, lkgk: number) => {
		loading.value = true;
		const klausurenUngeblockt = props.kursklausurmanager().getKursklausurenOhneTermin(quartal.value);
		// Aufruf von Blockungsalgorithmus
		KlausurterminblockungAlgorithmusConfig
		const blockConfig = new KlausurterminblockungAlgorithmusConfig();
		blockConfig.set_algorithmus(mode);
		blockConfig.set_lk_gk_modus(lkgk);
		const blockAlgo = new KlausurterminblockungAlgorithmus();
		await new Promise((resolve) => setTimeout(() => resolve(true), 0));
		const klausurTermine = blockAlgo.berechne(klausurenUngeblockt, blockConfig);
		for await (const klausurList of klausurTermine) {
			const termin = await props.erzeugeKlausurtermin(quartal.value);
			for await (const klausurId of klausurList) {
				const klausur = props.kursklausurmanager().gibKursklausur(klausurId);
				if (klausur !== null) {
					await props.setTerminToKursklausur(termin.id, klausur);
				}
			}
		}
		loading.value = false;
	};

	const loescheTermine = async () => {
		for (const termin of termine.value.toArray()) {
			await props.loescheKlausurtermin(termin as GostKlausurtermin);
		}
	}


</script>
