<template>
	<div class="flex gap-x-8 2xl:gap-x-16">
		<div class="flex-grow">
			<svws-ui-content-card class="pt-8">
				<div class="flex flex-wrap gap-2 mb-4">
					<svws-ui-button type="primary" @click="neueVorgabe" :disabled="selectedVorgabeRow !== undefined">Neue Klausurvorgabe</svws-ui-button>
					<svws-ui-button type="secondary" @click="erzeugeVorgabenAusVorlage" v-if="jahrgangsdaten?.abiturjahr !== -1">Fehlende Klausurvorgaben erzeugen</svws-ui-button>
				</div>
				<svws-ui-data-table :items="vorgaben" :columns="[{key: 'kursart', label: 'Kursart', sortable: true},{key:'idFach', label: 'Fach', sortable: true},{key: 'quartal', label: 'Quartal', sortable: true},{key: 'dauer', label: 'Länge in Min.', sortable: true},{key: 'features', label: 'Besonderheiten'}]" v-model:clicked="selectedVorgabeRow" clickable @click="startEdit">
					<template #cell(idFach)="{ value }">
						{{ faecherManager.get(value)?.bezeichnung }}
					</template>
					<template #cell(features)="value">
						<svws-ui-popover v-if="value.rowData.bemerkungVorgabe != null && value.rowData.bemerkungVorgabe.trim().length > 0">
							<template #trigger>
								<i-ri-information-line class="cursor-help" />
							</template>
							<template #content>
								<svws-ui-tooltip>
									Bemerkung: {{ value.rowData.bemerkungVorgabe }}
								</svws-ui-tooltip>
							</template>
						</svws-ui-popover>&nbsp;
						<svws-ui-popover v-if="value.rowData.auswahlzeit > 0">
							<template #trigger>
								<i-ri-time-line class="cursor-help" />
							</template>
							<template #content>
								<svws-ui-tooltip>
									Auswahlzeit: {{ value.rowData.auswahlzeit }} Minuten
								</svws-ui-tooltip>
							</template>
						</svws-ui-popover>&nbsp;
						<svws-ui-popover v-if="value.rowData.istMdlPruefung">
							<template #trigger>
								<i-ri-kakao-talk-line class="cursor-help" />
							</template>
							<template #content>
								<svws-ui-tooltip>
									Dies ist eine mündliche Prüfung.
								</svws-ui-tooltip>
							</template>
						</svws-ui-popover>&nbsp;
						<svws-ui-popover v-if="value.rowData.istAudioNotwendig">
							<template #trigger>
								<i-ri-music-line class="cursor-help" />
							</template>
							<template #content>
								<svws-ui-tooltip>
									inkl. Audioteil
								</svws-ui-tooltip>
							</template>
						</svws-ui-popover>&nbsp;
						<svws-ui-popover v-if="value.rowData.istVideoNotwendig">
							<template #trigger>
								<i-ri-video-add-line class="cursor-help" />
							</template>
							<template #content>
								<svws-ui-tooltip>
									inkl. Videoteil
								</svws-ui-tooltip>
							</template>
						</svws-ui-popover>
					</template>
				</svws-ui-data-table>
			</svws-ui-content-card>
		</div>
		<div class="w-1/4" v-if="activeVorgabe.idVorgabe >= 0">
			<svws-ui-content-card title="Klausurvorgabe" class="w-full">
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
								<svws-ui-radio-option v-for="kursart in formKursarten" v-model="activeVorgabe.kursart" :key="kursart" :value="kursart" name="formKursarten" :label="kursart" />
							</svws-ui-radio-group>
						</div>
						<svws-ui-multi-select :items="props.faecherManager.values().sort((a,b) => a.bezeichnung!.localeCompare(b.bezeichnung!))" :item-text="(fach) => fach.bezeichnung" v-model="inputVorgabeFach" />
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
</template>

<script setup lang="ts">

	import { GostKursklausurManager, GostKursklausur, List, GostKlausurtermin, GostFaecherManager, LehrerListeEintrag, KursListeEintrag, SchuelerListeEintrag, GostJahrgangsdaten, GostKlausurvorgabenManager, GostKlausurvorgabe, GostFach } from "@svws-nrw/svws-core";
	import { DataTableItem } from '@ui';
	import { computed, ref, ComputedRef, WritableComputedRef } from 'vue';
	import type { Ref } from 'vue'

	const props = defineProps<{
		jahrgangsdaten: GostJahrgangsdaten | undefined;
		kursklausurmanager: () => GostKursklausurManager;
		klausurvorgabenmanager: () => GostKlausurvorgabenManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		erzeugeKlausurvorgabe: (vorgabe: GostKlausurvorgabe) => Promise<GostKlausurvorgabe>;
		patchKlausurvorgabe: (vorgabe: Partial<GostKlausurvorgabe>, id: number) => Promise<boolean>;
		loescheKlausurvorgabe: (vorgabe: GostKlausurvorgabe) => Promise<boolean>;
		erzeugeVorgabenAusVorlage: () => Promise<boolean>;
	}>();

	const vorgaben = computed(() => props.klausurvorgabenmanager().getKlausurvorgaben());

	const selectedVorgabeRow = ref<GostKlausurvorgabe>();
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
		if (activeVorgabe.value.idFach === -1 || activeVorgabe.value.kursart === "" || activeVorgabe.value.quartal === -1) {
			console.log("Eingabefehler");
			return;
		}
		if (activeVorgabe.value.idVorgabe > 0) {
			result = await props.patchKlausurvorgabe({...activeVorgabe.value}, activeVorgabe.value.idVorgabe);
			activeVorgabe.value = new GostKlausurvorgabe();
			selectedVorgabeRow.value = undefined;
		} else {
			result = await props.erzeugeKlausurvorgabe(activeVorgabe.value);
			activeVorgabe.value = new GostKlausurvorgabe();
		}
	};

	const loescheKlausurvorgabe = async () => {
		const vorgabe = props.klausurvorgabenmanager().gibGostKlausurvorgabe(activeVorgabe.value.idVorgabe);
		if (vorgabe === null)
			return;
		const result = await props.loescheKlausurvorgabe(vorgabe);
		selectedVorgabeRow.value = undefined;
		activeVorgabe.value = new GostKlausurvorgabe();
	};

	const cancelEdit = () => {
		selectedVorgabeRow.value = undefined;
		activeVorgabe.value = new GostKlausurvorgabe();
	};

	const startEdit = () => {
		if (selectedVorgabeRow.value != null) {
			const v = props.klausurvorgabenmanager().gibGostKlausurvorgabe(selectedVorgabeRow.value.idVorgabe);
			activeVorgabe.value = v !== null ? v : new GostKlausurvorgabe();
		}
	};

</script>
