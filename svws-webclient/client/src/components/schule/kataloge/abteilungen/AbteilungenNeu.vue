<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField"
						v-model="modelProxy.proxy.bezeichnung"
						:validation="() => modelProxy.getFehler('bezeichnung')"
						required :max-len="50" />
					<svws-ui-text-input placeholder="Raum"
						v-model="modelProxy.proxy.raum"
						:validation="() => modelProxy.getFehler('raum')"
						:max-len="20" />
					<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
						v-model="modelProxy.proxy.email"
						:validation="() => modelProxy.getFehler('email')"
						:max-len="100" />
					<svws-ui-text-input placeholder="Durchwahl" type="tel"
						v-model="modelProxy.proxy.durchwahl"
						:validation="() => modelProxy.getFehler('durchwahl')"
						:max-len="20" />
					<ui-select label="Abteilungsleitung"
						:manager="lehrerSelectManager"
						v-model="modelProxy.abteilungsleiter.value" />
					<div />
					<svws-ui-spacing :size="2" />
					<svws-ui-content-card title="Ansicht & Sortierung">
						<svws-ui-input-number placeholder="Sortierung"
							v-model="modelProxy.proxy.sortierung"
							:validation="() => modelProxy.getFehler('sortierung')"
							:disabled :min="0"
							:removeable="false" required />
						<svws-ui-spacing />
						<svws-ui-checkbox v-model="modelProxy.proxy.istSichtbar">
							Sichtbar
						</svws-ui-checkbox>
					</svws-ui-content-card>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-input-wrapper class="mt-7" :grid="2">
				<div />
				<div class="flex justify-end">
					<svws-ui-checkbox v-model="addAbteilungInFolgeAbschnitt">
						<div class="pt-0.5">
							Zusätzlich im Folgeabschnitt ({{ getTextFolgeAbschnitt() }}) anlegen
						</div>
					</svws-ui-checkbox>
				</div>
			</svws-ui-input-wrapper>
			<div class="mt-4 flex flex-row gap-4 justify-end">
				<svws-ui-tooltip>
					<svws-ui-button type="secondary" @click="cancel">
						Abbrechen
					</svws-ui-button>
					<template #content>
						Anlage abbrechen
					</template>
				</svws-ui-tooltip>
				<svws-ui-button @click="addAbteilungAndZuordnungen" :disabled="mussFehlerExists">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card title="Klassen zuordnen">
			<div class="flex flex-col max-h-125">
				<svws-ui-table class="max-h-125" :columns
					:items="manager().klassenByIdAktAbschnitt.values()"
					v-model="klassenToAdd"
					:selectable="hatKompetenzAdd" count scroll />
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { AbteilungenNeuProps } from "~/components/schule/kataloge/abteilungen/AbteilungenNeuProps";
	import { computed, ref, watch } from "vue";
	import { AbteilungenModelProxy } from "~/components/schule/kataloge/abteilungen/modelproxy/AbteilungenModelProxy";
	import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
	import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
	import { Abteilung } from "@core/core/data/schule/Abteilung";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { Arrays } from "@core/java/util/Arrays";
	import type { List } from "@core/java/util/List";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import type { DataTableColumn } from "@ui/types";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { ViewType } from "@ui/ui/nav/ViewType";

	const props = defineProps<AbteilungenNeuProps>();
	const benutzerState = useBenutzerState();
	const abschnittState = useAbschnittState();

	const columns: DataTableColumn[] = [{ key: "kuerzel", label: "Klasse" }];

	const data = ref<Abteilung>(Object.assign(new Abteilung(), { istSichtbar: true, sortierung: 32000 }));
	const isLoading = ref<boolean>(false);
	const addAbteilungInFolgeAbschnitt = ref<boolean>(true);

	const klassenToAdd = ref<KlassenListeEintrag[]>([]);
	const klassenIdsToAdd = computed<List<number>>(() => Arrays.asList(klassenToAdd.value.map(klasse => klasse.id)));
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);
	const availableLehrer = computed(() => props.manager().lehrerById.values());
	const mussFehlerExists = computed(() => [...modelProxy.getAlleFehler()].some(fehler => fehler.getFehlerart().ordinal() === ValidatorFehlerart.MUSS.ordinal()));

	const modelProxy = new AbteilungenModelProxy(() => data.value, props.manager, ViewType.HINZUFUEGEN);

	const lehrerSelectManager = new SelectManager({
		options: availableLehrer,
		optionDisplayText: v => v.vorname + ' ' + v.nachname,
		selectionDisplayText: v => v.vorname + ' ' + v.nachname,
	});

	async function cancel() {
		props.checkpoint.active = false;
		await props.goToDefaultView(null);
	}

	async function addAbteilungAndZuordnungen() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;

		const { id, idSchuljahresabschnitt, klassenzuordnungen, ...partialData } = modelProxy.proxy;
		const idAbteilungNeu = await props.add(partialData, klassenIdsToAdd.value, addAbteilungInFolgeAbschnitt.value);

		isLoading.value = false;
		await props.goToDefaultView(idAbteilungNeu);
	}

	function getTextFolgeAbschnitt() {
		const folgeAbschnitt = props.manager().schuljahresabschnitte.get(abschnittState.auswahl.idFolgeAbschnitt ?? -1);
		if ((folgeAbschnitt === null) || folgeAbschnitt.schuljahr <= 0) {
			return '';
		}
		return `${folgeAbschnitt.schuljahr}/${(folgeAbschnitt.schuljahr + 1) % 100}.${folgeAbschnitt.abschnitt}`;
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
