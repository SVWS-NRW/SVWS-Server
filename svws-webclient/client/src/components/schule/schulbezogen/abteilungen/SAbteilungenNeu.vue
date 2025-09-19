<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung" :min-len="1" :max-len="50" v-model="data.bezeichnung" required :disabled
					:valid="fieldIsValid('bezeichnung')" />
				<svws-ui-text-input placeholder="Raum" :max-len="20" v-model="data.raum" :valid="fieldIsValid('raum')" :disabled />
				<div class="flex flex-col my-auto space-y-1">
					<div v-if="!isUniqueInList(data.bezeichnung, props.manager().liste.list(), 'bezeichnung')" class="flex items-center">
						<span class="icon i-ri-alert-line mx-0.5 mr-1" />
						<p>Diese Bezeichnung wird bereits verwendet.</p>
					</div>
					<div v-if="data.bezeichnung.length > 50" class="flex items-center">
						<span class="icon i-ri-alert-line mx-0.5 mr-1" />
						<p>Diese Bezeichnung verwendet zu viele Zeichen.</p>
					</div>
				</div>
				<div />
				<svws-ui-text-input placeholder="Email" type="email" :max-len="100" v-model="data.email" :valid="fieldIsValid('email')" :disabled />
				<svws-ui-text-input placeholder="Durchwahl" type="tel" :max-len="20" v-model="data.durchwahl" :valid="fieldIsValid('durchwahl')" :disabled />
				<svws-ui-spacing />
				<ui-select label="Lehrer" v-model="idLehrer" :manager="lehrerSelectManager" :disabled />
				<svws-ui-button :disabled="data.idAbteilungsleiter === null" type="transparent"
					@click="goToLehrer(data.idAbteilungsleiter ?? -1)">
					<span class="icon i-ri-link" /> Zum Lehrer
				</svws-ui-button>
				<div class="mt-7 flex flex-row gap-4 justify end">
					<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
					<svws-ui-button @click="add" :disabled="!formIsValid || !hatKompetenzAdd">Speichern</svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Klassen zuordnen">
			<svws-ui-table :items="manager().getKlassen().values()" :columns selectable v-model="klassenToBeAdded" scroll :disabled />
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { AbteilungenNeuProps } from "~/components/schule/schulbezogen/abteilungen/SAbteilungenNeuProps";
	import type { DataTableColumn} from "@ui";
	import type { KlassenDaten, LehrerListeEintrag, List } from "@core";
	import { BenutzerKompetenz, Abteilung, AbteilungKlassenzuordnung, ArrayList } from "@core";
	import { SelectManager } from "@ui";
	import { computed, ref, watch } from "vue";
	import { emailIsValid, isUniqueInList, mandatoryInputIsValid, optionalInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<AbteilungenNeuProps>();
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);
	const data = ref<Abteilung>(new Abteilung());
	const isLoading = ref<boolean>(false);
	const klassenToBeAdded = ref<KlassenDaten[]>([])

	const idLehrer = computed({
		get: () => props.manager().getLehrer().get(data.value.idAbteilungsleiter),
		set: (v: LehrerListeEintrag | null) => data.value.idAbteilungsleiter = v?.id ?? null,
	});
	const lehrer = computed(() => props.manager().getLehrer().values());

	const lehrerSelectManager = new SelectManager({	options: lehrer, optionDisplayText: v => v.vorname + ' ' + v.nachname,
		selectionDisplayText: v => v.vorname + ' ' + v.nachname,
	});

	function fieldIsValid(field: keyof Abteilung | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return bezeichnungIsValid(data.value.bezeichnung);
				case 'raum':
					return optionalInputIsValid(data.value.raum, 20);
				case 'email':
					return emailIsValid(data.value.email, 100);
				case 'durchwahl':
					return optionalInputIsValid(data.value.durchwahl, 20);
				default:
					return true;
			}
		}
	}

	function bezeichnungIsValid(value: string | null) {
		if (!mandatoryInputIsValid(value, 50))
			return false;

		return isUniqueInList(value, props.manager().liste.list(), 'bezeichnung')
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof Abteilung);
			const fieldValue = data.value[field as keyof Abteilung] as string | null;
			return validateField(fieldValue);
		})
	})

	async function add() {
		if (isLoading.value)
			return;

		props.checkpoint.active = false;
		isLoading.value = true;
		data.value.idSchuljahresabschnitt = props.manager().getSchuljahresabschnittSchule().id;
		const { id, klassenzuordnungen, ...partialData } = data.value;
		const idAbteilung = await props.addAbteilung(partialData);
		const zuordnungen = createKlassenzuordnungen(idAbteilung);
		await props.addKlassenzuordnungen(zuordnungen, idAbteilung);
		isLoading.value = false;
	}

	function createKlassenzuordnungen(idAbteilung: number): List<AbteilungKlassenzuordnung> {
		if (klassenToBeAdded.value.length === 0)
			return new ArrayList<AbteilungKlassenzuordnung>();

		const klassenzuordnungen = new ArrayList<AbteilungKlassenzuordnung>();
		for (const klasse of klassenToBeAdded.value ) {
			const zuordnung = new AbteilungKlassenzuordnung()
			zuordnung.idAbteilung = idAbteilung;
			zuordnung.idKlasse = klasse.id;
			const { id, ...partialData } = zuordnung;
			klassenzuordnungen.add(partialData as AbteilungKlassenzuordnung);
		}
		return klassenzuordnungen;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.goToDefaultView(null);
	}

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Klasse" },
	];

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
