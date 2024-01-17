<template>
	<div class="mt-6 -mx-6">
		<div>
			<svws-ui-checkbox type="toggle" v-model="nurRegelverletzungen" class="mx-6"> Nur Regelverletzungen anzeigen </svws-ui-checkbox>
			<Blockungsregel_1 v-if="!nurRegelverletzungen || regeln[1].value.length" v-model="regel" :schienen="schienen" :regeln="regeln[1].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" :get-ergebnismanager="getErgebnismanager" />
			<Blockungsregel_6 v-if="!nurRegelverletzungen || regeln[6].value.length" v-model="regel" :schienen="schienen" :regeln="regeln[6].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" :get-ergebnismanager="getErgebnismanager" />
			<Blockungsregel_2 v-if="!nurRegelverletzungen || regeln[2].value.length" v-model="regel" :kurse="kurse" :schienen="schienen" :map-faecher="mapFaecher" :regeln="regeln[2].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" :get-ergebnismanager="getErgebnismanager" />
			<Blockungsregel_3 v-if="!nurRegelverletzungen || regeln[3].value.length" v-model="regel" :kurse="kurse" :schienen="schienen" :map-faecher="mapFaecher" :regeln="regeln[3].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" :get-ergebnismanager="getErgebnismanager" />
			<Blockungsregel_7 v-if="!nurRegelverletzungen || regeln[7].value.length" v-model="regel" :kurse="kurse" :map-faecher="mapFaecher" :regeln="regeln[7].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" :get-ergebnismanager="getErgebnismanager" />
			<Blockungsregel_8 v-if="!nurRegelverletzungen || regeln[8].value.length" v-model="regel" :kurse="kurse" :map-faecher="mapFaecher" :regeln="regeln[8].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" :get-ergebnismanager="getErgebnismanager" />
			<Blockungsregel_9 v-if="!nurRegelverletzungen || regeln[9].value.length" v-model="regel" :get-datenmanager="getDatenmanager" :map-faecher="mapFaecher" :regeln="regeln[9].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" :get-ergebnismanager="getErgebnismanager" />
			<Blockungsregel_4 v-if="!nurRegelverletzungen || regeln[4].value.length" v-model="regel" :kurse="kurse" :map-faecher="mapFaecher" :map-schueler="mapSchueler" :regeln="regeln[4].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" :get-ergebnismanager="getErgebnismanager" />
			<Blockungsregel_5 v-if="!nurRegelverletzungen || regeln[5].value.length" v-model="regel" :kurse="kurse" :map-faecher="mapFaecher" :map-schueler="mapSchueler" :regeln="regeln[5].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" :get-ergebnismanager="getErgebnismanager" />
		</div>
		<Blockungsregel_10 v-model="regel" :regeln="regeln[10].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" />
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from 'vue';
	import { computed, shallowRef, ref } from 'vue';
	import type { GostBlockungKurs, GostBlockungRegel, GostBlockungSchiene, GostBlockungsdatenManager, GostBlockungsergebnisManager, GostFach, GostFaecherManager, List, SchuelerListeEintrag } from "@core";
	import { GostKursblockungRegelTyp } from "@core";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		patchRegel: (data: GostBlockungRegel, id: number) => Promise<void>;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
		faecherManager: GostFaecherManager;
		mapSchueler: Map<number, SchuelerListeEintrag>;
	}>();

	const nurRegelverletzungen = ref<boolean>(false);

	const mapFaecher: ComputedRef<Map<number, GostFach>> = computed(() => {
		const result = new Map<number, GostFach>();
		const faecher = props.faecherManager.faecher() || [];
		for (const fach of faecher)
			result.set(fach.id, fach);
		return result;
	});

	const disabled = computed<boolean>(() => props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() !== 1);

	const schienen = computed<List<GostBlockungSchiene>>(() => {
		return props.getDatenmanager().schieneGetListe();
	});

	const kurse = computed<List<GostBlockungKurs>>(() => props.getDatenmanager().kursGetListeSortiertNachKursartFachNummer());

	const _regel = shallowRef<GostBlockungRegel | undefined>(undefined);

	const regel = computed<GostBlockungRegel|undefined>({
		get: () => _regel.value,
		set: value => {
			_regel.value = value;
			if (value === undefined || value.id < 1)
				return;
			if (value.typ === GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ)
				void props.patchRegel(value, value.id);
		}
	})

	const verletzungen = computed(()=> new Set(props.getErgebnismanager().getErgebnis().bewertung.regelVerletzungen));

	const regeln: ComputedRef<GostBlockungRegel[]>[] = [];

	for (let i = 1; i < 11; i++)
		regeln[i] = computed(() => {
			const a = [];
			for (const r of props.getDatenmanager().regelGetListe())
				if (r.typ === i)
					if (!nurRegelverletzungen.value || verletzungen.value.has(r.id))
						a.push(r);
			return a;
		});

	async function regelEntfernen(r: GostBlockungRegel) {
		await props.removeRegel(r.id);
		if (r.id === regel.value?.id)
			regel.value = undefined;
	}

	async function regelSpeichern() {
		if (regel.value === undefined)
			return;
		const id = regel.value.id
		id > 0
			? await props.patchRegel(regel.value, id)
			: await props.addRegel(regel.value);
		regel.value = undefined;
	}

</script>
