<template>
	<div class="flex flex-col">
		<div>
			<svws-ui-button class="secondary mb-5" @click="neueVorgabe" :disabled="selectedVorgabeRow !== undefined">Neue Klausurvorgabe</svws-ui-button>
		</div>
		<div class="flex flex-row flex-wrap gap-4 w-full">
			<div class="w-3/4">
				<svws-ui-data-table :items="vorgaben" :columns="[{key: 'kursartAllg', label: 'Kursart', sortable: true},{key:'idFach', label: 'Fach', sortable: true},{key: 'quartal', label: 'Quartal', sortable: true},{key: 'dauer', label: 'Länge in Min.', sortable: true},{key: 'features', label: 'Besonderheiten'}]" v-model:clicked="selectedVorgabeRow" clickable @click="startEdit">
					<template #cell(idFach)="{ value }">
						{{ faecherManager.get(value)?.bezeichnung }}
					</template>
					<template #cell(features)="value">
						<svws-ui-popover>
							<template #trigger>
								<i-ri-information-line v-if="value.rowData.bemerkungVorgabe != null && value.rowData.bemerkungVorgabe.trim().length > 0" />
							</template>
							<template #content>
								<svws-ui-tooltip>
									Bemerkung: {{ value.rowData.bemerkungVorgabe }}
								</svws-ui-tooltip>
							</template>
						</svws-ui-popover>
						<svws-ui-popover>
							<template #trigger>
								<i-ri-time-line class="cursor-help" v-if="value.rowData.auswahlzeit > 0" />
							</template>
							<template #content>
								<svws-ui-tooltip>
									Auswahlzeit: {{ value.rowData.auswahlzeit }} Minuten
								</svws-ui-tooltip>
							</template>
						</svws-ui-popover>
						<svws-ui-popover>
							<template #trigger>
								<i-ri-kakao-talk-line class="cursor-help" v-if="value.rowData.istMdlPruefung" />
							</template>
							<template #content>
								<svws-ui-tooltip>
									Dies ist eine mündliche Prüfung.
								</svws-ui-tooltip>
							</template>
						</svws-ui-popover>
						<svws-ui-popover>
							<template #trigger>
								<i-ri-music-line class="cursor-help" v-if="value.rowData.istAudioNotwendig" />
							</template>
							<template #content>
								<svws-ui-tooltip>
									inkl. Audioteil
								</svws-ui-tooltip>
							</template>
						</svws-ui-popover>
						<svws-ui-popover>
							<template #trigger>
								<i-ri-video-add-line class="cursor-help" v-if="value.rowData.istVideoNotwendig" />
							</template>
							<template #content>
								<svws-ui-tooltip>
									inkl. Videoteil
								</svws-ui-tooltip>
							</template>
						</svws-ui-popover>
					</template>
				</svws-ui-data-table>
			</div>
			<div class="w-1/5">
				<svws-ui-content-card v-if="activeVorgabe.idVorgabe >= 0" title="Klausurvorgabe" class="w-full">
					<template #actions>
						<svws-ui-button @click="loescheKlausurvorgabe" v-if="selectedVorgabeRow !== undefined">Löschen</svws-ui-button>
						<svws-ui-button @click="cancelEdit">Abbrechen</svws-ui-button>
						<svws-ui-button @click="saveKlausurvorgabe">Speichern</svws-ui-button>
					</template>
					<div class="flex flex-row items-start space-x-4">
						<div class="flex-grow space-y-3">
							<div class="flex flex-row items-center">
								<label for="rbgKursart">Kursart: </label>
								<svws-ui-radio-group id="rbgKursart" :row="true">
									<svws-ui-radio-option v-for="kursart in formKursarten" v-model="activeVorgabe.kursartAllg" :key="kursart" :value="kursart" name="formKursarten" :label="kursart" />
								</svws-ui-radio-group>
							</div>
							<svws-ui-multi-select :items="props.faecherManager.values()" :item-text="(fach) => fach.bezeichnung" v-model="inputVorgabeFach" />
							<div class="flex flex-row items-center">
								<label for="rbgQuartal">Quartal: </label>
								<svws-ui-radio-group id="rbgQuartal" :row="true">
									<svws-ui-radio-option v-for="quartal in formQuartale" :key="quartal" :value="quartal+''" name="formQuartale" :label="quartal+''" :model-value="activeVorgabe.quartal+''" @click="activeVorgabe.quartal = quartal" />
								</svws-ui-radio-group>
							</div>
							<svws-ui-text-input placeholder="Dauer" type="number" v-model:modelValue="activeVorgabe.dauer" />
							<svws-ui-text-input placeholder="Auswahlzeit" type="number" v-model:modelValue="activeVorgabe.auswahlzeit" />
							<div class="flex flex-row items-center">
								<label for="rbgMdlPruefung">Mündliche Prüfung: </label>
								<svws-ui-radio-group id="rbgMdlPruefung" :row="true">
									<svws-ui-radio-option v-for="value in formJaNein" :key="value.name" :value="value.name" name="formMdlPruefung" :label="value.name" :model-value="activeVorgabe.istMdlPruefung ? 'Ja' : 'Nein'" @click="activeVorgabe.istMdlPruefung = value.key" />
								</svws-ui-radio-group>
							</div>
							<div class="flex flex-row items-center">
								<label for="rbgAudioNotwendig">Audio notwendig: </label>
								<svws-ui-radio-group id="rbgAudioNotwendig" :row="true">
									<svws-ui-radio-option v-for="value in formJaNein" :key="value.name" :value="value.name" name="formAudioNotwendig" :label="value.name" :model-value="activeVorgabe.istAudioNotwendig ? 'Ja' : 'Nein'" @click="activeVorgabe.istAudioNotwendig = value.key" />
								</svws-ui-radio-group>
							</div>
							<div class="flex flex-row items-center">
								<label for="rbgVideoNotwendig">Video notwendig: </label>
								<svws-ui-radio-group id="rbgVideoNotwendig" :row="true">
									<svws-ui-radio-option v-for="value in formJaNein" :key="value.name" :value="value.name" name="formVideoNotwendig" :label="value.name" :model-value="activeVorgabe.istVideoNotwendig ? 'Ja' : 'Nein'" @click="activeVorgabe.istVideoNotwendig = value.key" />
								</svws-ui-radio-group>
							</div>
							<svws-ui-textarea-input placeholder="Bemerkungen" v-model="activeVorgabe.bemerkungVorgabe">dasdas</svws-ui-textarea-input>
						</div>
					</div>
				</svws-ui-content-card>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostKursklausurManager, GostKursklausur, List, GostKlausurtermin, GostFaecherManager, LehrerListeEintrag, KursListeEintrag, SchuelerListeEintrag, GostJahrgangsdaten, GostKlausurvorgabenManager, GostKlausurvorgabe, GostFach } from '@svws-nrw/svws-core-ts';
	import { DataTableItem } from '@svws-nrw/svws-ui';
	import { computed, ref, ComputedRef, WritableComputedRef } from 'vue';
	import type { Ref } from 'vue'

	const props = defineProps<{
		jahrgangsdaten: GostJahrgangsdaten | undefined;
		kursklausurmanager: () => GostKursklausurManager;
		klausurvorgabenmanager: () => GostKlausurvorgabenManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		erzeugeKlausurvorgabe: (vorgabe: GostKlausurvorgabe) => Promise<GostKlausurvorgabe>;
		patchKlausurvorgabe: (vorgabe: GostKlausurvorgabe) => Promise<boolean>;
		loescheKlausurvorgabe: (vorgabe: GostKlausurvorgabe) => Promise<boolean>;
	}>();

	const vorgaben = computed(() => props.klausurvorgabenmanager().getKlausurvorgaben());

	const selectedVorgabeRow = ref<DataTableItem[]>();
	const activeVorgabe: Ref<GostKlausurvorgabe> = ref(new GostKlausurvorgabe());

	const formKursarten = computed(() => ["GK", "LK"]);
	const formJaNein = computed(() => [{key: true, name: "Ja"}, {key: false, name: "Nein"}]);
	const formQuartale = computed(() => [1, 2]);
	const inputVorgabeFach: WritableComputedRef<GostFach | undefined> = computed({
		get() {
			const fach = props.faecherManager.get(activeVorgabe.value.idFach);
			return fach === null ? undefined : fach;
		},
		set(val) {
			if (val !== undefined)
				activeVorgabe.value.idFach = val.id;
		}
	});

	const neueVorgabe = () => {
		activeVorgabe.value.idVorgabe = 0;
	};

	const saveKlausurvorgabe = async () => {
		let result;
		if (activeVorgabe.value.idVorgabe > 0) {
			result = await props.patchKlausurvorgabe(activeVorgabe.value);
			activeVorgabe.value = new GostKlausurvorgabe();
			selectedVorgabeRow.value = undefined;
		} else {
			result = await props.erzeugeKlausurvorgabe(activeVorgabe.value);
			activeVorgabe.value = new GostKlausurvorgabe();
		}
	};

	const loescheKlausurvorgabe = async () => {
		const result = await props.loescheKlausurvorgabe(activeVorgabe.value);
		selectedVorgabeRow.value = undefined;
		activeVorgabe.value = new GostKlausurvorgabe();
	};

	const cancelEdit = () => {
		selectedVorgabeRow.value = undefined;
		activeVorgabe.value = new GostKlausurvorgabe();
	};

	const startEdit = () => {
		const v = props.klausurvorgabenmanager().gibGostKlausurvorgabe({...selectedVorgabeRow.value as DataTableItem}.idVorgabe);
		if (v !== null) {
			activeVorgabe.value = v;
		}
	};

</script>
