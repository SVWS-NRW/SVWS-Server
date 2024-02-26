<template>
	<div class="mt-6 -mx-6">
		<svws-ui-checkbox type="toggle" v-model="nurRegelverletzungen" class="mx-6"> Nur Regelverletzungen anzeigen </svws-ui-checkbox>
		<!-- Regeltyp 1 			 -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_01" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'kursart', label: 'Kursart gesperrt in Schienen', span: 2}, {key: 'von', label: 'von'}, {key: 'bis', label: 'bis'}, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ GostKursart.fromID(r.parameter.get(0)).beschreibung }} </div>
				<div class="svws-ui-td" role="cell"> {{ r.parameter.get(1) }} </div>
				<div class="svws-ui-td" role="cell"> {{ r.parameter.get(2) }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterKursart(regel, 0).value" :items="GostKursart.values()" :item-text="i => i.beschreibung" />
				<svws-ui-select v-model="regelParameterSchiene(schienen, regel, 1).value" :items="schienen" :item-text="i => i.nummer.toString()" />
				<svws-ui-select v-model="regelParameterSchiene(schienen, regel, 2).value" :items="schienen" :item-text="i => i.nummer.toString()" />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 2 			 -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_06" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'kursart', label: 'Kursart allein in Schienen', span: 2}, {key: 'von', label: 'von'}, {key: 'bis', label: 'bis'}, ]">
			<template #regelRead="{regel: r}">
				<div class="svws-ui-td" role="cell"> {{ GostKursart.fromID(r.parameter.get(0)).beschreibung }} </div>
				<div class="svws-ui-td" role="cell"> {{ r.parameter.get(1) }} </div>
				<div class="svws-ui-td" role="cell"> {{ r.parameter.get(2) }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterKursart(regel, 0).value" :items="GostKursart.values()" :item-text="i => i.beschreibung" />
				<svws-ui-select v-model="regelParameterSchiene(schienen, regel, 1).value" :items="schienen" :item-text="i => i.nummer.toString()" />
				<svws-ui-select v-model="regelParameterSchiene(schienen, regel, 2).value" :items="schienen" :item-text="i => i.nummer.toString()" />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 2  -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_02" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'kursart', label: 'Kurs fixiert'}, {key: 'in', label: 'in Schiene'}, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(0)), mapFaecher) }} </div>
				<div class="svws-ui-td" role="cell"> {{ r.parameter.get(1) }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterKurs(kurse, regel, 0).value" :items="kurse" :item-text="i => getErgebnismanager().getOfKursName(i.id)" />
				<svws-ui-select v-model="regelParameterSchiene(schienen, regel, 1).value" :items="schienen" :item-text="i => i.nummer.toString()" />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 3  -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_03" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'kursart', label: 'Kurs gesperrt'}, {key: 'in', label: 'in Schiene'}, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(0)), mapFaecher) }} </div>
				<div class="svws-ui-td" role="cell"> {{ r.parameter.get(1) }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterKurs(kurse, regel, 0).value" :items="kurse" :item-text="i => getErgebnismanager().getOfKursName(i.id)" />
				<svws-ui-select v-model="regelParameterSchiene(schienen, regel, 1).value" :items="schienen" :item-text="i => i.nummer.toString()" />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 7  -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_07" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'kurs1', label: 'Kurs nie zusammen'}, {key: 'kurs2', label: 'mit Kurs'}, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(0)), mapFaecher) }} </div>
				<div class="svws-ui-td" role="cell"> {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(1)), mapFaecher) }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterKurs(kurse, regel, 0).value" :items="kurse" :item-text="i => getErgebnismanager().getOfKursName(i.id)" />
				<svws-ui-select v-model="regelParameterKurs(kurse, regel, 1).value" :items="kurse" :item-text="i => getErgebnismanager().getOfKursName(i.id)" />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 8  -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_08" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'kurs1', label: 'Kurs immer zusammen'}, {key: 'kurs2', label: 'mit Kurs'}, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(0)), mapFaecher) }} </div>
				<div class="svws-ui-td" role="cell"> {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(1)), mapFaecher) }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterKurs(kurse, regel, 0).value" :items="kurse" :item-text="i => getErgebnismanager().getOfKursName(i.id)" />
				<svws-ui-select v-model="regelParameterKurs(kurse, regel, 1).value" :items="kurse" :item-text="i => getErgebnismanager().getOfKursName(i.id)" />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 9  -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_09" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'kurs', label: 'Kurs auffüllen mit'}, {key: 'anzahl', label: 'externen Schülern', tooltip: 'Dummy-Daten'}, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(0)), mapFaecher) }} </div>
				<div class="svws-ui-td" role="cell"> {{ r.parameter.get(1) }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterKurs(kurse, regel, 0).value" :items="kurseFiltered(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN).value" :item-text="i => getErgebnismanager().getOfKursName(i.id)" />
				<svws-ui-input-number placeholder="externe Schüler" v-model="regelParameterAnzahlSuS" :min="1" />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 15  -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_15" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'kurs', label: 'Kurs hat'}, {key: 'anzahl', label: 'maximale Schülerzahl' }, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(0)), mapFaecher) }} </div>
				<div class="svws-ui-td" role="cell"> {{ r.parameter.get(1) }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterKurs(kurse, regel, 0).value" :items="kurseFiltered(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL).value" :item-text="i => getErgebnismanager().getOfKursName(i.id)" />
				<svws-ui-input-number placeholder="maximale Schülerzahl" v-model="regelParameterAnzahlSuS" :min="0" :max="100" />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 4  -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_04" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'schueler', label: 'Schüler fixiert'}, {key: 'in', label: 'in Kurs'}, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ getSchuelerName(r.parameter.get(0)) }} </div>
				<div class="svws-ui-td" role="cell"> {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(1)), mapFaecher) }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterSchueler(props.mapSchueler, regel, 0).value" :items="mapSchueler" :item-text="i => `${i.nachname}, ${i.vorname}`" :item-filter="(items, search) => items.filter(i => i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()) || i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase()))" autocomplete />
				<svws-ui-select v-model="regelParameterKurs(kurse, regel, 1).value" :items="kurse" :item-text="i => getErgebnismanager().getOfKursName(i.id)" />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 5  -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_05" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'schueler', label: 'Schüler verboten'}, {key: 'in', label: 'in Kurs'}, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ getSchuelerName(r.parameter.get(0)) }} </div>
				<div class="svws-ui-td" role="cell"> {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(1)), mapFaecher) }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterSchueler(props.mapSchueler, regel, 0).value" :items="mapSchueler" :item-text="i => `${i.nachname}, ${i.vorname}`" :item-filter="(items, search) => items.filter(i => i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()) || i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase()))" autocomplete />
				<svws-ui-select v-model="regelParameterKurs(kurse, regel, 1).value" :items="kurse" :item-text="i => getErgebnismanager().getOfKursName(i.id)" />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 11  -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_11" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'schueler', label: 'Schüler zusammen'}, {key: 'schueler', label: 'mit Schüler'}, {key: 'in', label: 'in Fach'}, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ getSchuelerName(r.parameter.get(0)) }} </div>
				<div class="svws-ui-td" role="cell"> {{ getSchuelerName(r.parameter.get(0)) }} </div>
				<div class="svws-ui-td" role="cell"> {{ getErgebnismanager().getFach(r.parameter.get(2)).bezeichnung ?? '??' }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterSchueler(props.mapSchueler, regel, 0).value" :items="mapSchueler" :item-text="i => `${i.nachname}, ${i.vorname}`" :item-filter="(items, search) => items.filter(i => i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()) || i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase()))" autocomplete />
				<svws-ui-select v-model="regelParameterSchueler(props.mapSchueler, regel, 1).value" :items="mapSchueler" :item-text="i => `${i.nachname}, ${i.vorname}`" :item-filter="(items, search) => items.filter(i => i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()) || i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase()))" autocomplete />
				<svws-ui-select v-model="regelParameterFach(mapFaecher, regel, 2).value" :items="mapFaecher" :item-text="i => i.bezeichnung ?? '??'" />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 12  -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_12" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'schueler', label: 'Schüler verbieten'}, {key: 'schueler', label: 'mit Schüler'}, {key: 'in', label: 'in Fach'}, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ getSchuelerName(r.parameter.get(0)) }} </div>
				<div class="svws-ui-td" role="cell"> {{ getSchuelerName(r.parameter.get(0)) }} </div>
				<div class="svws-ui-td" role="cell"> {{ getErgebnismanager().getFach(r.parameter.get(2)).bezeichnung ?? '??' }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterSchueler(props.mapSchueler, regel, 0).value" :items="mapSchueler" :item-text="i => `${i.nachname}, ${i.vorname}`" :item-filter="(items, search) => items.filter(i => i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()) || i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase()))" autocomplete />
				<svws-ui-select v-model="regelParameterSchueler(props.mapSchueler, regel, 1).value" :items="mapSchueler" :item-text="i => `${i.nachname}, ${i.vorname}`" :item-filter="(items, search) => items.filter(i => i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()) || i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase()))" autocomplete />
				<svws-ui-select v-model="regelParameterFach(mapFaecher, regel, 2).value" :items="mapFaecher" :item-text="i => i.bezeichnung ?? '??'" />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 13  -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_13" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'schueler', label: 'Schüler zusammen'}, {key: 'schueler', label: 'mit Schüler'}, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ getSchuelerName(r.parameter.get(0)) }} </div>
				<div class="svws-ui-td" role="cell"> {{ getSchuelerName(r.parameter.get(0)) }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterSchueler(props.mapSchueler, regel, 0).value" :items="mapSchueler" :item-text="i => `${i.nachname}, ${i.vorname}`" :item-filter="(items, search) => items.filter(i => i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()) || i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase()))" autocomplete />
				<svws-ui-select v-model="regelParameterSchueler(props.mapSchueler, regel, 1).value" :items="mapSchueler" :item-text="i => `${i.nachname}, ${i.vorname}`" :item-filter="(items, search) => items.filter(i => i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()) || i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase()))" autocomplete />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 14  -->
		<BlockungsregelBase v-model="regel" :regel-typ="GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
			:regel-hinzufuegen="regelHinzufuegen_14" :regel-speichern="regelSpeichern" :regel-entfernen="regelEntfernen" :disabled="disabled" :cols="[ {key: 'schueler', label: 'Schüler verbieten'}, {key: 'schueler', label: 'mit Schüler'}, ]">
			<template #regelRead="{ regel: r }">
				<div class="svws-ui-td" role="cell"> {{ getSchuelerName(r.parameter.get(0)) }} </div>
				<div class="svws-ui-td" role="cell"> {{ getSchuelerName(r.parameter.get(0)) }} </div>
			</template>
			<template #regelEdit>
				<svws-ui-select v-model="regelParameterSchueler(props.mapSchueler, regel, 0).value" :items="mapSchueler" :item-text="i => `${i.nachname}, ${i.vorname}`" :item-filter="(items, search) => items.filter(i => i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()) || i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase()))" autocomplete />
				<svws-ui-select v-model="regelParameterSchueler(props.mapSchueler, regel, 1).value" :items="mapSchueler" :item-text="i => `${i.nachname}, ${i.vorname}`" :item-filter="(items, search) => items.filter(i => i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()) || i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase()))" autocomplete />
			</template>
		</BlockungsregelBase>
		<!-- Regeltyp 10  -->
		<div class="mt-10 px-6">
			<svws-ui-checkbox type="toggle" v-model="hatRegel" :disabled="disabled"> {{ GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.bezeichnung }} </svws-ui-checkbox>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef, Ref } from 'vue';
	import { computed, ref } from 'vue';
	import type { GostBlockungsdatenManager, GostBlockungsergebnisManager, GostFaecherManager, List } from "@core";
	import { GostFach, SchuelerListeEintrag, ArrayList, GostBlockungKurs, GostBlockungRegel, GostBlockungSchiene, GostKursblockungRegelTyp, GostKursart, SetUtils, GostBlockungRegelUpdate } from "@core";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
		faecherManager: GostFaecherManager;
		mapSchueler: Map<number, SchuelerListeEintrag>;
	}>();

	const nurRegelverletzungen = ref<boolean>(false);

	const mapFaecher = computed<Map<number, GostFach>>(() => {
		const result = new Map<number, GostFach>();
		const faecher = props.faecherManager.faecher() || [];
		for (const fach of faecher)
			result.set(fach.id, fach);
		return result;
	});

	const disabled = computed<boolean>(() => props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() !== 1);

	const schienen = computed<List<GostBlockungSchiene>>(() => props.getDatenmanager().schieneGetListe());

	const kurse = computed<List<GostBlockungKurs>>(() => props.getDatenmanager().kursGetListeSortiertNachKursartFachNummer());

	const regel: Ref<GostBlockungRegel | undefined> = ref(undefined);

	const verletzungen = computed(()=> new Set(props.getErgebnismanager().getErgebnis().bewertung.regelVerletzungen));

	const regeln: Map<GostKursblockungRegelTyp, ComputedRef<GostBlockungRegel[]>> = new Map();

	for (const regelTyp of GostKursblockungRegelTyp.values())
		regeln.set(regelTyp, computed(() => {
			const a = [];
			for (const r of props.getDatenmanager().regelGetListe())
				if (r.typ === regelTyp.typ)
					if (!nurRegelverletzungen.value || verletzungen.value.has(r.id))
						a.push(r);
			return a;
		}));

	function regelHinzufuegen_01() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ;
		r.parameter.add(1);
		r.parameter.add(1);
		r.parameter.add(1);
		regel.value = r;
	}

	function regelHinzufuegen_06() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ;
		r.parameter.add(1);
		r.parameter.add(1);
		r.parameter.add(1);
		regel.value = r;
	}

	function regelHinzufuegen_02() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		const kurs = kurse.value.getFirst();
		if (kurs !== null) {
			r.parameter.add(kurs.id);
			r.parameter.add(1);
			regel.value = r;
		}
	}

	function regelHinzufuegen_03() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		const kurs = kurse.value.getFirst();
		if (kurs !== null) {
			r.parameter.add(kurs.id);
			r.parameter.add(1);
			regel.value = r;
		}
	}
	function regelHinzufuegen_07() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ;
		const kurs = kurse.value.getFirst();
		if (kurs !== null) {
			r.parameter.add(kurs.id);
			r.parameter.add(kurs.id);
			regel.value = r;
		}
	}

	function regelHinzufuegen_08() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ;
		const kurs = kurse.value.getFirst();
		if (kurs !== null) {
			r.parameter.add(kurs.id);
			r.parameter.add(kurs.id);
			regel.value = r;
		}
	}

	function regelHinzufuegen_09() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ;
		if (kurseFiltered(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN).value.isEmpty())
			return;
		r.parameter.add(kurseFiltered(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN).value.get(0).id);
		r.parameter.add(1);
		regel.value = r;
	}

	const kurseFiltered = (regelTyp: GostKursblockungRegelTyp) => computed<List<GostBlockungKurs>>(() => {
		const usedIDs = new Set<number>();
		const arr = regeln.get(regelTyp)?.value;
		if (arr === undefined)
			return props.getDatenmanager().kursGetListeSortiertNachKursartFachNummer();
		for (const r of arr)
			usedIDs.add(r.parameter.get(0));
		const result = new ArrayList<GostBlockungKurs>();
		for (const k of props.getDatenmanager().kursGetListeSortiertNachKursartFachNummer())
			if (!usedIDs.has(k.id))
				result.add(k);
		return result;
	});

	function regelHinzufuegen_15() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ;
		if (kurseFiltered(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL).value.isEmpty())
			return;
		r.parameter.add(kurseFiltered(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL).value.get(0).id);
		r.parameter.add(1);
		regel.value = r;
	}

	function regelHinzufuegen_04() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		const kurs = kurse.value.getFirst();
		if (kurs !== null) {
			r.parameter.add(props.mapSchueler.values().next().value.id);
			r.parameter.add(kurs.id);
			regel.value = r;
		}
	}

	function regelHinzufuegen_05() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		const kurs = kurse.value.getFirst();
		if (kurs !== null) {
			r.parameter.add(props.mapSchueler.values().next().value.id);
			r.parameter.add(kurs.id);
			regel.value = r;
		}
	}

	function regelHinzufuegen_11() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ;
		r.parameter.add(props.mapSchueler.values().next().value.id);
		r.parameter.add(props.mapSchueler.values().next().value.id);
		r.parameter.add(mapFaecher.value.values().next().value.id);
		regel.value = r;
	}

	function regelHinzufuegen_12() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ;
		r.parameter.add(props.mapSchueler.values().next().value.id);
		r.parameter.add(props.mapSchueler.values().next().value.id);
		r.parameter.add(mapFaecher.value.values().next().value.id);
		regel.value = r;
	}

	function regelHinzufuegen_13() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ;
		r.parameter.add(props.mapSchueler.values().next().value.id);
		r.parameter.add(props.mapSchueler.values().next().value.id);
		regel.value = r;
	}

	function regelHinzufuegen_14() {
		const r = new GostBlockungRegel();
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ;
		r.parameter.add(props.mapSchueler.values().next().value.id);
		r.parameter.add(props.mapSchueler.values().next().value.id);
		regel.value = r;
	}

	const hatRegel = computed<boolean>({
		get: () => regeln.get(GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN)?.value.length === 0 ? false : true,
		set: (erstellen) => void props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_10_LEHRKRAEFTE_BEACHTEN(erstellen))
	})

	async function regelEntfernen(r: GostBlockungRegel) {
		const update = new GostBlockungRegelUpdate();
		update.listEntfernen.add(r);
		await props.regelnUpdate(update);
		if (r.id === regel.value?.id)
			regel.value = undefined;
	}

	async function regelSpeichern() {
		if (regel.value === undefined)
			return;
		let update = new GostBlockungRegelUpdate();
		const p = regel.value.parameter;
		switch (regel.value.typ) {
			case GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ:
				update = props.getErgebnismanager().regelupdateCreate_01_KURSART_SPERRE_SCHIENEN_VON_BIS(p.get(0), p.get(1), p.get(2));
				break;
			case GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ:
				update = props.getErgebnismanager().regelupdateCreate_02_KURS_FIXIERE_IN_SCHIENE(SetUtils.create1(p.get(0)), SetUtils.create1(p.get(1)));
				break;
			case GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ:
				update = props.getErgebnismanager().regelupdateCreate_03_KURS_SPERRE_IN_SCHIENE(SetUtils.create1(p.get(0)), SetUtils.create1(p.get(1)));
				break;
			case GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ:
				update = props.getErgebnismanager().regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS(SetUtils.create1(p.get(0)), SetUtils.create1(p.get(1)));
				break
			case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ:
				update = props.getErgebnismanager().regelupdateCreate_05_SCHUELER_VERBIETEN_IN_KURS(SetUtils.create1(p.get(0)), SetUtils.create1(p.get(1)));
				break;
			case GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ:
				update = props.getErgebnismanager().regelupdateCreate_06_KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(p.get(0), p.get(1), p.get(2));
				break;
			case GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ:
				update = props.getErgebnismanager().regelupdateCreate_07_KURS_VERBIETEN_MIT_KURS(SetUtils.create2(p.get(0), p.get(1)));
				break;
			case GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ:
				update = props.getErgebnismanager().regelupdateCreate_08_KURS_ZUSAMMEN_MIT_KURS(SetUtils.create2(p.get(0), p.get(1)));
				break;
			case GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ:
				update = props.getErgebnismanager().regelupdateCreate_09_KURS_MIT_DUMMY_SUS_AUFFUELLEN(p.get(0), p.get(1));
				break;
			case GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ:
				update = props.getErgebnismanager().regelupdateCreate_11_SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(p.get(0), p.get(1),p.get(2));
				break
			case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ:
				update = props.getErgebnismanager().regelupdateCreate_12_SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(p.get(0), p.get(1), p.get(2));
				break;
			case GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ:
				update = props.getErgebnismanager().regelupdateCreate_13_SCHUELER_ZUSAMMEN_MIT_SCHUELER(p.get(0), p.get(1));
				break;
			case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ:
				update = props.getErgebnismanager().regelupdateCreate_14_SCHUELER_VERBIETEN_MIT_SCHUELER(p.get(0), p.get(1));
				break;
			case GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ:
				update = props.getErgebnismanager().regelupdateCreate_15_KURS_MAXIMALE_SCHUELERANZAHL(p.get(0), p.get(1));
				break;
		}
		if (regel.value.id > 0)
			update.listEntfernen.add(regel.value);
		await props.regelnUpdate(update);
		regel.value = undefined;
	}

	function getKursbezeichnung(kurs: GostBlockungKurs, mapFaecher: Map<number, GostFach>): string {
		const kuerzel = mapFaecher.get(kurs.fach_id)?.kuerzelAnzeige;
		const kursart = kurs.kursart > 0 ? GostKursart.fromID(kurs.kursart) : 'kursart-fehlt';
		const suffix = kurs.suffix ? "-" + kurs.suffix : "";
		return `${kuerzel}-${kursart}${kurs.nummer}${suffix}`
	}

	function getKursFromId(kurse: Iterable<GostBlockungKurs>, kursId: number): GostBlockungKurs {
		for (const kurs of kurse)
			if (kurs.id === kursId)
				return kurs;
		return new GostBlockungKurs();
	}


	function getSchuelerName(id: number) {
		const schueler = props.mapSchueler.get(id);
		return schueler ? `${schueler.nachname}, ${schueler.vorname}` : "";
	}

	const regelParameterAnzahlSuS = computed<number>({
		get: () => {
			if (regel.value === undefined)
				return 0;
			return regel.value.parameter.get(1);
		},
		set: (value) => {
			if (regel.value !== undefined)
				regel.value.parameter.set(1, value)
		}
	})

	const regelParameterKursart = (regel: GostBlockungRegel | undefined, parameter: number) => computed<GostKursart>({
		get: () => regel === undefined ? GostKursart.LK : GostKursart.fromID(regel.parameter.get(parameter)),
		set: (value) => {
			if (regel !== undefined)
				regel.parameter.set(parameter, value.id)
		}
	})

	const regelParameterSchiene = (schienen: Iterable<GostBlockungSchiene>, regel: GostBlockungRegel | undefined, parameter: number) => computed<GostBlockungSchiene>({
		get: () => {
			for (const schiene of schienen)
				if (schiene.nummer === regel?.parameter.get(parameter))
					return schiene;
			return new GostBlockungSchiene();
		},
		set: (value) => {
			if (regel !== undefined)
				regel.parameter.set(parameter, value.nummer)
		}
	})

	const regelParameterKurs = (kurse: Iterable<GostBlockungKurs>, regel: GostBlockungRegel | undefined, parameter: number) => computed<GostBlockungKurs>({
		get: () => {
			for (const kurs of kurse)
				if (kurs.id === regel?.parameter.get(parameter))
					return kurs;
			return new GostBlockungKurs();
		},
		set: (value) => {
			if (regel !== undefined)
				regel.parameter.set(parameter, value.id)
		}
	})

	const regelParameterSchueler = (mapSchueler: Map<number, SchuelerListeEintrag>, regel: GostBlockungRegel | undefined, parameter: number) => computed<SchuelerListeEintrag>({
		get: () => {
			if (regel === undefined)
				return new SchuelerListeEintrag();
			return mapSchueler.get(regel.parameter.get(parameter)) || new SchuelerListeEintrag();
		},
		set: (value) => {
			if (regel !== undefined)
				regel.parameter.set(parameter, value.id)
		}
	})

	const regelParameterFach = (faecher: Iterable<GostFach> | Map<number, GostFach>, regel: GostBlockungRegel | undefined, parameter: number) => computed<GostFach>({
		get: () => {
			if (faecher instanceof Map)
				faecher = faecher.values();
			for (const fach of faecher)
				if (fach.id === regel?.parameter.get(parameter))
					return fach;
			return new GostFach();
		},
		set: (value) => {
			if (regel !== undefined)
				regel.parameter.set(parameter, value.id)
		}
	})
</script>
