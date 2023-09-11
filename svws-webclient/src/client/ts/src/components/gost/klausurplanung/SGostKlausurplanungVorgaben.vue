<template>
	<svws-ui-sub-nav>
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" />
		<svws-ui-button type="primary" @click="neueVorgabe">Neue Vorgabe</svws-ui-button>
		<svws-ui-button type="secondary" @click="erzeugeVorgabenAusVorlage(quartalsauswahl.value)" v-if="jahrgangsdaten?.abiturjahr !== -1">Fehlende Klausurvorgaben kopieren</svws-ui-button>
		<svws-ui-button @click="saveKlausurvorgabe" :disabled="activeVorgabe.idVorgabe < 0" class="ml-auto">Vorgabe speichern</svws-ui-button>
		<svws-ui-button type="danger" size="small" @click="loescheKlausurvorgabe" :disabled="activeVorgabe.idVorgabe < 0"><i-ri-delete-bin-line />Vorgabe löschen</svws-ui-button>
		<svws-ui-modal-hilfe> <s-gost-klausurplanung-vorgaben-hilfe /> </svws-ui-modal-hilfe>
	</svws-ui-sub-nav>

	<div class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<div class="flex-grow">
			<svws-ui-content-card>
				<svws-ui-data-table :items="vorgaben" :columns="[{key:'idFach', label: 'Fach', sortable: true},{key: 'kursart', label: 'Kursart', sortable: true},{key: 'quartal', label: 'Quartal', sortable: true},{key: 'dauer', label: 'Länge in Minuten', sortable: true},{key: 'features', label: 'Besonderheiten'}]" v-model:clicked="selectedVorgabeRow" clickable @click="startEdit">
					<template #cell(idFach)="{ value }">
						{{ faecherManager.get(value)?.bezeichnung }}
					</template>
					<template #cell(features)="value">
						<div class="cursor-pointer flex items-center gap-1">
							<svws-ui-tooltip
								v-if="value.rowData.bemerkungVorgabe !== null && value.rowData.bemerkungVorgabe.trim().length > 0">
								<i-ri-information-line />
								<template #content>
									Bemerkung: {{ value.rowData.bemerkungVorgabe }}
								</template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-if="value.rowData.auswahlzeit > 0">
								<i-ri-time-line />
								<template #content>
									Auswahlzeit: {{ value.rowData.auswahlzeit }} Minuten
								</template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-if="value.rowData.istMdlPruefung">
								<i-ri-chat-1-line />
								<template #content>
									Mündliche Prüfung
								</template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-if="value.rowData.istAudioNotwendig">
								<i-ri-headphone-line />
								<template #content>
									Inkl. Audioteil
								</template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-if="value.rowData.istVideoNotwendig">
								<i-ri-vidicon-line />
								<template #content>
									Inkl. Videoteil
								</template>
							</svws-ui-tooltip>
						</div>
					</template>
				</svws-ui-data-table>
			</svws-ui-content-card>
		</div>
		<svws-ui-content-card title="Vorgabe bearbeiten" class="w-1/4">
			<template #actions>
				<svws-ui-button type="secondary" @click="cancelEdit" :disabled="activeVorgabe.idVorgabe < 0">Abbrechen</svws-ui-button>
			</template>
			<div class="flex flex-col gap-4">
				<svws-ui-input-wrapper>
					<svws-ui-radio-group id="rbgKursart" :row="true">
						<svws-ui-radio-option v-for="kursart in formKursarten" v-model="activeVorgabe.kursart" :key="kursart" :value="kursart" name="formKursarten" :label="kursart" :disabled="activeVorgabe.idVorgabe !== 0" />
					</svws-ui-radio-group>
					<svws-ui-multi-select :items="faecherSortiert" :item-text="(fach : GostFach) => fach.bezeichnung || ''" v-model="inputVorgabeFach" title="Fach" :disabled="activeVorgabe.idVorgabe !== 0" />
				</svws-ui-input-wrapper>
				<svws-ui-radio-group id="rbgQuartal" :row="true">
					<svws-ui-radio-option v-for="quartal in formQuartale" :key="quartal" :value="quartal+''" name="formQuartale" :label="quartal+'. Quartal'" :model-value="activeVorgabe.quartal+''" @click="activeVorgabe.quartal = quartal" :disabled="activeVorgabe.idVorgabe !== 0" />
				</svws-ui-radio-group>
				<svws-ui-input-wrapper>
					<svws-ui-text-input placeholder="Dauer" type="number" v-model:modelValue="activeVorgabe.dauer" :disabled="activeVorgabe.idVorgabe < 0" />
					<svws-ui-text-input placeholder="Auswahlzeit" type="number" v-model:modelValue="activeVorgabe.auswahlzeit" :disabled="activeVorgabe.idVorgabe < 0" />
				</svws-ui-input-wrapper>
				<div class="flex flex-col gap-1">
					<div class="flex flex-row items-center">
						<label for="rbgMdlPruefung">Mündliche Prüfung: </label>
						<svws-ui-radio-group id="rbgMdlPruefung" :row="true">
							<svws-ui-radio-option v-for="value in formJaNein" :key="value.name" :value="value.name" name="formMdlPruefung" :label="value.name" :model-value="activeVorgabe.istMdlPruefung ? 'Ja' : 'Nein'" @click="activeVorgabe.istMdlPruefung = value.key" :disabled="activeVorgabe.idVorgabe < 0" />
						</svws-ui-radio-group>
					</div>
					<div class="flex flex-row items-center">
						<label for="rbgAudioNotwendig">Audio notwendig: </label>
						<svws-ui-radio-group id="rbgAudioNotwendig" :row="true">
							<svws-ui-radio-option v-for="value in formJaNein" :key="value.name" :value="value.name" name="formAudioNotwendig" :label="value.name" :model-value="activeVorgabe.istAudioNotwendig ? 'Ja' : 'Nein'" @click="activeVorgabe.istAudioNotwendig = value.key" :disabled="activeVorgabe.idVorgabe < 0" />
						</svws-ui-radio-group>
					</div>
					<div class="flex flex-row items-center">
						<label for="rbgVideoNotwendig">Video notwendig: </label>
						<svws-ui-radio-group id="rbgVideoNotwendig" :row="true">
							<svws-ui-radio-option v-for="value in formJaNein" :key="value.name" :value="value.name" name="formVideoNotwendig" :label="value.name" :model-value="activeVorgabe.istVideoNotwendig ? 'Ja' : 'Nein'" @click="activeVorgabe.istVideoNotwendig = value.key" :disabled="activeVorgabe.idVorgabe < 0" />
						</svws-ui-radio-group>
					</div>
				</div>
				<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="activeVorgabe.bemerkungVorgabe" @change="bemerkungVorgabe => activeVorgabe.bemerkungVorgabe = bemerkungVorgabe" resizeable="vertical" :disabled="activeVorgabe.idVorgabe < 0" />
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { GostFach } from "@core";
	import type { Ref , WritableComputedRef } from 'vue'
	import { ArrayList, GostKlausurvorgabe } from "@core";
	import { computed, ref } from 'vue';
	import type { GostKlausurplanungVorgabenProps } from "./SGostKlausurplanungVorgabenProps";

	const props = defineProps<GostKlausurplanungVorgabenProps>();

	const vorgaben = computed(() => props.klausurvorgabenmanager().vorgabeGetMengeByQuartal(props.quartalsauswahl.value));

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

	const faecherSortiert = computed(() => {
		const f = new ArrayList(props.faecherManager.faecher());
		//		f.sort({ compare: (a: GostFach, b: GostFach) => a.bezeichnung.localeCompare(b.bezeichnung) });
		return f;
	});

	const neueVorgabe = () => {
		activeVorgabe.value.idVorgabe = 0;
	};

	const saveKlausurvorgabe = async () => {
		if (activeVorgabe.value.idFach === -1 || activeVorgabe.value.kursart === "" || activeVorgabe.value.quartal === -1) {
			console.log("Eingabefehler");
			return;
		}
		if (activeVorgabe.value.idVorgabe > 0) {
			await props.patchKlausurvorgabe({
				dauer: activeVorgabe.value.dauer,
				auswahlzeit: activeVorgabe.value.auswahlzeit,
				istMdlPruefung: activeVorgabe.value.istMdlPruefung,
				istAudioNotwendig: activeVorgabe.value.istAudioNotwendig,
				istVideoNotwendig: activeVorgabe.value.istVideoNotwendig,
				bemerkungVorgabe: activeVorgabe.value.bemerkungVorgabe,
			}, activeVorgabe.value.idVorgabe);
			activeVorgabe.value = new GostKlausurvorgabe();
			selectedVorgabeRow.value = undefined;
		} else {
			await props.erzeugeKlausurvorgabe(activeVorgabe.value);
			activeVorgabe.value = new GostKlausurvorgabe();
		}
	};

	const loescheKlausurvorgabe = async () => {
		await props.loescheKlausurvorgabe(activeVorgabe.value.idVorgabe);
		selectedVorgabeRow.value = undefined;
		activeVorgabe.value = new GostKlausurvorgabe();
	};

	const cancelEdit = () => {
		selectedVorgabeRow.value = undefined;
		activeVorgabe.value = new GostKlausurvorgabe();
	};

	const startEdit = () => {
		if (selectedVorgabeRow.value !== null && selectedVorgabeRow.value !== undefined) {
			const v = props.klausurvorgabenmanager().vorgabeGetByIdOrException(selectedVorgabeRow.value.idVorgabe);
			activeVorgabe.value = v !== null ? v : new GostKlausurvorgabe();
		}
	};

</script>
