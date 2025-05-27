<template>
	<svws-ui-modal v-if="show" :show size="small">
		<template #modalTitle>
			Grund für Fehlen angeben
		</template>
		<template #modalContent>
			<svws-ui-text-input focus placeholder="z.B. Krankheit" @update:model-value="bemerkung => terminSelected.bemerkung = bemerkung" @keyup.enter="createTermin(true)" />
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="createTermin(false)"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="createTermin(true)"> Nachschreibtermin erstellen </svws-ui-button>
		</template>
	</svws-ui-modal>

	<div v-if="kMan().vorgabeByKursklausur(kursklausur).bemerkungVorgabe !== null && kMan().vorgabeByKursklausur(kursklausur).bemerkungVorgabe!.trim().length > 0">
		<h3 class="border-b text-headline-md">Bemerkung zur Vorgabe</h3>
		{{ kMan().vorgabeByKursklausur(kursklausur).bemerkungVorgabe }}
	</div>
	<h2 class="border-b text-headline my-4 whitespace-nowrap">Klausurschreiber im Kurs {{ kMan().kursKurzbezeichnungByKursklausur(kursklausur) }}</h2>
	<table class="w-full">
		<tr v-for="s in kMan().schuelerklausurterminGetMengeByKursklausur(kursklausur)" :key="s.id" class="border-b">
			<td>
				<template v-if="termin !== undefined && !kMan().schuelerSchreibtKlausurtermin(kMan().schuelerGetBySchuelerklausurtermin(s).id, termin)">
					<div>
						<span class="line-through text-ui-danger">
							{{ kMan().schuelerGetBySchuelerklausurtermin(s).nachname }}, {{ kMan().schuelerGetBySchuelerklausurtermin(s).vorname }}
						</span>
					</div>
				</template>
				<span v-else>
					{{ kMan().schuelerGetBySchuelerklausurtermin(s).nachname }}, {{ kMan().schuelerGetBySchuelerklausurtermin(s).vorname }}
				</span>
			</td>
			<td class="text-center pl-5">
				<svws-ui-button v-if="patchKlausur && createSchuelerklausurTermin && hatKompetenzUpdate && termin !== undefined && kMan().schuelerSchreibtKlausurtermin(kMan().schuelerGetBySchuelerklausurtermin(s).id, termin)" @click="terminSelected = s; show = true">
					<svws-ui-tooltip>
						<template #content>
							Klausur nicht mitgeschrieben
						</template>
						<span class="icon i-ri-user-forbid-line" />
					</svws-ui-tooltip>
				</svws-ui-button>
				<span class="whitespace-nowrap flex items-center" v-else-if="hatKompetenzUpdate && termin !== undefined && !kMan().schuelerSchreibtKlausurtermin(kMan().schuelerGetBySchuelerklausurtermin(s).id, termin)">
					<span>(</span><svws-ui-textarea-input :disabled="!hatKompetenzUpdate || !patchKlausur" class="text-sm inline-block align-middle" headless :rows="1" resizeable="none" autoresize placeholder="Grund des Fehlens" :model-value="s.bemerkung" @change="bemerkung => patchKlausur(s, {bemerkung})" /><span>)</span>
				</span>
			</td>
		</tr>
	</table>
	<div class="my-4">
		<svws-ui-textarea-input placeholder="Bemerkungen zum Kurs" resizeable="none" autoresize :disabled="!hatKompetenzUpdate || !patchKlausur" :model-value="kursklausur.bemerkung" @change="bemerkung => patchKlausur(kursklausur, {bemerkung})" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watchEffect } from 'vue';
	import type { GostKlausurplanManager, GostKursklausur, GostKlausurtermin, GostKlausurenCollectionSkrsKrsData } from '@core';
	import { BenutzerKompetenz, GostSchuelerklausurTermin } from '@core';

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const props = withDefaults(defineProps<{
		kMan: () => GostKlausurplanManager;
		kursklausur: GostKursklausur;
		termin?: GostKlausurtermin;
		createSchuelerklausurTermin?: (id: number) => Promise<void>;
		patchKlausur?: (klausur: GostKursklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausurTermin>) => Promise<GostKlausurenCollectionSkrsKrsData>;
		benutzerKompetenzen: Set<BenutzerKompetenz>,
	}>(), {
		termin: undefined,
		createSchuelerklausurTermin: undefined,
		patchKlausur: undefined,
	});

	const emit = defineEmits<{
		'modal': [value: boolean];
	}>();

	const show = ref<boolean>(false);

	watchEffect(() => {
		if (show.value)
			emit('modal', true);
		else
			emit('modal', false);
	})

	const terminSelected = ref<GostSchuelerklausurTermin>(new GostSchuelerklausurTermin());

	const createTermin = async (create: boolean) => {
		if (props.patchKlausur && props.createSchuelerklausurTermin && create) {
			await props.patchKlausur(terminSelected.value, { bemerkung: terminSelected.value.bemerkung } );
			await props.createSchuelerklausurTermin(terminSelected.value.idSchuelerklausur);
		}
		show.value = false;
		terminSelected.value = new GostSchuelerklausurTermin();
	};

</script>