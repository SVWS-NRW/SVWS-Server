<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung" :min-len="1" :max-len="50" v-model="data.bezeichnung" required :valid="fieldIsValid('bezeichnung')" />
				<svws-ui-text-input placeholder="Raum" :max-len="20" v-model="data.raum" :valid="fieldIsValid('raum')" />
				<svws-ui-text-input placeholder="Email" type="email" :max-len="100" v-model="data.email" :valid="fieldIsValid('email')" />
				<svws-ui-text-input placeholder="Durchwahl" type="tel" :max-len="20" v-model="data.durchwahl" :valid="fieldIsValid('durchwahl')" />
				<svws-ui-spacing />
				<ui-select label="Lehrer" v-model="idLehrer" :select-manager="lehrerSelectManager" />
				<svws-ui-button :disabled="data.idAbteilungsleiter === null" type="transparent"
					@click="goToLehrer(data.idAbteilungsleiter ?? -1)">
					<span class="icon i-ri-link" /> Zum Lehrer
				</svws-ui-button>
				<svws-ui-spacing />
				<div class="mt-7 flex flex-row gap-4 justify end">
					<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
					<svws-ui-button @click="add" :disabled="!formIsValid">Speichern</svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Klassen">
			<svws-ui-table :items="manager().getKlassen().values()" :columns selectable v-model="klassenToBeAdded" scroll >
				<template #cell(kuerzel)="{ rowData: s }">
					<span>{{ s.kuerzel }}</span>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { AbteilungenNeuProps } from "~/components/schule/kataloge/abteilungen/SAbteilungenNeuProps";
	import type { KlassenDaten, LehrerListeEintrag, List } from "@core";
	import type { DataTableColumn } from "@ui";
	import { Abteilung, AbteilungKlassenzuordnung, ArrayList, JavaString } from "@core";
	import { ObjectSelectManager } from "../../../../../../ui/src/ui/controls/select/selectManager/ObjectSelectManager";

	const props = defineProps<AbteilungenNeuProps>();
	const data = ref<Abteilung>(new Abteilung());
	const isLoading = ref<boolean>(false);
	const klassenToBeAdded = ref<KlassenDaten[]>([])

	const idLehrer = computed({
		get: () => props.manager().getLehrer().get(data.value.idAbteilungsleiter),
		set: (v: LehrerListeEintrag | null) => data.value.idAbteilungsleiter = v?.id ?? null,
	});
	const lehrerListe = computed(() => props.manager().getLehrer().values());
	const lehrerSelectManager = new ObjectSelectManager(false, lehrerListe.value, v => v.vorname + ' ' + v.nachname,
		v => v.vorname + ' ' + v.nachname);

	function fieldIsValid(field: keyof Abteilung | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return bezeichnungIsValid();
				case 'raum':
					return optionalInputIsValid(data.value.raum, 20);
				case 'email':
					return optionalInputIsValid(data.value.email, 100);
				case 'durchwahl':
					return optionalInputIsValid(data.value.durchwahl, 20);
				default:
					return true;
			}
		}
	}

	function bezeichnungIsValid() {
		return (!JavaString.isBlank(data.value.bezeichnung)) && (data.value.bezeichnung.length <= 50);
	}

	function optionalInputIsValid(input : string | null, maxLength : number) {
		if ((input === null) || (JavaString.isBlank(input)))
			return true;
		return input.length <= maxLength;
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

	function cancel() {
		props.checkpoint.active = false;
		void props.goToDefaultView(null);
	}

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel"},
	];

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;
		props.checkpoint.active = true;
	}, {immediate: false, deep: true});

</script>
