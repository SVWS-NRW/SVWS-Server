<template>
	<div class="flex flex-col gap-2 my-auto">
		<svws-ui-modal :show="showModal" size="small" class="hidden">
			<template #modalTitle>Lehrkräfte für Kurs {{ kursbezeichnung }}</template>
			<template #modalContent>
				<svws-ui-table :items="[...getDatenmanager().kursGetLehrkraefteSortiert(kurs.id)]" :columns="cols" selectable v-model="selected" count>
					<template #cell(nachname)="{ rowData }">
						{{ rowData.nachname }}, {{ rowData.vorname }}
					</template>
					<template #actions>
						<svws-ui-button @click="remove" type="trash" :disabled="selected.length === 0" />
						<svws-ui-select v-model="neueLehrkraft" class="flex-1" autocomplete headless ref="select" title="Lehrkräfte für Kurs hinzufügen"
							:item-filter="lehrer_filter" :items="lehrer_liste" :item-text="l => `${l.nachname}, ${l.vorname} (${l.kuerzel})`" />
					</template>
				</svws-ui-table>
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="toggle_zusatzkraefte_modal">Schließen</svws-ui-button>
			</template>
		</svws-ui-modal>
		<div class="flex gap-4">
			<div class="text-sm font-bold">Zusatzkräfte:</div>
			<div class="inline-flex gap-1" :class="{'-mt-1': anzahl_zusatzkraefte}">
				<div v-if="anzahl_zusatzkraefte">{{ [...getDatenmanager().kursGetLehrkraefteSortiert(kurs.id)].map(lehrer => lehrer?.kuerzel).join(", ") }}</div>
				<svws-ui-button :type="anzahl_zusatzkraefte ? 'transparent' : 'secondary'" @click="toggle_zusatzkraefte_modal">
					<template v-if="anzahl_zusatzkraefte">
						<i-ri-edit2-line />
					</template>
					<template v-else>
						Anlegen
					</template>
				</svws-ui-button>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { List, GostBlockungKurs, GostBlockungsdatenManager, GostBlockungKursLehrer , LehrerListeEintrag } from "@core";
	import { ArrayList } from "@core";
	import { lehrer_filter } from "~/utils/helfer";
	import { SvwsUiSelect } from "@ui";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		addLehrerRegel: () => Promise<void>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		kurs: GostBlockungKurs;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;
	const selected = ref<GostBlockungKursLehrer[]>([]);
	const select = ref<ComponentExposed<typeof SvwsUiSelect<LehrerListeEintrag>> | null>(null);

	const neueLehrkraft = computed({
		get(): LehrerListeEintrag | undefined | null {
			return undefined;
		},
		set(lehrer: LehrerListeEintrag | undefined | null) {
			if (lehrer && !props.getDatenmanager().kursGetLehrkraftMitIDExists(props.kurs.id, lehrer.id) && select.value) {
				void props.addKursLehrer(props.kurs.id, lehrer.id);
				void props.addLehrerRegel();
				select.value.reset();
			}
		}
	});

	const cols = [
		{ key: "kuerzel", label: "Lehrkraft", span: 1 },
		{ key: "nachname", label: "", span: 3 },
	];
	const kursbezeichnung = computed<string>(() => props.getDatenmanager().kursGetName(props.kurs.id));

	function toggle_zusatzkraefte_modal() {
		showModal().value = !showModal().value;
	}

	async function remove() {
		for (const l of selected.value)
			await props.removeKursLehrer(props.kurs.id, l.id);
		selected.value = [];
	}

	const anzahl_zusatzkraefte = computed(() => {
		const nr = props.getDatenmanager().kursGetLehrkraefteSortiert(props.kurs.id).size();
		return nr ? `${nr}` : ""
	});

	const kurslehrer = computed<List<LehrerListeEintrag>>(() => {
		const liste = props.getDatenmanager().kursGetLehrkraefteSortiert(props.kurs.id);
		const tmp = new ArrayList<GostBlockungKursLehrer>(liste);
		tmp.sort({ compare(a : GostBlockungKursLehrer, b : GostBlockungKursLehrer) {
			return a.reihenfolge - b.reihenfolge;
		}});
		const result = new ArrayList<LehrerListeEintrag>();
		for (const l of tmp) {
			const lehrer = props.mapLehrer.get(l.id);
			if (lehrer !== undefined)
				result.add(lehrer);
		}
		return result;
	})

	const lehrer_liste = computed<LehrerListeEintrag[]>(() => {
		const vergeben = new Set();
		for (const l of kurslehrer.value)
			vergeben.add(l.id);
		const result = [];
		for (const l of props.mapLehrer.values())
			if ((!vergeben.has(l.id)) && (l.istSichtbar))
				result.push(l);
		return result;
	})

</script>
