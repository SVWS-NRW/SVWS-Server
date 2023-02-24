<template>
	<div>
		<div class="flex flex-row items-center">
			<label for="rgQuartalAuswahl">Quartalauswahl: </label>
			<svws-ui-radio-group id="rgQuartalAuswahl" :row="true">
				<svws-ui-radio-option name="rgQuartalAuswahl" label="beide" value="0" @input="chooseQuartal(-1)" model-value="0" />
				<svws-ui-radio-option name="rgQuartalAuswahl" label="1" value="1" @input="chooseQuartal(1)" />
				<svws-ui-radio-option name="rgQuartalAuswahl" label="2" value="2" @input="chooseQuartal(2)" />
			</svws-ui-radio-group>
			<svws-ui-button class="secondary" @click="erstelleTermin" :disabled="quartal === -1">Neuer Termin</svws-ui-button>
		</div>
		<div class="flex flex-row gap-8 mt-5">
			<s-gost-klausurplanung-schienen-termin :quartal="quartal"
				:kursklausurmanager="kursklausurmanager"
				:termin="null"
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
						:faecher-manager="faecherManager"
						:map-lehrer="mapLehrer"
						:set-termin-to-kursklausur="setTerminToKursklausur"
						:drag-status="dragStatus"
						:drag-klausur="dragKlausur"
						:map-schueler="mapSchueler"
						:loesche-klausurtermin="loescheKlausurtermin" />
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostKursklausurManager, GostKursklausur, List, GostKlausurtermin, GostFaecherManager, LehrerListeEintrag, KursListeEintrag, SchuelerListeEintrag } from '@svws-nrw/svws-core-ts';
	import { computed, ref, ComputedRef } from 'vue';

	const props = defineProps<{
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		setTerminToKursklausur: (idTermin: number | null, klausur: GostKursklausur) => Promise<boolean>;
		erzeugeKlausurtermin: (quartal: number) => Promise<GostKlausurtermin>;
		loescheKlausurtermin: (termin: GostKlausurtermin) => Promise<boolean>;
		mapSchueler: Map<number, SchuelerListeEintrag>;
	}>();

	const quartal = ref(-1);
	const chooseQuartal = (q: number) => quartal.value = q;

	const dragKlausur = ref<GostKursklausur | null>(null);

	const dragStatus = (klausur: GostKursklausur | null) =>	dragKlausur.value = klausur;

	const dropOverCssClasses = (termin: GostKlausurtermin) => ({
		"bg-green-100": dragKlausur.value !== null && dragKlausur.value.quartal === termin.quartal,
		"bg-red-100": dragKlausur.value !== null && dragKlausur.value.quartal !== termin.quartal,
	});

	const termine = computed(() => quartal.value === -1 ? props.kursklausurmanager().getKlausurtermine() : props.kursklausurmanager().getKlausurtermine(quartal.value));
	const erstelleTermin = async () => await props.erzeugeKlausurtermin(quartal.value);

</script>
