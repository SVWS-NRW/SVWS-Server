<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<template #actions>
				<svws-ui-checkbox v-model="data.istSichtbar" :disabled>Ist sichtbar</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" required :min-len="1" :max-len="20" :disabled v-model="data.kuerzel"
					:valid="fieldIsValid('kuerzel')" />
				<svws-ui-select title="Lehrer" :disabled v-model="idLehrer" :items="manager().lehrer.list()"
					:item-text="l => l.vorname + ' ' + l.nachname" removable statistics />
				<svws-ui-select title="Fach" :disabled v-model="idFach" required :valid="fieldIsValid('idFach')" :items="manager().faecher.list()"
					:item-text="f => f.bezeichnung" statistics />
				<svws-ui-select title="Kursart" :disabled removable statistics :items="kursarten.keys()" v-model="data.kursartAllg" required
					:item-text="k => k + ' (' + (kursarten.get(k) ?? '???') + ')'" />
				<svws-ui-input-number placeholder="Wochenstunden" :disabled statistics v-model="data.wochenstunden" />
				<svws-ui-multi-select title="Jahrgänge" :disabled statistics :item-text="jg => jg?.kuerzel ?? ''" v-model="auswahlJahrgaenge" :items="jahrgaenge" />
				<svws-ui-input-number placeholder="Sortierung" v-model="data.sortierung" :disabled :min="0" :max="32000" />
				<svws-ui-text-input placeholder="Zeugnisbezeichnung" :disabled v-model="data.bezeichnungZeugnis" :max-len="130"
					:valid="fieldIsValid('bezeichnungZeugnis')" />
				<svws-ui-select title="Fortschreibungsart" :disabled :items="KursFortschreibungsart.values()" v-model="idKursFortschreibungsart" removable
					:item-text="f => f.beschreibung" />
				<svws-ui-multi-select title="Schienen" :disabled :items="Array.from({length: 40}, (_, i) => i + 1)" v-model="schienen"
					:item-text="s => 'Schiene ' + s" />
				<div class="mt-7 flex flex-row gap-4 justify end">
					<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
					<svws-ui-button @click="addKurs" :disabled="!formIsValid || !hatKompetenzAdd">Speichern</svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { KurseNeuProps } from "~/components/kurse/SKurseNeuProps";
	import type { JahrgangsDaten, FachDaten, LehrerListeEintrag, List } from "@core"
	import { ArrayList, BenutzerKompetenz, JavaString, KursDaten, KursFortschreibungsart, ZulaessigeKursart } from "@core";
	import { computed, ref, watch } from "vue";

	const props = defineProps<KurseNeuProps>();
	const data = ref<KursDaten>(Object.assign(new KursDaten(), { wochenstunden: 0, istSichtbar: true }));
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));
	const schuljahr = computed<number>(() => props.manager().getSchuljahr());
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);
	const isLoading = ref<boolean>(false);

	const idLehrer = computed({
		get: () => props.manager().lehrer.get(data.value.lehrer ?? -1),
		set: (v: LehrerListeEintrag | null) => data.value.lehrer = v?.id ?? null,
	})

	const idFach = computed({
		get: () => props.manager().faecher.get(data.value.idFach),
		set: (v: FachDaten) => data.value.idFach = v.id,
	})

	const auswahlJahrgaenge = computed({
		get: () => {
			const result = [];
			for (const id of data.value.idJahrgaenge) {
				const jahrgang = props.manager().jahrgaenge.get(id);
				if (jahrgang !== null)
					result.push(jahrgang);
			}
			return result;
		},
		set: (value) => {
			const result = new ArrayList<number>();
			value.forEach(j => result.add(j.id));
			data.value.idJahrgaenge = result;
		},
	})

	const idKursFortschreibungsart = computed({
		get: () => KursFortschreibungsart.fromID(data.value.idKursFortschreibungsart),
		set: (v: KursFortschreibungsart | null) => data.value.idKursFortschreibungsart = v?.id ?? 0,
	})

	const schienen = computed<number[]>({
		get: () => {
			return data.value.schienen.toArray(new Array<number>);
		},
		set: (value) => {
			const result = new ArrayList<number>();
			let changed = false;
			for (const s of value) {
				if (!data.value.schienen.contains(s))
					changed = true;
				result.add(s);
			}
			if (!changed)
				changed = (data.value.schienen.size() !== result.size());
			if (changed)
				data.value.schienen = result;
		},
	});

	const kursarten = computed<Map<string, string>>(() => {
		const arten = new Map<string, string>();
		for (const art of ZulaessigeKursart.data().getWerteBySchuljahr(schuljahr.value)) {
			const daten = art.daten(schuljahr.value);
			if (daten === null)
				continue;
			if (daten.kuerzel === "PUK")
				continue;
			if ((daten.kuerzelAllg !== null) && (daten.bezeichnungAllg !== null))
				arten.set(daten.kuerzelAllg, daten.bezeichnungAllg);
			else
				arten.set(daten.kuerzel, daten.text);
			if (daten.kuerzelAllg === "DK")
				arten.set(daten.kuerzel, daten.text);
		}
		return new Map([...arten.entries()].sort());
	});

	const jahrgaenge = computed<List<JahrgangsDaten>>(() => {
		const result = new ArrayList<JahrgangsDaten>();
		for (const jg of props.manager().jahrgaenge.list())
			result.add(jg);
		return result;
	});

	function fieldIsValid(field: keyof KursDaten | null): (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'kuerzel':
					return kuerzelIsValid();
				case 'idFach':
					return data.value.idFach > 0;
				case 'bezeichnungZeugnis':
					return bezeichnungZeugnisIsValid(data.value.bezeichnungZeugnis);
				case 'kursartAllg':
					return !JavaString.isBlank(data.value.kursartAllg);
				default:
					return true;
			}
		}
	}

	function kuerzelIsValid() {
		if (JavaString.isBlank(data.value.kuerzel) || data.value.kuerzel.length > 20)
			return false;
		for (const kurs of props.manager().liste.list()) {
			if (JavaString.equalsIgnoreCase(data.value.kuerzel, kurs.kuerzel))
				return false;
		}
		return true;
	}

	function bezeichnungZeugnisIsValid(input: string | null) {
		if (input === null)
			return true;
		return input.length <= 130;
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof KursDaten);
			const fieldValue = data.value[field as keyof KursDaten] as string | null;
			return validateField(fieldValue);
		})
	})

	async function addKurs() {
		if (isLoading.value)
			return;

		props.checkpoint.active = false;
		isLoading.value = true;
		data.value.idSchuljahresabschnitt = props.manager().getSchuljahresabschnittSchule().id;
		const { id, weitereLehrer, wochenstundenLehrer, schueler, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	function cancel() {
		props.checkpoint.active = false;
		void props.goToDefaultView(null);
	}

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
